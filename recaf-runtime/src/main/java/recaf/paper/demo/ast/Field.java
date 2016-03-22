package recaf.paper.demo.ast;

public class Field extends Expr {
	private Expr exp;
	private String field;

	public Field(Expr exp, String field) {
		this.exp = exp;
		this.field = field;
	}
}
