package recaf.core.direct;

import static recaf.core.EvalJavaHelper.toValue;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import recaf.core.EvalJavaHelper;
import recaf.core.IRef;
import recaf.core.Ref;
import recaf.core.ReflectRef;
import recaf.core.alg.JavaExprAlg;

public interface EvalJavaExpr extends JavaExprAlg<IEval> {
	
	@Override
	default IEval Closure(Object lambda) {
		throw new UnsupportedOperationException();
	}

	@Override
	default IEval Cond(IEval c, IEval t, IEval e) {
		return () -> (Boolean) c.eval()?t.eval():e.eval();
	}

	@Override
	default IEval ArrayAccess(IEval array, IEval index) {
		return () -> Array.get(array.eval(), (Integer) index.eval());
	}

	@Override
	default IEval InvokeSuper(IEval self, String method, IEval... args) {
		throw new UnsupportedOperationException();
	}

	@Override
	default IEval InvokeSuper(IEval self, Class<?> clazz, String method, IEval... args) {
		throw new UnsupportedOperationException();
	}

	@Override
	default IEval SuperField(String name, Object self) {
		throw new UnsupportedOperationException();
	}

	@Override
	default IEval SuperField(Class<?> clazz, String name, Object self) {
		throw new UnsupportedOperationException();
	}

	@Override
	default IEval CastPrim(Class<?> clazz, IEval e) {
		return () -> clazz.cast(e.eval());
	}

	@Override
	default IEval PostDecr(IEval arg) {
		return () -> {
			IRef<Integer> r = (IRef<Integer>) arg.eval();
			r.setValue(r.value() - 1);
			return r.value();
		};
	}

	@Override
	default IEval Plus(IEval arg) {
		return () -> + (Integer) toValue(arg.eval());
	}

	@Override
	default IEval Not(IEval arg) {
		return () -> !((Boolean) arg.eval());
	}

	@Override
	default IEval Complement(IEval arg) {
		return () -> ~ ((Integer) arg.eval());
	}

	@Override
	default IEval PreIncr(IEval arg) {
		return () -> {
			IRef<Integer> r = (IRef<Integer>) arg.eval();
			Integer saved = r.value();
			r.setValue(saved + 1);
			return saved;
		};
	}

	@Override
	default IEval PreDecr(IEval arg) {
		return () -> {
			IRef<Integer> r = (IRef<Integer>) arg.eval();
			Integer saved = r.value();
			r.setValue(saved - 1);
			return saved;
		};
	}

	@Override
	default IEval Minus(IEval arg) {
		return () -> - (Integer) toValue(arg.eval());
	}

	@Override
	default IEval Div(IEval lhs, IEval rhs) {
		return () -> {
			return Integer.valueOf((Integer) toValue(lhs.eval()) / (Integer) toValue(rhs.eval()));
		};
	}

	@Override
	default IEval Remain(IEval lhs, IEval rhs) {
		return () -> {
			return Integer.valueOf((Integer) toValue(lhs.eval()) % (Integer) toValue(rhs.eval()));
		};
	}

	@Override
	default IEval Mul(IEval lhs, IEval rhs) {
		return () -> {
			return Integer.valueOf((Integer) toValue(lhs.eval()) * (Integer) toValue(rhs.eval()));
		};
	}

	@Override
	default IEval RightShift(IEval lhs, IEval rhs) {
		return () -> {
			return Integer.valueOf((Integer) toValue(lhs.eval()) >> (Integer) toValue(rhs.eval()));
		};
	}

	@Override
	default IEval URightShift(IEval lhs, IEval rhs) {
		return () -> {
			return Integer.valueOf((Integer) toValue(lhs.eval()) >>> (Integer) toValue(rhs.eval()));
		};
	}

	@Override
	default IEval LeftShift(IEval lhs, IEval rhs) {
		return () -> {
			return Integer.valueOf((Integer) toValue(lhs.eval()) << (Integer) toValue(rhs.eval()));
		};
	}

