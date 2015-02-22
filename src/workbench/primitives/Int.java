package primitives;

import rules.Primitive;

public class Int extends Primitive {

	private final int value;
	
	public Int (int i) {
		value = i;
	}
	
	public String toString() {
		return "" + value;
	}
	
	public int toInt() {
		return value;
	}
	
	public boolean equals (Object other) {
		if (other instanceof Integer){
			int i = (int)other;
			return i == value;
		}
		else if (other instanceof Int) {
			return ((Int)other).value == value;
		}
		else return false;
	}
	
	@Override
	public String typeName() {
		return "Int";
	}
	
}
