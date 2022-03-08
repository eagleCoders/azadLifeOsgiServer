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
		
		rest("/kys").get("/hello").to("direct:hello").post("/registration").to("direct-vm:proceedRegistration");		
		
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
