package recaf.gui;
import java.util.function.Supplier;

import recaf.core.cps.SD;

public class HandleGUI extends GUI {
	private final String buttonClicked;

	public HandleGUI(String buttonClicked) {
		this.buttonClicked = buttonClicked;
	}
	
	@Override
	public SD<Void> Tag(Supplier<String> t, SD<Void> body) {
		return body;
	}
	
	@Override
	public SD<Void> Button(Supplier<String> label, SD<Void> body) {
		// in handle, we execute the body if the current button was clickd.
		return (label0, rho, sigma, brk, contin, err) -> {
			String id = nextId();
			if (buttonClicked.equals(id)) {
				body.accept(null, rho, sigma, brk, contin, err);
			}
			else {
				sigma.call();
			}
		};
	}
	
	@Override
	public SD<Void> Echo(Supplier<String> exp) {
		return Empty();
	}
	
}
