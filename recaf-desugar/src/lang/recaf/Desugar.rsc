module lang::recaf::Desugar

import lang::recaf::Recaf;
import List;
import Set;
import String;
import IO;  
import util::Maybe;


start[CompilationUnit] desugar(start[CompilationUnit] cu) {
   
   list[Id] algFields = [];
   visit (cu) {
      case (FieldDec)`<BeforeField* bf1> recaf <BeforeField* bf2> <Type t> <Id x> = <Expr e>;`:
        algFields += [x];
      case (FieldDec)`<BeforeField* bf1> recaf <BeforeField* bf2> <Type t> <Id x>;`:
        algFields += [x];
   }
      


   return top-down visit (cu) {
      case (FieldDec)`<BeforeField* bf1> recaf <BeforeField* bf2> <Type t> <Id x> = <Expr e>;`
        => (FieldDec)`<BeforeField* bf1> <BeforeField* bf2> <Type t> <Id x> = <Expr e>;`

      case (FieldDec)`<BeforeField* bf0> recaf <BeforeField* bf3> <Type t> <Id x>;`
        => (FieldDec)`<BeforeField* bf0> <BeforeField* bf3> <Type t> <Id x>;`
      
      case (MethodDec)`<BeforeMethod* bm1> <TypeParams? tp1> <ResultType rt> <Id meth>(recaf <ClassOrInterfaceType t> <Id alg>, <{FormalParam ","}* fs>) <Block b>` 
       => (MethodDec)`<BeforeMethod* bm1> <TypeParams? tp1> <ResultType rt> <Id meth>(<ClassOrInterfaceType t> <Id alg>, <{FormalParam ","}* fs>) {
                     '  <BlockStm* bs>
                     '  <Stm ret>
                     '}`
        when <bs, names> := fps2decls(fs, declaredNames(b) + { x | /Id x := fs }),
             cps := method2alg(b, alg, names),
             ret := makeReturn(rt, cps)
      
      case (MethodDec)`<BeforeMethod* bm0> recaf <BeforeMethod* bm2> <TypeParams? tp> <ResultType rt> <Id meth>(<{FormalParam ","}* fs>) <Block b>` 
       => (MethodDec)`<BeforeMethod* bm0> <BeforeMethod* bm2> <TypeParams? tp> <ResultType rt> <Id meth>(<{FormalParam ","}* fs>) {
                     '  <BlockStm* bs>
                     '  <Stm ret>
                     '}`
        when
          Id alg <- algFields, 
          <bs, names> := fps2decls(fs, declaredNames(b) + { x | /Id x := fs }),
          cps := method2alg(b, alg, names),
          ret := makeReturn(rt, cps)       
   }
}

Stm makeReturn((ResultType)`<RefType rt>`, Expr cps) 
  = (Stm)`return (<RefType rt>)<Expr cps>;`;

Stm makeReturn((ResultType)`<PrimType pt>`, Expr cps) 
  = (Stm)`return (<RefType rt>)<Expr cps>;`
  when 
    (Type)`<RefType rt>` := boxed((Type)`<PrimType pt>`);

Stm makeReturn((ResultType)`void`, Expr cps) 
  = (Stm)`<Expr cps>;`;

set[Id] declaredNames(Block b) {
   ns = {};
   visit (b) {
     case (FormalParam)`<Type _> <Id x>`: ns += {x};
     case (VarDec)`<Id x>`: ns += {x};
     case (VarDec)`<Id x> = <Expr _>`: ns += {x};
   }
   return ns;
}

Id gensym(Id x, set[Id] names) {
  do {
    x = [Id]"$<x>";
  }
  while (x in names);
  return x;
}

alias Names = tuple[set[Id] refs, map[Id, Id] renaming];

tuple[LocalVarDec, Names] fp2decl((FormalParam)`final <Type t> <Id x>`, Names names) {
  Id y = gensym(x, names.renaming<1>);
  ld = (LocalVarDec)`final <Type t> <Id y> = <Id x>`;
  return <ld, <names.refs, names.renaming + (x: y)>>;
}

tuple[LocalVarDec,Names] fp2decl((FormalParam)`<Type t> <Id x>`, Names names) {
  Id y = gensym(x, names.renaming<1>);
  t2 = boxed(t);
  ld = (LocalVarDec)`recaf.core.Ref\<<Type t2>\> <Id y> = new recaf.core.Ref\<<Type t2>\>(<Id x>)`;
  return <ld, <names.refs + {y}, names.renaming + (x: y)>>;
}

