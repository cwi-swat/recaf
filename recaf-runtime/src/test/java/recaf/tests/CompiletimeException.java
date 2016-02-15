package recaf.tests;

import java.io.IOException;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public class CompiletimeException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<Diagnostic<? extends JavaFileObject>> diagnostics;
	Exception inner;
	
	public CompiletimeException(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
		this.diagnostics = diagnostics;
	}

	public CompiletimeException(IOException e) {
		this.inner = e;	
	}

	@Override
	public String toString() {
		StringBuilder errorMessage = new StringBuilder();
        for (Diagnostic<?> diagnostic : diagnostics) {
        	errorMessage.append("Code: ")
        				.append(diagnostic.getCode())
        				.append("\nKind: ")
        				.append(diagnostic.getKind())
        				.append("\nPosition: ")
			            .append(diagnostic.getPosition())
        				.append("\nSource: ")
			            .append(diagnostic.getSource())
        				.append("\nInternal Message: ")
			            .append(diagnostic.getMessage(null))
        				.append("\n\t");
        }
		return errorMessage.toString();
	}
	
	

}
