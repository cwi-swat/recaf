package recaf.core.expr;

import static recaf.core.expr.EvalJavaHelper.toValue;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import recaf.core.alg.JavaExprNoHOASAlg;
import recaf.core.direct.IEvalEnv;

public interface EvalJavaExprNoHOAS extends JavaExprNoHOASAlg<IEvalEnv> {

	@Override
	default IEvalEnv Closure(Object lambda) {
		return env -> lambda;
	}

	@Override
	default IEvalEnv Cond(IEvalEnv c, IEvalEnv t, IEvalEnv e) {
		return env -> (Boolean) c.eval(env) ? t.eval(env) : e.eval(env);
	}

	@Override
	default IEvalEnv ArrayAccess(IEvalEnv array, IEvalEnv index) {
		return env -> Array.get(array.eval(env), (Integer) index.eval(env));
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
	default IEvalEnv InvokeSuper(IEvalEnv self, String method, IEvalEnv... args) {
		return env -> {
			Object o = self.eval(env);
			Object[] evaluatedArgs = EvalJavaHelper.evaluateArguments(env, args);
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
	default IEvalEnv InvokeSuper(IEvalEnv self, Class<?> clazz, String method, IEvalEnv... args) {
		throw new UnsupportedOperationException();
	}

	@Override
	default IEvalEnv SuperField(String name, Object self) {
		return env -> {
			Field f = EvalJavaHelper.findField(self.getClass().getSuperclass(), name);
			f.setAccessible(true);
			return f.get(self);
		};
	}

	@Override
	default IEvalEnv SuperField(Class<?> clazz, String name, Object self) {
		throw new UnsupportedOperationException();
	}

	@Override
	default IEvalEnv CastPrim(Class<?> clazz, IEvalEnv e) {
		return env -> clazz.cast(e.eval(env));
	}

	@Override
	default IEvalEnv PostDecr(IEvalEnv arg) {
		return env -> {
			IRef<Integer> r = (IRef<Integer>) arg.eval(env);
			return r.setValue(r.value() - 1);
		};
	}

	@Override
	default IEvalEnv Plus(IEvalEnv arg) {
		return env -> +(Integer) toValue(arg.eval(env));
	}

	@Override
	default IEvalEnv Not(IEvalEnv arg) {
		return env -> !((Boolean) arg.eval(env));
	}

	@Override
	default IEvalEnv Complement(IEvalEnv arg) {
		return env -> ~((Integer) arg.eval(env));
	}

	@Override
	default IEvalEnv PreIncr(IEvalEnv arg) {
		return env -> {
			IRef<Integer> r = (IRef<Integer>) arg.eval(env);
			Integer saved = r.value();
			return r.setValue(saved + 1);
		};
	}

	@Override
	default IEvalEnv PreDecr(IEvalEnv arg) {
		return env -> {
			IRef<Integer> r = (IRef<Integer>) arg.eval(env);
			Integer saved = r.value();
			return r.setValue(saved - 1);
		};
	}

	@Override
	default IEvalEnv Minus(IEvalEnv arg) {
		return env -> -(Integer) toValue(arg.eval(env));
	}

	@Override
	default IEvalEnv Div(IEvalEnv lhs, IEvalEnv rhs) {
		return env -> {
			return Integer.valueOf((Integer) toValue(lhs.eval(env)) / (Integer) toValue(rhs.eval(env)));
		};
	}

	@Override
	default IEvalEnv Remain(IEvalEnv lhs, IEvalEnv rhs) {
		return env -> {
			return Integer.valueOf((Integer) toValue(lhs.eval(env)) % (Integer) toValue(rhs.eval(env)));
		};
	}

	@Override
	default IEvalEnv Mul(IEvalEnv lhs, IEvalEnv rhs) {
		return env -> {
			return Integer.valueOf((Integer) toValue(lhs.eval(env)) * (Integer) toValue(rhs.eval(env)));
		};
	}

	@Override
	default IEvalEnv RightShift(IEvalEnv lhs, IEvalEnv rhs) {
		return env -> {
			return Integer.valueOf((Integer) toValue(lhs.eval(env)) >> (Integer) toValue(rhs.eval(env)));
		};
	}

	@Override
	default IEvalEnv URightShift(IEvalEnv lhs, IEvalEnv rhs) {
		return env -> {
			return Integer.valueOf((Integer) toValue(lhs.eval(env)) >>> (Integer) toValue(rhs.eval(env)));
		};
	}

	@Override
	default IEvalEnv LeftShift(IEvalEnv lhs, IEvalEnv rhs) {
		return env -> {
			return Integer.valueOf((Integer) toValue(lhs.eval(env)) << (Integer) toValue(rhs.eval(env)));
		};
	}

	@Override
	default IEvalEnv GtEq(IEvalEnv lhs, IEvalEnv rhs) {
		return env -> {
			return Boolean.valueOf((Integer) toValue(lhs.eval(env)) >= (Integer) toValue(rhs.eval(env)));
		};
	}

	@Override
	default IEvalEnv InstanceOf(IEvalEnv lhs, IEvalEnv rhs) {
		return env -> ((Class<?>) rhs.eval(env)).isInstance(lhs.eval(env));
	}

	@Override
	default IEvalEnv LtEq(IEvalEnv lhs, IEvalEnv rhs) {
		return env -> {
			return Boolean.valueOf((Integer) toValue(lhs.eval(env)) <= (Integer) toValue(rhs.eval(env)));
		};
	}

	@Override
	default IEvalEnv Eq(IEvalEnv lhs, IEvalEnv rhs) {
		return env -> toValue(lhs.eval(env)).equals(toValue(rhs.eval(env)));
	}

	@Override
	default IEvalEnv NotEq(IEvalEnv lhs, IEvalEnv rhs) {
		return env -> !toValue(lhs.eval(env)).equals(toValue(rhs.eval(env)));
	}

	@Override
	default IEvalEnv And(IEvalEnv lhs, IEvalEnv rhs) {
		return env -> ((Boolean) lhs.eval(env)) & ((Boolean) rhs.eval(env));
	}

	@Override
	default IEvalEnv ExcOr(IEvalEnv lhs, IEvalEnv rhs) {
		return env -> ((Boolean) lhs.eval(env)) ^ ((Boolean) rhs.eval(env));
	}

	@Override
	default IEvalEnv Or(IEvalEnv lhs, IEvalEnv rhs) {
		return env -> ((Boolean) lhs.eval(env)) | ((Boolean) rhs.eval(env));
	}

	@Override
	default IEvalEnv LazyAnd(IEvalEnv lhs, IEvalEnv rhs) {
		return env -> ((Boolean) lhs.eval(env)) && ((Boolean) rhs.eval(env));
	}

	@Override
	default IEvalEnv LazyOr(IEvalEnv lhs, IEvalEnv rhs) {
		return env -> ((Boolean) lhs.eval(env)) || ((Boolean) rhs.eval(env));
	}

	@Override
	default IEvalEnv AssignLeftShift(IEvalEnv lhs, IEvalEnv rhs) {
		return Assign(lhs, LeftShift(lhs, rhs));
	}

	@Override
	default IEvalEnv AssignOr(IEvalEnv lhs, IEvalEnv rhs) {
		return Assign(lhs, Or(lhs, rhs));
	}

	@Override
	default IEvalEnv AssignAnd(IEvalEnv lhs, IEvalEnv rhs) {
		return Assign(lhs, And(lhs, rhs));
	}

	@Override
	default IEvalEnv AssignRightShift(IEvalEnv lhs, IEvalEnv rhs) {
		return Assign(lhs, RightShift(lhs, rhs));
	}

	@Override
	default IEvalEnv AssignRemain(IEvalEnv lhs, IEvalEnv rhs) {
		return Assign(lhs, Remain(lhs, rhs));
	}

	@Override
	default IEvalEnv AssignPlus(IEvalEnv lhs, IEvalEnv rhs) {
		return Assign(lhs, Plus(lhs, rhs));
	}

	@Override
	default IEvalEnv AssignExcOr(IEvalEnv lhs, IEvalEnv rhs) {
		return Assign(lhs, ExcOr(lhs, rhs));
	}

	@Override
	default IEvalEnv AssignDiv(IEvalEnv lhs, IEvalEnv rhs) {
		return Assign(lhs, Div(lhs, rhs));
	}

	@Override
	default IEvalEnv AssignURightShift(IEvalEnv lhs, IEvalEnv rhs) {
		return Assign(lhs, URightShift(lhs, rhs));
	}

	@Override
	default IEvalEnv AssignMinus(IEvalEnv lhs, IEvalEnv rhs) {
		return Assign(lhs, Minus(lhs, rhs));
	}

	@Override
	default IEvalEnv AssignAdd(IEvalEnv lhs, IEvalEnv rhs) {
		return Assign(lhs, Plus(lhs, rhs));
	}

	@Override
	default IEvalEnv Lit(int n) {
		return env -> n;
	}

	@Override
	default IEvalEnv Lit(double d) {
		return env -> d;
	}

	@Override
	default IEvalEnv Lit(long l) {
		return env -> l;
	}

	@Override
	default IEvalEnv Lit(float f) {
		return env -> f;
	}

	@Override
	default IEvalEnv Lit(String s) {
		return env -> s;
	}

	@Override
	default IEvalEnv Lit(boolean b) {
		return env -> b;
	}

	@Override
	default IEvalEnv Assign(IEvalEnv lhs, IEvalEnv rhs) {
		return env -> {
			IRef r = (IRef<?>) lhs.eval(env);
			return r.setValue(toValue(rhs.eval(env)));
		};
	}

	@Override
	default IEvalEnv Ref(String name) {
		return env -> env.lookup(name);
	}
	
	@Override
	default IEvalEnv Var(String name) {
		return env -> env.lookup(name);
	}

	@Override
	default IEvalEnv Field(IEvalEnv recv, String name) {
		return env -> {
			Object o = toValue(recv.eval(env));
			return new ReflectRef(o, name);
		};
	}

	@Override
	default IEvalEnv This(Object self) {
		return env -> self;
	}

	@Override
	default IEvalEnv PostIncr(IEvalEnv arg) {
		return env -> {
			IRef<Integer> r = (IRef<Integer>) arg.eval(env);
			return r.setValue(r.value() + 1);

		};
	}

	@Override
	default IEvalEnv Gt(IEvalEnv l, IEvalEnv r) {
		return env -> {
			return Boolean.valueOf((Integer) toValue(l.eval(env)) > (Integer) toValue(r.eval(env)));
		};
	}

	default IEvalEnv Lt(IEvalEnv l, IEvalEnv r) {
		return env -> {
			return (Integer) toValue(l.eval(env)) < (Integer) toValue(r.eval(env));
		};
	}

	@Override
	default IEvalEnv Plus(IEvalEnv l, IEvalEnv r) {
		return env -> {
			Object lo = toValue(l.eval(env));
			Object ro = toValue(r.eval(env));

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
	default IEvalEnv Minus(IEvalEnv l, IEvalEnv r) {
		return env -> {
			return Integer.valueOf((Integer) toValue(l.eval(env)) - (Integer) toValue(r.eval(env)));
		};
	}

	@Override
	default IEvalEnv New(Class<?> clazz, IEvalEnv... args) {
		return env -> {
			// todo: this does not deal with non-static inner classes, which require
			// the enclosing instance as first param to the constructor. 
			Object[] evaluatedArgs = EvalJavaHelper.evaluateArguments(env, args);
			Constructor<?> constructor = EvalJavaHelper.findConstructor(clazz, evaluatedArgs);
			constructor.setAccessible(true);
			return constructor.newInstance(evaluatedArgs);
		};
	}

	@Override
	default IEvalEnv Invoke(IEvalEnv obj, String method, IEvalEnv... args) {
		return env -> {
			Object o = toValue(obj.eval(env));
			Object[] evaluatedArgs = EvalJavaHelper.evaluateArguments(env, args);
			Method m = EvalJavaHelper.findMethod(o.getClass(), method, evaluatedArgs);
			m.setAccessible(true);
			return m.invoke(o, evaluatedArgs);
		};
	}

}