tuple[BlockStm*, Names] fps2decls({FormalParam ","}* fs, set[Id] ns) {
   b = (Stm)`{}`;
   names = <{}, ( x: x | x <- ns )>; // init with identity renaming.
   for (f <- fs, (Stm)`{<BlockStm* bs>}` := b) {
     <ld, names> = fp2decl(f, names);
     b = (Stm)`{<BlockStm* bs> <LocalVarDec ld>;}`;
   }
   if ((Stm)`{<BlockStm* bs>}` := b) {
      return <bs, names>;
   }
   throw "Cannot happen";
}

Expr method2alg(Block b, Id alg, Names names) 
  = (Expr)`<Id alg>.Method(<Expr bcps>)`
  when
    bcps := block2alg(b, alg, names);
    
Names declare(Id x, Names names) 
  = <names.refs + {x}, names.renaming>;

Names declare((VarDec)`<Id x>`, Names names) 
  = <names.refs + {x}, names.renaming>;

Names declare((VarDec)`<Id x> = <Expr _>`, Names names) 
  = <names.refs + {x}, names.renaming>;

Names declare((FormalParam)`<Type t> <Id x>`, Names names) 
  = <names.refs + {x}, names.renaming>;

default Names declare(FormalParam f, Names names) 
  = names;


/// Extensions

// return-like
Expr stm2alg((Stm)`<KId ext>! <Expr e>;`, Id alg, Names names) 
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>)`
  when
    Expr ecps := expr2alg(e, alg, names),
    Id method := [Id]capitalize("<ext>");

// for like
Expr stm2alg((Stm)`<KId ext> (<FormalParam f>: <Expr e>) <Stm s>`, Id alg, Names names) 
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, (<FormalParam f>) -\> {return <Expr scps>;})`
  when 
    Expr ecps := expr2alg(e, alg, names),
    Expr scps := stm2alg(s, alg, declare(f, names)),
    Id method := [Id]capitalize("<ext>");

// while like
Expr stm2alg((Stm)`<KId ext> (<Expr c>) <Block b>`, Id alg, Names names) 
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, <Expr bcps>)`
  when 
    Expr ecps := expr2alg(c, alg, names),
    Expr bcps := block2alg(b, alg, names),
    Id method := [Id]capitalize("<ext>");

// try-like
Expr stm2alg((Stm)`<KId ext> <Block b>`, Id alg, Names names) 
  = (Expr)`<Id alg>.<Id method>(<Expr bcps>)`
  when 
    Expr bcps := block2alg(b, alg, names),
    Id method := [Id]capitalize("<ext>");

// switch-like
Expr stm2alg((Stm)`<KId ext> (<FormalParam f>: <Expr e>) {<Item+ items>}`, Id alg, Names names) 
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, (<FormalParam f>) -\> {return <Expr itemscps>;})`
  when 
    Expr ecps := expr2alg(e, alg, names),
    Expr itemscps := items2alg([ i | i <- items ], alg, declare(f, names)),
    Id method := [Id]capitalize("<ext>");

