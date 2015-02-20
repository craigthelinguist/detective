package functions;

public class UsageTemplate {

	public static String make (String signature, String inputMsg, String outputMsg) {
		String s = signature + "\n\n-- input --\n";
		s += inputMsg + "\n\n- output --\n";
		s += outputMsg + "\n";
		return s;
	}
	
}
