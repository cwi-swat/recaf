package recaf.demo.cps.stream;

import java.util.concurrent.Future;

public interface StreamSubscription<T> {
	Future<T> cancel();
	void pause();
	void resume();
	void close();
}
