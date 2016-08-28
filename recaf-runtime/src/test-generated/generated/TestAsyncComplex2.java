package generated;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import recaf.demo.cps.AsyncFull;
 
class MyPrinter{
	public MyPrinter(){}

	public Void print(String msg) {
		System.out.println(msg);		
		return null;
	}	
} 
 
public class TestAsyncComplex2 {

    private static AsyncFull<Integer> alg = new AsyncFull<Integer>() {};

	public Void pause(Integer sleeptime) {
	    try {   
    	    new Thread().sleep(sleeptime);
    	    return null;
    	} catch (InterruptedException ex) {
    		return null;
    	}
	}
	

	  CompletableFuture<Integer> op() {
  return (CompletableFuture<Integer>)alg.Method(alg.Seq(alg.If(alg.Lt(alg.Lit(1), alg.Lit(2)), alg.Seq(alg.ExpStat(alg.Invoke(alg.This(this), "pause", alg.Lit(2500))), alg.Seq(alg.ExpStat(alg.Invoke(alg.New(MyPrinter.class), "print", alg.Lit("delayed op"))), alg.Return(alg.Lit(42))))), alg.Return(alg.Lit(41))));
}

	  CompletableFuture<Integer> op2() {
  return (CompletableFuture<Integer>)alg.Method(alg.Seq(alg.If(alg.Lt(alg.Lit(1), alg.Lit(2)), alg.Await(alg.Invoke(alg.This(this), "op"), (x) -> { return alg.Await(alg.Invoke(alg.This(this), "op"), (y) -> { return alg.Return(alg.Plus(alg.Var("x", x), alg.Var("y", y))); }); })), alg.Seq(alg.ExpStat(alg.Invoke(alg.This(this), "pause", alg.Lit(5000))), alg.Return(alg.Lit(41)))));
}
  	
  	public static void main(String[] args) throws InterruptedException, ExecutionException{
  		CompletableFuture<Integer> answer;
  				
  		answer = new TestAsyncComplex2().op2();
			
		new MyPrinter().print("main");
		
  		System.out.println(answer.get());
  	}
}