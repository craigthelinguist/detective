package primitives;

import java.util.Iterator;
import java.util.List;

import rules.Primitive;

public class Seq<T> extends Primitive implements Iterable<T> {
	
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

	@Override
	public Iterator<T> iterator() {
		return elems.iterator();
	}

	public int size() {
		return elems.size();
	}
	
}
