/**
 * 
 */
package talktome.nlp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.doccat.BagOfWordsFeatureGenerator;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.doccat.FeatureGenerator;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.model.ModelUtil;

/**
 * @author anees-ur-rehman
 *
 */
public class NLPUtils {
	
	private static Map<String, String> timeWithSequestMap = new HashMap<>();

	public static Map<String, String> wordWithGrammerMap = new HashMap<>();
	
	private static List<String> pricesBowList = new ArrayList<>();

	static DoccatModel model;

	static BundleContext bundleContext;

	public static DoccatModel trainCategorizerModel(BundleContext bundleContext,String resourcePath) throws FileNotFoundException, IOException {
		NLPUtils.bundleContext = bundleContext;
//		 File karafEtc = new File(System.getProperty("karaf.etc"));
		 
//		File file = new File(bundleContext.getBundle().getResource(resourcePath).getFile());
//		File file = new File(NLPUtils.class.getResource(resourcePath).getFile());
		File file = new File( System.getProperty( "karaf.etc" ) + File.separator + resourcePath );
		System.out.println("NLPUtils==>> the trainingFile is : "+ file);

		InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(file);
		
		ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8);
		
		ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

		DoccatFactory factory = new DoccatFactory(new FeatureGenerator[] { new BagOfWordsFeatureGenerator() });

		TrainingParameters params = ModelUtil.createDefaultTrainingParameters();
		params.put(TrainingParameters.CUTOFF_PARAM, 0);

