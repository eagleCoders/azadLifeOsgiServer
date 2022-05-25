/**
 * 
 */
package registrations.routes;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * @author anees-ur-rehman
 *
 */
public class GuestUserCreationProcessor implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> bodyMap = (Map<String, String>) exchange.getIn().getBody();
		System.out.println("the Body from Map is : " + bodyMap);
		bodyMap.put("userRoles", "00");
		bodyMap.put("status", "APPROVED");
		bodyMap.put("securityId", bodyMap.get("userName"));
		bodyMap.put("cellphone", bodyMap.get("phoneNumber"));
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(bodyMap.get("userName"));
		stringBuilder.append(".");
		stringBuilder.append(bodyMap.get("userRoles"));
		stringBuilder.append(".");
		stringBuilder.append(String.valueOf(bodyMap.get("userPassword")));
		stringBuilder.append(".");
		stringBuilder.append(bodyMap.get("email"));
		stringBuilder.append(".");
		stringBuilder.append("this is the first time user");

		bodyMap.put("globalId", stringBuilder.toString());

		exchange.getIn().setBody(bodyMap);
		
	}

}
