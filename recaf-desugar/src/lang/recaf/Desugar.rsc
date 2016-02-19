module lang::recaf::Desugar

extend lang::recaf::desugar::Stmt;
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
    

// provide the implementation for injectExpr    
Expr injectExpr(Expr e, Id alg, Names names)
  = (Expr)`<Id alg>.Exp(() -\> <Expr e2>)`
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
