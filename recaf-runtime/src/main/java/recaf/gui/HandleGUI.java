package recaf.gui;

public class HandleGUI extends GUI {
	private final String buttonClicked;

	public HandleGUI(String buttonClicked) {
		this.buttonClicked = buttonClicked;
	}
	
	@Override
	public Cont<Void> Tag(Cont<String> t, Cont<Void> body) {
		return body;
	}
	
	@Override
	public Cont<Void> Button(Cont<String> label, Cont<Void> body) {
		// in handle, we execute the body if the current button was clickd.
		return Cont.fromSD((rho, sigma, err) -> {
			String id = nextId();
			if (buttonClicked.equals(id)) {
				body.statementDenotation.accept(rho, sigma, err);
			}
			else {
				sigma.call();
			}
		});
	}
	
	@Override
	public Cont<Void> Echo(Cont<String> exp) {
		return Empty();
	}
	
}
