package generated; 

import recaf.core.full.FullJavaDirect;

class AA{
	private int a = 0;
	
	private AA(){

	}
	
	@Override
	public String toString(){
		return "AA("+a+")";
	} 
}


public class TestExprPrivateConstructor   {
  private static FullJavaDirect<Object> alg = new FullJavaDirect<Object>() {};
  
  private static  AA  simple() {
  return (AA )alg.Method(alg.Decl(alg.New(AA.class), (recaf.core.Ref<AA > a) -> {return alg.Return(alg.Ref("a", a));}));
} 

  public static void main(String args[]) {
    AA aa = simple();
    System.out.println(aa);
    
  }
}