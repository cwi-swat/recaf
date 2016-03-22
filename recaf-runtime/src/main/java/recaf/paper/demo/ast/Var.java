package recaf.paper.demo.ast;

public class Var extends Expr {
	private String name;
	private Object it;

	public Var(String name, Object it) {
		this.name = name;
		this.it = it;
	}
}
