package recaf.paper.demo.ast;

public class Decl extends Stm {
	private String x;
	private Expr init;
	private Stm body;

	public Decl(String x, Expr init, Stm body) {
		this.x = x;
		this.init = init;
		this.body = body;
	}
}
