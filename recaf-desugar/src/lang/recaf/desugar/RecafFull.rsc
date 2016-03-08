module lang::recaf::desugar::RecafFull

extend lang::recaf::desugar::Expr;
import lang::recaf::desugar::Util;
import List;
import Set;
import String;
import IO;  
import util::Maybe;

MethodDec recafFull(MethodDec m, list[Id] algFields)
  = recafMethod(m, algFields);

MethodDec recafMethod((MethodDec)`<BeforeMethod* bm1> <TypeParams? tp1> <ResultType rt> <Id meth>(recaff <ClassOrInterfaceType t> <Id alg>, <{FormalParam ","}* fs>) <Block b>`, list[Id] algFields) 
  = (MethodDec)`<BeforeMethod* bm1> <TypeParams? tp1> <ResultType rt> <Id meth>(<ClassOrInterfaceType t> <Id alg>, <{FormalParam ","}* fs>) {
               '  <BlockStm* bs>
               '  <Stm ret>
               '}`
        when <bs, names> := fps2decls(fs, declaredNames(b) + { x | /Id x := fs }),
             cps := method2alg(b, alg, names),
             ret := makeReturn(rt, cps);

MethodDec recafMethod((MethodDec)`<BeforeMethod* bm0> recaff <BeforeMethod* bm2> <TypeParams? tp> <ResultType rt> <Id meth>(<{FormalParam ","}* fs>) <Block b>`, list[Id] algFields) 
   = (MethodDec)`<BeforeMethod* bm0> <BeforeMethod* bm2> <TypeParams? tp> <ResultType rt> <Id meth>(<{FormalParam ","}* fs>) {
                '  <BlockStm* bs>
                '  <Stm ret>
                '}`
    when
      Id alg <- algFields, 
      <bs, names> := fps2decls(fs, declaredNames(b) + { x | /Id x := fs }),
      cps := method2alg(b, alg, names),
      ret := makeReturn(rt, cps);

default MethodDec recafMethod(MethodDec m, list[Id] algFields) = m;

// NB: cannot use expr2alg because of void expressions.
Expr stm2alg((Stm)`<Expr e>;`, Id alg, Names names) 
  = (Expr)`<Id alg>.ExpStat(<Expr e2>)`
  when 
    Expr e2 := expr2alg(unwrapRefs(e, names), alg, names);
 
Expr injectExpr(Expr e, Id alg, Names names)
  = (Expr)`<Id alg>.Exp(<Expr e2>)`
  when
    Expr e2 := expr2alg(unwrapRefs(e, names), alg, names);


