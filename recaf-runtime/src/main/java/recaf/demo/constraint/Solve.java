package recaf.demo.constraint;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VariableFactory;

import recaf.core.alg.JavaMethodAlg;
import recaf.core.direct.IExec;

public class Solve implements JavaMethodAlg<Iterable<Map<String,Integer>>, IExec>  {

	private Solver solver;
	private int varCount = 0;
	private List<IntVar> vars = new ArrayList<>();
	private Map<IntVar, String> names = new HashMap<>();
	
	private String nextName() {
		return "var" + varCount++;
	}
	
	@Override
	public Iterable<Map<String,Integer>> Method(IExec body) {
		this.solver = new Solver();
		try {
			body.exec(null);
		}
		catch (Throwable e) {
			throw new RuntimeException(e);
		}
		IntVar[] arr = new IntVar[vars.size()];
		for (int i = 0; i < vars.size(); i++) {
			arr[i] = vars.get(i);
		}
		solver.set(IntStrategyFactory.lexico_LB(arr));
		return new Iterable<Map<String,Integer>>() {
			
			@Override
			public Iterator<Map<String, Integer>> iterator() {
				return new Iterator<Map<String,Integer>>() {
					boolean first = true;
					@Override
					public boolean hasNext() {
						if (first) {
							first = false;
							return solver.findSolution();
						}
						return solver.nextSolution();
					}

					@Override
					public Map<String, Integer> next() {
						Map<String, Integer> sol = new HashMap<>();
						for (IntVar var: vars) {
							sol.put(names.get(var), var.getValue());
						}
						return sol;
					}
				};
			}
		};
	}
	
	public <T> Supplier<T> Exp(T t) {
		return () -> t;
	}
	
	public <T extends IntVar> IExec Var(Supplier<Integer> from, Supplier<Integer> to, Function<? super IntVar, IExec> body) {
		return l -> {
			// todo: use reflection on body to obtain the variable name.
			IntVar x = VariableFactory.bounded(nextName(), from.get(), to.get(), solver);
			vars.add(x);
			body.apply(x).exec(null);
		};
	}
	
	public IExec Solve(Supplier<Constraint> x) {
		return l -> { solver.post(x.get()); };
	}
	
	public IExec Seq(IExec s1, IExec s2) {
		return l -> {
			s1.exec(l);
			s2.exec(null);
		};
	}
	
	public IntVar Var(String name, IntVar var) {
		// hack to get nice names
		names.put(var, name);
		return var;
	}

	public int Lit(int n) {
		return n; //VariableFactory.bounded(nextName(), n, n, solver);
	}
	
	private static abstract class LinExp {
		protected final IntVar lhs;
		protected final IntVar rhs;

		LinExp(IntVar lhs, IntVar rhs) {
			this.lhs = lhs;
			this.rhs = rhs;
		}
		
		abstract String op();
	}
	
	private static class Add extends LinExp {
		Add(IntVar lhs, IntVar rhs) {
			super(lhs, rhs);
		}

		@Override
		String op() {
			return "+";
		}
	}

	private static class Sub extends LinExp {
		Sub(IntVar lhs, IntVar rhs) {
			super(lhs, rhs);
		}

		@Override
		String op() {
			return "-";
		}
		
	}


	public Sub Minus(IntVar lhs, IntVar rhs) {
		return new Sub(lhs, rhs);
	}

	public Add Plus(IntVar lhs, IntVar rhs) {
		return new Add(lhs, rhs);
	}

	public Constraint GtEq(LinExp lhs, int rhs) {
		return IntConstraintFactory.arithm(lhs.lhs, lhs.op(), lhs.rhs, ">=", rhs);
	}

	public Constraint GtEq(IntVar lhs, int rhs) {
		return IntConstraintFactory.arithm(lhs, ">=", rhs);
	}
	
	public Constraint GtEq(IntVar lhs, IntVar rhs) {
		return IntConstraintFactory.arithm(lhs, ">=", rhs);
	}
	
	public Constraint Gt(LinExp lhs, int rhs) {
		return IntConstraintFactory.arithm(lhs.lhs, lhs.op(), lhs.rhs, ">", rhs);
	}

	public Constraint Gt(IntVar lhs, int rhs) {
		return IntConstraintFactory.arithm(lhs, ">", rhs);
	}
	
	public Constraint Gt(IntVar lhs, IntVar rhs) {
		return IntConstraintFactory.arithm(lhs, ">", rhs);
	}
	

	public Constraint LtEq(LinExp lhs, int rhs) {
		return IntConstraintFactory.arithm(lhs.lhs, lhs.op(), lhs.rhs, "<=", rhs);
	}

	public Constraint LtEq(IntVar lhs, int rhs) {
		return IntConstraintFactory.arithm(lhs, "<=", rhs);
	}
	
	public Constraint LtEq(IntVar lhs, IntVar rhs) {
		return IntConstraintFactory.arithm(lhs, "<=", rhs);
	}
	
	
	public Constraint Lt(LinExp lhs, int rhs) {
		return IntConstraintFactory.arithm(lhs.lhs, lhs.op(), lhs.rhs, "<", rhs);
	}

	public Constraint Lt(IntVar lhs, int rhs) {
		return IntConstraintFactory.arithm(lhs, "<", rhs);
	}
	
	public Constraint Lt(IntVar lhs, IntVar rhs) {
		return IntConstraintFactory.arithm(lhs, "<", rhs);
	}
	
	public Constraint Eq(LinExp lhs, int rhs) {
		return IntConstraintFactory.arithm(lhs.lhs, lhs.op(), lhs.rhs, "==", rhs);
	}

	public Constraint Eq(IntVar lhs, int rhs) {
		return IntConstraintFactory.arithm(lhs, "==", rhs);
	}
	
	public Constraint Eq(IntVar lhs, IntVar rhs) {
		return IntConstraintFactory.arithm(lhs, "==", rhs);
	}
	
	
	public Constraint NotEq(LinExp lhs, int rhs) {
		return IntConstraintFactory.arithm(lhs.lhs, lhs.op(), lhs.rhs, "!=", rhs);
	}

	public Constraint NotEq(IntVar lhs, int rhs) {
		return IntConstraintFactory.arithm(lhs, "!=", rhs);
	}
	
	public Constraint NotEq(IntVar lhs, IntVar rhs) {
		return IntConstraintFactory.arithm(lhs, "!=", rhs);
	}
	
	
}
