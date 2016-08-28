package generated; 

import recaf.core.full.FullJavaDirect;

public class TestExprFieldSuper extends BaseTestExprFieldSuper {
  private static FullJavaDirect<Object> alg = new FullJavaDirect<Object>() {};
  
  private int x = 5;
  
  public  int getThisX() {
  return (Integer)alg.Method(alg.Return(alg.Field(alg.This(this), "x")));
} 
  
   public  int getSuperX() {
  return (Integer)alg.Method(alg.Return(alg.SuperField("x", this)));
} 

  public static void main(String args[]) {
    TestExprFieldSuper o = new TestExprFieldSuper();
    System.out.println(o.getThisX());
    System.out.println(o.getSuperX());
  }
}