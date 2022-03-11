/**
 * 
 */
package registrations.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;

/**
 * @author anees-ur-rehman
 * 
 *         String text = "This is a message";
 * 
 *         Security.addProvider(new BouncyCastleProvider());
 * 
 *         KeyStore ks = KeyStore.getInstance(KEYSTORE_INSTANCE); ks.load(new
 *         FileInputStream(KEYSTORE_FILE), KEYSTORE_PWD.toCharArray()); Key key
 *         = ks.getKey(KEYSTORE_ALIAS, KEYSTORE_PWD.toCharArray());
 * 
 *         //Sign PrivateKey privKey = (PrivateKey) key; Signature signature =
 *         Signature.getInstance("SHA1WithRSA", "BC");
 *         signature.initSign(privKey); signature.update(text.getBytes());
 * 
 *         Store certs = new JcaCertStore(certList); CMSSignedDataGenerator gen
 *         = new CMSSignedDataGenerator();
 * 
 *         ContentSigner sha1Signer = new
 *         JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(privKey);
 * 
 *         gen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new
 *         JcaDigestCalculatorProviderBuilder().setProvider("BC").build()).build(sha1Signer,
 *         cert)); gen.addCertificates(certs);
 * 
 *         CMSSignedData sigData = gen.generate(msg, false);
 * 
 *         FileOutputStream sigfos = new
 *         FileOutputStream("D:\\SBI-DATA\\file\\signature_1.txt");
 *         sigfos.write(Base64.encodeBase64(sp.getEncoded()));
 * 
 *         sigfos.close();
 *
 */
public class TestingX509Certificate {

	private static X509Certificate cert;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String text = "This is a message and fuck you all beautiful data";

//		Security.addProvider(new BouncyCastleProvider());

		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(4096);

		KeyPair keyPair = keyPairGenerator.generateKeyPair();

		PrivateKey privateKey = keyPair.getPrivate();

		cert = SelfSignedCertGenerator.generate(keyPair, "SHA256withRSA", "localhost", 0);

		X509CertificateHolder certHolder = new X509CertificateHolder(cert.getEncoded());

		List<X509CertificateHolder> certChain = new ArrayList<X509CertificateHolder>();
		certChain.add(certHolder);

		Store certs = new JcaCertStore(certChain);

		CMSSignedDataGenerator cmsSignedDataGenerator = new CMSSignedDataGenerator();

		ContentSigner sha1Signer = new JcaContentSignerBuilder("SHA1withRSA").build(privateKey);
//				.setProvider(BouncyCastleProvider.PROVIDER_NAME).build(privateKey);

			CMSTypedData msg = new CMSProcessableByteArray(text.getBytes());

//		cmsSignedDataGenerator.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(
//				new JcaDigestCalculatorProviderBuilder().setProvider(BouncyCastleProvider.PROVIDER_NAME).build()).build(sha1Signer, cert));

		cmsSignedDataGenerator.addSignerInfoGenerator(
				new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().build()).build(sha1Signer,
						cert));

		cmsSignedDataGenerator.addCertificates(certs);

		CMSSignedData sigData = cmsSignedDataGenerator.generate(msg, true);

		System.out.println("[TestingX509Certificate] : cert : type :" + cert);

		System.out.println("[TestingX509Certificate] : cert : publicKey :" + cert.getPublicKey());

		System.out.println("[TestingX509Certificate] : cert : algName :" + cert.getSigAlgName());

		System.out.println("[TestingX509Certificate] : cert : issuerDN :" + cert.getNotAfter());

		System.out.println("[TestingX509Certificate] : sign : data :" + sigData);

//		String encodedSignedData = Base64.getEncoder().encodeToString(sigData.getEncoded());

//		String signedContent = Base64.getEncoder().encodeToString((byte[]) sigData.getSignedContent().getContent());
//
//		System.out.println("Signed content: " + signedContent + "\n");
//
//		String envelopedData = Base64.getEncoder().encodeToString(sigData.getEncoded());

//		System.out.println("Enveloped data: " + envelopedData);

		Thread.sleep(10000L);
		System.out.println("============== After Sleep ==========");

//		byte[] sig = Base64.getDecoder().decode(encodedSignedData);
////		
//		byte[] data_byte = new byte[sig.length];
//
//		CMSSignedData signedData = new CMSSignedData(new CMSProcessableByteArray(data_byte), sig);

