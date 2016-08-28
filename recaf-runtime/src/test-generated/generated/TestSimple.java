package generated; 
import recaf.core.Ref;
import recaf.core.full.FullJavaDirect;
import java.util.stream.IntStream;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.function.*;  

class A{
	int i = 1;
	
	int a(int x, int y){
		return ++i;
	}
	
	public A(){
	
	}
}

public class TestSimple    {
  private static FullJavaDirect<Object> alg = new FullJavaDirect<Object>() {};
  
  private static  Integer  simple() {
  return (Integer )alg.Method(alg.Decl(alg.Lit(4), (recaf.core.Ref<Integer> a) -> {return alg.Decl(alg.PostIncr(alg.Ref("a", a)), (recaf.core.Ref<Integer> b) -> {return alg.Decl(alg.Lit(0), (recaf.core.Ref<Integer> d) -> {return alg.Seq(alg.While(alg.Lt(alg.Ref("d", d), alg.Lit(5)), alg.ExpStat(alg.PreIncr(alg.Ref("d", d)))), alg.Return(alg.Ref("d", d)));});});}));
} 
  
  private static  Integer  access() {
  return (Integer )alg.Method(alg.Decl(alg.Lit(4), (recaf.core.Ref<Integer> a) -> {return alg.Return(alg.Ref("a", a));}));
} 
  
  private static  Integer  methodCall() {
  return (Integer )alg.Method(alg.Return(alg.Invoke(alg.New(A.class), "a", alg.Lit(1),alg.Lit(2))));
}  
  
  private static  Integer  fieldAccess() {
  return (Integer )alg.Method(alg.Return(alg.Field(alg.New(A.class), "i")));
}  
  
  private static  Integer  many() {
  return (Integer )alg.Method(alg.Decl(alg.New(A.class), (recaf.core.Ref<A > a) -> {return alg.Seq(alg.ExpStat(alg.Assign(alg.Field(alg.Ref("a", a), "i"), alg.Lit(3))), alg.Return(alg.Field(alg.Ref("a", a), "i")));}));
}
  
  
  public static void main(String args[]) {
    int n = many();
    System.out.println(n);
    
  }
}