package generated;

import recaf.demo.pegs.MemoPEG;
import recaf.demo.pegs.PEG;
import recaf.demo.pegs.PEGwithLayout;
import recaf.demo.pegs.Parser;

public class TestPEG {

  abstract class Exp { }
  
  class Bin extends Exp {
    String op; Exp lhs, rhs;
    Bin(String op, Exp lhs, Exp rhs) {
      this.op = op;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    @Override
    public String toString() {
        return "(" + lhs + op + rhs + ")";
    }
  }
  
  class Int extends Exp {
    int value;
    Int(String n) {
      this.value = Integer.parseInt(n);
    }
    @Override
    public String toString() {
        return "" + value;
    }
  }
  
  class XML extends Exp {
    Exp exp;
    String tag;
    XML(String tag, Exp exp) {
      this.tag = tag;
      this.exp = exp;
    }
    
    @Override
    public String toString() {
      return "<" + tag + ">" + exp + "</" + tag + ">";
    }
  }
  
  private final PEG<Exp> alg;
  
  TestPEG(PEG<Exp> alg) {
    this.alg = alg;
  }
  
    Parser<Exp> exp() {
  return (Parser<Exp>)alg.Method(alg.Let(() -> addSub(), (e) -> { return alg.Return(() -> e); }));
}

    Parser<Exp> xml(String tag) {
  recaf.core.Ref<String > $tag = new recaf.core.Ref<String >(tag);
  return (Parser<Exp>)alg.Method(alg.Seq(alg.Lit(() -> "<" + $tag.value + ">"), alg.Let(() -> exp(), (e) -> { return alg.Seq(alg.Lit(() -> "</" + $tag.value + ">"), alg.Return(() -> new XML($tag.value, e))); })));
}
  
  
//BEGIN_PARSER_ADDSUB
    Parser<Exp> addSub() {
  return (Parser<Exp>)alg.Method(alg.Let(() -> mulDiv(), (l) -> { return alg.Star(() -> l, (Exp e) -> { return alg.Regexp(() -> "[+\\-]", (o) -> { return alg.Let(() -> mulDiv(), (r) -> { return alg.Return(() -> new Bin(o, e, r)); }); }); }, (Exp e) -> { return alg.Return(() -> e); }); }));
}
//END_PARSER_ADDSUB

//BEGIN_PARSER_MULDIV
    Parser<Exp> mulDiv() {
  return (Parser<Exp>)alg.Method(alg.Let(() -> primary(), (l) -> { return alg.Star(() -> l, (Exp e) -> { return alg.Regexp(() -> "[*/]", (o) -> { return alg.Let(() -> primary(), (r) -> { return alg.Return(() -> new Bin(o, e, r)); }); }); }, (Exp e) -> { return alg.Return(() -> e); }); }));
}
//END_PARSER_MULDIV
  
//BEGIN_PARSER_PRIMARY
    Parser<Exp> primary() {
  return (Parser<Exp>)alg.Method(alg.Choice(java.util.Arrays.asList(alg.Alt(() -> "value", alg.Regexp(() -> "[0-9]+", (n) -> { return alg.Return(() -> new Int(n)); })), alg.Alt(() -> "bracket", alg.Seq(alg.Lit(() -> "("), alg.Let(() -> addSub(), (e) -> { return alg.Seq(alg.Lit(() -> ")"), alg.Return(() -> e)); }))))));
}
//END_PARSER_PRIMARY
  
  public static String genExp(int n)  {
    if (n == 0) {
      return "1";
    }
    if (n % 2 == 0) {
	    return genExp(n - 1) + "+" + genExp(n - 1);
	}
	return genExp(n - 1) + "*" + genExp(n - 1);
  }
  
  public static void main(String args[]) {
    //Parser<Exp> p = new TestPEG(new PEG<Exp>() {}).exp();
    //System.out.println(p.parse("<xml>(1+2)*(3+4)</xml>", 0).getValue());
    
    Parser<Exp> p = new TestPEG(new PEG<Exp>() {}).exp();
    System.out.println(p.parse("(1+2)*(3+4)", 0).getValue());
    
    p = new TestPEG(new MemoPEG<Exp>()).exp();
    System.out.println(p.parse("(1+2)*(3+4)", 0).getValue());
    
    p = new TestPEG(new PEGwithLayout<Exp>("[\\ \\t\\n]*")).exp();
    System.out.println(p.parse("(1 + 2) * (3 + 4)", 0).getValue());
    
    
    System.out.println("Generating genExp(20)");
    String big = genExp(5);
    System.out.println(big);
    
    System.out.println("NO memo");
    p = new TestPEG(new PEG<Exp>() {}).exp();
    //System.out.println(p.parse(big, 0).getValue());
    
    System.out.println("WITH memo");
    p = new TestPEG(new MemoPEG<Exp>()).exp();
    //System.out.println(p.parse(big, 0).getValue());
    System.out.println("done");
    
  }
  
}