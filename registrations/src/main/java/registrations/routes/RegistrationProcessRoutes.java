/**
 * 
 */
package registrations.routes;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author anees-ur-rehman
 *
 */
public class RegistrationProcessRoutes extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		from("direct-vm:proceedRequestForRegistration").log("");
		
	}

}
