package recaf.demo.direct;

import java.io.Closeable;
import java.util.function.Function;

import recaf.core.ISupply;
import recaf.core.direct.IExec;
import recaf.core.full.FullJavaDirect;

public interface Using<R> extends FullJavaDirect<R> {

	default <U extends Closeable> IExec Using(ISupply<U> resource, Function<U, IExec> body) {
		return l -> {
			U u = null;
			try {
				u = resource.get();
				body.apply(u).exec(null);
			}
			finally {
				if (u != null) {
					u.close();
				}
			}
		};
	}
	
	//  more generic 
	//	public interface Using<R, S extends IExec> extends FullJavaDirect<R> {
	//
	//		default <U extends Closeable> S Using(ISupply<U> resource, Function<U, S> body) {
	//			return (S) new IExec() {
	//				@Override
	//				public void exec(String label) throws Throwable {
	//				
	//						U u = null;
	//						try {
	//							u = resource.get();
	//							body.apply(u).exec(null);
	//						}
	//						finally {
	//							if (u != null) {
	//								u.close();
	//							}
	//						}
	//							
	//				}};
//		}
	
}
