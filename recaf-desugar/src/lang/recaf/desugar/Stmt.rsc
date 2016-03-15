module lang::recaf::desugar::Stmt

import lang::recaf::Recaf;
import lang::recaf::desugar::Util;
import List;
import Set;
import String;
import IO;  
import util::Maybe;
import ParseTree;

// TODO: factor out extension desugaring.

// this should ABSTRACT, to be provided by clients.
default Expr injectExpr(Expr e, Id alg, Names names) {
  throw "please implement this";
}

Expr method2alg(Block b, Id alg, Names names) 
  = (Expr)`<Id alg>.Method(<Expr bcps>)`
  when
    bcps := block2alg(b, alg, names);

/// Extensions TODO: move to its own module.

// return-like
Expr stm2alg((Stm)`<KId ext>! <Expr e>;`, Id alg, Names names) 
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>)`
  when
    Expr ecps := injectExpr(e, alg, names),
    Id method := [Id]capitalize("<ext>");

// return-like
Expr stm2alg((Stm)`<KId ext>!;`, Id alg, Names names) 
  = (Expr)`<Id alg>.<Id method>()`
  when
    Id method := [Id]capitalize("<ext>");

// for like
Expr stm2alg((Stm)`<KId ext> (<FormalParam f>: <Expr e>) <Stm s>`, Id alg, Names names) 
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, (<FormalParam f>) -\> {return <Expr scps>;})`
  when 
    Expr ecps := injectExpr(e, alg, names),
    Expr scps := stm2alg(s, alg, names),
    Id method := [Id]capitalize("<ext>");

// while like
Expr stm2alg((Stm)`<KId ext> (<{Expr ","}+ cs>) <Block b>`, Id alg, Names names) 
  = (Expr)`<Id alg>.<Id method>(<{Expr ","}+ ecps>, <Expr bcps>)`
  when 
    {Expr ","}+ ecps := injectExprs(cs, alg, names),
    Expr bcps := block2alg(b, alg, names),
    Id method := [Id]capitalize("<ext>");

// combinations of while/for.
Expr stm2alg((Stm)`<KId ext> (<{Expr ","}+ es>, <FormalParam f>: <Expr e>) <Stm s>`, Id alg, Names names) 
  = (Expr)`<Id alg>.<Id method>(<{Expr ","}+ escps>, <Expr ecps>, (<FormalParam f>) -\> {return <Expr scps>;})`
  when 
    {Expr ","}+ escps := injectExprs(es, alg, names),
    Expr ecps := injectExpr(e, alg, names),
    Expr scps := stm2alg(s, alg, names),
    Id method := [Id]capitalize("<ext>");


{Expr ","}+ injectExprs({Expr ","}+ es, Id alg, Names names) {
  // hack
  i = 0;
  while (i < size(es.args)) {
    if (Expr e := es.args[i]) {
      es = appl(es.prod, es.args[0..i] + [injectExpr(e, alg, names)] + es.args[i+1..]);
    } 
    else {
      throw "Non expression in expression list: <es.args[i]>";
    }
    i += 4;
  }
  return es;
}

// try-like
//Expr stm2alg((Stm)`<KId ext> <Block b>`, Id alg, Names names) 
//  = (Expr)`<Id alg>.<Id method>(<Expr bcps>)`
//  when 
//    Expr bcps := block2alg(b, alg, names),
//    Id method := [Id]capitalize("<ext>");


// try-like
Expr stm2alg((Stm)`<KId ext> <Stm s>`, Id alg, Names names) 
  = (Expr)`<Id alg>.<Id method>(<Expr scps>)`
  when 
    Expr scps := stm2alg(s, alg, names),
    Id method := [Id]capitalize("<ext>");

