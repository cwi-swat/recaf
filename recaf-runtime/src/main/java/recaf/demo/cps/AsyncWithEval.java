package recaf.demo.cps;

import java.util.concurrent.Future;

import recaf.core.alg.JavaMethodAlg;
import recaf.core.cps.FullJava;
import recaf.core.cps.SD;
import recaf.core.direct.IEval;

public class AsyncWithEval<R> extends Async<R, IEval> implements FullJava<R>, JavaMethodAlg<Future<R>, SD<R>> {

}
