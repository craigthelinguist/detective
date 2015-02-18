package stats;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Histogram <KEY> {
	
	// Fields.
	// --------------------------------------------------------------------------------

	private Map <KEY, Integer> hist;
	private Map <KEY, Double> histNormed;
	private int numObservations;
	private boolean smoothed = false; // whether you're using LaPlace smoothing or not, default false
	

	// Constants.
	// --------------------------------------------------------------------------------

	private static final char[] alphabet = new char[]{ 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 
													   'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	private static final Map<Integer, List<String>> NGRAM_MAP = new HashMap<>();
	static {
		NGRAM_MAP.put(1, allNgrams(alphabet, 1));
		NGRAM_MAP.put(2, allNgrams(alphabet, 2));
		NGRAM_MAP.put(3, allNgrams(alphabet, 3));
	}
	
	
	
	// Constructors.
	// --------------------------------------------------------------------------------
	public Histogram () {
		hist = new HashMap<>();
		histNormed = new HashMap<>();
		numObservations = 0;
	}
	
	public Histogram (Iterable<KEY> observations) {
		hist = new HashMap<>();
		numObservations = 0;
		for (KEY k : observations) this.add(k, 1);
		this.normalise();
	}
	
	
	
	// Instance methods.
	// --------------------------------------------------------------------------------

	/**
	 * Add one observation for a given key.
	 * @param k: key that was observed.
	 */
	public void add (KEY k) {
		this.add(k, 1);
	}
	
	/**
	 * Add a number of observations for a given key to the histogram.
	 * @param k: key you'll be adding observations for.
	 * @param count: number of times key was observed.
	 */
	public void add (KEY k, int count) {
		if (hist.containsKey(k)) hist.put(k, hist.get(k) + count);
		else hist.put(k, count);
		numObservations += count;
		histNormed = null;
	}
	
	/**
	 * Get the number of times the given key appears in the histogram.
	 * @param k: key to check
	 * @return int
	 */
	public int getCount (KEY k) {
		if (hist.containsKey(k)) return hist.get(k);
		else return 0;
	}
	
	/**
	 * Get the probability that the given key appears in the histogram.
	 * @param k: key to check.
	 * @return double
	 */
	public double getProb (KEY k) {
		if (histNormed == null)
			throw new UnsupportedOperationException("Histogram must be normed before getting probability.");
		if (histNormed.containsKey(k)) return histNormed.get(k);
		else return 0.0;
	}
	
	/**
	 * Compute the normalisation of this histogram so you can check the
	 * probability of a given key. Normalisation must be re-computed everytime
	 * you add keys to the histogram.
	 */
	public void normalise () {
		if (this.histNormed != null) return; // already been normed
		this.histNormed = new HashMap<>();
		for (KEY k : hist.keySet()) {
			int count = hist.get(k);
			histNormed.put(k, 1.0 * count / numObservations);
		}
	}
	
	/**
	 * Return the number of observations in this histogram.
	 * @return int
	 */
	public int observations () {
		return this.numObservations;
	}
	
	
	
	// Iterators.
	// --------------------------------------------------------------------------------

	public Collection<Double> getProbabilities () {
		if (this.histNormed == null)
			throw new UnsupportedOperationException("Histogram must be normed before getting probabilities.");
		return this.histNormed.values();
	}
	
	public Collection<Integer> getCounts () {
		return this.hist.values();
	}
	
	public Collection<KEY> getKeys () {
		return this.hist.keySet();
	}

	

	// Static methods for automatically making ngram histograms.
	// --------------------------------------------------------------------------------
	
	/**
	 * Construct a histogram that counts the frequency of ngrams in a given collection of strings.
	 * @param s1: collection of strings.
	 * @param degree: degree of n-grams to count. E.g.: 2 means count all bigrams.
	 * @return histogram (which has already been normalised).
	 */
	public static Histogram<String> ngrams(Collection<String> s1, int degree) {
		return ngrams(s1, degree, false);
	}

	/**
	 * Compute all the ngrams of a certain degree that can be made from the given alphabet.
	 * @param unigrams: the alphabet of valid characters.
	 * @param degree: degree of ngrams to compute
	 * @return list of all ngrams of the specified degree.
	 */
	private static List<String> allNgrams (char[] unigrams, int degree) {
		
		// make initial list of unigrams
		List<StringBuilder> ngrams = new ArrayList<>();
		for (char c : unigrams) ngrams.add(new StringBuilder(""+c));
		
		// keep adding
		for (int i = 0; i < degree - 1; i++) {
			List<StringBuilder> newNgrams = new ArrayList<>();
			for (char c : unigrams) {
				for (StringBuilder sb : ngrams) {
					newNgrams.add(new StringBuilder(sb.toString() + c));
				}
			}
		}
		
		// turn into list<String>, return
		List<String> finalNgrams = new ArrayList<>();
		for (StringBuilder sb : ngrams) {
			finalNgrams.add(sb.toString());
		}
		return finalNgrams;
	}
	
	/**
	 * Compute a histogram of the ngrams in the given collection of strings. Assumes that the valid
	 * alphabet of character is [a-z]
	 * @param s1: collection of strings
	 * @param degree: degree of ngrams to take
	 * @param smoothing: whether to employ LaPlace smoothing. the valid alphabet is assumed to be [a-z]
	 * @return Histogram<String> that has already been normalised
	 */
	public static Histogram<String> ngrams(Collection<String> s1, int degree, boolean smoothing) {
		
		// construct histogram from the collection of strings.
		Histogram<String> hist = new Histogram<>();
		hist.smoothed = false;
		for (String str : s1) {
			List<String> ngrams = Histogram.ngramsIn(str, degree);
			for (String ngram : ngrams) hist.add(ngram);
		}
		
		// do LaPlace smoothing, if need be.
		if (smoothing) {
			List<String> validNgrams = NGRAM_MAP.containsKey(degree) ? NGRAM_MAP.get(degree) : allNgrams(alphabet, degree);
			for (String ngram : validNgrams) {
				hist.add(ngram);
			}
		}
		
		// normalise and return.
		hist.normalise();
		return hist;
	}
	
	/**
	 * Returns a list of all ngrams, of the specified degree, in the given string.
	 * @param string: string whose ngrams you want.
	 * @param degree: degree of ngrams to compute.
	 * @return a list of all ngrams of that degree.
	 */
	public static List<String> ngramsIn(String string, int degree) {
		List<String> ngrams = new ArrayList<>();
		char[] chars = string.toCharArray();
		for (int i = 0; i < string.length() - degree + 1; i++){
			StringBuilder sb = new StringBuilder();
			for (int j = i; j < i + degree; j++) sb.append(chars[j]);
			ngrams.add(sb.toString());
		}
		return ngrams;
	}
	
}
