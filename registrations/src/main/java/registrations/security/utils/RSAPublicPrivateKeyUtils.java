/**
 * 
 */
package registrations.security.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchProviderException;
import java.util.Base64;
import java.util.Iterator;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;

/**
 * @author anees-ur-rehman
 *
 */
public class RSAPublicPrivateKeyUtils {

	/**
	 */
	public static PGPPublicKey readPublicKey(String publicKeyString) throws IOException, PGPException {
		byte[] publicBytesKey = Base64.getDecoder().decode(publicKeyString);
		ByteArrayInputStream bio = new ByteArrayInputStream(publicBytesKey);

		InputStream in = org.bouncycastle.openpgp.PGPUtil.getDecoderStream(bio);

		PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(in, new JcaKeyFingerprintCalculator());

		//
		// we just loop through the collection till we find a key suitable for
		// encryption, in the real
		// world you would probably want to be a bit smarter about this.
		//
		PGPPublicKey key = null;

		//
		// iterate through the key rings.
		//
		Iterator<PGPPublicKeyRing> rIt = pgpPub.getKeyRings();

		while (key == null && rIt.hasNext()) {
			PGPPublicKeyRing kRing = rIt.next();
			Iterator<PGPPublicKey> kIt = kRing.getPublicKeys();
			while (key == null && kIt.hasNext()) {
				PGPPublicKey k = kIt.next();
				if (k.isEncryptionKey()) {
					key = k;
				}
			}
		}

		if (key == null) {
			throw new IllegalArgumentException("Can't find encryption key in key ring.");
		}

		return key;
	}

	/**
	 * 
	 * @param pgpPublicEncodedBytes
	 * @return
	 * @throws IOException
	 */
	public static PGPPublicKey getPublicKey(byte[] pgpPublicEncodedBytes) throws IOException {
		PGPPublicKeyRing publicKeyRing = new PGPPublicKeyRing(pgpPublicEncodedBytes, new JcaKeyFingerprintCalculator());
		return publicKeyRing.getPublicKey();
	}
	
	/**
	 * 
	 * @param pgpSecreteEncodedBytes
	 * @param _passPharase
	 * @return
	 * @throws PGPException
	 * @throws IOException
	 */
	public static PGPPrivateKey getPrivateKey(byte[] pgpSecreteEncodedBytes, String _passPharase)
			throws PGPException, IOException {

		PGPSecretKeyRing secreteKeyRing = new PGPSecretKeyRing(pgpSecreteEncodedBytes,
				new JcaKeyFingerprintCalculator());
		PGPSecretKey secreteKey = secreteKeyRing.getSecretKey();

		BcPBESecretKeyDecryptorBuilder bcPBESecretKeyDecryptorBuilder = new BcPBESecretKeyDecryptorBuilder(
				new BcPGPDigestCalculatorProvider());
		PBESecretKeyDecryptor pskd = bcPBESecretKeyDecryptorBuilder.build(_passPharase.toCharArray());

		return secreteKey.extractPrivateKey(pskd);
	}

	/**
	 * 
	 * @param pgpSecreteEncodedBytes
	 * @param _passPharase
	 * @param keyID
	 * @return
	 * @throws IOException
	 * @throws PGPException
	 * @throws NoSuchProviderException
	 */
	public static PGPPrivateKey getPrivateKey(byte[] pgpSecreteEncodedBytes, String _passPharase, long keyID)
			throws IOException, PGPException {
		PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(pgpSecreteEncodedBytes,
				new JcaKeyFingerprintCalculator());
		
		PGPSecretKey pgpSecKey = pgpSec.getSecretKey(keyID);
		if (pgpSecKey == null) {
			return null;
		}
		BcPBESecretKeyDecryptorBuilder bcPBESecretKeyDecryptorBuilder = new BcPBESecretKeyDecryptorBuilder(
				new BcPGPDigestCalculatorProvider());
		PBESecretKeyDecryptor pskd = bcPBESecretKeyDecryptorBuilder.build(_passPharase.toCharArray());
		return pgpSecKey.extractPrivateKey(pskd);
	}

}
