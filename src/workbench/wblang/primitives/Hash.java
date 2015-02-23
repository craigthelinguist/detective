package wblang.primitives;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import wblang.rules.Primitive;

public class Hash extends Primitive implements Iterable<Primitive> {
	
	private Map<Primitive, Primitive> hash;
	
	public Hash () {
		hash = new HashMap<>();
	}
	
	public void add (Primitive key, Primitive value) {
		hash.put(key, value);
	}
	
	public Primitive get (Primitive key) {
		return hash.get(key);
	}

	@Override
	public String typeName() {
		return "Hash";
	}

	@Override
	public Iterator<Primitive> iterator() {
		return hash.keySet().iterator();
	}

	@Override
	public String toString () {
		return "Hash, size " + hash.size();
	}
	
}
