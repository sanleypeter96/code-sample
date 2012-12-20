import java.util.*; 

public class LaplaceBigramLanguageModel implements LanguageModel {

	private Map<String, Map<String, Integer>> m_bigramMatrix; 
	private Set<String> m_vocabularySet;
	private int m_vocabularySize; 
	
	/** Initialize your data structures in the constructor. */
	public LaplaceBigramLanguageModel(HolbrookCorpus corpus) {
		m_bigramMatrix = new HashMap<String, Map<String, Integer>>(); 
		m_vocabularySet = new HashSet<String>(); 
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
			}
		}
		m_vocabularySize = m_vocabularySet.size(); 
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
			double probability =  score(sentence, i);   
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
		double numerator = 0;
		double denominator = 0;
		if (!m_bigramMatrix.containsKey(prevWord))
		{
			numerator = 1;
			denominator = m_vocabularySize; 
		}
		else
		{
			numerator = m_bigramMatrix.get(prevWord).containsKey(word) ? m_bigramMatrix.get(prevWord).get(word) + 1 : 1;
			Map<String, Integer> allWordsStartWithPrevWord = m_bigramMatrix.get(prevWord);
			for (String nextWord : allWordsStartWithPrevWord.keySet())
			{
				denominator += allWordsStartWithPrevWord.get(nextWord); 
			}
			denominator += m_vocabularySize;	 
		}
		return Math.log(numerator / denominator);
	}
}
