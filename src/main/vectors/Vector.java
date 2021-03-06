package vectors;

import java.util.Arrays;
import java.util.List;

import dns.Host;

public class Vector {

	// Fields.
	// --------------------------------------------------------------------------------
	private double[] components;
	private Host host;
	private List<String> queries;
	
	
	// Constructors.
	// --------------------------------------------------------------------------------	
	public Vector (double[] components) {
		this.components = components;
	}
	
	public Vector (double[] components, Host host, List<String> queries) {
		this.components = components;
		this.host = host;
		this.queries = queries;
	}
	
	// Getters.
	// --------------------------------------------------------------------------------
	
	public double get (int x){
		if (x >= components.length) return 0.0;
		return components[x];
	}

	public int dimensions () {
		return components.length;
	}
	
	public Host getHost () {
		return this.host;
	}
	
	public List<String> getQueries () {
		return this.queries;
	}
	
	// Overridden methods.
	// --------------------------------------------------------------------------------
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("VECTOR: [");
		for (int i = 0; i < components.length; i++){
			sb.append(components[i] + ", ");
		}
		sb.delete(sb.length()-2, sb.length());
		sb.append("]");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((queries == null) ? 0 : queries.hashCode());
		result = prime * result + Arrays.hashCode(components);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Vector other = (Vector) obj;
		
		// check null
		if (host != other.host) return false;
		if (host != null && other.host != null && !host.equals(other.host)) return false;
		
		// check queries
		if (queries != other.queries) return false;
		if (queries != null && other.queries != null && !queries.equals(other.queries)) return false;
		
		if (!Arrays.equals(components, other.components)) return false;
		return true;
	}
	
	@Override
	public Vector clone () {
		
		double[] comps = new double[components.length];
		for (int i = 0; i < components.length; i++) {
			comps[i] = components[i];
		}
		return new Vector(comps);
		
	}
	
}
