/**
 * 
 */
package registrations.routes;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.gson.GsonDataFormat;
import org.postgresql.util.PSQLException;

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

	@SuppressWarnings("unchecked")
	@Override
	public void configure() throws Exception {

		gsonDataFormat = new GsonDataFormat();
		gsonDataFormat.setUnmarshalType(Map.class);

		
		onException(Exception.class, SQLException.class, PSQLException.class)
		.handled(true).process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				log.info("PSQL Exception caught....");
				Throwable caused = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
				Map<String, String> map = new HashMap<String, String>();
				map.put("message", "Exception caught");
				exchange.getIn().setBody(map);

			}
		}).marshal(gsonDataFormat).convertBodyTo(String.class);

//		=============== Admin Registration Route(One time when started and No Admin user setup by the server) ======================
		from("timer:stream?repeatCount=1").routeId("timer_startupAdminConfiguration")
				.log("Planning for base setup base super admin")
				.setBody(constant("select globalid,user_name from user_registration where user_name = 'super@admin'"))
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
						List resultBody = (List) exchange.getIn().getBody();
						Map<String, String> mapData =(Map<String, String>) resultBody.get(0);
						exchange.getIn().setHeader("globalid", mapData.get("globalid"));
						exchange.getIn().setHeader("userId", mapData.get("user_name"));
					}
				}).wireTap("direct-vm:createTalk2meCreate");

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

						exchange.getIn().setHeader("globalid", encryptedGlobalID);
						exchange.getIn().setHeader("userId", cnic);

						exchange.getIn().setBody(insertStatement.toString());
					}
				}).to("jdbc:azadPDS").wireTap("direct-vm:createTalk2meCreate");

//		=================== Registration for Every User before adding themselves as Merchandiser or Customer ======================

		from("direct-vm:proceedRegistration").routeId("direct-vm_proceedRegistration")
				.log("Welcome to the Registration Process ${body}")

				.process(new GuestUserCreationProcessor()).process(new SecurityKeyGenGlobalProcessor())
				.to("jdbc:azadPDS").process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						// TODO Auto-generated method stub
//						UserRegistrationBean userRegistrationBean = new UserRegistrationBean();
//						userRegistrationBean.setMessage("Registraion is completed Successully");
						Map<String, String> map = new HashMap<String, String>();
						map.put("message", "Registraion is completed Successully");
						exchange.getIn().setBody(map);
					}
				}).marshal(gsonDataFormat).convertBodyTo(String.class).wireTap("direct-vm:createTalk2meCreate");

//		=================== Registered Users updation according to the usage e.g. Merchandiser, Retailer, and Consumer ============

//		from("direct-vm:updateConsumer").routeId("direct-vm:updateConsumer").log("Welcome to the Consumer Update Route")
//		.unmarshal(gsonDataFormat)
//		.process(new Processor() {
//			
//			@Override
//			public void process(Exchange exchange) throws Exception {
//				Map<String, String> bodyMap = (Map<String, String>) exchange.getIn().getBody();
//				String globalId = (bodyMap.containsKey("globalid")) ? bodyMap.get("globalid") : "consumer01";
//				UserType userTypeSelected = (bodyMap.containsKey("user_type_level"))
//						? UserType.valueOf(bodyMap.get("user_type_level"))
//						: UserType.AGENT;
//				Integer userTypeLevel = (bodyMap.containsKey("user_type_level"))
//						? Integer.valueOf(bodyMap.get("user_type_level"))
//						: 0;
//				exchange.getIn().setHeader("AZADPAY_GLOBALID", globalId);
//
//				UserTypeChangeBean bean = new UserTypeChangeBean();
//				bean.setGlobalId(globalId);
//				bean.setUserType(userTypeSelected);
//				bean.setUserTypeLevel(userTypeLevel);
//				exchange.getIn().setHeader("AZADPAY_MERCHANTTYPE", bean);
//				exchange.getIn().setBody(bean);
//				
//			}
//		}).to("direct:userTypeSelectionQuery");

//		from("direct-vm:updateAgent").routeId("direct-vm:updateAgent").log("Welcome to the Agent Update Route")
//		.unmarshal(gsonDataFormat)
//		.process(new Processor() {
//			
//			@Override
//			public void process(Exchange exchange) throws Exception {
//				Map<String, String> bodyMap = (Map<String, String>) exchange.getIn().getBody();
//				String globalId = (bodyMap.containsKey("globalid")) ? bodyMap.get("globalid") : "agent01";
//				UserType userTypeSelected = (bodyMap.containsKey("user_type_level"))
//						? UserType.valueOf(bodyMap.get("user_type_level"))
//						: UserType.AGENT;
//				Integer userTypeLevel = (bodyMap.containsKey("user_type_level"))
//						? Integer.valueOf(bodyMap.get("user_type_level"))
//						: 0;
//				exchange.getIn().setHeader("AZADPAY_GLOBALID", globalId);
//
//				UserTypeChangeBean bean = new UserTypeChangeBean();
//				bean.setGlobalId(globalId);
//				bean.setUserType(userTypeSelected);
//				bean.setUserTypeLevel(userTypeLevel);
//				exchange.getIn().setHeader("AZADPAY_MERCHANTTYPE", bean);
//				exchange.getIn().setBody(bean);
//				
//			}
//		}).to("direct:userTypeSelectionQuery");

