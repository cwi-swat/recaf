package recaf.core;

public class Cont<T> {

	public static <R> Cont<R> fromED(ED<R> expressionDenotation) {
		return new Cont<R>(expressionDenotation, null);
	}
	public static <R> Cont<R> fromSD(SD<R> statementDenotation) {
		return new Cont<R>(null, statementDenotation);
	}

	public ED<T> expressionDenotation;
	public SD<T> statementDenotation;

	public Cont(ED<T> expressionDenotation, SD<T> statementDenotation) {
		super();
		this.expressionDenotation = expressionDenotation;
		this.statementDenotation = statementDenotation;
	}
}