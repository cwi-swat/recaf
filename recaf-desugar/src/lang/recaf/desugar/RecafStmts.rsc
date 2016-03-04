module lang::recaf::desugar::RecafStmts

extend lang::recaf::desugar::Stmt;
import lang::recaf::desugar::Util;

import List;
import Set;
import String;
import IO;  
import util::Maybe;

MethodDec recafStms(MethodDec m, list[Id] algFields)
  = recafMethod(m, algFields);

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

// provide the implementation for injectExpr    
Expr injectExpr(Expr e, Id alg, Names names)
  = (Expr)`() -\> <Expr e2>`
  when
    Expr e2 := unwrapRefs(e, names);

// and exp stats.
Expr stm2alg((Stm)`<Expr e>;`, Id alg, Names names) 
  = (Expr)`<Id alg>.ExpStat(() -\> { <Expr e2>; return null; })`
  when 
    Expr e2 := unwrapRefs(e, names);


// TODO: stop at closure boundaries and anonymous inner classes.
Expr unwrapRefs(Expr e, Names names) {
  //println("Unwrapping for <e>");
  //for (x <- names.refs) {
  //  println("Ref: <x>");
  //}
  //for (k <- names.renaming) {
  //  println("<k> -\> <names.renaming[k]>");
  //}
  // ugh this is ugly.
  return visit (e) {
   case (LHS) `<Id x>` => (LHS) `<Id x>.value`
     when x in names.refs, x notin names.renaming
     
   case (Expr) `<Id x>` => (Expr)`<Id x>.value` 
     when x in names.refs, x notin names.renaming

   case (AmbName) `<Id x>` => (AmbName)`<Id x>.value` 
     when x in names.refs, x notin names.renaming

   case (LHS) `<Id x>` => (LHS) `<Id y>.value`
     when x in names.renaming, 
          Id y := names.renaming[x],
          y in names.refs
     
   case (Expr) `<Id x>` => (Expr)`<Id y>.value` 
     when x in names.renaming,
          Id y := names.renaming[x],
          y in names.refs

   case (AmbName) `<Id x>` => (AmbName)`<Id y>.value` 
     when x in names.renaming,
          Id y := names.renaming[x],
          y in names.refs
          
   case (LHS) `<Id x>` => (LHS) `<Id y>`
     when x in names.renaming,
          Id y := names.renaming[x],
          y notin names.refs
     
   case (Expr) `<Id x>` => (Expr)`<Id y>` 
     when x in names.renaming,
          Id y := names.renaming[x],
          y notin names.refs

   case (AmbName) `<Id x>` => (AmbName)`<Id y>` 
     when x in names.renaming,
          Id y := names.renaming[x],
          y notin names.refs

  }    
}