// switch-like
Expr stm2alg((Stm)`<KId ext> (<FormalParam f>: <Expr e>) {<Item+ items>}`, Id alg, Names names) 
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, (<FormalParam f>) -\> {return <Expr itemscps>;})`
  when 
    Expr ecps := injectExpr(e, alg, names),
    Expr itemscps := items2alg([ i | i <- items ], alg, names),
    Id method := [Id]capitalize("<ext>");

Expr stm2alg((Stm)`<KId ext> (<{Expr ","}+ es>) {<Item+ items>}`, Id alg, Names names) 
  = (Expr)`<Id alg>.<Id method>(<{Expr ","}+ ecps>, <Expr itemscps>)`
  when 
    {Expr ","}+ ecps := injectExprs(es, alg, names),
    Expr itemscps := items2alg([ i | i <- items ], alg, names),
    Id method := [Id]capitalize("<ext>");

Expr stm2alg((Stm)`<KId ext> {<Item+ items>}`, Id alg, Names names) 
  = (Expr)`<Id alg>.<Id method>(<Expr itemscps>)`
  when 
    Expr itemscps := items2alg([ i | i <- items ], alg, names),
    Id method := [Id]capitalize("<ext>");

Expr items2alg(list[Item] items:[], Id alg, Names names)
  = (Expr)`java.util.Arrays.asList()`;

Expr items2alg(list[Item] items:[Item x, *xs], Id alg, Names names)
  = (Expr)`java.util.Arrays.asList(<Expr xcps>, <{Expr ","}* es>)`
  when
    Expr xcps := item2alg(x, alg, names),
    (Expr)`java.util.Arrays.asList(<{Expr ","}* es>)` := items2alg(xs, alg, names);

Expr item2alg((Item)`<KId kw> <Expr e>: <BlockStm+ stms>`, Id alg, Names names)
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, <Expr stmscps>)`
  when 
    Id method := [Id]capitalize("<kw>"),
    Expr ecps := injectExpr(e, alg, names),
    Expr stmscps := block2alg((Block)`{<BlockStm+ stms>}`, alg, names);
  
Expr item2alg((Item)`<KId kw> <FormalParam f>: <BlockStm+ stms>`, Id alg, Names names)
  = (Expr)`<Id alg>.<Id method>((<FormalParam f>) -\> { return <Expr stmscps>; })`
  when 
    Id method := [Id]capitalize("<kw>"),
    Expr stmscps := block2alg((Block)`{<BlockStm+ stms>}`, alg, names);


// new style (only final vars).
Expr stm2alg((Stm)`<KId kw> (<{FormalParam ","}+ fs>) <Stm stm>`, Id alg, Names names)
  = (Expr)`<Id alg>.<Id method>((<{FormalParam  ","}+ fs>) -\> { return <Expr stmcps>; })`
  when
    Id method := [Id]capitalize("<kw>"),
    Expr stmcps := stm2alg(stm, alg, names /* ( names | declare(f, it) | f <- fs ) */);
  

//////


Expr stm2alg((Stm)`continue <Id x>;`, Id alg, Names names) 
  = (Expr)`<Id alg>.Continue(<Expr label>)`
  when 
    Expr label := [Expr]"\"<x>\"";
  
Expr stm2alg((Stm)`continue;`, Id alg, Names names) 
  = (Expr)`<Id alg>.Continue()`;

Expr stm2alg((Stm)`break <Id x>;`, Id alg, Names names) 
  = (Expr)`<Id alg>.Break(<Expr label>)`
  when 
    Expr label := [Expr]"\"<x>\"";
  
Expr stm2alg((Stm)`break;`, Id alg, Names names) 
  = (Expr)`<Id alg>.Break()`;

FormalParam fp2ref((FormalParam)`<Type t> <Id x>`)
  = (FormalParam)`recaf.core.Ref\<<Type t2>\> <Id x>`
  when
    t2 := boxed(t);


// NB: for loop vars are mutable.
Expr stm2alg((Stm)`for (<FormalParam f>: <Expr e>) <Stm s>`, Id alg, Names names) 
  = (Expr)`<Id alg>.\<<Type t>\>ForEach(<Expr ecps>, (<FormalParam f2>) -\> <Expr scps>)`
  when 
    Expr ecps := injectExpr(e, alg, names),
    Expr scps := stm2alg(s, alg, declare(f, names)),
    FormalParam f2 := fp2ref(f),
    Type t := boxed(typeOf(f));

// For loops with init expressions
Expr stm2alg((Stm)`for (<{Expr ","}* init>; <Expr cond>; <{Expr ","}* update>) <Stm body>`, Id alg, Names names)
  = (Expr)`<Id alg>.For(<Expr initcps>, <Expr condcps>, <Expr updatecps>, <Expr bodycps>)`
  when
    Expr initcps := exps2alg(init, alg, names),
    Expr condcps := injectExpr(cond, alg, names),
    Expr updatecps := exps2alg(update, alg, names),
    Expr bodycps := stm2alg(body, alg, names);

