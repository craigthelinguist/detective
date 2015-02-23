package wblang.primitives;

import wblang.rules.Primitive;

public class Kore extends Primitive {

	private Kore () {}
	
	public static Kore kore = new Kore();
	
	public String toString () {
		return "void";
	}
	
	@Override
	public String typeName() {
		return "Kore";
	}
	
}
