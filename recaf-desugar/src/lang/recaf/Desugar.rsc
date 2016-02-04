module lang::recaf::Desugar

import lang::recaf::Recaf;
import List;
import Set;
import String;
import IO;  
  
start[CompilationUnit] desugar(start[CompilationUnit] cu) {
   return visit (cu) {
     case (MethodDec)`<RefType rt> <Id meth>(@Builder <ClassOrInterfaceType t> <Id alg>, <{FormalParam ","}* fs>) <Block b>` 
       => (MethodDec)`<RefType rt> <Id meth>(<ClassOrInterfaceType t> <Id alg>, <{FormalParam ","}* fs>) {
                     '  return (<RefType rt>)<Expr cps>;
                     '}`
        when cps := method2cps(b, alg)
     
     case (MethodDec)`@Recaf(ext=<Id t>.class, arg=<Id et>.class) 
                     '<RefType rt> <Id meth>(<{FormalParam ","}* fs>) <Block b>` 
       => (MethodDec)`<RefType rt> <Id meth>(<{FormalParam ","}* fs>) {
                     '  <Id t>\<<Id et>\> <Id alg> = new <Id t>\<<Id et>\>();
                     '  return (<RefType rt>)<Expr cps>;
                     '}`
        when
          alg := (Id)`$alg`, 
          cps := method2cps(b, alg) 
     
      case (MethodDec)`<RefType rt> <Id meth>(recaf <ClassOrInterfaceType t> <Id alg>, <{FormalParam ","}* fs>) <Block b>` 
       => (MethodDec)`<RefType rt> <Id meth>(<ClassOrInterfaceType t> <Id alg>, <{FormalParam ","}* fs>) {
                     '  return (<RefType rt>)<Expr cps>;
                     '}`
        when cps := method2cps(b, alg)
       
      case (MethodDec)`[<ClassOrInterfaceType t>] 
                     '<RefType rt> <Id meth>(<{FormalParam ","}* fs>) <Block b>` 
       => (MethodDec)`<RefType rt> <Id meth>(<{FormalParam ","}* fs>) {
                     '  <ClassOrInterfaceType t> <Id alg> = new <ClassOrInterfaceType t>();
                     '  return (<RefType rt>)<Expr cps>;
                     '}`
        when
          alg := (Id)`$alg`, 
          cps := method2cps(b, alg)

	  case (MethodDec)`[<ClassOrInterfaceType t>] 
                     'public static <RefType rt> <Id meth>(<{FormalParam ","}* fs>) <Block b>` 
       => (MethodDec)`public static <RefType rt> <Id meth>(<{FormalParam ","}* fs>) {
                     '  <ClassOrInterfaceType t> <Id alg> = new <ClassOrInterfaceType t>();
                     '  return (<RefType rt>)<Expr cps>;
                     '}`
        when
          alg := (Id)`$alg`, 
          cps := method2cps(b, alg)

   }
}

Expr method2cps(Block b, Id alg) 
  = (Expr)`<Id alg>.Method(<Expr bcps2>)`
  when
    localIds := collectVars(b),
    bcps := block2cps(b, alg, localIds),
    newBlock := addRefs(b, localIds),
    bcps2 := block2cps(newBlock, alg, localIds);
    
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
    	case (LocalVarDec) `<Type t> <Id x> = <Expr e>` => (LocalVarDec) `Ref\<<Type newt>\> <Id x> = new Ref\<<Type newt>\>(<Expr e>)`
    		when newt := boxed(t)
};

//LocalVarDec wrap(Type t, Id x, Expr e) =  (LocalVarDec) `<Type t> <Id x> = new Ref(<Expr e>)`;

/// Extensions

// return-like
Expr stm2cps((Stm)`<KId ext>! <Expr e>;`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>)`
  when
    Expr ecps := expr2cps(e, alg, localIds),
    Id method := [Id]capitalize("<ext>");

// for like
Expr stm2cps((Stm)`<KId ext> (<FormalParam f>: <Expr e>) <Stm s>`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, (<FormalParam f>) -\> {return <Expr scps>;})`
  when 
    Expr ecps := expr2cps(e, alg, localIds),
    Expr scps := stm2cps(s, alg, localIds),
    Id method := [Id]capitalize("<ext>");

