package wblang.primitives;

import java.util.Iterator;
import java.util.List;

import wblang.rules.Primitive;

public class Seq<T extends Primitive> extends Primitive implements Iterable<T> {
	
	private List<T> elems;
	
	public Seq (List<T> elems) {
		this.elems = elems;
	}

	public String toString() {
		return "Sequence of size " + elems.size();
	}
	
	public List<T> asList () {
		return elems;
	}

	public Primitive get (int indx) {
		return elems.get(indx);
	}
	
	public boolean contains (T k) {
		return elems.contains(k);
	}
	
	@Override
	public Iterator<T> iterator() {
		return elems.iterator();
	}

	public int size() {
		return elems.size();
	}
	
	@Override
	public String typeName() {
		Primitive p = elems.get(0);
		String type = p.typeName();
		return "Seq<" + type + ">";
	}
	
}
