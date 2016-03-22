package recaf.paper.demo.ast;

public class Exp extends Stm {
	private Expr exp;

	public Exp(Expr exp) {
		this.exp = exp;
	}
}
