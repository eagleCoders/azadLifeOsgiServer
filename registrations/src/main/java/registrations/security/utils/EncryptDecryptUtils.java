/**
 * 
 */
package registrations.security.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyDataDecryptorFactoryBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;

/**
 * @author anees-ur-rehman
 *
 */
public class EncryptDecryptUtils {

	/**
	 * 
	 * @param clearData
	 * @param publicKeyString
	 * @param fileName
	 * @param withIntegrityCheck
	 * @param armor
	 * @return
	 * @throws PGPException
	 * @throws IOException
	 */
	public static byte[] encrypt(byte[] clearData, String publicKeyString, String fileName, boolean withIntegrityCheck,
			boolean armor) throws PGPException, IOException {

		PGPPublicKey pgpPublicKey = RSAPublicPrivateKeyUtils.readPublicKey(publicKeyString);

		if (fileName == null) {
			fileName = PGPLiteralData.CONSOLE;
		}
		ByteArrayOutputStream encOut = new ByteArrayOutputStream();

		OutputStream out = encOut;
		if (armor) {
			out = new ArmoredOutputStream(out);
		}

		ByteArrayOutputStream bOut = new ByteArrayOutputStream();

		PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(PGPCompressedDataGenerator.ZIP);
		OutputStream cos = comData.open(bOut); // open it with the final
		// destination
		PGPLiteralDataGenerator lData = new PGPLiteralDataGenerator();

		// we want to generate compressed data. This might be a user option
		// later,
		// in which case we would pass in bOut.
		OutputStream pOut = lData.open(cos, // the compressed output stream
				PGPLiteralData.BINARY, fileName, // "filename" to store
				clearData.length, // length of clear data
				new Date() // current time
		);
		pOut.write(clearData);

		lData.close();
		comData.close();

		PGPEncryptedDataGenerator encGen = new PGPEncryptedDataGenerator(
				new JcePGPDataEncryptorBuilder(PGPEncryptedData.CAST5).setWithIntegrityPacket(true)
						.setSecureRandom(new SecureRandom()).setProvider("BC"));

		encGen.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(pgpPublicKey));

		byte[] bytes = bOut.toByteArray();

		OutputStream cOut = encGen.open(out, bytes.length);

		cOut.write(bytes); // obtain the actual bytes from the compressed stream

		cOut.close();

		out.close();

		return encOut.toByteArray();

	}

	/**
	 * 
	 * @param encryptedFile
	 * @param secreteKey
	 * @param passPharase
	 * @return
	 * @throws IOException
	 * @throws PGPException
	 */
	public static String decrypt(String encryptedFile, String secreteKey, String passPharase)
			throws IOException, PGPException {

		byte[] encdata = Base64.getDecoder().decode(encryptedFile);

		ByteArrayInputStream bais = new ByteArrayInputStream(encdata); // ----- Decrypt the file

		InputStream in = PGPUtil.getDecoderStream(bais);

		PGPPrivateKey pgpPrivateKey = RSAPublicPrivateKeyUtils.getPrivateKey(Base64.getDecoder().decode(secreteKey),
				passPharase);

		PGPObjectFactory pgpF = new PGPObjectFactory(in, new JcaKeyFingerprintCalculator());

		PGPEncryptedDataList enc;
		Object o = pgpF.nextObject();

		if (o instanceof PGPEncryptedDataList) {
			enc = (PGPEncryptedDataList) o;
		} else {
			enc = (PGPEncryptedDataList) pgpF.nextObject();
		}

		Iterator it = enc.getEncryptedDataObjects();
		PGPPrivateKey sKey = null;
		PGPPublicKeyEncryptedData pbe = null;
		while (sKey == null && it.hasNext()) {
			pbe = (PGPPublicKeyEncryptedData) it.next();
			System.out.println("pbe id=" + pbe.getKeyID());
			sKey = RSAPublicPrivateKeyUtils.getPrivateKey(Base64.getDecoder().decode(secreteKey), passPharase,
					pbe.getKeyID());
		}
		if (sKey == null) {
			throw new IllegalArgumentException("secret key for message not found.");
		}

		JcePublicKeyDataDecryptorFactoryBuilder jpdfb = new JcePublicKeyDataDecryptorFactoryBuilder();
		jpdfb.setContentProvider(new BouncyCastleProvider());
		jpdfb.build(pgpPrivateKey);
		InputStream clear = pbe.getDataStream(jpdfb.build(sKey));

		PGPObjectFactory plainFact = new PGPObjectFactory(clear, new JcaKeyFingerprintCalculator());
		Object message = plainFact.nextObject();

		if (message instanceof PGPCompressedData) {
			PGPCompressedData cData = (PGPCompressedData) message;
			PGPObjectFactory pgpFact = new PGPObjectFactory(cData.getDataStream(), new JcaKeyFingerprintCalculator());
			message = pgpFact.nextObject();
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (message instanceof PGPLiteralData) {
			PGPLiteralData ld = (PGPLiteralData) message;
			InputStream unc = ld.getInputStream();
			int ch;
			while ((ch = unc.read()) >= 0) {
				baos.write(ch);
			}
		}else if (message instanceof PGPOnePassSignatureList) {
			throw new PGPException(
					"encrypted message contains a signed message - not literal data.");
		} else {
			throw new PGPException(
					"message is not a simple encrypted file - type unknown.");		
			}
		return baos.toString();
	}

}
