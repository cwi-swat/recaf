module lang::recaf::Desugar

import lang::recaf::Recaf;
import List;
import Set;
import String;
import IO;  

/*
 * TODO
 * - fix Ref wrapping, it doesn't respect block scoping of declarations
 * - the unwrapping of Ref vars should also happen within nested closures
 *   if they're not shadowed by the formal parameters. 
 * - introduces new (Ref) local variables for non-final formal params
 * - pass down a renaming to rename local vars if needed because a formal 
 *   param has been renamed
 * - do something about calls to void methods.  
 */
  
start[CompilationUnit] desugar(start[CompilationUnit] cu) {
   return visit (cu) {
      case (MethodDec)`<BeforeMethod* bm1> <RefType rt> <Id meth>(recaf <ClassOrInterfaceType t> <Id alg>, <{FormalParam ","}* fs>) <Block b>` 
       => (MethodDec)`<BeforeMethod* bm1> <RefType rt> <Id meth>(<ClassOrInterfaceType t> <Id alg>, <{FormalParam ","}* fs>) {
                     '  return (<RefType rt>)<Expr cps>;
                     '}`
        when cps := method2alg(b, alg)
       
      case (MethodDec)`[<ClassOrInterfaceType t>] 
                     '<BeforeMethod* bm> <RefType rt> <Id meth>(<{FormalParam ","}* fs>) <Block b>` 
       => (MethodDec)`<BeforeMethod* bm> <RefType rt> <Id meth>(<{FormalParam ","}* fs>) {
                     '  <ClassOrInterfaceType t> <Id alg> = new <ClassOrInterfaceType t>();
                     '  return (<RefType rt>)<Expr cps>;
                     '}`
        when
          alg := (Id)`$alg`, 
          cps := method2alg(b, alg)
   }
}

Expr method2alg(Block b, Id alg) 
  = (Expr)`<Id alg>.Method(<Expr bcps2>)`
  when
    localIds := collectVars(b),
    bcps := block2alg(b, alg, localIds),
    newBlock := addRefs(b, localIds),
    bcps2 := block2alg(newBlock, alg, localIds);
    
set[Id] collectVars(Block b){
	set[Id] localVars = {};
	visit(b){
		case (VarDec) `<Id x>`:{ localVars += {x}; }
		case (VarDec) `<Id x> = <VarInit e>`:{ localVars += {x} ; }
		default:{};
	};
	return localVars;
}

Block addRefs(Block b, set[Id] localIds) = visit(b){
    	case (LHS) `<Id x>` => x in localIds ? (LHS) `<Id x>.value` : (LHS) `<Id x>`
    	case (Expr) `<Id x>` => x in localIds ? (Expr) `<Id x>.value` : (Expr) `<Id x>`	
    	case (LocalVarDec) `<Type t> <Id x>` => (LocalVarDec) `Ref\<<Type newt>\> <Id x>`
    		when newt := boxed(t)
    	case (LocalVarDec) `<Type t> <Id x> = <Expr e>` => (LocalVarDec) `Ref\<<Type newt>\> <Id x> = new Ref\<<Type newt>\>(<Expr e>)`
    		when newt := boxed(t)
};

//LocalVarDec wrap(Type t, Id x, Expr e) =  (LocalVarDec) `<Type t> <Id x> = new Ref(<Expr e>)`;

/// Extensions

// return-like
Expr stm2alg((Stm)`<KId ext>! <Expr e>;`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>)`
  when
    Expr ecps := expr2alg(e, alg, localIds),
    Id method := [Id]capitalize("<ext>");

// for like
Expr stm2alg((Stm)`<KId ext> (<FormalParam f>: <Expr e>) <Stm s>`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, (<FormalParam f>) -\> {return <Expr scps>;})`
  when 
    Expr ecps := expr2alg(e, alg, localIds),
    Expr scps := stm2alg(s, alg, localIds),
    Id method := [Id]capitalize("<ext>");

// while like
Expr stm2alg((Stm)`<KId ext> (<Expr c>) <Block b>`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, <Expr bcps>)`
  when 
    Expr ecps := expr2alg(c, alg, localIds),
    Expr bcps := block2alg(b, alg, localIds),
    Id method := [Id]capitalize("<ext>");

// try-like
Expr stm2alg((Stm)`<KId ext> <Block b>`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.<Id method>(<Expr bcps>)`
  when 
    Expr bcps := block2alg(b, alg, localIds),
    Id method := [Id]capitalize("<ext>");

