/**
 * 
 */
package talktome.routes;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.gson.GsonDataFormat;

/**
 * @author anees-ur-rehman
 *
 */
public class TalkToMeRoutes extends RouteBuilder{

	private GsonDataFormat gsonDataFormat;
	
	@Override
	public void configure() throws Exception {
	
		gsonDataFormat = new GsonDataFormat();
		gsonDataFormat.setUnmarshalType(Map.class);
		
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
				Map<String, String> messageMap = new HashMap<String, String>();
				messageMap.put("messaegType", "WELCOME");
				messageMap.put("messaeg", "WELCOME From the Server: You are Registered to communicate");
				
				RouteBuilder.addRoutes(getContext(),  rb->{
					rb.from("timer://simpleOfferCountDownTimer?delay=1s&repeatCount=1").routeId(routeId).process(new Processor() {
						
						@Override
						public void process(Exchange exchange) throws Exception {
							exchange.getIn().setBody(messageMap);
							
						}
					}).marshal(gsonDataFormat).convertBodyTo(String.class).log("the data to send is ${body}").toD(destinationId)
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
