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
				messageMap.put("category", "CHATBOX");
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
		}).to("direct:updateInfoToAdmin");
		
		from("direct:updateInfoToAdmin").routeId("direct:updateInfoToAdmin").log("Update the information to the Adminstration Role Users").process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				
				Map<String, String> map = (Map<String, String>) exchange.getIn().getHeader("AzadLifeUserRegistrationMap");
				if(null != map) {
					String destinationId = "activemq:queue:super@admin";
//					destinationBuilder.append("activemq:queue:");
//					destinationBuilder.append(userQueueiD);
					String routeId = "createRouteQueueId"+System.currentTimeMillis();
//					Map<String, String> messageMap = new HashMap<String, String>();
					
					map.put("messaegType", "NEW_REGISTRATION_REQ");
					map.put("messaeg", "New Registration Request");
					map.put("category", "USER_PROFILE");
					map.put("actions", "NONE");
					RouteBuilder.addRoutes(getContext(),  rb->{
						rb.from("timer://simpleOfferCountDownTimer?delay=1s&repeatCount=1").routeId(routeId).process(new Processor() {
							
							@Override
							public void process(Exchange exchange) throws Exception {
								exchange.getIn().setBody(map);
								
							}
						}).marshal(gsonDataFormat).convertBodyTo(String.class).log("the data to send is ${body}").toD(destinationId)
						.process(new Processor() {
							
							@Override
							public void process(Exchange exchange) throws Exception {
								getContext().removeRoute("");
							}
						});
					});
				}


			}
		});
		
		from("direct-vm:updateUserStatusToAdmin").routeId("direct-vm:updateUserStatusToAdmin").log("Welcome to bean Info to Admin").process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				Map<String, String> map = (Map<String, String>) exchange.getIn().getBody();
				if(null != map) {
					String routeId = "createRouteQueueId"+System.currentTimeMillis();
					String destinationId = "activemq:queue:super@admin";
					
					RouteBuilder.addRoutes(getContext(),  rb->{
						rb.from("timer://simpleOfferCountDownTimer?delay=1s&repeatCount=1").routeId(routeId).process(new Processor() {
							
							@Override
							public void process(Exchange exchange) throws Exception {
								exchange.getIn().setBody(map);
								
							}
						}).marshal(gsonDataFormat).convertBodyTo(String.class).log("the data to send is ${body}").toD(destinationId)
						.process(new Processor() {
							
							@Override
							public void process(Exchange exchange) throws Exception {
								getContext().removeRoute("");
							}
						});
					});

				}
			}
		});
		
		from("direct-vm:updateBusinessRegistrationToAdmin").routeId("direct-vm:updateBusinessRegistrationToAdmin").log("Welcome to Biz Info to Admin").process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				Map<String, Object> bodyMap = (Map<String, Object>)exchange.getIn().getBody();
				System.out.println("[TalkToMeRoutes] : updateBusinessRegistrationToAdmin : "+bodyMap);
				
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
				
				System.out.println("[TalkToMeRoutes] : the NLP reply Message : "+replymsg);
				String reply = replymsg.get("reply");
				
				String routeId = "createRouteQueueId"+host;
				
				Map<String, Object> messageMap = new HashMap<String, Object>();

				String category = replymsg.get("category");
				if(category.equals("greeting") || category.equals("seller") || category.equals("buyer")) {
					replymsg.put("messaegType", "WELCOME");
					replymsg.put("messaeg", reply);
//					replymsg.put("category", replymsg.get("category"));
					replymsg.put("ttl", "7000");
					
				}
				if(category.equals("help_femalesexual") || category.equals("help_malesexual") || category.equals("doctor_search") || category.equals("self_triage") || category.equals("self_psyhc")) {
					replymsg.put("messaegType", "HEALTHCARE");
					replymsg.put("messaeg", reply);
//					replymsg.put("category", replymsg.get("category"));
					replymsg.put("deleteable", "false");
					replymsg.put("ttl", "16000");
				}
				
				if(category.equals("payments")) {
					replymsg.put("messaegType", "PAYMENT");
					replymsg.put("messaeg", reply);
//					replymsg.put("category", replymsg.get("category"));
					replymsg.put("deleteable", "false");
					replymsg.put("ttl", "16000");
					
				}
				if(category.equals("appointment")) {
					replymsg.put("messaegType", "APPOINTMENT");
					replymsg.put("messaeg", reply);
//					replymsg.put("category", replymsg.get("category"));
					replymsg.put("ttl", "20000");
					
				}
				if(category.equals("accounts_open")) {
					replymsg.put("messaegType", "ACCOUNTS");
					replymsg.put("messaeg", reply);
					replymsg.put("deleteable", "false");
					replymsg.put("ttl", "20000");
				}
				if(category.equals("registration")) {
					replymsg.put("messaegType", "REGISTRATION");
					replymsg.put("messaeg", reply);
					replymsg.put("deleteable", "false");
					replymsg.put("ttl", "20000");
					
				}

				RouteBuilder.addRoutes(getContext(),  rb->{
					rb.from("timer://simpleOfferCountDownTimer?delay=1s&repeatCount=1").routeId(routeId).process(new Processor() {
						
						@Override
						public void process(Exchange exchange) throws Exception {
							exchange.getIn().setBody(replymsg);
							
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
