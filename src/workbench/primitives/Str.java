package primitives;

import rules.Primitive;

public class Str extends Primitive {

	public final static Str EmptyString = new Str("");
	private final String str;
	
	public Str (String str) {
		this.str = str;
	}
	
	public final String toString() {
		return str;
	}
	
	@Override
	public String eval () {
		return str;
	}

}