// while like
Expr stm2cps((Stm)`<KId ext> (<Expr c>) <Block b>`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, <Expr bcps>)`
  when 
    Expr ecps := expr2cps(c, alg, localIds),
    Expr bcps := block2cps(b, alg, localIds),
    Id method := [Id]capitalize("<ext>");

// try-like
Expr stm2cps((Stm)`<KId ext> <Block b>`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.<Id method>(<Expr bcps>)`
  when 
    Expr bcps := block2cps(b, alg, localIds),
    Id method := [Id]capitalize("<ext>");

// switch-like
Expr stm2cps((Stm)`<KId ext> (<FormalParam f>: <Expr e>) {<Item+ items>}`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, (<FormalParam f>) -\> {return <Expr itemscps>;})`
  when 
    Expr ecps := expr2cps(e, alg, localIds),
    Expr itemscps := items2cps([ i | i <- items ], alg, localIds),
    Id method := [Id]capitalize("<ext>");

Expr stm2cps((Stm)`<KId ext> (<Expr e>) {<Item+ items>}`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, <Expr itemscps>)`
  when 
    Expr ecps := expr2cps(e, alg, localIds),
    Expr itemscps := items2cps([ i | i <- items ], alg, localIds),
    Id method := [Id]capitalize("<ext>");

