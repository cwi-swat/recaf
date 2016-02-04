package recaf.core;

import recaf.core.functional.ED;
import recaf.core.functional.K;
import recaf.core.functional.K0;
import recaf.core.functional.QuadriConsumer;
import recaf.core.functional.SD;

public class Cont<T> {

	public static <R> Cont<R> fromED(ED<R> expressionDenotation) {
		return new Cont<R>(expressionDenotation, null);
	}

	public static <R> Cont<R> fromSD(SD<R> statementDenotation) {
		return new Cont<R>(null, statementDenotation);
	}
	
	public static <R> Cont<R> fromSwitchD(QuadriConsumer<K<R>, K0, K0, K<Throwable>> enhancedStatementDenotation) {
		return new Cont<R>(null, null, enhancedStatementDenotation);
	}

	public ED<T> expressionDenotation;
	public SD<T> statementDenotation;
	public QuadriConsumer<K<T>, K0, K0, K<Throwable>> enhancedStatementDenotation;

	public Cont(ED<T> expressionDenotation, SD<T> statementDenotation) {
		super();
		this.expressionDenotation = expressionDenotation;
		this.statementDenotation = statementDenotation;
	}

	public Cont(ED<T> expressionDenotation, SD<T> statementDenotation, QuadriConsumer<K<T>, K0, K0, K<Throwable>> enhancedStatementDenotation) {
		this(expressionDenotation, statementDenotation);
		this.enhancedStatementDenotation = enhancedStatementDenotation;
	}
}