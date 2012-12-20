import java.util.List;
import java.util.*;

public class LaplaceUnigramLanguageModel implements LanguageModel {

	private Map<String, Integer> m_wordWeights;
	private int m_totalTokenCount;
	private int m_vocabularySize;
	
	/** Initialize your data structures in the constructor. */
	public LaplaceUnigramLanguageModel(HolbrookCorpus corpus) {
		m_wordWeights = new HashMap<String, Integer>();
		train(corpus);
	}

	/**
	 * Takes a corpus and trains your language model. Compute any counts or
	 * other corpus statistics in this function.
	 */
	public void train(HolbrookCorpus corpus) {
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
		m_vocabularySize = m_wordWeights.keySet().size();
	}

	/**
	 * Takes a list of strings as argument and returns the log-probability of
	 * the sentence using your language model. Use whatever data you computed in
	 * train() here.
	 */
	public double score(List<String> sentence) {
		double score = 0.0;
		for (String word : sentence) {
			double numerator = m_wordWeights.containsKey(word) ? m_wordWeights.get(word) + 1 : 1;
			double denominator = m_totalTokenCount + m_vocabularySize;
			double probability =   Math.log(numerator / denominator);  
			score += probability;
		}
		return score;
	}
}
