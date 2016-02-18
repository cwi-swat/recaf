package recaf.core.direct;

import recaf.core.Ref;

public interface EvalJavaExpr extends JavaExprAlg<IEval> {
	
	@Override
	default IEval Lit(int n) {
		return () -> {
			return Integer.valueOf(n);
		};
	}

	@Override
	default IEval Lit(double d) {
		return () -> {
			return Double.valueOf(d);
		};
	}

	@Override
	default IEval Lit(long l) {
		return () -> {
			return Long.valueOf(l);
		};
	}

	@Override
	default IEval Lit(float f) {
		return () -> {
			return Float.valueOf(f);
		};
	}

	@Override
	default IEval Lit(String s) {
		return () -> {
			return String.valueOf(s);
		};
	}

	@Override
	default IEval Lit(boolean b) {
		return () -> {
			return Boolean.valueOf(b);
		};
	}

	@Override
	default IEval Class(String name, java.lang.Class<?> klass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default IEval AssignVar(String name, Ref<Object> ref, IEval value) {
		return () -> {
			ref.value = value.eval();
			return ref.value;
		};
	}

	@Override
	default IEval Var(String name, Ref<Object> val) {
		return () -> {
			return val.value;
		};
	}
	
	@Override
	default IEval Call(IEval recv, String name, IEval... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default IEval AssignField(IEval recv, String name, IEval value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default IEval Field(IEval recv, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default IEval This() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default IEval PostIncr(String name, Ref<Object> ref) {
		return () -> {
			Object o = ref.value;
			Integer old = null;
			if (o instanceof Integer){
				old = (Integer) o;
				ref.value = old+1;
			}
			return old;
		};
	}
	

	@Override
	default IEval PostDecr(String name, Ref<Object> ref) {
		return () -> {
			Object o = ref.value;
			Integer old = null;
			if (o instanceof Integer){
				old = (Integer) o;
				ref.value = old-1;
			}
			return old;
		};
	}

	@Override
	default IEval PreIncr(String name, Ref<Object> ref) {
		return () -> {
			Object o = ref.value;
			if (o instanceof Integer){
				ref.value = ((Integer)o)+1;
			}
			return ref.value;
		};
	}
	

	@Override
	default IEval PreDecr(String name, Ref<Object> ref) {
		return () -> {
			Object o = ref.value;
			if (o instanceof Integer){
				ref.value = ((Integer)o)-1;
			}
			return ref.value;
		};
	}

	@Override
	default IEval GreaterThan(IEval l, IEval r) {
		return () -> {
			return Boolean.valueOf((Integer) l.eval() >= (Integer) r.eval());
		};
	}

	@Override
	default IEval LessThan(IEval l, IEval r) {
		return () -> {
			return Boolean.valueOf((Integer) l.eval() < (Integer) r.eval());
		};
	}

	@Override
	default IEval Plus(IEval l, IEval r) {
		return () -> {
			return Integer.valueOf((Integer) l.eval() + (Integer) r.eval());
		};
	}

	@Override
	default <T> IEval VarFinal(String name, T val) {
		// TODO Auto-generated method stub
		return null;
	}
}
