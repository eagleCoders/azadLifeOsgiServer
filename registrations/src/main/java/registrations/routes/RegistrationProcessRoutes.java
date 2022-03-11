/**
 * 
 */
package registrations.routes;

import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.gson.GsonDataFormat;

import registrations.domain.UserTypeChangeBean;
import registrations.domain.types.UserType;
import registrations.security.SecurityKeyGenGlobalProcessor;
import registrations.security.SecurityKeyGenProcessor;
import registrations.security.utils.EncryptDecryptUtils;

/**
 * @author anees-ur-rehman
 *
 */
public class RegistrationProcessRoutes extends RouteBuilder {

	private GsonDataFormat gsonDataFormat;

	@Override
	public void configure() throws Exception {

		gsonDataFormat = new GsonDataFormat();
		gsonDataFormat.setUnmarshalType(Map.class);

//		=============== Admin Registration Route(One time when started and No Admin user setup by the server) ======================
		from("timer:stream?repeatCount=1").routeId("timer_startupAdminConfiguration")
				.log("Planning for base setup base super admin")
				.setBody(constant("select globalid from user_registration where user_name = 'super@admin'"))
				.to("jdbc:azadPDS").log("Result from Body ${body}").process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						Object obj = exchange.getIn().getBody();
						List resultBody = (List) obj;

						Map<String, Object> map = new HashMap<>();
						if (resultBody.isEmpty()) {
							map.put("admin_user_status", false);
						} else {
							map.put("admin_user_status", true);
						}

						exchange.getIn().setHeaders(map);
					}
				}).log("User registertion validation : ${header.admin_user_status}").choice()
				.when(header("admin_user_status").isEqualTo(false)).to("direct:createAdminUser")
				.when(header("admin_user_status").isEqualTo(true)).log("Base Admin Configuration is already done")
				.process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						System.out.println("[MainRoute]: The Admin User is already created");
					}
				});

		from("direct:createAdminUser").routeId("direct_createAdminUser").log("Start creating Admin Super User")
				.process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("userName", "super@admin");
						map.put("userRoles", "*");
						map.put("password", "admin123");
						map.put("status", "APPROVED");
						map.put("cnic", "9999999999xxx");
						map.put("secretePhrase", "admin has to manage the stuff");

						StringBuilder stringBuilder = new StringBuilder();
						stringBuilder.append("super@admin");
						stringBuilder.append(".");
						stringBuilder.append("*");
						stringBuilder.append(".");
						stringBuilder.append("admin123");
						stringBuilder.append(".");
						stringBuilder.append("admin has to manage the stuff");

						map.put("globalId", stringBuilder.toString());
						exchange.getIn().setHeaders(map);
					}
				}).process(new SecurityKeyGenProcessor()).process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						String publicKey = (String) exchange.getIn().getHeader("publicKey");
						String privateKey = (String) exchange.getIn().getHeader("privateKey");

						String username = (String) exchange.getIn().getHeader("userName");
						String password = (String) exchange.getIn().getHeader("password");
						String cnic = (String) exchange.getIn().getHeader("cnic");
						String userRoles = (String) exchange.getIn().getHeader("userRoles");
						String status = (String) exchange.getIn().getHeader("status");
						String globalID = (String) exchange.getIn().getHeader("globalId");

						byte[] encrypted = EncryptDecryptUtils.encrypt(globalID.getBytes(), publicKey, null, true,
								true);
						String encryptedGlobalID = Base64.getEncoder().encodeToString(encrypted);

						LocalDate localDate = LocalDate.now();

						StringBuilder insertStatement = new StringBuilder();
						insertStatement.append(
								"insert into user_registration (security_id, publickey, privatekey, globalid, cellphone,  user_name, user_password, user_role, status, createdby, creationdt, email)");
						insertStatement.append(" ");
						insertStatement.append("VALUES (");
						insertStatement.append("'" + cnic + "',");
						insertStatement.append("'" + publicKey + "',");
						insertStatement.append("'" + privateKey + "',");
						insertStatement.append("'" + encryptedGlobalID + "',");
						insertStatement.append("'999-999-999',");
						insertStatement.append("'" + username + "',");
						insertStatement.append("'" + password + "',");
						insertStatement.append("'" + userRoles + "',");
						insertStatement.append("'" + status + "',");
						insertStatement.append("'system',");
						insertStatement.append("'" + localDate + "',");
						insertStatement.append("'info@gamimatricz.com')");

						exchange.getIn().setBody(insertStatement.toString());
					}
				}).to("jdbc:azadPDS");

//		=================== Registration for Every User before adding themselves as Merchandiser or Customer ======================
		from("direct-vm:proceedRegistration").routeId("direct-vm_proceedRegistration")
				.log("Welcome to the Registration Process").process(new GuestUserCreationProcessor())
				.process(new SecurityKeyGenGlobalProcessor()).to("jdbc:azadPDS").process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						// TODO Auto-generated method stub

					}
				});

//		=================== Registered Users updation according to the usage e.g. Merchandiser, Retailer, and Consumer ============
		from("direct-vm:updateMerchandiser").routeId("direct-vm_updateMerchandiser")
				.log("Welcome to the Merchandiser update route")
//				.unmarshal(gsonDataFormat)
				.process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
//						Map<String, String> bodyMap = (Map<String, String>) exchange.getIn().getBody();
						Map<String, String> bodyMap  = new HashMap<String, String>();
						String globalId =(bodyMap.containsKey("globalid")) ? bodyMap.get("globalid") : "LMNOPQ1234";
						UserType userTypeSelected = (bodyMap.containsKey("user_type_level")) ? UserType.valueOf( bodyMap.get("user_type_level")): UserType.MERCHANDISER;
						Integer userTypeLevel = (bodyMap.containsKey("user_type_level")) ? Integer.valueOf(bodyMap.get("user_type_level")): 0;
						exchange.getIn().setHeader("AZADPAY_MERCHANTTYPE", bodyMap);
						exchange.getIn().setHeader("AZADPAY_GLOBALID", globalId);
						UserTypeChangeBean bean = new UserTypeChangeBean();
						bean.setGlobalId(globalId);
						bean.setUserType(userTypeSelected);
						bean.setUserTypeLevel(userTypeLevel);
						exchange.getIn().setBody(bean);
					}
				})
//				.outputType(UserTypeChangeBean.class)
				.transacted()
				.toD("jpa://" + UserTypeChangeBean.class.getName() + "?usePersist=true")

//				.toD("jpa://" + UserTypeChangeBean.class.getName() + "?resultClass="+UserTypeChangeBean.class.getName()+"&query=select b from "
//						+ UserTypeChangeBean.class.getName() + " b where b.userTypeid = '{header.AZADPAY_GLOBALID}'")
				.process(new Processor() {
							
							@Override
							public void process(Exchange exchange) throws Exception {
//								List<UserTypeChangeBean> listMap = (List<UserTypeChangeBean>) exchange.getIn().getBody();
								System.out.println("The incoming Types are : "+exchange.getIn().getBody());
							}
						});
	}

}
