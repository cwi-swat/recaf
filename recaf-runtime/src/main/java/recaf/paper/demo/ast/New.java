package recaf.paper.demo.ast;

public class New extends Expr {
	private Class<?> klass;
	private Expr[] args;

	public New(Class<?> klass, Expr ...es) {
		this.klass = klass;
		this.args = es;
	}
}
