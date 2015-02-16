package dns;

public class Domains {

	// Constructors.
	// --------------------------------------------------------------------------------
	private Domains () {}
	
	
	// Constants.
	// --------------------------------------------------------------------------------
	private static final String[] TLDS = new String[]{
		"nz", "kiwi", "com", "net", "org", "int", "edu", "mil", "arpa", "biz",
		"info"
	};

	private static final String[] SLDS = new String[]{
		"co", "mil", "govt", "ac", "edu", "org", "net", "gen", "maori", "iwi", "health",
		"cri", "parliament"
	};
	

	// Static methods.
	// --------------------------------------------------------------------------------
	
	/**
	 * Return a domain name with its higher-level zones stripped.
	 * @param domain: domain name to strip.
	 * @return String, domain with higher-level zones stripped.
	 */
	public static String stripHLD(String domain){
		String[] split = domain.split("\\.");
		int toStrip = 0;
		if (isTLD(split[split.length-1])){
			toStrip++;
			if (isSLD(split[split.length-2])){
				toStrip++;
			}
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < split.length - toStrip; i++){
			sb.append(split[i]);
		}
		return sb.toString();
	}
	
	/**
	 * Return the second-level part onwards of the given domain name.
	 * @param domain: domain to extract 2LD from
	 * @return second-level domain onwards
	 */
	public static String extract2LD(String domain){
		boolean endsWithRoot = domain.charAt(domain.length()-1) == '.';
		String[] split = domain.split("\\.");
		StringBuilder sb = new StringBuilder();
		for (int i = split.length - 2 ; i < split.length; i++){
			sb.append(split[i]);
			sb.append(".");
		}
		if (!endsWithRoot) sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}

	/**
	 * Return the third-level part onwards of the given domain name.
	 * @param domain: domain to extract 3LD from.
	 * @return third-level domain onwards
	 */
	public static String extract3LD(String domain){
		boolean endsWithRoot = domain.charAt(domain.length()-1) == '.';
		String[] split = domain.split("\\.");
		if (split.length < 3) return extract2LD(domain);
		StringBuilder sb = new StringBuilder();
		for (int i = split.length - 3 ; i < split.length; i++){
			sb.append(split[i]);
			sb.append(".");
		}
		if (!endsWithRoot) sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	

	// Helper methods.
	// --------------------------------------------------------------------------------
	
	public static boolean isTLD(String name){
		for (int i = 0; i < TLDS.length; i++){
			if (TLDS[i].equals(name)) return true;
		}
		return false;
	}
	
	public static boolean isSLD(String name){
		for (int i = 0; i < SLDS.length; i++){
			if (SLDS[i].equals(name)) return true;
		}
		return false;
	}
	
	
}
