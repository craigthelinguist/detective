package algorithms;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import stats.Histogram;

public class Strings {

	private Strings () {}
	
	/**
	 * Return true if two strings share no common characters.
	 * @param s1: first string
	 * @param s2: second string
	 * @return true if they have no chars in common
	 */
	public boolean disjoint (String s1, String s2) {
		for (char c : s1.toCharArray()) {
			if (s1.contains(""+c)) return false;
		}
		return true;
	}

	public double Bhattacharyya (Collection<String> s1, Collection<String> s2) {
		return Bhattacharyya(s1,s2,2);
	}
	
	/**
	 * Compute the Bhattacharyya distance between two strings. Assumes that the valid characters
	 * in the strings are [A-Za-z]
	 * @param s1: first string
	 * @param s2: second string
	 * @param degree: degree of ngrams to use
	 * @return Bhattacharyya distance
	 */
	public double Bhattacharyya (Collection<String> s1, Collection<String> s2, int degree) {
		
		// remember a distance of 0 means either they are the same or they have nothing in common
		throw new UnsupportedOperationException("Not yet implemented.");
	}
	
}
