 
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import recaf.async.AsyncExtension;
 
public class TestAsyncComplex {
	
	public static Void pause(long sleeptime) {
	    try {
    	    Thread.sleep(sleeptime);
    	} catch (InterruptedException ex) {	}
    	return null;
	}
	
	public static Void print(String msg) {
		System.out.println(msg);		
		return null;
	}

	CompletableFuture<Integer> op(AsyncExtension<Integer> alg) {
  return (CompletableFuture<Integer>)alg.Method(alg.Seq(alg.If(alg.Exp(() -> { return 1 < 2; }), alg.Seq(alg.ExpStat(alg.Exp(() -> { return pause(2500); })), alg.Seq(alg.ExpStat(alg.Exp(() -> { return print("delayed op"); })), alg.Return(alg.Exp(() -> { return 42; }))))), alg.Return(alg.Exp(() -> { return 41; }))));
}

	CompletableFuture<Integer> op2(AsyncExtension<Integer> alg) {
  return (CompletableFuture<Integer>)alg.Method(alg.Seq(alg.If(alg.Exp(() -> { return 1 < 2; }), alg.Await(alg.Exp(() -> { return op(alg); }), (Integer x) -> {return alg.Await(alg.Exp(() -> { return op(alg); }), (Integer y) -> {return alg.Return(alg.Exp(() -> { return x + y; }));});})), alg.Seq(alg.ExpStat(alg.Exp(() -> { return pause(5000); })), alg.Return(alg.Exp(() -> { return 41; })))));
}
  	
  	public static void main(String[] args) throws InterruptedException, ExecutionException{
  		CompletableFuture answer;
  				
  		answer = new TestAsyncComplex().op2(new AsyncExtension<Integer>());
			
		print("main");
		
  		System.out.println(answer.get());
  	}
}