// For loops with decls (unlabeled and labeled)
// NB multiple vardecs cannot be supported
Expr stm2alg((Stm)`for (<Type t> <Id x>; <Expr cond>; <{Expr ","}* update>) <Stm body>`, Id alg, Names names)
  =(Expr)`<Id alg>.ForDecl(null, (recaf.core.Ref\<<Type t2>\> <Id x>) -\> <Id alg>.ForBody(<Expr condcps>, <Expr updatecps>, <Expr bodycps>))`
  when
    Names names2 := declare(x, names),
    Expr condcps := injectExpr(cond, alg, names2),
    Expr updatecps := exps2alg(update, alg, names2),
    Expr bodycps := stm2alg(body, alg, names2),
    Type t2 := boxed(t);

Expr stm2alg((Stm)`for (<Type t> <Id x> = <Expr init>; <Expr cond>; <{Expr ","}* update>) <Stm body>`, Id alg, Names names)
  =(Expr)`<Id alg>.ForDecl(<Expr initcps>, (recaf.core.Ref\<<Type t2>\> <Id x>) -\> <Id alg>.ForBody(<Expr condcps>, <Expr updatecps>, <Expr bodycps>))`
  when
    Expr initcps := injectExpr(init, alg, names),
    Names names2 := declare(x, names),
    Expr condcps := injectExpr(cond, alg, names2),
    Expr updatecps := exps2alg(update, alg, names2),
    Expr bodycps := stm2alg(body, alg, names2),
    Type t2 := boxed(t);
  

Expr exps2alg({Expr ","}* exps, Id alg, Names names) {
  cur = (Expr)`<Id alg>.Empty()`;
  for (e <- reverse([ e | e <- exps ])) {
    s = stm2alg((Stm)`<Expr e>;`, alg, names);
    cur = (Expr)`<Id alg>.Seq(<Expr s>, <Expr cur>)`; 
  }
  return cur; 
}
    
Expr stm2alg((Stm)`throw <Expr e>;`, Id alg, Names names) 
  = (Expr)`<Id alg>.Throw(<Expr ecps>)`
  when
    Expr ecps := injectExpr(e, alg, names);

Expr stm2alg((Stm)`return <Expr e>;`, Id alg, Names names) 
  = (Expr)`<Id alg>.Return(<Expr ecps>)`
  when
    Expr ecps := injectExpr(e, alg, names);

Expr stm2alg((Stm)`return;`, Id alg, Names names) 
  = (Expr)`<Id alg>.Return()`;

Expr stm2alg((Stm)`;`, Id alg, Names names) = (Expr)`<Id alg>.Empty()`;

Expr stm2alg((Stm)`<Block b>`, Id alg, Names names) = block2alg(b, alg, names);

// TODO: ClassDec as blockstm
Expr block2alg((Block)`{}`, Id alg, Names names) = (Expr)`<Id alg>.Empty()`;

Expr block2alg((Block)`{<Type t> <VarDec vd>, <{VarDec ","}+ vs>;}`, Id alg, Names names) 
  = block2alg((Block)`{<Type t> <VarDec vd>; <Type t> <{VarDec ","}+ vs>;}`);

Expr block2alg((Block)`{<Type t> <VarDec vd>;}`, Id alg, Names names)
  = varDec2alg(t, vd, (Expr)`<Id alg>.Empty()`, alg, names); 

// todo: annos/modifiers
Expr block2alg((Block)`{<Type t> <VarDec vd>; <BlockStm+ ss>}`, Id alg, Names names) 
  = varDec2alg(t, vd, sscps, alg, names)
  when
    Expr sscps := block2alg((Block)`{<BlockStm+ ss>}`, alg, declare(vd, names));

// binding extensions (let/maybe etc.) introduce final variables. 
Expr block2alg((Block)`{<KId kw> <FormalParam f>;}`, Id alg, Names names) 
  = (Expr)`<Id alg>.<Id method>((<FormalParam f>) -\> <Id alg>.Empty())`
  when
    Id method := [Id]capitalize("<kw>"),
    Type rt := boxed(typeOf(f));

// KId {Expr ","}+ "," FormalParam ";"    

