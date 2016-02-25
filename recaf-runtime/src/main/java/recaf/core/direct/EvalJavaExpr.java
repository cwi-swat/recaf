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
		return () -> n;
	}

	@Override
	default IEval Lit(double d) {
		return () -> d;
	}

	@Override
	default IEval Lit(long l) {
		return () -> l;
	}

	@Override
	default IEval Lit(float f) {
		return () -> f;
	}

	@Override
	default IEval Lit(String s) {
		return () -> s;
	}

	@Override
	default IEval Lit(boolean b) {
		return () -> b;
	}

	@Override
	default IEval Assign(IEval lhs, IEval rhs) {
		return () -> {
			Object r = lhs.eval();
			((Ref) r).value = toValue(rhs.eval());
			return r;
		};
	}

	@Override
	default IEval Var(String name, Ref<?> val) {
		return () -> val;
	}
	
	@Override
	default IEval Field(IEval recv, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default IEval This(Object self) {
		return () -> self;
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
	default IEval Gt(IEval l, IEval r) {
		return () -> {
			return Boolean.valueOf((Integer) toValue(l.eval()) > (Integer) toValue(r.eval()));
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
			return Integer.valueOf((Integer) toValue(l.eval()) + (Integer) toValue(r.eval()));
		};
	}
	
	@Override
	default IEval Minus(IEval l, IEval r) {
		return () -> {
			return Integer.valueOf((Integer) toValue(l.eval()) - (Integer) toValue(r.eval()));
		};
	}

	@Override
	default IEval New(Class<?> clazz, IEval... args) {
		return () -> {
			try {
				List<Object> evaluatedArgs = new ArrayList<Object>();
				List<Class<?>> argClasses = new ArrayList<Class<?>>();
				for (IEval a: args){
					Object ea = toValue(a.eval());
					evaluatedArgs.add(ea);
					argClasses.add(ea.getClass());
				}
				Constructor<?> constructor = clazz.getConstructor(argClasses.toArray(new Class<?>[0]));
				constructor.setAccessible(true);
				return constructor.newInstance(evaluatedArgs.toArray(new Object[0]));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException | NoSuchMethodException e) {
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
				Method m = EvalJavaHelper.findMethod(o, method, argClasses.toArray(new Class[0]));
				m.setAccessible(true);
				return m.invoke(o, evaluatedArgs.toArray(new Object[0]));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| SecurityException e) {
				e.printStackTrace();
				return null;
			}
		};
	}
	
}
