package primitives;

public class Str implements Primitive {

	private final String str;
	
	public Str (String str) {
		this.str = str;
	}
	
	public final String toString() {
		return str;
	}

}