// TODO: fix the duplication with singletons blocks.
Expr block2alg((Block)`{<KId kw> <{Expr ","}+ es>, <FormalParam f>;}`, Id alg, Names names) 
  = (Expr)`<Id alg>.<Id method>(<{Expr ","}+ escps>, (<FormalParam f>) -\> <Id alg>.Empty())`
  when
    {Expr ","}+ escps := injectExprs(es, alg, names),
    Id method := [Id]capitalize("<kw>"),
    Type rt := boxed(typeOf(f));

Expr block2alg((Block)`{<KId kw> <{Expr ","}+ es>, <FormalParam f> = <Expr e>;}`, Id alg, Names names)
  = (Expr)`<Id alg>.<Id method>(<{Expr ","}+ escps>, <Expr ecps>, (<FormalParam f>) -\> <Id alg>.Empty())`
  when
    Id method := [Id]capitalize("<kw>"),
    Type rt := boxed(typeOf(f)),
    ecps := injectExpr(e, alg, names),
    {Expr ","}+ escps := injectExprs(es, alg, names);


Expr block2alg((Block)`{<KId kw> <FormalParam f> = <Expr e>;}`, Id alg, Names names) 
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, (<FormalParam f>) -\> <Id alg>.Empty())`
  when
    Id method := [Id]capitalize("<kw>"),
    Type rt := boxed(typeOf(f)),
    Expr ecps := injectExpr(e, alg, names);

Expr block2alg((Block)`{<FormalParam f> = <KId kw>! <Expr e>;}`, Id alg, Names names) 
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, (<FormalParam f>) -\> <Id alg>.Empty())`
  when
    Id method := [Id]capitalize("<kw>"),
    Type rt := boxed(typeOf(f)),
    Expr ecps := injectExpr(e, alg, names);

Expr block2alg((Block)`{<KId kw> <FormalParam f> = (<Expr e>) <Block b>}`, Id alg, Names names)
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, (<FormalParam f>) -\> { return <Expr bcps>; }, (<FormalParam f>) -\> { return <Id alg>.Empty(); })`
  when
    Id method := [Id]capitalize("<kw>"),
    Type rt := boxed(typeOf(f)),
    Expr ecps := injectExpr(e, alg, names),
    Expr bcps := block2alg(b, alg, names);


default Expr block2alg((Block)`{<Stm s>}`, Id alg, Names names) 
  = stm2alg(s, alg, names);

default Expr block2alg((Block)`{<Stm s> <BlockStm+ ss>}`, Id alg, Names names) 
  = (Expr)`<Id alg>.Seq(<Expr scps>, <Expr sscps>)`
  when
    Expr scps := stm2alg(s, alg, names),
    Expr sscps := block2alg((Block)`{<BlockStm* ss>}`, alg, names);

// binding extensions (let/maybe etc.) introduce final variables, so NO declare
Expr block2alg((Block)`{<KId kw> <FormalParam f>; <BlockStm+ ss>}`, Id alg, Names names)
  = (Expr)`<Id alg>.<Id method>((<FormalParam f>) -\> { return <Expr sscps>; })`
  when
    Id method := [Id]capitalize("<kw>"),
    Type rt := boxed(typeOf(f)),
    Expr sscps := block2alg((Block)`{<BlockStm+ ss>}`, alg, names);

Expr block2alg((Block)`{<KId kw> <{Expr ","}+ es>, <FormalParam f>; <BlockStm+ ss>}`, Id alg, Names names)
  = (Expr)`<Id alg>.<Id method>(<{Expr ","}+ escps>, (<FormalParam f>) -\> { return <Expr sscps>; })`
  when
    Id method := [Id]capitalize("<kw>"),
    Type rt := boxed(typeOf(f)),
    {Expr ","}+ escps := injectExprs(es, alg, names),
    Expr sscps := block2alg((Block)`{<BlockStm+ ss>}`, alg, names);

//   | KId {Expr ","}+ "," FormalParam "=" Expr ";"

Expr block2alg((Block)`{<KId kw> <{Expr ","}+ es>, <FormalParam f> = <Expr e>; <BlockStm+ ss>}`, Id alg, Names names)
  = (Expr)`<Id alg>.<Id method>(<{Expr ","}+ escps>, <Expr ecps>, (<FormalParam f>) -\> { return <Expr sscps>; })`
  when
    Id method := [Id]capitalize("<kw>"),
    Type rt := boxed(typeOf(f)),
    ecps := injectExpr(e, alg, names),
    {Expr ","}+ escps := injectExprs(es, alg, names),
    Expr sscps := block2alg((Block)`{<BlockStm+ ss>}`, alg, names);

Type typeOf((FormalParam)`final <Type t> <Id _>`) = t;
Type typeOf((FormalParam)`<Type t> <Id _>`) = t;

Id removeType((FormalParam) `<Type t> <Id id>`)   
  = (Id) `<Id id>`;

Expr block2alg((Block)`{<KId kw> <FormalParam f> = <Expr e>; <BlockStm+ ss>}`, Id alg, Names names)
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, (<Id fp>) -\> { return <Expr sscps>; })`
  when
    Id method := [Id]capitalize("<kw>"),
    Id fp := removeType((FormalParam) `<FormalParam f>`),
    Type rt := boxed(typeOf(f)),
    Expr ecps := injectExpr(e, alg, names),
    Expr sscps := block2alg((Block)`{<BlockStm+ ss>}`, alg, names);

