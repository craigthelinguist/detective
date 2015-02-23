package wblang.primitives;

import vectors.Vector;
import wblang.rules.Primitive;

public class Vec extends Primitive {

	private Vector vector;
	
	public Vec (Vector v) {
		vector = v;
	}
	
	public String toString () {
		return vector.toString();
	}
	
	public boolean equals (Object other) {
		if (!(other instanceof Vec)) return false;
		Vec v = (Vec)other;
		return v.vector.equals(vector);
	}
	
	@Override
	public String typeName() {
		return "Vec";
	}
	
	public Vector asVector () {
		return vector;
	}
	
}
