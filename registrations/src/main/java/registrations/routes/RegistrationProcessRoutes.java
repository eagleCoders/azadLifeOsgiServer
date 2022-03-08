/**
 * 
 */
package registrations.routes;

import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import registrations.domain.UserRegistrationBean;
import registrations.security.SecurityKeyGenProcessor;
import registrations.security.utils.EncryptDecryptUtils;

/**
 * @author anees-ur-rehman
 *
 */
public class RegistrationProcessRoutes extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		
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
		from("direct-vm:proceedRegistration").routeId("direct-vm_proceedRegistration").log("Welcome to the Registration Process");
		
	}

}
