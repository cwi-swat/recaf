package generated; 

import recaf.core.full.FullJavaDirect;



public class TestExprNoConstructor  {
  private static FullJavaDirect<Object> alg = new FullJavaDirect<Object>() {};

  static class AB {
    private int a;
    
    @Override
    public String toString(){
        return "AB("+a+")";
    } 
  }
  
  private static  AB  simple() {
  return (AB )alg.Method(alg.Decl(alg.New(AB.class), (recaf.core.Ref<AB > a) -> {return alg.Return(alg.Ref("a", a));}));
} 

  public static void main(String args[]) {
    AB a = simple();
    System.out.println(a);
    
  }
}