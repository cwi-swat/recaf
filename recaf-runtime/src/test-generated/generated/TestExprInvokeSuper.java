package generated; 

import recaf.core.full.FullJavaDirect;

public class TestExprInvokeSuper extends BaseTestExprInvokeSuper {
  private static FullJavaDirect<Object> alg = new FullJavaDirect<Object>() {};
  
  @Override
  public  String  toString() {
  return (String )alg.Method(alg.Decl(alg.InvokeSuper(alg.This(this), "toString"), (recaf.core.Ref<String > s) -> {return alg.Return(alg.Plus(alg.Plus(alg.Lit("son("), alg.Ref("s", s)), alg.Lit(")")));}));
} 

  public static void main(String args[]) {
    TestExprInvokeSuper o = new TestExprInvokeSuper();
    System.out.println(o.toString());
    
  }
}