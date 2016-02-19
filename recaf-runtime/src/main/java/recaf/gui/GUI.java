package recaf.gui;

import java.util.function.Supplier;

import recaf.core.cps.EvalJavaStmt;
import recaf.core.cps.SD;

public abstract class GUI extends EvalJavaStmt<Void> {
	private int idCount = 0;
	
	protected String nextId() {
		return "id" + idCount++;
	}
	
	public Void Method(SD<Void> body) {
		return typePreserving(body);
	}
	
	public abstract SD<Void> Tag(Supplier<String> t, SD<Void> body);

	public abstract SD<Void> Button(Supplier<String> label, SD<Void> body);

	public abstract SD<Void> Echo(Supplier<String> exp);	
}
