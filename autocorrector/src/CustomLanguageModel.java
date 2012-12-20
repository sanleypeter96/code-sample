import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomLanguageModel implements LanguageModel {

	private Map<String, Map<String, Integer>> m_bigramMatrix; 
	private Map<String, Map<String, Integer>> m_bigramMatrixWordPrevWord; 
	private Set<String> m_vocabularySet;
	private int m_vocabularySize;
	private Map<String, Integer> m_wordWeights;
	private int m_totalTokenCount;
	private int m_countWordsRightBeforeAllWords; 
	private static final double D = 0.75; 
	private static final double SMALLOFFSET = 0.000001; 
	
	/** Initialize your data structures in the constructor. */
	public CustomLanguageModel(HolbrookCorpus corpus) {
		m_bigramMatrix = new HashMap<String, Map<String, Integer>>(); 
		m_bigramMatrixWordPrevWord = new HashMap<String, Map<String, Integer>>();
		m_vocabularySet = new HashSet<String>();
		m_wordWeights = new HashMap<String, Integer>();
		train(corpus);
	}

	/**
	 * Takes a corpus and trains your language model. Compute any counts or
	 * other corpus statistics in this function.
	 */
	public void train(HolbrookCorpus corpus) {
		for (Sentence sentence : corpus.getData()) {
			for(int i = 0; i < sentence.size(); i++)
			{
				putInMatrix(sentence, i);
				putInMatrixWordPrevWord(sentence, i);
			}
		}
		
		for (Sentence sentence : corpus.getData()) {
			for (Datum datum : sentence) { 
				String word = datum.getWord(); 
				if (m_wordWeights.containsKey(word))
				{
					m_wordWeights.put(word, new Integer(m_wordWeights.get(word) + 1));
				}
				else
				{
					m_wordWeights.put(word, new Integer(1));
				}
				m_totalTokenCount++;
			}
		}
		m_vocabularySize = m_vocabularySet.size();
		m_countWordsRightBeforeAllWords = getCountForWordsRightBeforeAllWords(); 
	}
	
	private void putInMatrixWordPrevWord(Sentence sentence, int index)
	{
		if (index < 0 || index - 1 < 0) 
			return; 
		Datum datum = sentence.get(index);
		Datum prevDatum = sentence.get(index - 1);
		String word = datum.getWord();
		String prevWord = prevDatum.getWord();
		if (m_bigramMatrixWordPrevWord.containsKey(word) && m_bigramMatrixWordPrevWord.get(word).containsKey(prevWord))
		{
			Integer updatedWeight = new Integer(m_bigramMatrixWordPrevWord.get(word).get(prevWord) + 1); 
			m_bigramMatrixWordPrevWord.get(word).put(prevWord, updatedWeight); 
		}
		else if (m_bigramMatrixWordPrevWord.containsKey(word) && !m_bigramMatrixWordPrevWord.get(word).containsKey(prevWord))
		{
			Integer updatedWeight = new Integer(1);
			m_bigramMatrixWordPrevWord.get(word).put(prevWord, updatedWeight); 
		}
		else 
		{
			Integer updatedWeight = new Integer(1); 
			Map<String, Integer> laterMap = new HashMap<String, Integer>();
			laterMap.put(prevWord, updatedWeight);
			m_bigramMatrixWordPrevWord.put(word, laterMap);
		}
	}
	
	private void putInMatrix(Sentence sentence, int index)
	{
		if (index < 0 || index - 1 < 0) 
			return; 
		Datum datum = sentence.get(index);
		Datum prevDatum = sentence.get(index - 1);
		String word = datum.getWord();
		String prevWord = prevDatum.getWord();
		m_vocabularySet.add(word);
		if (m_bigramMatrix.containsKey(prevWord) && m_bigramMatrix.get(prevWord).containsKey(word))
		{
			Integer updatedWeight = new Integer(m_bigramMatrix.get(prevWord).get(word) + 1); 
			m_bigramMatrix.get(prevWord).put(word, updatedWeight); 
		}
		else if (m_bigramMatrix.containsKey(prevWord) && !m_bigramMatrix.get(prevWord).containsKey(word))
		{
			Integer updatedWeight = new Integer(1);
			m_bigramMatrix.get(prevWord).put(word, updatedWeight); 
		}
		else 
		{
			Integer updatedWeight = new Integer(1); 
			Map<String, Integer> laterMap = new HashMap<String, Integer>();
			laterMap.put(word, updatedWeight);
			m_bigramMatrix.put(prevWord, laterMap);
		}
	}

	/**
	 * Takes a list of strings as argument and returns the log-probability of
	 * the sentence using your language model. Use whatever data you computed in
	 * train() here.
	 */
	public double score(List<String> sentence) {
		double score = 0.0;
		for (int i = 0; i < sentence.size(); i++) {
			double probability = score(sentence, i);
			score += probability;
		}
		return score;
	}
	
	private double score(List<String> sentence, int index)
	{
		if (index < 0 || index -1 < 0)
			return 0.0;
		String word = sentence.get(index);
		String prevWord = sentence.get(index-1);
		double partA = calculatePartA(prevWord, word); 
		double partB = calculatePartB(prevWord, word); 
		if ((partA + partB) == 0)
			return Math.log(SMALLOFFSET); 
		return Math.log(partA + partB + SMALLOFFSET);
	}
	
	private double calculatePartB(String prevWord, String word)
	{
		double partB = 0; 
		double countWi_1 = calculateCountForWord(prevWord);
		double countForWordsRightAfterWi_1 = getCountForWordsRightAfterWord(prevWord);
		double lambda = D * countForWordsRightAfterWi_1 / countWi_1;
		double countForWordsRightBeforeWi = getCountForWordsRightBeforeWord(word); 
		double p_continuation = countForWordsRightBeforeWi / m_countWordsRightBeforeAllWords; 
		partB = lambda * p_continuation;
		return partB; 
	}
	
	private double calculatePartA(String prevWord, String word)
	{
		double partA = 0; 
		double countWi_1 = calculateCountForWord(prevWord);
		double countWi_1Wi = calculateCountForPrevWordWord(prevWord, word);
		if (countWi_1Wi != 0)
		{
			partA = Math.max(countWi_1Wi - D, 0) / countWi_1; 
		}
		return partA; 
	}

	private double calculateCountForPrevWordWord(String prevWord, String word)
	{
		if (!m_bigramMatrix.containsKey(prevWord) || !m_bigramMatrix.get(prevWord).containsKey(word)) 
			return 0;
		return m_bigramMatrix.get(prevWord).get(word); 
	}
	
	private int calculateCountForWord(String word)
	{
		if (!m_wordWeights.containsKey(word))
			return 1; 
		return m_wordWeights.get(word); 
	}
	
	private int getCountForWordsRightAfterWord(String prevWord)
	{
		if (!m_bigramMatrix.containsKey(prevWord))
			return 0;
		else 
		{
			Map<String, Integer> allWordsStartWithPrevWord = m_bigramMatrix.get(prevWord);
			return allWordsStartWithPrevWord.keySet().size(); 
		}
	}
	
	private int getCountForWordsRightBeforeWord(String word)
	{
		if (!m_bigramMatrixWordPrevWord.containsKey(word))
		{
			return 0; 
		}
		return m_bigramMatrixWordPrevWord.get(word).keySet().size();  
	}
	
	private int getCountForWordsRightBeforeAllWords()
	{
		int result = 0; 
		for (String word : m_vocabularySet)
		{
			result += getCountForWordsRightBeforeWord(word);
		}
		return result; 
	}
}
