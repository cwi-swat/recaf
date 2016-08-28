package generated; 

import recaf.core.full.FullJavaDirect;

class AD{
	private int a;

	public AD(){
	
	}

	void setA(int a){
		this.a = a;
	}
	
	int getA(){
		return a;
	}
}

class AE extends AD{
	@Override
	int getA(){
		return -1;
	}
}


public class TestExprInheritanceMethodLookup   {
  private static FullJavaDirect<Object> alg = new FullJavaDirect<Object>() {};
  
  private static  int simple() {
  return (Integer)alg.Method(alg.Decl(alg.New(AE.class), (recaf.core.Ref<AE > a) -> {return alg.Seq(alg.ExpStat(alg.Invoke(alg.Ref("a", a), "setA", alg.Lit(4))), alg.Return(alg.Invoke(alg.Ref("a", a), "getA")));}));
} 

  public static void main(String args[]) {
    int n = simple();
    System.out.println(n);
    
  }
}