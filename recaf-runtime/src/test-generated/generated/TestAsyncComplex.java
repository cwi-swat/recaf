package generated;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import recaf.demo.cps.Async;
 
public class TestAsyncComplex {

    private static Async<Integer> alg = new Async<Integer>() {};
    
	public static void pause(long sleeptime) {
	    try { 
    	    Thread.sleep(sleeptime);
    	} catch (InterruptedException ex) {	}
	}
	
	public static Void print(String msg) {
		System.out.println(msg);		
		return null;
	}

	  CompletableFuture<Integer> op() {
  return (CompletableFuture<Integer>)alg.Method(alg.Seq(alg.If(() -> 1 < 2, alg.Seq(alg.ExpStat(() -> { pause(2500); return null; }), alg.Seq(alg.ExpStat(() -> { print("delayed op"); return null; }), alg.Return(() -> 42)))), alg.Return(() -> 41)));
}

	  CompletableFuture<Integer> op2() {
  return (CompletableFuture<Integer>)alg.Method(alg.Seq(alg.If(() -> 1 < 2, alg.Await(() -> op(), (x) -> { return alg.Await(() -> op(), (y) -> { return alg.Return(() -> x + y); }); })), alg.Seq(alg.ExpStat(() -> { pause(5000); return null; }), alg.Return(() -> 41))));
}
  	
  	public static void main(String[] args) throws InterruptedException, ExecutionException{
  		CompletableFuture<Integer> answer;
  				
  		answer = new TestAsyncComplex().op2();
			
		print("main");
		
  		System.out.println(answer.get());
  	}
}