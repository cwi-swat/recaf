package recaf.gui;

import java.util.Collections;
import java.util.Map;

public class Tag {
	private final String name;
	private final Map<String, String> attrs;

	public Tag(String name) {
		this(name,  Collections.emptyMap());
	}
		
	public Tag(String name, Map<String, String> attrs) {
		this.name = name;
		this.attrs = attrs;
	}
	
	public String getName() {
		return name;
	}
	
	public Map<String, String> getAttrs() {
		return attrs;
	}
}
