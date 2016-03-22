package recaf.paper.demo.ast;

public class Lambda extends Expr {
	private Object lambda;
	private String[] params;
	private Stm body;

	public Lambda(String[] params, Stm body) {
		this.params = params;
		this.body = body;
	}
}