Expr stm2cps((Stm)`<KId ext> {<Item+ items>}`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.<Id method>(<Expr itemscps>)`
  when 
    Expr itemscps := items2cps([ i | i <- items ], alg, localIds),
    Id method := [Id]capitalize("<ext>");


Expr items2cps(list[Item] items:[], Id alg, set[Id] localIds)
  = (Expr)`java.util.Arrays.asList()`;

Expr items2cps(list[Item] items:[Item x, *xs], Id alg, set[Id] localIds)
  = (Expr)`java.util.Arrays.asList(<Expr xcps>, <{Expr ","}* es>)`
  when
    Expr xcps := item2cps(x, alg, localIds),
    (Expr)`java.util.Arrays.asList(<{Expr ","}* es>)` := items2cps(xs, alg, localIds);

Expr item2cps((Item)`<KId kw> <Expr e>: <BlockStm+ stms>`, Id alg, set[Id] localIds)
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, <Expr stmscps>)`
  when 
    Id method := [Id]capitalize("<kw>"),
    Expr ecps := expr2cps(e, alg, localIds),
    Expr stmscps := block2cps((Block)`{<BlockStm+ stms>}`, alg, localIds);
  
Expr item2cps((Item)`<KId kw> <FormalParam f>: <BlockStm+ stms>`, Id alg, set[Id] localIds)
  = (Expr)`<Id alg>.<Id method>((<FormalParam f>) -\> { return <Expr stmscps>; })`
  when 
    Id method := [Id]capitalize("<kw>"),
    Expr stmscps := block2cps((Block)`{<BlockStm+ stms>}`, alg, localIds);


// new style
Expr stm2cps((Stm)`<KId kw> (<{FormalParam ","}+ fs>) <Stm stm>`, Id alg, set[Id] localIds)
  = (Expr)`<Id alg>.<Id method>((<{FormalParam  ","}+ fs>) -\> { return <Expr stmcps>; })`
  when
    Id method := [Id]capitalize("<kw>"),
    Expr stmcps := stm2cps(stm, alg, localIds);
  

//////


Expr stm2cps((Stm)`continue <Id x>;`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.Continue(<Expr label>)`
  when 
    Expr label := [Expr]"\"<x>\"";
  
Expr stm2cps((Stm)`continue;`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.Continue()`;

Expr stm2cps((Stm)`break <Id x>;`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.Break(<Expr label>)`
  when 
    Expr label := [Expr]"\"<x>\"";
  
Expr stm2cps((Stm)`break;`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.Break()`;

Expr stm2cps((Stm)`for (<FormalParam f>: <Expr e>) <Stm s>`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.For(<Expr ecps>, (<FormalParam f>) -\> {return <Expr scps>;})`
  when 
    Expr ecps := expr2cps(e, alg, localIds),
    Expr scps := stm2cps(s, alg, localIds);

Expr stm2cps((Stm)`for (<{Expr ","}* es1>; <Expr cond>; <{Expr ","}* update>) <Stm body>`, Id alg, set[Id] localIds)
  = TODO;  
    
Expr stm2cps((Stm)`throw <Expr e>;`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.Throw(<Expr ecps>)`
  when
    Expr ecps := expr2cps(e, alg, localIds);

Expr stm2cps((Stm)`return <Expr e>;`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.Return(<Expr ecps>)`
  when
    Expr ecps := expr2cps(e, alg, localIds);

Expr stm2cps((Stm)`return;`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.Return()`;

Expr stm2cps((Stm)`;`, Id alg, set[Id] localIds) = (Expr)`<Id alg>.Empty()`;

Expr stm2cps((Stm)`<Block b>`, Id alg, set[Id] localIds) = block2cps(b, alg, localIds);

// TODO: ClassDec as blockstm
Expr block2cps((Block)`{}`, Id alg, set[Id] localIds) = (Expr)`<Id alg>.Empty()`;

// singletons
Expr block2cps((Block)`{<Type t> <{VarDec ","}+ vs>;}`, Id alg, set[Id] localIds) 
  = varDecs2cps(t, vs, (Expr)`<Id alg>.Empty()`, alg, localIds);

Expr block2cps((Block)`{<KId kw> <FormalParam f>;}`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.<Id method>((<FormalParam f>) -\> <Id alg>.Empty())`
  when
    Id method := [Id]capitalize("<kw>");

Expr block2cps((Block)`{<KId kw> <FormalParam f> = <Expr e>;}`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, (<FormalParam f>) -\> <Id alg>.Empty())`
  when
    Id method := [Id]capitalize("<kw>"),
    Expr ecps := expr2cps(e, alg, localIds);

default Expr block2cps((Block)`{<Stm s>}`, Id alg, set[Id] localIds) = stm2cps(s, alg, localIds);

// end singletons

default Expr block2cps((Block)`{<Stm s> <BlockStm+ ss>}`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.Seq(<Expr scps>, <Expr sscps>)`
  when
    Expr scps := stm2cps(s, alg, localIds),
    Expr sscps := block2cps((Block)`{<BlockStm* ss>}`, alg, localIds);

// todo: annos/modifiers
Expr block2cps((Block)`{<Type t> <{VarDec ","}+ vs>; <BlockStm+ ss>}`, Id alg, set[Id] localIds) 
  = varDecs2cps(t, vs, sscps, alg, localIds)
  when
    Expr sscps := block2cps((Block)`{<BlockStm+ ss>}`, alg, localIds);

// decl like
//   | KId FormalParam ";"
//   | KId FormalParam "=" Expr ";"
   
Expr block2cps((Block)`{<KId kw> <FormalParam f>; <BlockStm+ ss>}`, Id alg, set[Id] localIds)
  = (Expr)`<Id alg>.<Id method>((<FormalParam f>) -\> { return <Expr sscps>; })`
  when
    Id method := [Id]capitalize("<kw>"),
    Expr sscps := block2cps((Block)`{<BlockStm+ ss>}`, alg, localIds);

Expr block2cps((Block)`{<KId kw> <FormalParam f> = <Expr e>; <BlockStm+ ss>}`, Id alg, set[Id] localIds)
  = (Expr)`<Id alg>.<Id method>(<Expr ecps>, (<FormalParam f>) -\> { return <Expr sscps>; })`
  when
    Id method := [Id]capitalize("<kw>"),
    Expr ecps := expr2cps(e, alg, localIds),
    Expr sscps := block2cps((Block)`{<BlockStm+ ss>}`, alg, localIds);


Expr varDecs2cps(Type t, {VarDec ","}+ vs, Expr k, Id alg, set[Id] localIds) {
   for (VarDec vd <- reverse([ v | v <- vs])) {
     k = varDec2cps(t, vd, k, alg, localIds);
   }
   return k;
}   

// TODO: remove null?
Expr varDec2cps(Type t, (VarDec)`<Id x>`, Expr k, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.\<<Type t>\>Decl(null, <Id x> -\> {return <Expr k>;})`;
  
Expr varDec2cps(Type t, (VarDec)`<Id x> = <VarInit e>`, Expr k, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.\<<Type t>\>Decl(<Expr ecps>, <Id x> -\> {return <Expr k>;})`
  when
    Expr ecps := varInit2cps(t, e, alg, localIds);

//VarInit wrap((VarInit) `<Expr e>`, Type t) = (VarInit) `new Ref\<<Type t>\>(<Expr e>)`;

Expr varInit2cps(Type t, (VarInit)`<Expr e>`, Id alg, set[Id] localIds)
  = expr2cps(e, alg, localIds);

Expr varInit2cps(Type t, (VarInit)`{<{VarInit ","}* inits>,}`, Id alg, set[Id] localIds)
  = TODO; 

Expr varInit2cps(Type t, (VarInit)`{<{VarInit ","}* inits>}`, Id alg, set[Id] localIds)
  = TODO; 

Expr stm2cps((Stm)`<Id l>: <Stm s>`, Id alg, set[Id] localIds)
   = (Expr)`<Id alg>.Labeled(<Expr label>, <Expr scps>)`
   when 
     Expr label := [Expr]"\"<l>\"",
     Expr scps := stm2cps(s, alg, localIds);
     
Expr stm2cps((Stm)`if (<Expr c>) <Stm s>`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.If(<Expr ecps>, <Expr scps>)`
  when 
    Expr ecps := expr2cps(c, alg, localIds),
    Expr scps := stm2cps(s, alg, localIds);

Expr stm2cps((Stm)`if (<Expr c>) <Stm s1> else <Stm s2>`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.If(<Expr ecps>, <Expr s1cps>, <Expr s2cps>)`
  when 
    Expr ecps := expr2cps(c, alg, localIds),
    Expr s1cps := stm2cps(s1, alg, localIds),
    Expr s2cps := stm2cps(s2, alg, localIds);
  
Expr stm2cps((Stm)`while (<Expr c>) <Stm s>`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.While(<Expr ecps>, <Expr scps>)`
  when 
    Expr ecps := expr2cps(c, alg, localIds),
    Expr scps := stm2cps(s, alg, localIds);

Expr stm2cps((Stm)`do <Stm s> while (<Expr c>);`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.Do(<Expr scps>, <Expr ecps>)`
  when 
    Expr scps := stm2cps(s, alg, localIds),
    Expr ecps := expr2cps(c, alg, localIds);

Expr stm2cps((Stm)`<Expr e>;`, Id alg, set[Id] localIds) 
  = (Expr)`<Id alg>.ExpStat(<Expr ecps>)`
  when
    Expr ecps := expr2cps(e, alg, localIds);

Expr stm2cps((Stm)`try <Block body> <CatchClause* catches> finally <Block fin>`, Id alg, set[Id] localIds)
  = (Expr)`<Id alg>.TryCatchFinally(<Expr bodycps>, <Expr catchescps>, <Expr fincps>)`
  when 
    Expr bodycps := block2cps(body, alg, localIds),
    Expr catchescps := catches2cps(catches, alg),
    Expr fincps := block2cps(body, alg, localIds); 

Expr stm2cps((Stm)`try <Block body> <CatchClause+ catches>`, Id alg, set[Id] localIds)
  = (Expr)`<Id alg>.TryCatch(<Expr bodycps>, <Expr catchescps>)`
  when 
    Expr bodycps := block2cps(body, alg, localIds),
    Expr catchescps := catches2cps(catches, alg);

// "catch"  "(" FormalParam ")" Block

Expr catch2cps((CatchClause)`catch (<Type t> <Id x>) <Block b>`, Id alg)
  =  (Expr)`<Id alg>.Catch(<Type t>.class, (<Type t> <Id x>) -\> {return <Expr bcps>;})`
  when
    Expr bcps := block2cps(b, alg, TODO);
    
Expr expr2cps(Expr e, Id alg, set[Id] localIds)
  = (Expr)`<Id alg>.Exp(() -\> { return <Expr e>; })` ;

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
//    Expr ecps := expr2cps(e, (Expr)`sigma`, (Expr)`err`);
//
//Expr expr2cps((Expr)`<Id x>! (<{Expr ","}* args>)`) = 0;
//
//Expr expr2cps((Expr)`<Literal lit>`, Expr k) 
//  = 0;
//
//Expr expr2cps((Expr)`<Expr l> + <Expr r>`, Expr sigma, Expr err) = 
//  (Expr)`(v1, v2) -\> k -\> <Expr sigma>.accept(v1 + v2)`
//  when
//    Expr lcps := expr2cps(l, expr2cps(r, (Expr)`(k, e) -\> k.accept(v1 + v2)`));


//Expr expr2cps((Expr)`<Expr l> + <Expr r>`, Expr k)
//  = expr2cps(l, (Expr)`v1 -\> <Expr e2>`)
//  when Expr e2 := expr2cps(r, (Expr)`v2 -\> <Expr k>.accept(v1 + v2)`); 
//  
//Expr expr2cps((Expr)`<Id x>`, Expr k)
//  = (Expr)`(<Expr k>).accept(<Id x>)`;



  
