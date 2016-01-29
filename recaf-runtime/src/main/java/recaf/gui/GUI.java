package recaf.gui;

import recaf.core.AbstractJavaCPS;
import recaf.core.ED;
import recaf.core.SD;

public abstract class GUI extends AbstractJavaCPS<Void> {
	private int idCount = 0;
	
	protected String nextId() {
		return "id" + idCount++;
	}
	
	public Void Method(Cont<Void> body) {
		return typePreserving(body);
	}
	
	public abstract Cont<Void> Tag(Cont<String> t, Cont<Void> body);

	public abstract Cont<Void> Button(Cont<String> label, Cont<Void> body);

	public abstract Cont<Void> Echo(Cont<String> exp);	
}