//		============ TEST Start ===============


//		CMSSignedData signedData = new CMSSignedData(Base64.getDecoder().decode(envelopedData.getBytes()));
//
//		CMSProcessable cmsProcesableContent = new CMSProcessableByteArray(
//				Base64.getDecoder().decode(signedContent.getBytes()));
//
//		signedData = new CMSSignedData(cmsProcesableContent, Base64.getDecoder().decode(envelopedData.getBytes()));
//
//		Store store1 = signedData.getCertificates();
//
//		SignerInformationStore signers1 = signedData.getSignerInfos();
//		Collection c1 = signers1.getSigners();
//		Iterator it1 = c1.iterator();
//
//		while (it1.hasNext()) {
//
//			SignerInformation signer = (SignerInformation) it1.next();
//			Collection certCollection = store1.getMatches(signer.getSID());
//			Iterator certIt = certCollection.iterator();
//
//			X509CertificateHolder certHolder1 = (X509CertificateHolder) certIt.next();
//			X509Certificate certFromSignedData = new JcaX509CertificateConverter()
//					.getCertificate(certHolder1);
//			if (signer.verify(
//					new JcaSimpleSignerInfoVerifierBuilder().build(certFromSignedData))) {
//				System.out.println("Signature verified========== FUCK FUCK =====");
//				
//				CMSTypedData cmsTypedData = signedData.getSignedContent();
//
//				byte obj[] = (byte[]) cmsTypedData.getContent();
//				String aaa = new String(obj);
//				System.out.println("========== and the content is before FUCK  : " + aaa);
//
//			} else {
//				System.out.println("Signature verification failed ++++ YA FUCK ++++ YA FUCK ======");
//			}
//		}
		
		
//		8888888888888 ------------------- 888888888888888

//		Store<X509CertificateHolder> storeSignedCertificate = signedData.getCertificates();
//		SignerInformationStore signerInformationStore = signedData.getSignerInfos();
//
//		Collection<SignerInformation> signerInformationCollection = signerInformationStore.getSigners();
//		Iterator<SignerInformation> s = signerInformationCollection.iterator();
//		while (s.hasNext()) {
//			SignerInformation signerInformation = s.next();
//			System.out.println(" lets do the other way round : " + signerInformation.getSID());
//			Collection<X509CertificateHolder> certificateCollections = storeSignedCertificate
//					.getMatches(signerInformation.getSID());
//			
//			Iterator<X509CertificateHolder> certificateHolderIterator = certificateCollections.iterator();
//			X509CertificateHolder certificateHolder = certificateHolderIterator.next();
//			
//			X509Certificate certificateFromSignedData = new JcaX509CertificateConverter().getCertificate(certificateHolder);
//
//			System.out.println(" lets do the other way round : certificate \n : " + certificateFromSignedData);
//			
//			if(signerInformation.verify(new JcaSimpleSignerInfoVerifierBuilder().build(certificateFromSignedData))) {
//				System.out.println("====== YEAHHHHH");
//			}else {
//				System.out.println("====== NAYYYYY");
//				
//			}
//
//		}

//		============ TEST STOP ============

		// Verifying the Certificate
		Store store = sigData.getCertificates();
//		Store store = signedData.getCertificates();

		SignerInformationStore signers = sigData.getSignerInfos();
//		SignerInformationStore signers = signedData.getSignerInfos();

		Collection c = signers.getSigners();

		Iterator it = c.iterator();

		while (it.hasNext()) {
			SignerInformation signer = (SignerInformation) it.next();
			Collection certCollection = store.getMatches(signer.getSID());
			Iterator certIt = certCollection.iterator();

			X509CertificateHolder certHolder0 = (X509CertificateHolder) certIt.next();
			X509Certificate certFromSignedData = new JcaX509CertificateConverter().getCertificate(certHolder0);
			if (signer.verify(new JcaSimpleSignerInfoVerifierBuilder().build(certFromSignedData))) {
				System.out.println("Signature verified");

//				CMSTypedData cmsTypedData = sigData.getSignedContent();
//
//				byte obj[] = (byte[]) cmsTypedData.getContent();
//				String aaa = new String(obj);
//				System.out.println("========== and the content is : " + aaa);
			} else {
				System.out.println("Signature verification failed");
			}
		}
	}

}
