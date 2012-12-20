//CS124 HW6 Wikipedia Relation Extraction
//Alan Joyce (ajoyce)

import java.util.regex.*;
import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Wiki {

	public static class Entity {
		public String Name;
		public String Text;
		
		public Entity(String name, String infoBox)
		{
			Name = name;
			Text = infoBox; 
		}
	}
	
	Map<String, ArrayList<String>> nameToSpouseMap; 
	List<String> sentencesWithMarriage;

	public List<String> addWives(String fileName) {
		List<String> wives = new ArrayList<String>();
		try {
			BufferedReader input = new BufferedReader(new FileReader(fileName));
			// for each line
			for (String line = input.readLine(); line != null; line = input
					.readLine()) {
				wives.add(line);
			}
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
		return wives;
	}

	/*
	 * read through the wikipedia file and attempts to extract the matching
	 * husbands. note that you will need to provide two different
	 * implementations based upon the useInfoBox flag.
	 */
	public List<String> processFile(File f, List<String> wives, boolean useInfoBox) 
	{
		if (useInfoBox)
		{
			return getHusbandsUsingInfoBox(f, wives);
		}
		else 
		{
			return getHusbandsWithoutUsingInfoBox(f, wives); 
		}
	}
	
	private List<String> getHusbandsWithoutUsingInfoBox(File f, List<String> wives)
	{
		List<String> husbands = new ArrayList<String>();
		Document doc = parseXmlFile(f);
		processDom(doc, false);
		
		for (String sentence : sentencesWithMarriage)
		{
			System.out.println(sentence);
			System.out.println("------------------------------");
		}
		
		processSentencesWithMarriage();
		
		for (String wife : wives) {
			String mostSimilarWife = findMostSimilarWife(wife);
			if (mostSimilarWife == null)
			{
				husbands.add("No Answer");
			}
			else
			{
				ArrayList<String> list = nameToSpouseMap.get(mostSimilarWife);
				if (list.size() > 0) 
				{
					String spouse = list.get(0);
					husbands.add("Who is " + spouse + "?");
				}
			}
		}
		return husbands; 
	}
	
	private void processSentencesWithMarriage()
	{
		for (int i = 0; i < sentencesWithMarriage.size(); i++)
		{
			String sentence = sentencesWithMarriage.get(i);
			updateNameToSpouseMap(sentence);
		}
	}
	
	private void updateNameToSpouseMap(String sentence)
	{
		String pattern = "married (?:to)*\\s*(.*?)[\\s,]+[a-z,]";
		Pattern datePatt = Pattern.compile(pattern, Pattern.MULTILINE);
		Matcher m = datePatt.matcher(sentence);
		while (m.find()) {
			String name = entity.Name.trim();;
			
			String spouse = m.group(2);
			String[] spouses = getNamesFromRawString(spouse); 
			for(int i = 0; i < spouses.length; i++)
			{
				if (!nameToSpouseMap.containsKey(name))
				{
					ArrayList<String> list = new ArrayList<String>();
					list.add(spouses[i]);
					nameToSpouseMap.put(name, list);
				}
				else
				{
					ArrayList<String> list = nameToSpouseMap.get(name);
					list.add(spouses[i]);
				}
				if (!nameToSpouseMap.containsKey(spouses[i]))
				{
					ArrayList<String> list = new ArrayList<String>();
					list.add(name);
					nameToSpouseMap.put(spouses[i], list);
				}
				else
				{
					ArrayList<String> list = nameToSpouseMap.get(spouses[i]);
					list.add(name);
				}
			}
		}
	}
	
	private List<String> getHusbandsUsingInfoBox(File f, List<String> wives)
	{
		List<String> husbands = new ArrayList<String>();
		Document doc = parseXmlFile(f);
		processDom(doc, true);
		
		for (String wife : wives) {
			String mostSimilarWife = findMostSimilarWife(wife);
			if (mostSimilarWife == null)
			{
				husbands.add("No Answer");
			}
			else
			{
				ArrayList<String> list = nameToSpouseMap.get(mostSimilarWife);
				if (list.size() > 0) 
				{
					String spouse = list.get(0);
					husbands.add("Who is " + spouse + "?");
				}
			}
		}
		return husbands;
	}

	private String findMostSimilarWife(String wife)
	{
		String bestAlternateWife = null;
		int bestScore = -1; 
		for (String alternateWife : nameToSpouseMap.keySet())
		{
			if (alternateWife.equals(wife))
			{
				return alternateWife;
			}
			int score = getCosineSimilarity(wife, alternateWife);
			if (score >= 2 &&  score > bestScore)
			{
				bestScore = score; 
				bestAlternateWife = alternateWife; 
			}
		}
		return bestAlternateWife;
	}
	
	public int getCosineSimilarity(String wife, String alternateWife)
	{
		String[] wifeSplit = wife.split(" ");
		String[] alternateWifeSplit = alternateWife.split(" ");
		List<String> alternateWifeList = new ArrayList<String>(Arrays.asList(alternateWifeSplit));
		Set<String> set = new HashSet<String>();
		set.addAll(new ArrayList<String>(Arrays.asList(wifeSplit)));
		set.retainAll(alternateWifeList);
		return set.size(); 
	}
	
	private Document parseXmlFile(File f){
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
			Document dom = db.parse(f);
			return dom; 
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null; 
	}
	
	private String getFileAsString(File f) {
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
					fc.size());
			/* Instead of using default, pass in a decoder. */
			stream.close();
			return Charset.defaultCharset().decode(bb).toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Entity getEntity(Element element)
	{
		String name = getTextValue(element,"title");
		String infoBox = getTextValue(element,"text");
		Entity e = new Entity(name, infoBox); 
		return e;
	}
	
	/**
	 * I take a xml element and the tag name, look for the tag and get
	 * the text content
	 * i.e for <employee><name>John</name></employee> xml snippet if
	 * the Element points to employee node and tagName is 'name' I will return John
	 */
	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}
		return textVal;
	}
	
	private void processDom(Document dom, boolean useInfoBox) {
		Element docEle = dom.getDocumentElement();
		List<Entity> list = new ArrayList<Entity>(); 
		NodeList nl = docEle.getElementsByTagName("page");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				Element el = (Element)nl.item(i);
				Entity e = getEntity(el);
				list.add(e);
			}
		}
		for (int i = 0; i < list.size(); i++)
		{
			Entity entity = list.get(i);
			if (useInfoBox)
			{
				PopulateMap(entity);
			}
			else
			{
				PopulateMapWithoutInfoBox(entity);
			}
		}
	}
	
	private void PopulateMapWithoutInfoBox(Entity entity)
	{
		if (nameToSpouseMap == null)
		{
			nameToSpouseMap = new HashMap<String, ArrayList<String>>(); 
		}
		
		if (sentencesWithMarriage == null)
		{
			sentencesWithMarriage = new ArrayList<String>(); 
		}
		
		String pattern = "(?i)((?=[^.\\n]*\\bmarried\\b)[^.\\n]+\\.?)";
		Pattern datePatt = Pattern.compile(pattern, Pattern.MULTILINE);
		Matcher m = datePatt.matcher(entity.Text);
		while (m.find()) 
		{
			String sentence = m.group(1);
			sentencesWithMarriage.add(sentence); 
		}
	}
	
	private void PopulateMap(Entity entity)
	{ 
		if (nameToSpouseMap == null)
		{
			nameToSpouseMap = new HashMap<String, ArrayList<String>>(); 
		}
		
		String pattern = ".*?Infobox[\\s\\S]*?\\|\\s*?[n|N]ame\\s*?=\\s*?(.*)[\\s\\S]*?\\|\\s*?[sS]pouse\\s*?=\\s*?(.*)";
		Pattern datePatt = Pattern.compile(pattern, Pattern.MULTILINE);
		Matcher m = datePatt.matcher(entity.Text);
		while (m.find()) {
			String name = entity.Name.trim();;
			
			String spouse = m.group(2);
			String[] spouses = getNamesFromRawString(spouse); 
			for(int i = 0; i < spouses.length; i++)
			{
				if (!nameToSpouseMap.containsKey(name))
				{
					ArrayList<String> list = new ArrayList<String>();
					list.add(spouses[i]);
					nameToSpouseMap.put(name, list);
				}
				else
				{
					ArrayList<String> list = nameToSpouseMap.get(name);
					list.add(spouses[i]);
				}
				if (!nameToSpouseMap.containsKey(spouses[i]))
				{
					ArrayList<String> list = new ArrayList<String>();
					list.add(name);
					nameToSpouseMap.put(spouses[i], list);
				}
				else
				{
					ArrayList<String> list = nameToSpouseMap.get(spouses[i]);
					list.add(name);
				}
			}
		}
	}
	
	private String[] getNamesFromRawString(String rawString)
	{
		String pattern = "\\\"(.*?)\\\"\\s{1}";
		String result = rawString.replaceAll(pattern, "");
		pattern = "\\((.*?)\\)";
		result = result.replaceAll(pattern, "");
		result = result.replaceAll("\\[\\[", "").replaceAll("\\]\\]", "");
		result = result.replaceAll("\\{\\{", "").replaceAll("\\}\\}", "");
		result = result.replaceAll("nowrap\\|", "");
		String[] multipleNames = result.split("<br.*?>");
		List<String> list = new ArrayList<String>();
		
		for (int i = 0; i < multipleNames.length; i++)
		{
			String name = multipleNames[i];
			String[] splitBySeparator = name.split("\\|");
			for(int j = 0; j < splitBySeparator.length; j++)
			{
				list.add(splitBySeparator[j].trim());
			}
		}
		String[] resultArray = new String[list.size()]; 
		for (int i = 0; i < list.size(); i++)
		{
			//System.out.print(list.get(i) + ", "); 
			resultArray[i] = list.get(i);
		}
		//System.out.println();
		
		return resultArray; 
	}

	/*
	 * scores the results based upon the aforementioned criteria
	 */
	public void evaluateAnswers(boolean useInfoBox, List<String> husbandsLines,
			String goldFile) {
		int correct = 0;
		int wrong = 0;
		int noAnswers = 0;
		int score = 0;
		try {
			BufferedReader goldData = new BufferedReader(new FileReader(
					goldFile));
			List<String> goldLines = new ArrayList<String>();
			String line;
			while ((line = goldData.readLine()) != null) {
				goldLines.add(line);
			}
			if (goldLines.size() != husbandsLines.size()) {
				System.err
						.println("Number of lines in husbands file should be same as number of wives!");
				System.exit(1);
			}
			for (int i = 0; i < goldLines.size(); i++) {
				String husbandLine = husbandsLines.get(i).trim();
				String goldLine = goldLines.get(i).trim();
				boolean exampleWrong = true; // guilty until proven innocent
				if (husbandLine.equals("No Answer")) {
					exampleWrong = false;
					noAnswers++;
				} else { // check if correct.
					String[] golds = goldLine.split("\\|");
					for (String gold : golds) {
						if (husbandLine.equals(gold)) {
							correct++;
							score++;
							exampleWrong = false;
							break;
						}
					}
				}
				if (exampleWrong) {
					wrong++;
					score--;
				}
			}
			goldData.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Correct Answers: " + correct);
		System.out.println("No Answers     : " + noAnswers);
		System.out.println("Wrong Answers  : " + wrong);
		System.out.println("Total Score    : " + score);

	}

	public static void main(String[] args) {
		String wikiFile = "../data/small-wiki.xml";
		String wivesFile = "../data/wives.txt";
		String goldFile = "../data/gold.txt";
		boolean useInfoBox = false;
		Wiki pedia = new Wiki();
		List<String> wives = pedia.addWives(wivesFile);
		List<String> husbands = pedia.processFile(new File(wikiFile), wives,
				useInfoBox);
		pedia.evaluateAnswers(useInfoBox, husbands, goldFile);
	}
}