Expr block2alg((Block)`{<FormalParam f> = <KId kw>! <Expr e>; <BlockStm+ ss>}`, Id alg, Names names)
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, (<FormalParam f>) -\> { return <Expr sscps>; })`
  when
    Id method := [Id]capitalize("<kw>"),
    Type rt := boxed(typeOf(f)),
    Expr ecps := injectExpr(e, alg, names),
    Expr sscps := block2alg((Block)`{<BlockStm+ ss>}`, alg, names);

//KId FormalParam "=" "(" Expr ")" Block
Expr block2alg((Block)`{<KId kw> <FormalParam f> = (<Expr e>) <Block b> <BlockStm+ ss>}`, Id alg, Names names)
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, (<FormalParam f>) -\> { return <Expr bcps>; }, (<FormalParam f>) -\> { return <Expr sscps>; })`
  when
    Id method := [Id]capitalize("<kw>"),
    Type rt := boxed(typeOf(f)),
    Expr ecps := injectExpr(e, alg, names),
    Expr bcps := block2alg(b, alg, names),
    Expr sscps := block2alg((Block)`{<BlockStm+ ss>}`, alg, names);

// TODO: final modifiers.... (we don't support it now)
Expr varDec2alg(Type t, (VarDec)`<Id x>`, Expr k, Id alg, Names names) 
  = (Expr)`<Id alg>.Decl(null, (recaf.core.Ref\<<Type t2>\> <Id x>) -\> {return <Expr k>;})`
  when 
    Type t2 := boxed(t);
  
Expr varDec2alg(Type t, (VarDec)`<Id x> = <VarInit e>`, Expr k, Id alg, Names names) 
  = (Expr)`<Id alg>.Decl(<Expr ecps>, (recaf.core.Ref\<<Type t2>\> <Id x>) -\> {return <Expr k>;})`
  when
    Type t2 := boxed(t),
    Expr ecps := varInit2alg(t2, e, alg, names);

// assumes t is already boxed.
Expr varInit2alg(Type t, (VarInit)`<Expr e>`, Id alg, Names names)
  = injectExpr(e, alg, names);
  
Expr varInit2alg(Type t, (VarInit)`{<{VarInit ","}* inits>,}`, Id alg, Names names)
  = TODO; 

Expr varInit2alg(Type t, (VarInit)`{<{VarInit ","}* inits>}`, Id alg, Names names)
  = TODO; 


// default, because for loops need to capture labels
default Expr stm2alg((Stm)`<Id l>: <Stm s>`, Id alg, Names names)
   = (Expr)`<Id alg>.Labeled(<Expr label>, <Expr scps>)`
   when 
     Expr label := [Expr]"\"<l>\"",
     Expr scps := stm2alg(s, alg, names);
     
Expr stm2alg((Stm)`if (<Expr c>) <Stm s>`, Id alg, Names names) 
  = (Expr)`<Id alg>.If(<Expr ecps>, <Expr scps>)`
  when 
    Expr ecps := injectExpr(c, alg, names),
    Expr scps := stm2alg(s, alg, names);