		// Train a model with classifications from above file.
		DoccatModel model = DocumentCategorizerME.train("en", sampleStream, params, factory);
		return model;

	}
	
	
	/**
	 * Train categorizer model as per the category sample training data we created.
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static DoccatModel trainCategorizerModel() throws FileNotFoundException, IOException {
		// faq-categorizer.txt is a custom training data with categories as per our chat
		// requirements.
//		File file = new File(bundleContext.getBundle().getEntry("META-INF/training/kys-categorizer.txt").getFile());

		File file = new File( System.getProperty( "karaf.etc" ) + File.separator + "kys-categorizer.txt");

//		File file = new File(NLPUtils.class.getResource("META-INF/training/kys-categorizer.txt").getFile());
		InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(file);
		ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8);
		ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

		DoccatFactory factory = new DoccatFactory(new FeatureGenerator[] { new BagOfWordsFeatureGenerator() });

		TrainingParameters params = ModelUtil.createDefaultTrainingParameters();
		params.put(TrainingParameters.CUTOFF_PARAM, 0);

		// Train a model with classifications from above file.
		DoccatModel model = DocumentCategorizerME.train("en", sampleStream, params, factory);
		return model;
	}
	
	public static Map<String, String> getCategory(DoccatModel model, String inputString) {
		String answer = "";
		String repCategory = "";
		Map<String, String> categoryWithMsgMap = new HashMap<String, String>();

		try {
			categoryWithMsgMap.put("incomingText", inputString);
			String[] sentences = breakSentences(inputString);
			System.out.println(" incoming text : lengith : " + sentences.length);
			boolean conversationComplete = false;

			// Loop through sentences.
			for (String sentence : sentences) {

				// Separate words from each sentence using tokenizer.
				String[] tokens = tokenizeSentence(sentence);
				System.out.println(" the Tokens of the sentence : " + tokens);

				// Tag separated words with POS tags to understand their gramatical structure.
				String[] posTags = detectPOSTags(tokens);

				// Lemmatize each word so that its easy to categorize.
				String[] lemmas = lemmatizeTokens(tokens, posTags);

				// Determine BEST category using lemmatized tokens used a mode that we trained
				// at start.
				String category = detectCategory(model, lemmas);
				repCategory = category;
				System.out.println("========> category : " + category);

				detectTimename(tokens);

				detectName(tokens);

				getNounBasedTokens(tokens, posTags);
				// Get predefined answer from given category & add to answer.

//				System.out.println("========> questionAnswer : " + questionAnswer);

//				System.out.println("========> answer : " + questionAnswer.get(category));
//
//				answer = answer + " " + questionAnswer.get(category);
				StringBuilder replyBuilder = new StringBuilder();
				if(!"greeting".equals(category)) {
					if("seller".equals(category)) {
						replyBuilder.append("You want to Sell ");
						if(wordWithGrammerMap.entrySet().iterator().hasNext()) {
							String itenName = wordWithGrammerMap.entrySet().iterator().next().getKey();
							replyBuilder.append("Item = ");
							replyBuilder.append(itenName);
						}
					}else if("buyer".equals(category)) {
						replyBuilder.append("You want to Buy ");
						if(wordWithGrammerMap.entrySet().iterator().hasNext()) {
							String itenName = wordWithGrammerMap.entrySet().iterator().next().getKey();
							replyBuilder.append("Item = ");
							replyBuilder.append(itenName);
						}
					}
					
					if("accounts_open".equals(category))	{
						replyBuilder.append("Account Opening Form is by clicking down ");
					}
					
					if("account_transfer".equals(category)) {
						replyBuilder.append("Account Transfer from current account to other account Form is by clicking down ");
					}

					categoryWithMsgMap.put("category", category);
					categoryWithMsgMap.put("reply", replyBuilder.toString());
					
				}else {
					replyBuilder.append("Please use I want to buy|sell [product]");
					categoryWithMsgMap.put("category", category);
					categoryWithMsgMap.put("reply", replyBuilder.toString());
					
				}

//				// If category conversation-complete, we will end chat conversation.
//				if ("conversation-complete".equals(category)) {
//					conversationComplete = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("##### Chat Bot: " + answer);

		return categoryWithMsgMap;
	}

	/**
	 * Detect category using given token. Use categorizer feature of Apache OpenNLP.
	 * 
	 * @param model
	 * @param finalTokens
	 * @return
	 * @throws IOException
	 */
	public static String detectCategory(DoccatModel model, String[] finalTokens) throws IOException {

		// Initialize document categorizer tool
		DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);

		// Get best possible category.
		double[] probabilitiesOfOutcomes = myCategorizer.categorize(finalTokens);
		String category = myCategorizer.getBestCategory(probabilitiesOfOutcomes);
		System.out.println("Category: " + category);

		return category;

	}

	/**
	 * Break data into sentences using sentence detection feature of Apache OpenNLP.
	 * 
	 * @param data
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String[] breakSentences(String data) throws FileNotFoundException, IOException {
		// Better to read file once at start of program & store model in instance
		// variable. but keeping here for simplicity in understanding.
//		OpenNLPChatBot.class.getResource("/en-sent.bin").getFile();
//		File file = new File(bundleContext.getBundle().getEntry(resourcePath).getFile());
//		File file = new File( System.getProperty( "karaf.etc" ) + File.separator + "en-sent.bin");


//		try (InputStream modelIn = new FileInputStream(
//				new File(NLPUtils.class.getResource("META-INF/models/en-sent.bin").getFile()))) {

		try (InputStream modelIn = new FileInputStream(
				new File( System.getProperty( "karaf.etc" ) + File.separator + "en-sent.bin") )) {

			SentenceDetectorME myCategorizer = new SentenceDetectorME(new SentenceModel(modelIn));

			String[] sentences = myCategorizer.sentDetect(data);
			System.out.println("Sentence Detection: " + Arrays.stream(sentences).collect(Collectors.joining(" | ")));

			return sentences;
		}
	}

	/**
	 * Break sentence into words & punctuation marks using tokenizer feature of
	 * Apache OpenNLP.
	 * 
	 * @param sentence
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String[] tokenizeSentence(String sentence) throws FileNotFoundException, IOException {
		// Better to read file once at start of program & store model in instance
		// variable. but keeping here for simplicity in understanding.
//		new File(OpenNLPChatBot.class.getResource("/en-token.bin").getFile());
//		try (InputStream modelIn = new FileInputStream(
//				new File(NLPUtils.class.getResource("META-INF/models/en-token.bin").getFile()))) {

			try (InputStream modelIn = new FileInputStream(
					new File( System.getProperty( "karaf.etc" ) + File.separator + "en-token.bin")  )) {

			
			// Initialize tokenizer tool
			TokenizerME myCategorizer = new TokenizerME(new TokenizerModel(modelIn));

			// Tokenize sentence.
			String[] tokens = myCategorizer.tokenize(sentence);
			System.out.println("Tokenizer : " + Arrays.stream(tokens).collect(Collectors.joining(" | ")));

			return tokens;

		}
	}

	/**
	 * Find part-of-speech or POS tags of all tokens using POS tagger feature of
	 * Apache OpenNLP.
	 * 
	 * @param tokens
	 * @return
	 * @throws IOException
	 */
	public static String[] detectPOSTags(String[] tokens) throws IOException {
		// Better to read file once at start of program & store model in instance
		// variable. but keeping here for simplicity in understanding.
		;
//		try (InputStream modelIn = new FileInputStream(
//				new File(NLPUtils.class.getResource("META-INF/models/en-pos-maxent.bin").getFile()))) {

		try (InputStream modelIn = new FileInputStream(
				new File( System.getProperty( "karaf.etc" ) + File.separator + "en-pos-maxent.bin") )) {

			// Initialize POS tagger tool
			POSTaggerME myCategorizer = new POSTaggerME(new POSModel(modelIn));

			// Tag sentence.
			String[] posTokens = myCategorizer.tag(tokens);
			System.out.println("POS Tags : " + Arrays.stream(posTokens).collect(Collectors.joining(" | ")));

			return posTokens;

		}

	}

	/**
	 * 
	 * @param tokens
	 * @throws IOException
	 */
	private static void getNounBasedTokens(String[] tokens, String[] posTags) throws IOException {
//		try (InputStream modelIn = new FileInputStream(
//				new File(NLPUtils.class.getResource("META-INF/models/en-chunker.bin").getFile()))) {
			
		try (InputStream modelIn = new FileInputStream(
				new File( System.getProperty( "karaf.etc" ) + File.separator + "en-chunker.bin") )) {
			
			
//		try (InputStream modelIn = new FileInputStream(new File(OpenNLPChatBot.class.getResource("/en-parser-chunking.bin").getFile()))) {

//			System.out.println("[NLPUtils] : {getNounBasedTokens} : "+tokens);
//			
//			ParserModel parserModel = new ParserModel(modelIn);
//			opennlp.tools.parser.Parser parser = ParserFactory.create(parserModel);
//			
//			parser.parse(tokens);
			ChunkerModel model = new ChunkerModel(modelIn);
			ChunkerME chunkerMe = new ChunkerME(model);
			String chunk[] = chunkerMe.chunk(tokens, posTags);

//		 Span[] spans =	chunkerMe.chunkAsSpans(tokens, posTags);

//		 double probs[] = chunkerMe.probs();
//		 
//		 for(double s: probs) {
//			 System.out.println("s==> "+s);
//		 }
			wordWithGrammerMap.clear();
			for (int i = 0; i < chunk.length; i++) {
				String tags = posTags[i];
				if (tags.equals("NN")) {
					wordWithGrammerMap.put(tokens[i], posTags[i]);
					System.out.println(tokens[i] + " - " + posTags[i] + " - " + chunk[i]);
				} else if(tags.equals("NNS")) {
					wordWithGrammerMap.put(tokens[i], posTags[i]);
					System.out.println(tokens[i] + " - " + posTags[i] + " - " + chunk[i]);
					
				}
			}

//			Span[] nameSpans= nameFinderMe.find(tokens);
//			
//			String[] array=Span.spansToStrings(nameSpans,tokens);
//			
//			System.out.println("detectName Tags : " + Arrays.stream(array).collect(Collectors.joining(" | ")));
//			
//			for(int i = 0; i < array.length; i++) {
//				timeWithSequestMap.put(String.valueOf(i), String.valueOf(array[i]));
//			}
//			NameFinderME myCategorizer = new NameFinderME(new TokenNameFinderModel(modelIn));
//
//			// Tag sentence.
//			Span[] posTokens = myCategorizer.find(tokens);
//			System.out.println("Date/Time Tags : " + Arrays.stream(posTokens).findAny().get());

//			return posTokens;

		}

	}
	
	public static String verifyNounsFromBOW() {
		return "";
	}

	/**
	 * 
	 * @param tokens
	 * @throws IOException
	 */
	private static void detectName(String[] tokens) throws IOException {
//		en-ner-person.bin
//		try (InputStream modelIn = new FileInputStream(
//				new File(NLPUtils.class.getResource("META-INF/models/en-ner-person.bin").getFile()))) {

		try (InputStream modelIn = new FileInputStream(
				new File( System.getProperty( "karaf.etc" ) + File.separator + "en-ner-person.bin") )) {

			// Initialize POS tagger tool
			TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
			System.out.println("detectName : Model : " + model);
			NameFinderME nameFinderMe = new NameFinderME(model);
			Span[] nameSpans = nameFinderMe.find(tokens);

			String[] array = Span.spansToStrings(nameSpans, tokens);

			System.out.println("detectName Tags : " + Arrays.stream(array).collect(Collectors.joining(" | ")));

			for (int i = 0; i < array.length; i++) {
				timeWithSequestMap.put(String.valueOf(i), String.valueOf(array[i]));
			}
//			NameFinderME myCategorizer = new NameFinderME(new TokenNameFinderModel(modelIn));
//
//			// Tag sentence.
//			Span[] posTokens = myCategorizer.find(tokens);
//			System.out.println("Date/Time Tags : " + Arrays.stream(posTokens).findAny().get());

//			return posTokens;

		}
	}

	private static void detectTimename(String[] tokens) throws IOException {
//		try (InputStream modelIn = new FileInputStream(
//				new File(NLPUtils.class.getResource("META-INF/models/en-ner-date.bin").getFile()))) {

		try (InputStream modelIn = new FileInputStream(
				new File( System.getProperty( "karaf.etc" ) + File.separator + "en-ner-date.bin") )) {

			// Initialize POS tagger tool
			TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
			System.out.println("detectTimename : Model : " + model);
			NameFinderME nameFinderMe = new NameFinderME(model);
			Span[] nameSpans = nameFinderMe.find(tokens);

			String[] array = Span.spansToStrings(nameSpans, tokens);

			System.out.println("TimeTags Tags : " + Arrays.stream(array).collect(Collectors.joining(" | ")));

//			NameFinderME myCategorizer = new NameFinderME(new TokenNameFinderModel(modelIn));
//
//			// Tag sentence.
//			Span[] posTokens = myCategorizer.find(tokens);
//			System.out.println("Date/Time Tags : " + Arrays.stream(posTokens).findAny().get());

//			return posTokens;

		}

	}
	
	
