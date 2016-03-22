package recaf.paper.demo.ast;

public class Return extends Stm {
	private Expr exp;

	public Return(Expr exp) {
		this.exp = exp;
	}
}
