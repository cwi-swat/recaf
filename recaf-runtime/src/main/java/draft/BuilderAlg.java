package draft;

import higher.App;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by bibou on 3/20/15.
 */
public interface BuilderAlg<M> {
    <T, U> App<M, U> Bind(App<M, T> app, Function<T, App<M, U>> f);
    <T> App<M, T> Delay(Supplier<App<M, T>> f);
    <T> App<M, T> Return(T t);
    <T> App<M, T> ReturnFrom(App<M, T> t);
    <T> App<M, T> Run(App<M, T> t);
    <T> App<M, T> Combine(App<M, T> f, App<M, T> g);
    <T> App<M, T> Yield(T t);
    <T> App<M, T> YieldFrom(App<M, T> t);
    <T> App<M, T> While(Supplier<Boolean> body, App<M, T> t);
}
