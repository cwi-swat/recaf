package generated; 

import recaf.core.full.FullJavaDirect;

class AC{
	private int a;

	public AC(){
	
	}

	void setA(int a){
		this.a = a;
	}
	
	int getA(){
		return a;
	}
}


public class TestExprSimpleMethodLookup   {
  private static FullJavaDirect<Object> alg = new FullJavaDirect<Object>() {};
  
  private static  int simple() {
  return (Integer)alg.Method(alg.Decl(alg.New(AC.class), (recaf.core.Ref<AC > a) -> {return alg.Seq(alg.ExpStat(alg.Invoke(alg.Ref("a", a), "setA", alg.Lit(4))), alg.Return(alg.Invoke(alg.Ref("a", a), "getA")));}));
} 

  public static void main(String args[]) {
    int n = simple();
    System.out.println(n);
    
  }
}