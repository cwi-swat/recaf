package recaf.paper.expr;

@SuppressWarnings("unchecked")
public
//BEGIN_MUEXPJAVA
interface MuExpJava<E> {
	E Lit(Object x);
	E This(Object x);
	E Field(E x, String f);
	E New(Class<?> c, E...es);
	E Invoke(E x, String m, E...es);
	E Lambda(Object f);
	E Var(String x, Object it);
}
//END_MUEXPJAVA
