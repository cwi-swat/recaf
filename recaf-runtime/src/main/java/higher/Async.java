package higher;

/**
 * Created by bibou on 3/20/15.
 */

public interface Async<T> extends App<Async.t, T> {
    static <A> Async<A> prj(App<Async.t, A> app) {
        return (Async<A>) app;
    }

    static class t {
    }
}