//	private static void detectMoney(String[] tokens) throws IOException {
//		
//		try (InputStream modelIn = new FileInputStream(
//				new File(OpenNLPChatBot.class.getResource("/en-ner-money.bin").getFile()))) {
//			
//			
//		}
//	}

	/**
	 * Find lemma of tokens using lemmatizer feature of Apache OpenNLP.
	 * 
	 * @param tokens
	 * @param posTags
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static String[] lemmatizeTokens(String[] tokens, String[] posTags)
			throws InvalidFormatException, IOException {
		// Better to read file once at start of program & store model in instance
		// variable. but keeping here for simplicity in understanding.
//		new File(OpenNLPChatBot.class.getResource("/en-lemmatizer.bin").getFile());
//		try (InputStream modelIn = new FileInputStream(
//				new File(NLPUtils.class.getResource("META-INF/models/en-lemmatizer.bin").getFile()))) {

		try (InputStream modelIn = new FileInputStream(
				new File( System.getProperty( "karaf.etc" ) + File.separator + "en-lemmatizer.bin") )) {
		
			// Tag sentence.
			LemmatizerME myCategorizer = new LemmatizerME(new LemmatizerModel(modelIn));
			String[] lemmaTokens = myCategorizer.lemmatize(tokens, posTags);
			System.out.println("Lemmatizer : " + Arrays.stream(lemmaTokens).collect(Collectors.joining(" | ")));

			return lemmaTokens;

		}
	}



}
