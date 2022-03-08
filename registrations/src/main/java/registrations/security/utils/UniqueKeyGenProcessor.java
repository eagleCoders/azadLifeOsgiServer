/**
 * 
 */
package registrations.security.utils;

import java.util.Base64;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

/**
 * @author anees-ur-rehman
 *
 */
public class UniqueKeyGenProcessor implements Processor {
	
	private StringBuilder uniqueKeyBuilder;

	@Override
	public void process(Exchange exchange) throws Exception {
		
		
		
		Message message = exchange.getIn();
		
		Map<String, Object> map = message.getHeaders();
		String cnic = (String) message.getHeader("cnic");
		String country =(String) message.getHeader("countryOfBirth");
		String city =(String) message.getHeader("cityOfBirth");
		
		
		
		if(null == cnic) {
			Map<String, String> body  = (Map)exchange.getIn().getBody();
			System.out.println("===========>  Body :"+body);
			cnic = body.get("cnic");
			country = body.get("countryOfBirth");
			city = body.get("cityOfBirth");
			map.put("cnic", cnic);
			map.put("countryOfBirth", country);
			map.put("cityOfBirth", city);
			map.put("userName", body.get("userName"));
			map.put("password", body.get("password"));
			map.put("firstName", body.get("firstName"));
			map.put("familyName", body.get("familyName"));
			map.put("secretePhrase", body.get("secretePhrase"));
			map.put("emailId", body.get("emailId"));
		}
		
		uniqueKeyBuilder = new StringBuilder();

		uniqueKeyBuilder.append(String.valueOf(System.currentTimeMillis()));
		uniqueKeyBuilder.append(".");
		uniqueKeyBuilder.append(cnic);
		uniqueKeyBuilder.append(".");
		uniqueKeyBuilder.append(city);
		uniqueKeyBuilder.append(".");
		uniqueKeyBuilder.append(country);

		String key = uniqueKeyBuilder.toString();
		String finaUniquelKey = Base64.getEncoder().encodeToString(key.getBytes());
		System.out.println("UID is : "+ finaUniquelKey);

		map.put("UID", finaUniquelKey);
		
		System.out.println("===========> "+map);
		
		exchange.getIn().setHeaders(map);

	}

}
