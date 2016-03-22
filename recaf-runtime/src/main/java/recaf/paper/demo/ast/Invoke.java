package recaf.paper.demo.ast;

public class Invoke extends Expr {
	private Expr recv;
	private String method;
	private Expr[] args;

	public Invoke(Expr recv, String method, Expr ...args) {
		this.recv = recv;
		this.method = method;
		this.args = args;
	}
}
