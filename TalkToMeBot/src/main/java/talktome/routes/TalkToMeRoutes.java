/**
 * 
 */
package talktome.routes;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author anees-ur-rehman
 *
 */
public class TalkToMeRoutes extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		from("direct-vm:createTalk2me").routeId("direct-vm:createTalk2me").log("This is the body message for Queue ${body}")
		.multicast().parallelProcessing()
		.to("activemq:queue:${body}_in", "activemq:queue:${body}_out");

	}

}