Expr stm2alg((Stm)`<KId ext> (<Expr e>) {<Item+ items>}`, Id alg, Names names) 
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, <Expr itemscps>)`
  when 
    Expr ecps := expr2alg(e, alg, names),
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
    Expr ecps := expr2alg(e, alg, names),
    Expr stmscps := block2alg((Block)`{<BlockStm+ stms>}`, alg, names);
  
Expr item2alg((Item)`<KId kw> <FormalParam f>: <BlockStm+ stms>`, Id alg, Names names)
  = (Expr)`<Id alg>.<Id method>((<FormalParam f>) -\> { return <Expr stmscps>; })`
  when 
    Id method := [Id]capitalize("<kw>"),
    Expr stmscps := block2alg((Block)`{<BlockStm+ stms>}`, alg, declare(f, names));


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
// TODO: what to do with labels?
Expr stm2alg((Stm)`for (<FormalParam f>: <Expr e>) <Stm s>`, Id alg, Names names) 
  = (Expr)`<Id alg>.\<<Type t>\>For(null, <Expr ecps>, (<FormalParam f2>) -\> {return <Expr scps>;})`
  when 
    Expr ecps := expr2alg(e, alg, names),
    Expr scps := stm2alg(s, alg, declare(f, names)),
    FormalParam f2 := fp2ref(f),
    Type t := typeOf(f);

// labeled for each
Expr stm2alg((Stm)`<Id label>: for (<FormalParam f>: <Expr e>) <Stm s>`, Id alg, Names names) 
  = (Expr)`<Id alg>.\<<Type t>\>For(<Expr labelExp>, <Expr ecps>, (<FormalParam f2>) -\> {return <Expr scps>;})`
  when 
    Expr ecps := expr2alg(e, alg, names),
    Expr scps := stm2alg(s, alg, declare(f, names)),
    FormalParam f2 := fp2ref(f),
    Type t := typeOf(f),
    Expr labelExp := [Expr]"\"<label>\"";


// For loops with init expressions (unlabeled and labeled)
Expr stm2alg((Stm)`for (<{Expr ","}* es1>; <Expr cond>; <{Expr ","}* update>) <Stm body>`, Id alg, Names names)
  = stm2alg((Stm)`{<{Expr ","}* es1>; for (; <Expr cond>; <{Expr ","}* update>) <Stm body>}`, alg, names)
  when _ <- es1;  

Expr stm2alg((Stm)`<Id label>: for (<{Expr ","}* es1>; <Expr cond>; <{Expr ","}* update>) <Stm body>`, Id alg, Names names)
  = stm2alg((Stm)`{<{Expr ","}* es1>; <Id label>: for (; <Expr cond>; <{Expr ","}* update>) <Stm body>}`, alg, names)  
  when _ <- es1;  

// For loops with decls (unlabeled and labeled)
Expr stm2alg((Stm)`for (<LocalVarDec vd>; <Expr cond>; <{Expr ","}* update>) <Stm body>`, Id alg, Names names)
  = stm2alg((Stm)`{<LocalVarDec vd>; for(; <Expr cond>; <{Expr ","}* update>) <Stm body>}`, alg, names);  

Expr stm2alg((Stm)`<Id label>: for (<LocalVarDec vd>; <Expr cond>; <{Expr ","}* update>) <Stm body>`, Id alg, Names names)
  = stm2alg((Stm)`{<LocalVarDec vd>; <Id label>: for(; <Expr cond>; <{Expr ","}* update>) <Stm body>}`, alg, names);  

// base case for for loops (init has been hoisted already)
Expr stm2alg((Stm)`for (; <Expr cond>; <{Expr ","}* update>) <Stm body>`, Id alg, Names names)
  = (Expr)`<Id alg>.For(null, <Expr condcps>, <Expr updatecps>, <Expr bodycps>)`
  when
    Expr condcps := expr2alg(cond, alg, names),
    Expr updatecps := exps2alg(update, alg, names),
    Expr bodycps := stm2alg(body, alg, names);
    

Expr exps2alg({Expr ","}* exps, Id alg, Names names) {
  cur = (Expr)`<Id alg>.Empty()`;
  for (e <- reverse([ e | e <- exps ])) {
    s = stm2alg((Stm)`<Expr e>;`, alg, names);
    cur = (Expr)`<Id alg>.Seq(<Expr s>, <Expr cur>)`; 
  }
  return cur; 
}
  //= ( (Expr)`<Id alg>.Empty()` | stm2alg((Stm)`<Expr e>;`, alg, names) | e <- reverse([e | 

Expr stm2alg((Stm)`<Id label>: for (; <Expr cond>; <{Expr ","}* update>) <Stm body>`, Id alg, Names names)
  = (Expr)`<Id alg>.For(<Expr labelExp>, <Expr condcps>, <Expr updatecps>, <Expr bodycps>)`
  when
    Expr condcps := expr2alg(cond, alg, names),
    Expr updatecps := stm2alg((Stm)`{<{Expr ","}* update>;}`, alg, names),
    Expr bodycps := stm2alg(body, alg, names),
    Expr labelExp := [Expr]"\"<label>\"";
    
Expr stm2alg((Stm)`throw <Expr e>;`, Id alg, Names names) 
  = (Expr)`<Id alg>.Throw(<Expr ecps>)`
  when
    Expr ecps := expr2alg(e, alg, names);

Expr stm2alg((Stm)`return <Expr e>;`, Id alg, Names names) 
  = (Expr)`<Id alg>.Return(<Expr ecps>)`
  when
    Expr ecps := expr2alg(e, alg, names);

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
  = (Expr)`<Id alg>.\<<Type rt>\><Id method>((<FormalParam f>) -\> <Id alg>.Empty())`
  when
    Id method := [Id]capitalize("<kw>"),
    Type rt := boxed(typeOf(f));
    

Expr block2alg((Block)`{<KId kw> <FormalParam f> = <Expr e>;}`, Id alg, Names names) 
  = (Expr)`<Id alg>.\<<Type rt>\><Id method>(<Expr ecps>, (<FormalParam f>) -\> <Id alg>.Empty())`
  when
    Id method := [Id]capitalize("<kw>"),
    Type rt := boxed(typeOf(f)),
    Expr ecps := expr2alg(e, alg, names);

Expr block2alg((Block)`{<FormalParam f> = <KId kw>! <Expr e>;}`, Id alg, Names names) 
  = (Expr)`<Id alg>.\<<Type rt>\><Id method>(<Expr ecps>, (<FormalParam f>) -\> <Id alg>.Empty())`
  when
    Id method := [Id]capitalize("<kw>"),
    Type rt := boxed(typeOf(f)),
    Expr ecps := expr2alg(e, alg, names);

default Expr block2alg((Block)`{<Stm s>}`, Id alg, Names names) 
  = stm2alg(s, alg, names);

default Expr block2alg((Block)`{<Stm s> <BlockStm+ ss>}`, Id alg, Names names) 
  = (Expr)`<Id alg>.Seq(<Expr scps>, <Expr sscps>)`
  when
    Expr scps := stm2alg(s, alg, names),
    Expr sscps := block2alg((Block)`{<BlockStm* ss>}`, alg, names);

// binding extensions (let/maybe etc.) introduce final variables
// and get the reflective class of the declaration. 
Expr block2alg((Block)`{<KId kw> <FormalParam f>; <BlockStm+ ss>}`, Id alg, Names names)
  = (Expr)`<Id alg>.\<<Type rt>\><Id method>((<FormalParam f>) -\> { return <Expr sscps>; })`
  when
    Id method := [Id]capitalize("<kw>"),
    Type rt := boxed(typeOf(f)),
    Expr sscps := block2alg((Block)`{<BlockStm+ ss>}`, alg, names);

Type typeOf((FormalParam)`final <Type t> <Id _>`) = t;
Type typeOf((FormalParam)`<Type t> <Id _>`) = t;

Expr block2alg((Block)`{<KId kw> <FormalParam f> = <Expr e>; <BlockStm+ ss>}`, Id alg, Names names)
  = (Expr)`<Id alg>.\<<Type rt>\><Id method>(<Expr ecps>, (<FormalParam f>) -\> { return <Expr sscps>; })`
  when
    Id method := [Id]capitalize("<kw>"),
    Type rt := boxed(typeOf(f)),
    Expr ecps := expr2alg(e, alg, names),
    Expr sscps := block2alg((Block)`{<BlockStm+ ss>}`, alg, names);

Expr block2alg((Block)`{<FormalParam f> = <KId kw>! <Expr e>; <BlockStm+ ss>}`, Id alg, Names names)
  = (Expr)`<Id alg>.\<<Type rt>\><Id method>(<Expr ecps>, (<FormalParam f>) -\> { return <Expr sscps>; })`
  when
    Id method := [Id]capitalize("<kw>"),
    Type rt := boxed(typeOf(f)),
    Expr ecps := expr2alg(e, alg, names),
    Expr sscps := block2alg((Block)`{<BlockStm+ ss>}`, alg, names);

// TODO: final modifiers.... (we don't support it now)
Expr varDec2alg(Type t, (VarDec)`<Id x>`, Expr k, Id alg, Names names) 
  = (Expr)`<Id alg>.\<<Type t2>\>Decl(null, <Id x> -\> {return <Expr k>;})`
  when 
    Type t2 := boxed(t);
  
Expr varDec2alg(Type t, (VarDec)`<Id x> = <VarInit e>`, Expr k, Id alg, Names names) 
  = (Expr)`<Id alg>.\<<Type t2>\>Decl(<Expr ecps>, <Id x> -\> {return <Expr k>;})`
  when
    Type t2 := boxed(t),
    Expr ecps := varInit2alg(t2, e, alg, names);

// assumes t is already boxed.
Expr varInit2alg(Type t, (VarInit)`<Expr e>`, Id alg, Names names)
  = expr2alg(e, alg, names);
  
//Expr init2alg(Expr e, Id alg, Names names)
//  = (Expr)`<Id alg>.Exp(() -\> new recaf.core.Ref(<Expr e2>))`
//  when 
//    Expr e2 := unwrapRefs(e, names);
//  //expr2alg(e, alg, names);

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
    Expr ecps := expr2alg(c, alg, names),
    Expr scps := stm2alg(s, alg, names);

Expr stm2alg((Stm)`if (<Expr c>) <Stm s1> else <Stm s2>`, Id alg, Names names) 
  = (Expr)`<Id alg>.If(<Expr ecps>, <Expr s1cps>, <Expr s2cps>)`
  when 
    Expr ecps := expr2alg(c, alg, names),
    Expr s1cps := stm2alg(s1, alg, names),
    Expr s2cps := stm2alg(s2, alg, names);
  
Expr stm2alg((Stm)`while (<Expr c>) <Stm s>`, Id alg, Names names) 
  = (Expr)`<Id alg>.While(<Expr ecps>, <Expr scps>)`
  when 
    Expr ecps := expr2alg(c, alg, names),
    Expr scps := stm2alg(s, alg, names);

Expr stm2alg((Stm)`do <Stm s> while (<Expr c>);`, Id alg, Names names) 
  = (Expr)`<Id alg>.Do(<Expr scps>, <Expr ecps>)`
  when 
    Expr scps := stm2alg(s, alg, names),
    Expr ecps := expr2alg(c, alg, names);


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

// NB: cannot use expr2alg because of void expressions.
Expr stm2alg((Stm)`<Expr e>;`, Id alg, Names names) 
  = (Expr)`<Id alg>.ExpStat(() -\> { <Expr e2>; })`
  when 
    Expr e2 := unwrapRefs(e, names);

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
    
Expr expr2alg(Expr e, Id alg, Names names)
  = (Expr)`<Id alg>.Exp(() -\> <Expr e2>)`
  when
    Expr e2 := unwrapRefs(e, names);

Expr stm2alg((Stm)`switch (<Expr e>) { <SwitchGroup* groups> <SwitchLabel* labels>}`, Id alg, Names names)
  = (Expr)`<Id alg>.Switch(<Expr ecps>, <{Expr ","}* es>)`
  when
    ecps := expr2alg(e, alg, names),
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


Type boxed((Type)`int`) = (Type) `Integer`;
Type boxed((Type)`long`) = (Type)`Long`;
Type boxed((Type)`boolean`) = (Type)`Boolean`;
Type boxed((Type)`double`) = (Type)`Double`;
Type boxed((Type)`float`) = (Type)`Float`;
Type boxed((Type)`char`) = (Type)`Character`;

default Type boxed(Type t) = t;

//
//Expr exprTop(Expr e) 
//  = (Expr)`(sigma, err) -\> <Expr ecps>`
//  when
//    Expr ecps := expr2alg(e, (Expr)`sigma`, (Expr)`err`);
//
//Expr expr2alg((Expr)`<Id x>! (<{Expr ","}* args>)`) = 0;
//
//Expr expr2alg((Expr)`<Literal lit>`, Expr k) 
//  = 0;
//
//Expr expr2alg((Expr)`<Expr l> + <Expr r>`, Expr sigma, Expr err) = 
//  (Expr)`(v1, v2) -\> k -\> <Expr sigma>.accept(v1 + v2)`
//  when
//    Expr lcps := expr2alg(l, expr2alg(r, (Expr)`(k, e) -\> k.accept(v1 + v2)`));


//Expr expr2alg((Expr)`<Expr l> + <Expr r>`, Expr k)
//  = expr2alg(l, (Expr)`v1 -\> <Expr e2>`)
//  when Expr e2 := expr2alg(r, (Expr)`v2 -\> <Expr k>.accept(v1 + v2)`); 
//  
//Expr expr2alg((Expr)`<Id x>`, Expr k)
//  = (Expr)`(<Expr k>).accept(<Id x>)`;



  
