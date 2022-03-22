/**
 * 
 */
package registrations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.gson.GsonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;

/**
 * @author anees-ur-rehman
 *
 */
public class BaseRoutes extends RouteBuilder {

	private GsonDataFormat gsonDataFormat;
	
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		gsonDataFormat = new GsonDataFormat();
		gsonDataFormat.setUnmarshalType(Map.class);

		
		restConfiguration().component("jetty").host("0.0.0.0").port(9091).bindingMode(RestBindingMode.json).dataFormatProperty("prettyPrint","true");
		
		rest("/azadLife").get("/hello").to("direct:hello")
		.get("/loadAllUsers").to("direct:loadUsers")
		.post("/registration").to("direct-vm:proceedRegistration")
		.post("/updateUserType").to("direct-vm:updateUserType")
		
//		.post("/updateAsMerchandiser").to("direct-vm:updateMerchandiser")
//		.post("/updateRetailer").to("direct-vm:updateRetailer")
//		.post("/updateAsAgent").to("direct-vm:updateAgent")
//		.post("/updateAsConsumer").to("direct-vm:updateConsumer")
		.post("/loggedIn").to("direct-vm:loggedIn")
		;
		
		
		from("direct:hello").transform().constant("Hello World").setBody(constant("select * from testingtable")).to("jdbc:azadPDS").process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				Object obj = exchange.getIn().getBody();
				List resultBody = (List) obj;

				System.out.println("Going to test rest get : list of result size : "+resultBody.size());
				Map<String, String> map = new HashMap<String, String>();
				map.put("msg", "Hello World");
				exchange.getIn().setBody(map);
				
			}
		}).marshal(gsonDataFormat).convertBodyTo(String.class);
		
		from("direct:loadUsers").routeId("direct:loadUsers").process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				StringBuilder selectStatement = new StringBuilder();
				selectStatement
						.append("select user_name, user_password, user_role ,globalid ,email from user_registration");

				exchange.getIn().setBody(selectStatement.toString());				
			}
		}).to("jdbc:azadPDS").marshal(gsonDataFormat).convertBodyTo(String.class);;
		
//		from("timer:stream?repeatCount=3").routeId("timer_buzzTelegram").process(new Processor() {
//
//			@Override
//			public void process(Exchange exchange) throws Exception {
//				System.out.println("Testing the DS");
//				exchange.getIn().setBody("Testing Telegram Bot");
//			}
//		});

	}

}
