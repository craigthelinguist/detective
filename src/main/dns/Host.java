package dns;

import java.util.List;

public class Host {
	
	// Fields.
	// --------------------------------------------------------------------------------
	private final String address;
	private final List<String> queries;
	
	// Constructor.
	// --------------------------------------------------------------------------------
	public Host (String address, List<String> queries) {
		this.address = address;
		this.queries = queries;
	}
	
	public List<String> getQueries() {
		return this.queries;
	}
	

	// Overridden methods.
	// --------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return address;
	}

	@Override
	public int hashCode() {
		return address.hashCode();
	}
	
}
