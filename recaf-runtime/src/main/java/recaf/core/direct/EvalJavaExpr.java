package recaf.core.direct;

import static recaf.core.direct.EvalJavaHelper.toValue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import recaf.core.Ref;
import recaf.core.alg.JavaExprAlg;

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
	default IEval Var(String name, Ref<?> val) {
		return () -> {
			return val;
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
	default IEval PostIncr(IEval expr) {
		return () -> {
			Object o = expr.eval();
			((Ref) o).value = (Integer) ((Ref) o).value + 1; 
			return o;
		};
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
	default IEval Gt(IEval l, IEval r) {
		return () -> {
			return Boolean.valueOf((Integer) l.eval() >= (Integer) r.eval());
		};
	}

	default IEval Lt(IEval l, IEval r) {
		return () -> {
			return (Integer) toValue(l.eval()) < (Integer) toValue(r.eval());
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
	
	@Override
	default <T> IEval New(Class<T> clazz, IEval... args) {
		return () -> {
			try {
				List<Object> evaluatedArgs = new ArrayList<Object>();
				List<Class<?>> argClasses = new ArrayList<Class<?>>();
				for (IEval a: args){
					Object ea = toValue(a.eval());
					evaluatedArgs.add(ea);
					argClasses.add(ea.getClass());
				}
				Constructor<T> constructor = clazz.getConstructor(argClasses.toArray(new Class<?>[0]));
				constructor.setAccessible(true);
				return constructor.newInstance(evaluatedArgs.toArray(new Object[0]));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				return null;
			}
		};
	}
	
	@Override
	default IEval Invoke(IEval obj, String method, IEval... args) {
		return () -> {
			Object o = toValue(obj.eval());
			List<Object> evaluatedArgs = new ArrayList<Object>();
			List<Class<?>> argClasses = new ArrayList<Class<?>>();
			for (IEval a: args){
				Object ea = toValue(a.eval());
				evaluatedArgs.add(ea);
				argClasses.add(ea.getClass());
			}
			try {
				Method m = o.getClass().getMethod(method, argClasses.toArray(new Class<?>[0]));
				m.setAccessible(true);
				return m.invoke(o, evaluatedArgs.toArray(new Object[0]));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				return null;
			}
		};
	}
	
}
