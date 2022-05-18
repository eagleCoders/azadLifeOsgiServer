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
import org.ehcache.Cache;
import org.ehcache.CacheManager;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;

/**
 * @author anees-ur-rehman
 *
 */
public class TalkToMeRoutes extends RouteBuilder{

	private GsonDataFormat gsonDataFormat;
	
	Cache<String, DoccatModel> cacheNLPModel;
	Cache<String, TokenNameFinderModel> cacheNLPNameFinderModel;
	
	public TalkToMeRoutes(CacheManager cacheManager) {
		cacheNLPModel = cacheManager.getCache("nlpModel", String.class, DoccatModel.class);
		cacheNLPNameFinderModel = cacheManager.getCache("nlpNameFinderModel", String.class, TokenNameFinderModel.class);
	}
	
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
		
		
		
		from("direct-vm:updateNLPCommunication").routeId("direct-vm_updateNLPCommunication").log("Welcome to the NLP Chat Route").process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				Map<String, String> incomingMsgMap =(Map<String, String>) exchange.getIn().getBody();
				
				System.out.println("[TalkToMeRouter] : incoming Message : "+incomingMsgMap);
				
				String host = incomingMsgMap.get("sending-host");
				String target = incomingMsgMap.get("target-host");
				String incomingMsgText = incomingMsgMap.get("message");
				
				String destinationId = "activemq:queue:"+target;

				TokenNameFinderModel tokenNameFinderModel = cacheNLPNameFinderModel.get("nlpBuyingSelling");
				NameFinderME nameFinderModel = new NameFinderME(tokenNameFinderModel);
				Map<String, String> replymsg = talktome.nlp.NLPUtils.getCategory(cacheNLPModel.get("nlpDukkan"), nameFinderModel,
						incomingMsgText);
				
				String reply = replymsg.get("reply");
				
				String routeId = "createRouteQueueId"+host;
				
				Map<String, String> messageMap = new HashMap<String, String>();
				messageMap.put("messaegType", "WELCOME");
				messageMap.put("messaeg", reply);

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
