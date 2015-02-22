package primitives;

import rules.Primitive;

public class Err extends Primitive {

	private final String msg;
	
	public Err (String msg) {
		this.msg = msg;
	}
	
	public String toString() {
		return "=== ERROR ===\n"+msg;
	}
	
	public boolean equals(Object other) {
		if (other instanceof String){
			return msg.equals(other);
		}
		else if (other instanceof Str) {
			return msg.equals(other.toString());
		}
		else return false;
	}

	@Override
	public Primitive exec () {
		return this;
	}
	
	@Override
	public String eval() {
		return toString();
	}
	
	@Override
	public String typeName() {
		return "Err";
	}
	
}