Expr stm2alg((Stm)`if (<Expr c>) <Stm s1> else <Stm s2>`, Id alg, Names names) 
  = (Expr)`<Id alg>.If(<Expr ecps>, <Expr s1cps>, <Expr s2cps>)`
  when 
    Expr ecps := injectExpr(c, alg, names),
    Expr s1cps := stm2alg(s1, alg, names),
    Expr s2cps := stm2alg(s2, alg, names);
  
Expr stm2alg((Stm)`while (<Expr c>) <Stm s>`, Id alg, Names names) 
  = (Expr)`<Id alg>.While(<Expr ecps>, <Expr scps>)`
  when 
    Expr ecps := injectExpr(c, alg, names),
    Expr scps := stm2alg(s, alg, names);

Expr stm2alg((Stm)`do <Stm s> while (<Expr c>);`, Id alg, Names names) 
  = (Expr)`<Id alg>.Do(<Expr scps>, <Expr ecps>)`
  when 
    Expr scps := stm2alg(s, alg, names),
    Expr ecps := injectExpr(c, alg, names);

Expr stm2alg((Stm)`try <Block body> <CatchClause* catches> finally <Block fin>`, Id alg, Names names)
  = (Expr)`<Id alg>.TryCatchFinally(<Expr bodycps>, <Expr catchescps>, <Expr fincps>)`
  when 
    Expr bodycps := block2alg(body, alg, names),
    Expr catchescps := catches2alg(catches, alg),
    Expr fincps := block2alg(body, alg, names); 

Expr stm2alg((Stm)`try <Block body> <CatchClause+ catches>`, Id alg, Names names)
  = (Expr)`<Id alg>.TryCatch(<Expr bodycps>, <Expr catchescps>)`
  when 
    Expr bodycps := block2alg(body, alg, names),
    Expr catchescps := catches2alg(catches, alg, names);


Expr catch2alg((CatchClause)`catch (<Type t> <Id x>) <Block b>`, Id alg, Names names)
  =  (Expr)`<Id alg>.Catch(<Type t>.class, (<Type t> <Id x>) -\> {return <Expr bcps>;})`
  when
    Expr bcps := block2alg(b, alg, declare(x, names));
    
Expr stm2alg((Stm)`switch (<Expr e>) { <SwitchGroup* groups> <SwitchLabel* labels>}`, Id alg, Names names)
  = (Expr)`<Id alg>.Switch(<Expr ecps>, <{Expr ","}* es>)`
  when
    ecps := injectExpr(e, alg, names),
    exprs := ( [] | it + group2alg(g, alg, names) | g <- groups ) + labels2alg(labels, alg),
    es := lst2sepExps(exprs);
    
{Expr ","}* lst2sepExps(list[Expr] es) {
  call = (Expr)`f()`; // ugly hack
  for (e <- reverse(es), (Expr)`f(<{Expr ","}* args>)` := call) {
    call = (Expr)`f(<Expr e>, <{Expr ","}* args>)`; 
  }
  if ((Expr)`f(<{Expr ","}* args>)` := call) {
    return args;
  }
  // cannot come here.
}

list[Expr] labels2alg(SwitchLabel* labels, Id alg)
  = ( [] | it + group2alg((SwitchGroup)`<SwitchLabel l> ;`, alg) | l <- labels );

list[Expr] group2alg((SwitchGroup)`<SwitchLabel label> <BlockStm+ stms>`, Id alg, Names names)
  = [case2alg(label, stms, alg, names)];

list[Expr] group2alg((SwitchGroup)`<SwitchLabel label> <SwitchLabel+ labels> <BlockStm+ stms>`, Id alg)
  = group2alg((SwitchGroup)`<SwitchLabel label> ;`, alg)
  + group2alg((SwitchGroup)`<SwitchLabel+ labels> <BlockStm+ stms>`, alg);
  
Expr case2alg((SwitchLabel)`case <Expr e>:`, BlockStm+ stms, Id alg, Names names)
  = (Expr)`<Id alg>.Case(<Expr e>, <Expr stmscps>)`
  when
    Expr stmscps := block2alg((Block)`{<BlockStm+ stms>}`, alg,names);

Expr case2alg((SwitchLabel)`default:`, BlockStm+ stms, Id alg, Names names)
  = (Expr)`<Id alg>.Default(<Expr stmscps>)`
  when
    Expr stmscps := block2alg((Block)`{<BlockStm+ stms>}`, alg, names);


