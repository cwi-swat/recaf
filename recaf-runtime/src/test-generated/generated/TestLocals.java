package generated;

import recaf.core.cps.StmtJavaCPS;
import static java.util.Arrays.asList;

public class TestLocals {

  public  Integer  doit(StmtJavaCPS<Integer> alg, int x, final Integer $x) {
  recaf.core.Ref<Integer> $$$x = new recaf.core.Ref<Integer>(x); final Integer  $$$$x = $x;
  return (Integer )alg.Method(alg.Seq(alg.Return(() -> $$$x.value), alg.Seq(alg.Return(() -> $$$$x), alg.Decl(null, (recaf.core.Ref<Object[]> objs) -> {return alg.<Object >ForEach(() -> asList(objs.value), (recaf.core.Ref<Object > $$x) -> alg.Empty());}))));
}

}