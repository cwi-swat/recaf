package recaf.asynciter;

import java.util.concurrent.Future;

public interface StreamSubscription<T> {
	Future<T> cancel();
	void pause();
	void resume();
}
