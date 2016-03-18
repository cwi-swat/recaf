package recaf.paper.expr;

public interface PrintAndEval extends MuExpJavaZip<IEval, String> {
	@Override
	default MuExpJava<IEval> alg1() {
		return new MuExpJavaBase() { };
	}
	@Override
	default MuExpJava<String> alg2() {
		return new Print() { };
	}
	
	@Override
	default Class<IEval> class1() {
		return IEval.class;
	}

	@Override
	default Class<String> class2() {
		return String.class;
	}
	
}
