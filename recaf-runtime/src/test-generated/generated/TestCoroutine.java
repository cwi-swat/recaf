
package generated;

import recaf.demo.cps.Coroutine;
import java.util.Random;


public class TestCoroutine {

  static void producer(Coroutine.Co<Integer, Integer> other) throws InterruptedException {
    while (true) {
      Thread.sleep(1000);
      int data = (new Random()).nextInt();
      System.out.println("Sending " + data);
      System.out.println("Producer received: " + other.resume(data));
    }
  }
  
  static  Coroutine.Co<Integer, Integer> consumer(Coroutine<Integer,Integer> alg) {
  return (Coroutine.Co<Integer, Integer>)alg.Method(alg.Decl(() -> 0, (recaf.core.Ref<Integer> i) -> {return alg.While(() -> true, alg.Yield(() -> i.value++, (Integer x) -> { return alg.Seq(alg.ExpStat(() -> { System.out.println("consumer received: " + x); return null; }), alg.Yield(() -> 2 * i.value++)); }));}));
}
  
  public static void main(String args[]) throws InterruptedException {
     Coroutine.Co<Integer,Integer> coro = consumer(new Coroutine<Integer,Integer>());
     coro.run();
     producer(coro);
  }

}