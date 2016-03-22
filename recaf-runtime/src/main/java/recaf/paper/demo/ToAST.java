package recaf.paper.demo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.function.Function;

import recaf.paper.demo.ast.Expr;
import recaf.paper.demo.ast.If;
import recaf.paper.demo.ast.Stm;
import recaf.paper.expr.MuExpJava;
import recaf.paper.full.MuStmJava;

public class ToAST implements MuStmJava<Stm, Expr>, MuExpJava<Expr> {

	private Stm callClosure(Object f) {
		for (java.lang.reflect.Method m: f.getClass().getMethods()) {
			Object args = new Object[m.getParameterCount()];
			try {
				return (Stm) m.invoke(f, args);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}


	private <T> String getName(Function<T, Stm> s) {
		return getParams(s)[0];
	}

	private String[] getParams(Object f) {
		for (java.lang.reflect.Method m: f.getClass().getMethods()) {
			String args[] = new String[m.getParameterCount()];
			int i = 0;
			for (Parameter p: m.getParameters()) {
				args[i] = p.getName();
				i++;
			}
			return args;
		}
		return null;
	}
	
	@Override
	public Stm Exp(Expr x) {
		return new recaf.paper.demo.ast.Exp(x);
	}
	
	
	@Override
	public <T> Stm Decl(Expr x, Function<T, Stm> s) {
		return new recaf.paper.demo.ast.Decl(getName(s), x, s.apply(null));
	}
	

	@Override
	public <T> Stm For(Expr x, Function<T, Stm> s) {
		return new recaf.paper.demo.ast.For(getName(s), x, s.apply(null));
	}

	@Override
	public Stm If(Expr c, Stm s1, Stm s2) {
		return new If(c, s1, s2);
	}

	@Override
	public Stm Return(Expr x) {
		return new recaf.paper.demo.ast.Return(x);
	}

	@Override
	public Stm Seq(Stm s1, Stm s2) {
		return new recaf.paper.demo.ast.Seq(s1, s2);
	}

	@Override
	public Stm Empty() {
		return new recaf.paper.demo.ast.Empty();
	}

	@Override
	public Expr Lit(Object x) {
		return new recaf.paper.demo.ast.Lit(x);
	}

	@Override
	public Expr This(Object x) {
		return new recaf.paper.demo.ast.This(x);
	}

	@Override
	public Expr Field(Expr x, String f) {
		return new recaf.paper.demo.ast.Field(x, f);
	}

	@Override
	public Expr New(Class<?> c, Expr... es) {
		return new recaf.paper.demo.ast.New(c, es);
	}

	@Override
	public Expr Invoke(Expr x, String m, Expr... es) {
		return new recaf.paper.demo.ast.Invoke(x, m, es);
	}

	@Override
	public Expr Lambda(Object f) {
		return new recaf.paper.demo.ast.Lambda(getParams(f), callClosure(f));
	}

	@Override
	public Expr Var(String x, Object it) {
		return new recaf.paper.demo.ast.Var(x, it);
	}

}
