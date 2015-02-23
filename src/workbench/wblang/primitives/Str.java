package wblang.primitives;

import wblang.rules.Primitive;

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
	
	@Override
	public boolean equals (Object other) {
		if (!(other instanceof Str)) return false;
		Str s = (Str)other;
		return s.str.equals(str);
	}
	
	@Override
	public int hashCode () {
		return str.hashCode();
	}
	
	@Override
	public String typeName() {
		return "Str";
	}
	
}
