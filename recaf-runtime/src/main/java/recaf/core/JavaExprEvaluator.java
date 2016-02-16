package recaf.core;

import java.util.function.Supplier;

import recaf.core.definitional.JavaExprAlg;

interface JavaExprEvaluator extends JavaExprAlg<Supplier<Object>> {
	
	@Override
	default Supplier<Object> Lit(int n) {
		return () -> {
			return Integer.valueOf(n);
		};
	}

	@Override
	default Supplier<Object> Lit(double d) {
		return () -> {
			return Double.valueOf(d);
		};
	}

	@Override
	default Supplier<Object> Lit(long l) {
		return () -> {
			return Long.valueOf(l);
		};
	}

	@Override
	default Supplier<Object> Lit(float f) {
		return () -> {
			return Float.valueOf(f);
		};
	}

	@Override
	default Supplier<Object> Lit(String s) {
		return () -> {
			return String.valueOf(s);
		};
	}

	@Override
	default Supplier<Object> Lit(boolean b) {
		return () -> {
			return Boolean.valueOf(b);
		};
	}

	@Override
	default Supplier<Object> Class(String name, java.lang.Class<?> klass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default Supplier<Object> AssignVar(String name, Ref ref, Supplier<Object> value) {
		return () -> {
			ref.value = value.get();
			return ref.value;
		};
	}

	@Override
	default Supplier<Object> Var(String name, Ref<?> val) {
		return () -> {
			return val.value;
		};
	}
	
	@Override
	default Supplier<Object> Call(Supplier<Object> recv, String name, Supplier<Object>... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default Supplier<Object> AssignField(Supplier<Object> recv, String name, Supplier<Object> value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default Supplier<Object> Field(Supplier<Object> recv, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default Supplier<Object> This() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default Supplier<Object> PostIncr(String name, Ref ref) {
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
	default Supplier<Object> PostDecr(String name, Ref ref) {
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
	default Supplier<Object> PreIncr(String name, Ref ref) {
		return () -> {
			Object o = ref.value;
			if (o instanceof Integer){
				ref.value = ((Integer)o)+1;
			}
			return ref.value;
		};
	}
	

	@Override
	default Supplier<Object> PreDecr(String name, Ref ref) {
		return () -> {
			Object o = ref.value;
			if (o instanceof Integer){
				ref.value = ((Integer)o)-1;
			}
			return ref.value;
		};
	}

	@Override
	default Supplier<Object> GreaterThan(Supplier<Object> l, Supplier<Object> r) {
		return () -> {
			return Boolean.valueOf((Integer) l.get() >= (Integer) r.get());
		};
	}

	@Override
	default Supplier<Object> LessThan(Supplier<Object> l, Supplier<Object> r) {
		return () -> {
			return Boolean.valueOf((Integer) l.get() <= (Integer) r.get());
		};
	}

	@Override
	default Supplier<Object> Plus(Supplier<Object> l, Supplier<Object> r) {
		return () -> {
			return Integer.valueOf((Integer) l.get() + (Integer) r.get());
		};
	}

	@Override
	default <T> Supplier<Object> VarFinal(String name, T val) {
		// TODO Auto-generated method stub
		return null;
	}
}
