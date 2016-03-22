package recaf.paper.demo.ast;

public class Seq extends Stm {
	private Stm s1;
	private Stm s2;

	public Seq(Stm s1, Stm s2) {
		this.s1 = s2;
		this.s2 = s2;
	}
}
