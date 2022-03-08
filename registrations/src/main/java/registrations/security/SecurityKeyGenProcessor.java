/**
 * 
 */
package registrations.security;

import java.util.Base64;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.bouncycastle.openpgp.PGPKeyRingGenerator;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRing;

import registrations.security.utils.RSAKeyGenUtils;

/**
 * @author anees-ur-rehman
 *
 */
public class SecurityKeyGenProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

		Message message = exchange.getIn();
		String username = (String) message.getHeader("userName");
		String password = (String) message.getHeader("password");
		String cnic = (String) message.getHeader("cnic");
//		String UID= (String) message.getHeader("UID");

//		System.out.println(" UserName : " + username + " | Password : " + password + " | CNIC : " + cnic);

		PGPKeyRingGenerator krgen = RSAKeyGenUtils.generateKeyRingGenerator(username, cnic.toCharArray());

		// Generate public key ring, dump to file.
		PGPPublicKeyRing pkr = krgen.generatePublicKeyRing();
		String publicKey = Base64.getEncoder().encodeToString(pkr.getEncoded());

//		System.out.println(" the public key is : " + publicKey);

		// Generate private key, dump to file.
		PGPSecretKeyRing skr = krgen.generateSecretKeyRing();
		String privateKey = Base64.getEncoder().encodeToString(skr.getEncoded());

//		System.out.println("the private key is : " + privateKey);
		
//		Map<String, Object> map= message.getHeaders();
//		map.put("publicKey", publicKey);
//		map.put("privateKey", privateKey);
//		map.put("userName", username);
//		map.put("uid", UID);
//		map.put("password", password);
//		map.put("status", "APPLICANT"); // APPLICANT :AUTHORIZE : SUSPENDED : REJECTED
		

//		exchange.getOut().setBody(Arrays.asList(cnic, publicKey, privateKey, UID));
//		
//		exchange.getOut().setHeaders(map);
		exchange.getIn().setHeader("publicKey", publicKey);
		exchange.getIn().setHeader("privateKey", privateKey);
	}


}
