/**
 * 
 */
package registrations.routes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

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
						.to("jdbc:dataSource").log("Result from Body ${body}").process(new Processor() {

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
		
		from("direct-vm:proceedRegistration").routeId("direct-vm_proceedRegistration").log("Welcome to the Registration Process");
		
	}

}
