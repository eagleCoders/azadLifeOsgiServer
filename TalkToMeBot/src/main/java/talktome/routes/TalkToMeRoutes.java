/**
 * 
 */
package talktome.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * @author anees-ur-rehman
 *
 */
public class TalkToMeRoutes extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		
		StringBuilder destinationBuilder = new StringBuilder();
		from("direct-vm:createTalk2meCreate").routeId("direct-vm:createTalk2me").log("This is the body message for Queue ${body}")
//		.multicast().parallelProcessing()
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				exchange.getIn().getHeader("globalid");
				String userQueueiD =(String) exchange.getIn().getHeader("userId");
				String routeId = "createRouteQueueId";
				String destinationId = "activemq:queue:"+userQueueiD;
				destinationBuilder.append("activemq:queue:");
				destinationBuilder.append(userQueueiD);
				RouteBuilder.addRoutes(getContext(),  rb->{
					rb.from("timer://simpleOfferCountDownTimer?delay=1s&repeatCount=1").routeId(routeId).toD(destinationId)
					.process(new Processor() {
						
						@Override
						public void process(Exchange exchange) throws Exception {
							getContext().removeRoute(routeId);
						}
					});
					
				});
			}
		});

	}

}
