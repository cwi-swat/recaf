package recaf.paper.demo.ast;

public class If extends Stm {
	private Expr cond;
	private Stm s1;
	private Stm s2;

	public If(Expr cond, Stm s1, Stm s2) {
		this.cond = cond;
		this.s1 = s1;
		this.s2 = s2;
	}
}
