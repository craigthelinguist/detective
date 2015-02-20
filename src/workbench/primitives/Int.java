package primitives;

public class Int implements Primitive {

	private final int value;
	
	public Int (int i) {
		value = i;
	}
	
	public String toString() {
		return "" + value;
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
	
}
