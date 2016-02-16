package recaf.gui;

import recaf.core.cps.AbstractJavaImpl;
import recaf.core.functional.ED;
import recaf.core.functional.SD;

public abstract class GUI extends AbstractJavaImpl<Void> {
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