//		from("direct-vm:updateRetailer").routeId("direct-vm:updateRetailer").log("Welcome to the Retailer Update Route")
//		.unmarshal(gsonDataFormat)
//		.process(new Processor() {
//			
//			@Override
//			public void process(Exchange exchange) throws Exception {
//				Map<String, String> bodyMap = (Map<String, String>) exchange.getIn().getBody();
//				String globalId = (bodyMap.containsKey("globalid")) ? bodyMap.get("globalid") : "abcd999AAA";
//				UserType userTypeSelected = (bodyMap.containsKey("user_type_level"))
//						? UserType.valueOf(bodyMap.get("user_type_level"))
//						: UserType.RETAILER;
//				Integer userTypeLevel = (bodyMap.containsKey("user_type_level"))
//						? Integer.valueOf(bodyMap.get("user_type_level"))
//						: 0;
//				exchange.getIn().setHeader("AZADPAY_GLOBALID", globalId);
//
//				UserTypeChangeBean bean = new UserTypeChangeBean();
//				bean.setGlobalId(globalId);
//				bean.setUserType(userTypeSelected);
//				bean.setUserTypeLevel(userTypeLevel);
//				exchange.getIn().setHeader("AZADPAY_MERCHANTTYPE", bean);
//				exchange.getIn().setBody(bean);
//				
//			}
//		})
//		.to("direct:userTypeSelectionQuery");

		from("direct-vm:updateUserType").routeId("direct-vm:updateUserType")
				.log("Welcome to the Merchandiser update route").unmarshal(gsonDataFormat).process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						Map<String, String> bodyMap = (Map<String, String>) exchange.getIn().getBody();
//						Map<String, String> bodyMap = new HashMap<String, String>();
						String globalId = (bodyMap.containsKey("globalid")) ? bodyMap.get("globalid") : "abcd999AAAKLM";
						UserType userTypeSelected = (bodyMap.containsKey("user_type_level"))
								? UserType.valueOf(bodyMap.get("user_type_level"))
								: UserType.GUEST;
						Integer userTypeLevel = (bodyMap.containsKey("user_type_level"))
								? Integer.valueOf(bodyMap.get("user_type_level"))
								: 0;
						exchange.getIn().setHeader("AZADPAY_GLOBALID", globalId);

						UserTypeChangeBean bean = new UserTypeChangeBean();
						bean.setGlobalId(globalId);
						bean.setUserType(userTypeSelected);
						bean.setUserTypeLevel(userTypeLevel);
						exchange.getIn().setHeader("AZADPAY_MERCHANTTYPE", bean);
						exchange.getIn().setBody(bean);
					}
				}).to("direct:userTypeSelectionQuery");

		from("direct:userTypeSelectionQuery").routeId("direct:userTypeSelectionQuery").log("Usertype Selection query")
				.transacted()
				.toD("jpa://" + UserTypeChangeBean.class.getName() + "?resultClass="
						+ UserTypeChangeBean.class.getName() + "&query=select b from "
						+ UserTypeChangeBean.class.getName() + " b where b.globalId = '${header.AZADPAY_GLOBALID}'")
				.process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						List<UserTypeChangeBean> listMap = (List<UserTypeChangeBean>) exchange.getIn().getBody();
						System.out.println("The incoming Types are : " + listMap.size());
						if (listMap.size() > 0) {
							exchange.getIn().setHeader("AP_USERTYPE_ACTION", "1");
							UserTypeChangeBean bean = listMap.get(0);
							UserTypeChangeBean bean0 = (UserTypeChangeBean) exchange.getIn()
									.getHeader("AZADPAY_MERCHANTTYPE");
							bean0.setUserTypeid(bean.getUserTypeid());
							exchange.getIn().setHeader("AZADPAY_MERCHANTTYPE", bean0);
						} else {
							exchange.getIn().setHeader("AP_USERTYPE_ACTION", "0");

						}
					}
				}).choice().when(header("AP_USERTYPE_ACTION").isEqualTo("0")).to("direct:createUserType")
				.when(header("AP_USERTYPE_ACTION").isEqualTo("1")).to("direct:updateUserType").endChoice().end();

		from("direct:createUserType").routeId("direct:createUserType").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				UserTypeChangeBean bean = (UserTypeChangeBean) exchange.getIn().getHeader("AZADPAY_MERCHANTTYPE");
				exchange.getIn().setBody(bean);
			}
		}).transacted().toD("jpa://" + UserTypeChangeBean.class.getName() + "?usePersist=true");

		from("direct:updateUserType").routeId("direct:updateUserType").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				UserTypeChangeBean bean = (UserTypeChangeBean) exchange.getIn().getHeader("AZADPAY_MERCHANTTYPE");
				exchange.getIn().setBody(bean);
			}
		}).transacted().toD("jpa://" + UserTypeChangeBean.class.getName() + "?useExecuteUpdate=true");

	}

}
