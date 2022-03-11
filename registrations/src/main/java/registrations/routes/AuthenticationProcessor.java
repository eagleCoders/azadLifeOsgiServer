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
import org.apache.camel.component.gson.GsonDataFormat;

/**
 * @author anees-ur-rehman
 *
 */
public class AuthenticationProcessor extends RouteBuilder {

	private GsonDataFormat gsonDataFormat;

	@Override
	public void configure() throws Exception {

		gsonDataFormat = new GsonDataFormat();
		gsonDataFormat.setUnmarshalType(Map.class);

		from("direct-vm:loggedIn").routeId("direct-vm_loggedIn").log("Welcome to LoggedIn Route").unmarshal(gsonDataFormat).process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				Map<String, String> bodyMap = (Map<String, String>) exchange.getIn().getBody();

				System.out.println(" Username is : " + bodyMap.get("userName"));
				System.out.println(" Password is : " + bodyMap.get("password"));

//				username is cnic
				StringBuilder selectStatement = new StringBuilder();
				selectStatement.append("select user_name,globalId from user_registration");
				selectStatement.append(" ");
				selectStatement.append("Where user_name = '" + bodyMap.get("userName") + "'");
				selectStatement.append(" and user_password ='" + bodyMap.get("password") + "'");
				
				exchange.getIn().setBody(selectStatement.toString());
				
			}
		}).to("jdbc:azadPDS").process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				List<Map<String, String>> list = (List<Map<String, String>>) exchange.getIn().getBody();
				if (list.size() == 0) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("exception", "User Name and/or Password not Correct");
					list.add(map);
				}

				exchange.getIn().setBody(list);

			}
		}).marshal(gsonDataFormat).convertBodyTo(String.class);
	}


}
