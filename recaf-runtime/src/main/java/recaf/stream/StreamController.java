package recaf.stream;

import java.util.concurrent.Future;
import java.util.function.Consumer;

import recaf.core.functional.K0;

public class StreamController<T> {
	K0 onListen, onPause, onResume;
	
	public K0 getOnListen() {
		return onListen;
	}

	public void setOnListen(K0 onListen) {
		this.onListen = onListen;
	}

	public K0 getOnPause() {
		return onPause;
	}

	public void setOnPause(K0 onPause) {
		this.onPause = onPause;
	}

	public K0 getOnResume() {
		return onResume;
	}

	public void setOnResume(K0 onResume) {
		this.onResume = onResume;
	}

	public StreamController(K0 onListen, K0 onPause, K0 onResume) {
		super();
		this.onListen = onListen;
		this.onPause = onPause;
		this.onResume = onResume;
	}

	Stream<T> stream() {
		throw new UnsupportedOperationException();
	}

	public boolean isPaused() {
		throw new UnsupportedOperationException();
	}

	public Future<T> close() {
		throw new UnsupportedOperationException();
	}

	public void add(T t) {
		throw new UnsupportedOperationException();		
	}

	public Future<T> addStream(Stream<T> r) {
		throw new UnsupportedOperationException();
	}

}
