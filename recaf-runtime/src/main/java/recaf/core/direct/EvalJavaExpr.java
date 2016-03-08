package recaf.core.direct;

import static recaf.core.EvalJavaHelper.toValue;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import recaf.core.EvalJavaHelper;
import recaf.core.IRef;
import recaf.core.ReflectRef;
import recaf.core.alg.JavaExprAlg;

public interface EvalJavaExpr extends JavaExprAlg<IEval> {

	@Override
	default IEval Closure(Object lambda) {
		return () -> lambda;
	}

	@Override
	default IEval Cond(IEval c, IEval t, IEval e) {
		return () -> (Boolean) c.eval() ? t.eval() : e.eval();
	}

	@Override
	default IEval ArrayAccess(IEval array, IEval index) {
		return () -> Array.get(array.eval(), (Integer) index.eval());
	}

	// Invoking a superclass method using reflection
	//   http://stackoverflow.com/questions/5411434/how-to-call-a-superclass-method-using-java-reflection
	//
	// Forcing private access 
	//   https://rmannibucau.wordpress.com/2014/03/27/java-8-default-interface-methods-and-jdk-dynamic-proxies/
	//
	// TODO Investigate if is it possible to access the parent's method when
	//      the parent is a non-public class
	@Override
	default IEval InvokeSuper(IEval self, String method, IEval... args) {
		return () -> {
			Object o = self.eval();
			Object[] evaluatedArgs = EvalJavaHelper.evaluateArguments(args);
			Method m = EvalJavaHelper.findMethod(o.getClass().getSuperclass(), method, evaluatedArgs);
			m.setAccessible(true);
			final MethodHandles.Lookup lookup = MethodHandles.lookup().in(o.getClass().getSuperclass());
			final Field f = MethodHandles.Lookup.class.getDeclaredField("allowedModes");
	        final int modifiers = f.getModifiers();
	        if (Modifier.isFinal(modifiers)) { // should be done a single time
	            final Field modifiersField = Field.class.getDeclaredField("modifiers");
	            modifiersField.setAccessible(true);
	            modifiersField.setInt(f, modifiers & ~Modifier.FINAL);
	            f.setAccessible(true);
	            f.set(lookup, MethodHandles.Lookup.PRIVATE);
	        }
			MethodHandle mhh = lookup.
					unreflectSpecial(m, o.getClass().getSuperclass());
			return mhh.invoke(o);
		};
	}

	@Override
	default IEval InvokeSuper(IEval self, Class<?> clazz, String method, IEval... args) {
		throw new UnsupportedOperationException();
	}

	@Override
	default IEval SuperField(String name, Object self) {
		return () -> {
			Field f = EvalJavaHelper.findField(self.getClass().getSuperclass(), name);
			f.setAccessible(true);
			return f.get(self);
		};
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
			return r.setValue(r.value() - 1);
		};
	}

	@Override
	default IEval Plus(IEval arg) {
		return () -> +(Integer) toValue(arg.eval());
	}

	@Override
	default IEval Not(IEval arg) {
		return () -> !((Boolean) arg.eval());
	}

	@Override
	default IEval Complement(IEval arg) {
		return () -> ~((Integer) arg.eval());
	}

	@Override
	default IEval PreIncr(IEval arg) {
		return () -> {
			IRef<Integer> r = (IRef<Integer>) arg.eval();
			Integer saved = r.value();
			return r.setValue(saved + 1);
		};
	}

	@Override
	default IEval PreDecr(IEval arg) {
		return () -> {
			IRef<Integer> r = (IRef<Integer>) arg.eval();
			Integer saved = r.value();
			return r.setValue(saved - 1);
		};
	}

	@Override
	default IEval Minus(IEval arg) {
		return () -> -(Integer) toValue(arg.eval());
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
		return () -> ((Boolean) lhs.eval()) & ((Boolean) rhs.eval());
	}

	@Override
	default IEval ExcOr(IEval lhs, IEval rhs) {
		return () -> ((Boolean) lhs.eval()) ^ ((Boolean) rhs.eval());
	}

	@Override
	default IEval Or(IEval lhs, IEval rhs) {
		return () -> ((Boolean) lhs.eval()) | ((Boolean) rhs.eval());
	}

	@Override
	default IEval LazyAnd(IEval lhs, IEval rhs) {
		return () -> ((Boolean) lhs.eval()) && ((Boolean) rhs.eval());
	}

	@Override
	default IEval LazyOr(IEval lhs, IEval rhs) {
		return () -> ((Boolean) lhs.eval()) || ((Boolean) rhs.eval());
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
			return r.setValue(toValue(rhs.eval()));
		};
	}

	@Override
	default IEval Var(String name, IRef<?> val) {
		return () -> val;
	}

	@Override
	default IEval Field(IEval recv, String name) {
		return () -> {
			Object o = toValue(recv.eval());
			return new ReflectRef(o, name);
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
			return r.setValue(r.value() + 1);

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
			Object lo = toValue(l.eval());
			Object ro = toValue(r.eval());
			String lc = lo.getClass().getName();
			String rc = ro.getClass().getName();
			Object res = null;
			switch (lc) {
			case "java.lang.Integer":
				switch (rc) {
				case "java.lang.Integer":
					res = (Integer) lo + (Integer) ro;
					break;
				case "java.lang.String":
					res = (Integer) lo + (String) ro;
					break;
				case "java.lang.Double":
					res = (Integer) lo + (Double) ro;
					break;
				case "java.lang.Float":
					res = (Integer) lo + (Float) ro;
					break;
				}
				break;
			case "java.lang.String":
				switch (rc) {
				case "java.lang.Integer":
					res = (String) lo + (Integer) ro;
					break;
				case "java.lang.String":
					res = (String) lo + (String) ro;
					break;
				case "java.lang.Double":
					res = (String) lo + (Double) ro;
					break;
				case "java.lang.Float":
					res = (String) lo + (Float) ro;
					break;
				}
				break;
			case "java.lang.Double":
				switch (rc) {
				case "java.lang.Integer":
					res = (Double) lo + (Integer) ro;
					break;
				case "java.lang.String":
					res = (Double) lo + (String) ro;
					break;
				case "java.lang.Double":
					res = (Double) lo + (Double) ro;
					break;
				case "java.lang.Float":
					res = (Double) lo + (Float) ro;
					break;
				}
				break;
			case "java.lang.Float":
				switch (rc) {
				case "java.lang.Integer":
					res = (Float) lo + (Integer) ro;
					break;
				case "java.lang.String":
					res = (Float) lo + (String) ro;
					break;
				case "java.lang.Double":
					res = (Float) lo + (Double) ro;
					break;
				case "java.lang.Float":
					res = (Float) lo + (Float) ro;
					break;
				}
				break;
			}
			if (res != null)
				return res;
			else
				throw new RuntimeException("Wrong arguments to +.");
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
			Object[] evaluatedArgs = EvalJavaHelper.evaluateArguments(args);
			Constructor<?> constructor = EvalJavaHelper.findConstructor(clazz, evaluatedArgs);
			constructor.setAccessible(true);
			return constructor.newInstance(evaluatedArgs);
		};
	}

	@Override
	default IEval Invoke(IEval obj, String method, IEval... args) {
		return () -> {
			Object o = toValue(obj.eval());
			Object[] evaluatedArgs = EvalJavaHelper.evaluateArguments(args);
			Method m = EvalJavaHelper.findMethod(o.getClass(), method, evaluatedArgs);
			m.setAccessible(true);
			return m.invoke(o, evaluatedArgs);
		};
	}

}
