package recaf.gui;

import recaf.core.ED;
import recaf.core.SD;

public class HandleGUI extends GUIExtension {
	private final String buttonClicked;

	public HandleGUI(String buttonClicked) {
		this.buttonClicked = buttonClicked;
	}
	
	@Override
	public SD<Void> Tag(ED<String> t, SD<Void> body) {
		return body;
	}
	
	@Override
	public SD<Void> Button(ED<String> label, SD<Void> body) {
		// in handle, we execute the body if the current button was clickd.
		return (rho, sigma, err) -> {
			String id = nextId();
			if (buttonClicked.equals(id)) {
				body.accept(rho, sigma, err);
			}
			else {
				sigma.call();
			}
		};
	}
	
	@Override
	public SD<Void> Echo(ED<String> exp) {
		return Empty();
	}
	
}
