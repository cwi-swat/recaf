package recaf.demo.direct;

import java.io.StringWriter;
import java.util.function.Supplier;

import recaf.core.direct.IExec;
import recaf.core.full.FullJavaDirect;

public abstract class GUI implements FullJavaDirect<Void> {

	private int idCount = 0;
	
	protected String nextId() {
		return "id" + idCount++;
	}
	
	public abstract IExec Tag(Supplier<String> t, IExec body);

	public abstract IExec Button(Supplier<String> label, IExec body);

	public abstract IExec Echo(Supplier<String> exp);	
	
	
	public static class Render extends GUI {
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
		public IExec Tag(Supplier<String> t, IExec body) {
			return l -> {
				String tag = t.get();
				output("<" + tag + ">\n");
				indent();
				body.exec(null);
				dedent();
				output("</" + tag + ">\n");
			};
		}
		
		@Override
		public IExec Button(Supplier<String> label, IExec body) {
			// in render, we don't execute the body.
			return l -> output("<button id=\"" + nextId() + "\">" + escapeHTML(label.get()) + "</button>\n");
		}
		
		@Override
		public IExec Echo(Supplier<String> exp) {
			return l -> output(escapeHTML(exp.get()));
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

	public static class Handle extends GUI {
		private final String buttonClicked;

		public Handle(String buttonClicked) {
			this.buttonClicked = buttonClicked;
		}
		
		@Override
		public IExec Tag(Supplier<String> t, IExec body) {
			return body;
		}
		
		@Override
		public IExec Button(Supplier<String> label, IExec body) {
			// in handle, we execute the body if the current button was clickd.
			return l -> {
				String id = nextId();
				if (buttonClicked.equals(id)) {
					body.exec(null);
				}
			};
		}
		
		@Override
		public IExec Echo(Supplier<String> exp) {
			return Empty();
		}
		
	}
}
