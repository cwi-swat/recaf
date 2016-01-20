package recaf.gui;

import recaf.core.AbstractJavaCPS;
import recaf.core.ED;
import recaf.core.SD;

public abstract class GUIExtension extends AbstractJavaCPS<Void> {
	private int idCount = 0;
	
	protected String nextId() {
		return "id" + idCount++;
	}
	
	public Void Method(SD<Void> body) {
		return typePreserving(body);
	}
	
	public abstract SD<Void> Tag(ED<String> t, SD<Void> body);

	public abstract SD<Void> Button(ED<String> label, SD<Void> body);

	public abstract SD<Void> Echo(ED<String> exp);	
}
