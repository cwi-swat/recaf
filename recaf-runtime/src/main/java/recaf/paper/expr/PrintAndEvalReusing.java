package recaf.paper.expr;

public interface PrintAndEvalReusing extends MuExpJavaZip<IEval, String> {
	static MuExpJavaBase e = new MuExpJavaBase() { };
	static Print p = new Print() { };
	
	@Override
	default MuExpJava<IEval> alg1() {
		return e;
	}
	@Override
	default MuExpJava<String> alg2() {
		return p;
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
