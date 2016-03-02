module lang::recaf::desugar::RecafMethod

extend lang::recaf::desugar::Stmt;
import lang::recaf::desugar::Util;


MethodDec recafMethod((MethodDec)`<BeforeMethod* bm1> <TypeParams? tp1> <ResultType rt> <Id meth>(recaf <ClassOrInterfaceType t> <Id alg>, <{FormalParam ","}* fs>) <Block b>`, list[Id] algFields) 
  = (MethodDec)`<BeforeMethod* bm1> <TypeParams? tp1> <ResultType rt> <Id meth>(<ClassOrInterfaceType t> <Id alg>, <{FormalParam ","}* fs>) {
               '  <BlockStm* bs>
               '  <Stm ret>
               '}`
        when <bs, names> := fps2decls(fs, declaredNames(b) + { x | /Id x := fs }),
             cps := method2alg(b, alg, names),
             ret := makeReturn(rt, cps);


MethodDec recafMethod((MethodDec)`<BeforeMethod* bm0> recaf <BeforeMethod* bm2> <TypeParams? tp> <ResultType rt> <Id meth>(<{FormalParam ","}* fs>) <Block b>`, list[Id] algFields) 
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

Expr method2alg(Block b, Id alg, Names names) 
  = (Expr)`<Id alg>.Method(<Expr bcps>)`
  when
    bcps := block2alg(b, alg, names);

