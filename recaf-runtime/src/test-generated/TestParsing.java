

public class TestParsing {

   Parser<Element> element(String tag) {
       lit! "<" + tag + ">";
       star { nt! content(); }
       lit! "</" + tag + ">";
   }
   
   Parser<String> id() {
  Parsing<String> $alg = new Parsing<String>();
  return (Parser<String>)$alg.Method($alg.Lex($alg.Exp(() -> { return "[a-zA-Z][a-zA-Z0-9]*"; })));
}
   
   Parser<Exp> exp() {
  Parsing<Exp> $alg = new Parsing<Exp>();
  return (Parser<Exp>)$alg.Method($alg.Parse(java.util.Arrays.asList($alg.Alt($alg.Exp(() -> { return "add"; }), $alg.Let($alg.Exp(() -> { return exp(); }), (Exp lhs) -> { return $alg.Seq($alg.Lit($alg.Exp(() -> { return "+"; })), $alg.Let($alg.Exp(() -> { return exp(); }), (Exp rhs) -> { return $alg.Return($alg.Exp(() -> { return new Add(lhs, rhs); })); })); })), $alg.Alt($alg.Exp(() -> { return "var"; }), $alg.Let($alg.Exp(() -> { return id(); }), (String x) -> { return $alg.Return($alg.Exp(() -> { return new Var(x); })); })))));
}

}