/**
 * 
 */
package registrations.security;

import java.time.LocalDate;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.bouncycastle.openpgp.PGPKeyRingGenerator;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRing;

import registrations.domain.UserRegistrationBean;
import registrations.security.utils.EncryptDecryptUtils;
import registrations.security.utils.RSAKeyGenUtils;

/**
 * @author anees-ur-rehman
 *
 */
public class SecurityKeyGenGlobalProcessor implements Processor {

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {

		Message message = exchange.getIn();
		Map<String, String> bodyMap = (Map<String, String>)message.getBody();
		String username = (String)bodyMap.get("userName");
		String password = (String)bodyMap.get("userPassword");
		String userRoles = (String)bodyMap.get("userRoles");
		String cnic = (String) bodyMap.get("securityId");
		String status =(String) bodyMap.get("status");
		String globalID = (String) bodyMap.get("globalId");
		String email = (String) bodyMap.get("email");
		String cellphone=(String) bodyMap.get("cellphone");
		System.out.println(" the Global ID is : " + globalID);

		
		PGPKeyRingGenerator krgen = RSAKeyGenUtils.generateKeyRingGenerator(username, cnic.toCharArray());

		// Generate public key ring, dump to file.
		PGPPublicKeyRing pkr = krgen.generatePublicKeyRing();
		String publicKey = Base64.getEncoder().encodeToString(pkr.getEncoded());


		// Generate private key, dump to file.
		PGPSecretKeyRing skr = krgen.generateSecretKeyRing();
		String privateKey = Base64.getEncoder().encodeToString(skr.getEncoded());
		
//		Encrypt the userGlobalID
		byte[] encrypted = EncryptDecryptUtils.encrypt(globalID.getBytes(), publicKey, null, true,
				true);
		String encryptedGlobalID = Base64.getEncoder().encodeToString(encrypted);

		LocalDate localDate = LocalDate.now();
		
		UserRegistrationBean userRegistrationBean = new UserRegistrationBean();
		
		Map<String, Object> map= bodyMap.entrySet().stream().filter(e-> e.getValue() != null).collect(Collectors.toMap(e-> e.getKey(), e-> e.getValue()));
		System.out.println(" the Body for Return Back : "+map);
		map.put("globalId", encryptedGlobalID);
		StringBuilder insertStatement = new StringBuilder();
		insertStatement.append(
				"insert into user_registration (security_id, publickey, privatekey, globalid, cellphone, user_name, user_password, user_role, status, createdby, creationdt, email)");
		insertStatement.append(" ");
		insertStatement.append("VALUES (");
		insertStatement.append("'" + cnic + "',");
		insertStatement.append("'" + publicKey + "',");
		insertStatement.append("'" + privateKey + "',");
		insertStatement.append("'" + encryptedGlobalID + "',");
		insertStatement.append("'" + cellphone + "',");
		insertStatement.append("'" + username + "',");
		insertStatement.append("'" + password + "',");
		insertStatement.append("'" + userRoles + "',");
		insertStatement.append("'" + status + "',");
		insertStatement.append("'system',");
		insertStatement.append("'" + localDate + "',");
		insertStatement.append("'" + email + "')");
		
//		exchange.getIn().setHeaders(map);
		exchange.getIn().setHeader("globalid", encryptedGlobalID);
		exchange.getIn().setHeader("userId", cnic);
		
		exchange.getIn().setHeader("TelegramBotSecureMap", map);
		

		exchange.getIn().setBody(insertStatement.toString());

	}

}
