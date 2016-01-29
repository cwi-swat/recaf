
import recaf.visit.VisitExtension;

public class TestVisit {

	abstract class Exp {
	}

	abstract class Binary extends Exp {
		protected Exp l, r;

		public Binary(Exp l, Exp r) {
			this.l = l;
			this.r = r;
		}
	}

	class Add extends Binary {
		public Add(Exp l, Exp r) {
			super(l, r);
		}
	}

	class Sub extends Binary {
		public Sub(Exp l, Exp r) {
			super(l, r);
		}
	}

	class Var extends Exp {
		private final String x;

		public Var(String x) {
			this.x = x;
		}
	}

	private int n = 0;

	Integer countVars(VisitExtension<Integer> alg, Exp e) {
		return (Integer) alg.Method(alg.Seq(alg.Visit(alg.Exp(() -> {
			return e;
		}), java.util.Arrays.asList(alg.When((Var x) -> {
			return alg.ExpStat(alg.Exp(() -> {
				return n += 1;
			}));
		}))), alg.Return(alg.Exp(() -> {
			return n;
		}))));
	}

}