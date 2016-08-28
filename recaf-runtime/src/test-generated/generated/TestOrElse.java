package generated;

import recaf.demo.direct.OrElse;

public class TestOrElse {

  private static OrElse<Object> alg = new OrElse<Object>() {};

  Integer someMethod(int i) {
    System.out.println("Called: " + i);
    if (i % 2 != 0) {
      return null;
    }
    return i;
  }

    Object  orElse() {
  return (Object )alg.Method(alg.Return(alg.OrElse(alg.Lit(null), alg.Invoke(alg.This(this), "someMethod", alg.Lit(1)), alg.Invoke(alg.This(this), "someMethod", alg.Lit(2)), alg.Invoke(alg.This(this), "someMethod", alg.Lit(3)))));
}

  public static void main(String args[]) {
     new TestOrElse().orElse();
  }

}