	@Override
	default IEval GtEq(IEval lhs, IEval rhs) {
		return () -> {
			return Boolean.valueOf((Integer) toValue(lhs.eval()) >= (Integer) toValue(rhs.eval()));
		};
	}

	@Override
	default IEval InstanceOf(IEval lhs, IEval rhs) {
		return () -> ((Class<?>) rhs.eval()).isInstance(lhs.eval());		
	}

	@Override
	default IEval LtEq(IEval lhs, IEval rhs) {
		return () -> {
			return Boolean.valueOf((Integer) toValue(lhs.eval()) <= (Integer) toValue(rhs.eval()));
		};
	}

	@Override
	default IEval Eq(IEval lhs, IEval rhs) {
		return () -> toValue(lhs.eval()).equals(toValue(rhs.eval()));
	}

	@Override
	default IEval NotEq(IEval lhs, IEval rhs) {
		return () -> !toValue(lhs.eval()).equals(toValue(rhs.eval()));
	}

	@Override
	default IEval And(IEval lhs, IEval rhs) {
		return () -> ((Boolean) lhs.eval()) && ((Boolean) rhs.eval());
	}

	@Override
	default IEval ExcOr(IEval lhs, IEval rhs) {
		return () -> ((Boolean) lhs.eval()) ^ ((Boolean) rhs.eval());
	}

	@Override
	default IEval Or(IEval lhs, IEval rhs) {
		return () -> ((Boolean) lhs.eval()) || ((Boolean) rhs.eval());
	}

	@Override
	default IEval LazyAnd(IEval lhs, IEval rhs) {
		throw new UnsupportedOperationException();
	}

	@Override
	default IEval LazyOr(IEval lhs, IEval rhs) {
		throw new UnsupportedOperationException();
	}

	@Override
	default IEval AssignLeftShift(IEval lhs, IEval rhs) {
		return Assign(lhs, LeftShift(lhs, rhs));
	}

	@Override
	default IEval AssignOr(IEval lhs, IEval rhs) {
		return Assign(lhs, Or(lhs, rhs));
	}

	@Override
	default IEval AssignAnd(IEval lhs, IEval rhs) {
		return Assign(lhs, And(lhs, rhs));
	}

	@Override
	default IEval AssignRightShift(IEval lhs, IEval rhs) {
		return Assign(lhs, RightShift(lhs, rhs));
	}

	@Override
	default IEval AssignRemain(IEval lhs, IEval rhs) {
		return Assign(lhs, Remain(lhs, rhs));
	}

	@Override
	default IEval AssignPlus(IEval lhs, IEval rhs) {
		return Assign(lhs, Plus(lhs, rhs));
	}

	@Override
	default IEval AssignExcOr(IEval lhs, IEval rhs) {
		return Assign(lhs, ExcOr(lhs, rhs));
	}

	@Override
	default IEval AssignDiv(IEval lhs, IEval rhs) {
		return Assign(lhs, Div(lhs, rhs));
	}

	@Override
	default IEval AssignURightShift(IEval lhs, IEval rhs) {
		return Assign(lhs, URightShift(lhs, rhs));
	}

	@Override
	default IEval AssignMinus(IEval lhs, IEval rhs) {
		return Assign(lhs, Minus(lhs, rhs));
	}
	
	@Override
	default IEval AssignAdd(IEval lhs, IEval rhs) {
		return Assign(lhs, Plus(lhs, rhs));
	}

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
			IRef r = (IRef<?>) lhs.eval();
			r.setValue(toValue(rhs.eval()));
			return r;
		};
	}

	@Override
	default IEval Var(String name, Ref<?> val) {
		return () -> val;
	}
	
	@Override
	default IEval Field(IEval recv, String name) {
		return () -> {
			Object o = toValue(recv.eval());
			try {
				return new ReflectRef(o, name); 
			} catch (IllegalArgumentException | NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
				return null;
			}
		};
	}

	@Override
	default IEval This(Object self) {
		return () -> self;
	}

	@Override
	default IEval PostIncr(IEval arg) {
		return () -> {
			IRef<Integer> r = (IRef<Integer>) arg.eval();
			r.setValue(r.value() + 1);
			return r.value();
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
