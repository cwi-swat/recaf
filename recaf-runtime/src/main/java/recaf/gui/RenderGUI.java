package recaf.gui;

import java.io.StringWriter;

import recaf.core.cps.ED;
import recaf.core.cps.SD;

public class RenderGUI extends GUI {
	private StringWriter writer = new StringWriter();
	private int indent = 0;
	
	// this algebra will be shared, so we never return the DOM text;
	// it needs to be requested explicitly.
	// Ideally we want to construct a dom object of some kind...
	// Or maybe instantiate it for JavaFX?
	public String getHTML() {
		return writer.toString();
	}
	
	private void indent() {
		indent += 2;
	}
	
	private void dedent() {
		indent -= 2;
	}
	
	private void output(String s) {
		for (int i = 0; i < indent; i++) {
			writer.append(' ');
		}
		writer.append(s);
	}
	
	@Override
	public SD<Void> Tag(ED<String> t, SD<Void> body) {
		return (label, rho, sigma, brk, contin, err) -> {
			t.accept(tag -> {
				output("<" + tag + ">\n");
				indent();
				body.accept(null, rho, () -> {
					dedent();
					output("</" + tag + ">\n");
					sigma.call();
				},  brk, contin, err);
			}, err);
		};
	}
	
	@Override
	public SD<Void> Button(ED<String> label, SD<Void> body) {
		// in render, we don't execute the body.
		return (label0, rho, sigma, brk, contin, err) -> {
			label.accept(l -> {
				output("<button id=\"" + nextId() + "\">" + escapeHTML(l) + "</button>\n");
				sigma.call();
			}, err);
		};
	}
	
	@Override
	public SD<Void> Echo(ED<String> exp) {
		return (label, rho, sigma,  brk, contin, err) -> {
			exp.accept(txt -> {
				output(escapeHTML(txt));
				sigma.call();
			}, err);
		};
	}
	
	private static String escapeHTML(String s) {
	    StringBuilder out = new StringBuilder(Math.max(16, s.length()));
	    for (int i = 0; i < s.length(); i++) {
	        char c = s.charAt(i);
	        if (c > 127 || c == '"' || c == '<' || c == '>' || c == '&') {
	            out.append("&#");
	            out.append((int) c);
	            out.append(';');
	        } else {
	            out.append(c);
	        }
	    }
	    return out.toString();
	}
	
	
}
