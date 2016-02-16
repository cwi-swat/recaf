package recaf.core.definitional;

public abstract class Jump extends Exception {
	private final String label;

	public Jump(String label) {
		this.label = label;
	}

	
	public boolean hasLabel(String l) {
		if (l == label) {
			return true;
		}
		if (l != null) {
			return l.equals(label);
		}
		return false;
	}
}