// switch-like
Expr stm2alg((Stm)`<KId ext> (<FormalParam f>: <Expr e>) {<Item+ items>}`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, (<FormalParam f>) -\> {return <Expr itemscps>;})`
  when 
    Expr ecps := expr2alg(e, alg, localIds),
    Expr itemscps := items2alg([ i | i <- items ], alg, localIds),
    Id method := [Id]capitalize("<ext>");

Expr stm2alg((Stm)`<KId ext> (<Expr e>) {<Item+ items>}`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, <Expr itemscps>)`
  when 
    Expr ecps := expr2alg(e, alg, localIds),
    Expr itemscps := items2alg([ i | i <- items ], alg, localIds),
    Id method := [Id]capitalize("<ext>");

Expr stm2alg((Stm)`<KId ext> {<Item+ items>}`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.<Id method>(<Expr itemscps>)`
  when 
    Expr itemscps := items2alg([ i | i <- items ], alg, localIds),
    Id method := [Id]capitalize("<ext>");

Expr items2alg(list[Item] items:[], Id alg, set[Id] localIds)
  = (Expr)`java.util.Arrays.asList()`;

Expr items2alg(list[Item] items:[Item x, *xs], Id alg, set[Id] localIds)
  = (Expr)`java.util.Arrays.asList(<Expr xcps>, <{Expr ","}* es>)`
  when
    Expr xcps := item2alg(x, alg, localIds),
    (Expr)`java.util.Arrays.asList(<{Expr ","}* es>)` := items2alg(xs, alg, localIds);

Expr item2alg((Item)`<KId kw> <Expr e>: <BlockStm+ stms>`, Id alg, set[Id] localIds)
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, <Expr stmscps>)`
  when 
    Id method := [Id]capitalize("<kw>"),
    Expr ecps := expr2alg(e, alg, localIds),
    Expr stmscps := block2alg((Block)`{<BlockStm+ stms>}`, alg, localIds);
  
Expr item2alg((Item)`<KId kw> <FormalParam f>: <BlockStm+ stms>`, Id alg, set[Id] localIds)
  = (Expr)`<Id alg>.<Id method>((<FormalParam f>) -\> { return <Expr stmscps>; })`
  when 
    Id method := [Id]capitalize("<kw>"),
    Expr stmscps := block2alg((Block)`{<BlockStm+ stms>}`, alg, localIds);


// new style
Expr stm2alg((Stm)`<KId kw> (<{FormalParam ","}+ fs>) <Stm stm>`, Id alg, set[Id] localIds)
  = (Expr)`<Id alg>.<Id method>((<{FormalParam  ","}+ fs>) -\> { return <Expr stmcps>; })`
  when
    Id method := [Id]capitalize("<kw>"),
    Expr stmcps := stm2alg(stm, alg, localIds);
  

//////


Expr stm2alg((Stm)`continue <Id x>;`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.Continue(<Expr label>)`
  when 
    Expr label := [Expr]"\"<x>\"";
  
Expr stm2alg((Stm)`continue;`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.Continue()`;

Expr stm2alg((Stm)`break <Id x>;`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.Break(<Expr label>)`
  when 
    Expr label := [Expr]"\"<x>\"";
  
Expr stm2alg((Stm)`break;`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.Break()`;

Expr stm2alg((Stm)`for (<FormalParam f>: <Expr e>) <Stm s>`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.For(<Expr ecps>, (<FormalParam f>) -\> {return <Expr scps>;})`
  when 
    Expr ecps := expr2alg(e, alg, localIds),
    Expr scps := stm2alg(s, alg, localIds);

Expr stm2alg((Stm)`for (<{Expr ","}* es1>; <Expr cond>; <{Expr ","}* update>) <Stm body>`, Id alg, set[Id] localIds)
  = TODO;  

Expr stm2alg((Stm)`for (<LocalVarDec vd>; <Expr cond>; <{Expr ","}* update>) <Stm body>`, Id alg, set[Id] localIds)
  = stm2alg((Stm)`{<LocalVarDec vd>; for(; <Expr cond>; <{Expr ","}* update>) <Stm body>}`, alg, localIds);  
    
Expr stm2alg((Stm)`throw <Expr e>;`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.Throw(<Expr ecps>)`
  when
    Expr ecps := expr2alg(e, alg, localIds);

Expr stm2alg((Stm)`return <Expr e>;`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.Return(<Expr ecps>)`
  when
    Expr ecps := expr2alg(e, alg, localIds);

Expr stm2alg((Stm)`return;`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.Return()`;

Expr stm2alg((Stm)`;`, Id alg, set[Id] localIds) = (Expr)`<Id alg>.Empty()`;

Expr stm2alg((Stm)`<Block b>`, Id alg, set[Id] localIds) = block2alg(b, alg, localIds);

// TODO: ClassDec as blockstm
Expr block2alg((Block)`{}`, Id alg, set[Id] localIds) = (Expr)`<Id alg>.Empty()`;

// singletons
Expr block2alg((Block)`{<Type t> <{VarDec ","}+ vs>;}`, Id alg, set[Id] localIds) 
  = varDecs2alg(t, vs, (Expr)`<Id alg>.Empty()`, alg, localIds);

Expr block2alg((Block)`{<KId kw> <FormalParam f>;}`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.<Id method>((<FormalParam f>) -\> <Id alg>.Empty())`
  when
    Id method := [Id]capitalize("<kw>");

Expr block2alg((Block)`{<KId kw> <FormalParam f> = <Expr e>;}`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, (<FormalParam f>) -\> <Id alg>.Empty())`
  when
    Id method := [Id]capitalize("<kw>"),
    Expr ecps := expr2alg(e, alg, localIds);

default Expr block2alg((Block)`{<Stm s>}`, Id alg, set[Id] localIds) = stm2alg(s, alg, localIds);

// end singletons

default Expr block2alg((Block)`{<Stm s> <BlockStm+ ss>}`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.Seq(<Expr scps>, <Expr sscps>)`
  when
    Expr scps := stm2alg(s, alg, localIds),
    Expr sscps := block2alg((Block)`{<BlockStm* ss>}`, alg, localIds);

// todo: annos/modifiers
Expr block2alg((Block)`{<Type t> <{VarDec ","}+ vs>; <BlockStm+ ss>}`, Id alg, set[Id] localIds) 
  = varDecs2alg(t, vs, sscps, alg, localIds)
  when
    Expr sscps := block2alg((Block)`{<BlockStm+ ss>}`, alg, localIds);

// decl like
//   | KId FormalParam ";"
//   | KId FormalParam "=" Expr ";"
   
Expr block2alg((Block)`{<KId kw> <FormalParam f>; <BlockStm+ ss>}`, Id alg, set[Id] localIds)
  = (Expr)`<Id alg>.<Id method>((<FormalParam f>) -\> { return <Expr sscps>; })`
  when
    Id method := [Id]capitalize("<kw>"),
    Expr sscps := block2alg((Block)`{<BlockStm+ ss>}`, alg, localIds);

Expr block2alg((Block)`{<KId kw> <FormalParam f> = <Expr e>; <BlockStm+ ss>}`, Id alg, set[Id] localIds)
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, (<FormalParam f>) -\> { return <Expr sscps>; })`
  when
    Id method := [Id]capitalize("<kw>"),
    Expr ecps := expr2alg(e, alg, localIds),
    Expr sscps := block2alg((Block)`{<BlockStm+ ss>}`, alg, localIds);

Expr varDecs2alg(Type t, {VarDec ","}+ vs, Expr k, Id alg, set[Id] localIds) {
   for (VarDec vd <- reverse([ v | v <- vs])) {
     k = varDec2alg(t, vd, k, alg, localIds);
   }
   return k;
}   

// TODO: remove null?
Expr varDec2alg(Type t, (VarDec)`<Id x>`, Expr k, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.\<<Type t>\>Decl(null, <Id x> -\> {return <Expr k>;})`;
  
Expr varDec2alg(Type t, (VarDec)`<Id x> = <VarInit e>`, Expr k, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.\<<Type t>\>Decl(<Expr ecps>, <Id x> -\> {return <Expr k>;})`
  when
    Expr ecps := varInit2alg(t, e, alg, localIds);

//VarInit wrap((VarInit) `<Expr e>`, Type t) = (VarInit) `new Ref\<<Type t>\>(<Expr e>)`;

Expr varInit2alg(Type t, (VarInit)`<Expr e>`, Id alg, set[Id] localIds)
  = expr2alg(e, alg, localIds);

Expr varInit2alg(Type t, (VarInit)`{<{VarInit ","}* inits>,}`, Id alg, set[Id] localIds)
  = TODO; 

Expr varInit2alg(Type t, (VarInit)`{<{VarInit ","}* inits>}`, Id alg, set[Id] localIds)
  = TODO; 

Expr stm2alg((Stm)`<Id l>: <Stm s>`, Id alg, set[Id] localIds)
   = (Expr)`<Id alg>.Labeled(<Expr label>, <Expr scps>)`
   when 
     Expr label := [Expr]"\"<l>\"",
     Expr scps := stm2alg(s, alg, localIds);
     
Expr stm2alg((Stm)`if (<Expr c>) <Stm s>`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.If(<Expr ecps>, <Expr scps>)`
  when 
    Expr ecps := expr2alg(c, alg, localIds),
    Expr scps := stm2alg(s, alg, localIds);

Expr stm2alg((Stm)`if (<Expr c>) <Stm s1> else <Stm s2>`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.If(<Expr ecps>, <Expr s1cps>, <Expr s2cps>)`
  when 
    Expr ecps := expr2alg(c, alg, localIds),
    Expr s1cps := stm2alg(s1, alg, localIds),
    Expr s2cps := stm2alg(s2, alg, localIds);
  
Expr stm2alg((Stm)`while (<Expr c>) <Stm s>`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.While(<Expr ecps>, <Expr scps>)`
  when 
    Expr ecps := expr2alg(c, alg, localIds),
    Expr scps := stm2alg(s, alg, localIds);

Expr stm2alg((Stm)`do <Stm s> while (<Expr c>);`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.Do(<Expr scps>, <Expr ecps>)`
  when 
    Expr scps := stm2alg(s, alg, localIds),
    Expr ecps := expr2alg(c, alg, localIds);

Expr stm2alg((Stm)`<Expr e>;`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.ExpStat(<Expr ecps>)`
  when
    Expr ecps := expr2alg(e, alg, localIds);

Expr stm2alg((Stm)`try <Block body> <CatchClause* catches> finally <Block fin>`, Id alg, set[Id] localIds)
  = (Expr)`<Id alg>.TryCatchFinally(<Expr bodycps>, <Expr catchescps>, <Expr fincps>)`
  when 
    Expr bodycps := block2alg(body, alg, localIds),
    Expr catchescps := catches2alg(catches, alg),
    Expr fincps := block2alg(body, alg, localIds); 

Expr stm2alg((Stm)`try <Block body> <CatchClause+ catches>`, Id alg, set[Id] localIds)
  = (Expr)`<Id alg>.TryCatch(<Expr bodycps>, <Expr catchescps>)`
  when 
    Expr bodycps := block2alg(body, alg, localIds),
    Expr catchescps := catches2alg(catches, alg);

// "catch"  "(" FormalParam ")" Block

Expr catch2alg((CatchClause)`catch (<Type t> <Id x>) <Block b>`, Id alg)
  =  (Expr)`<Id alg>.Catch(<Type t>.class, (<Type t> <Id x>) -\> {return <Expr bcps>;})`
  when
    Expr bcps := block2alg(b, alg, TODO);
    
Expr expr2alg(Expr e, Id alg, set[Id] localIds)
  = (Expr)`<Id alg>.Exp(() -\> { return <Expr e>; })` ;

Expr stm2alg((Stm)`switch (<Expr e>) { <SwitchGroup* groups> <SwitchLabel* labels>}`, Id alg, set[Id] localIds)
  = (Expr)`<Id alg>.Switch(<Expr ecps>, <{Expr ","}* es>)`
  when
    ecps := expr2alg(e, alg),
    exprs := ( [] | it + group2alg(g, alg, localIds) | g <- groups ) + labels2alg(labels, alg),
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

list[Expr] group2alg((SwitchGroup)`<SwitchLabel label> <BlockStm+ stms>`, Id alg, set[Id] localIds)
  = [case2alg(label, stms, alg, localIds)];

list[Expr] group2alg((SwitchGroup)`<SwitchLabel label> <SwitchLabel+ labels> <BlockStm+ stms>`, Id alg)
  = group2alg((SwitchGroup)`<SwitchLabel label> ;`, alg)
  + group2alg((SwitchGroup)`<SwitchLabel+ labels> <BlockStm+ stms>`, alg);
  
Expr case2alg((SwitchLabel)`case <Expr e>:`, BlockStm+ stms, Id alg, set[Id] localIds)
  = (Expr)`<Id alg>.Case(<Expr ecps>, <Expr stmscps>)`
  when
    Expr ecps := expr2alg(e, alg),
    Expr stmscps := block2alg((Block)`{<BlockStm+ stms>}`, alg,localIds);

Expr case2alg((SwitchLabel)`default:`, BlockStm+ stms, Id alg, set[Id] localIds)
  = (Expr)`<Id alg>.Default(<Expr stmscps>)`
  when
    Expr stmscps := block2alg((Block)`{<BlockStm+ stms>}`, alg, localIds);


Expr expr2alg(Expr e, Id alg)
  // todo: substitute local vars in e here + apply renaming.
  = (Expr)`<Id alg>.Exp(() -\> { return <Expr e>; })`;

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



  
