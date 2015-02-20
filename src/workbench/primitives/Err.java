package primitives;

public class Err implements Primitive {

	private final String msg;
	
	public Err (String msg) {
		this.msg = msg;
	}
	
	public String toString() {
		return msg;
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
	
}
