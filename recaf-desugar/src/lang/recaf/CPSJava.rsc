module lang::recaf::CPSJava

import lang::recaf::ExtJava;
import List;
import String;
  
  
// TODO: pass alg argument through.
start[CompilationUnit] desugar(start[CompilationUnit] cu) {
   return visit (cu) {
     case (MethodDec)`<RefType rt> <Id meth>(@Builder <Type t> <Id alg>, <{FormalParam ","}* fs>) <Block b>` 
       => (MethodDec)`<RefType rt> <Id meth>(<Type t> <Id alg>, <{FormalParam ","}* fs>) {
                     '  <Type t> $alg = <Id alg>;   
                     '  return (<RefType rt>)<Expr cps>;
                     '}`
        when cps := method2cps(b) 
   }
}

Expr method2cps(Block b) 
  = (Expr)`$alg.Method(<Expr bcps>)`
  when
    bcps := block2cps(b);

//TypeParams? ResultType Id "(" {FormalParam ","}* ")" Throws? 
  //|  deprMethodDecHead: (MethodMod | Anno)* TypeParams? ResultType Id "(" {FormalParam ","}* ")" Dim+ Throws?


/// Extensions

// return-like
Expr stm2cps((Stm)`<KId ext>! <Expr e>;`) 
  = (Expr)`$alg.<Id method>(<Expr ecps>)`
  when
    Expr ecps := expr2cps(e),
    Id method := [Id]capitalize("<ext>");


// for like
Expr stm2cps((Stm)`<KId ext> (<FormalParam f>: <Expr e>) <Stm s>`) 
  = (Expr)`$alg.<Id method>(<Expr ecps>, (<FormalParam f>) -\> {return <Expr scps>;})`
  when 
    Expr ecps := expr2cps(e),
    Expr scps := stm2cps(s),
    Id method := [Id]capitalize("<ext>");

// while like
Expr stm2cps((Stm)`<KId ext> (<Expr c>) <Block b>`) 
  = (Expr)`$alg.<Id method>(<Expr ecps>, <Expr bcps>)`
  when 
    Expr ecps := expr2cps(c),
    Expr bcps := block2cps(b),
    Id method := [Id]capitalize("<ext>");

// try-like
Expr stm2cps((Stm)`<KId ext> <Block b>`) 
  = (Expr)`$alg.<Id method>(<Expr bcps>)`
  when 
    Expr bcps := block2cps(b),
    Id method := [Id]capitalize("<ext>");

// switch-like
Expr stm2cps((Stm)`<KId ext> (<FormalParam f>: <Expr e>) {<Item+ items>}`) 
  = (Expr)`$alg.<Id method>(<Expr ecps>, (<FormalParam f>) -\> {return <Expr itemscps>;})`
  when 
    Expr ecps := expr2cps(e),
    Expr itemscps := items2cps([ i | i <- items ]),
    Id method := [Id]capitalize("<ext>");

Expr stm2cps((Stm)`<KId ext> (<Expr e>) {<Item+ items>}`) 
  = (Expr)`$alg.<Id method>(<Expr ecps>, <Expr itemscps>)`
  when 
    Expr ecps := expr2cps(e),
    Expr itemscps := items2cps([ i | i <- items ]),
    Id method := [Id]capitalize("<ext>");

Expr stm2cps((Stm)`<KId ext> {<Item+ items>}`) 
  = (Expr)`$alg.<Id method>(<Expr itemscps>)`
  when 
    Expr itemscps := items2cps([ i | i <- items ]),
    Id method := [Id]capitalize("<ext>");


Expr items2cps(list[Item] items:[])
  = (Expr)`java.util.Arrays.asList()`;

Expr items2cps(list[Item] items:[Item x, *xs])
  = (Expr)`java.util.Arrays.asList(<Expr xcps>, <{Expr ","}* es>)`
  when
    Expr xcps := item2cps(x),
    (Expr)`java.util.Arrays.asList(<{Expr ","}* es>)` := items2cps(xs);

Expr item2cps((Item)`<KId kw> <Expr e>: <Stm stm>`)
  = (Expr)`$alg.<Id method>(<Expr ecps>, <Expr stmcps>)`
  when 
    Id method := [Id]capitalize("<kw>"),
    Expr ecps := expr2cps(e),
    Expr stmcps := stm2cps(stm);
  

//////


Expr stm2cps((Stm)`continue <Id x>;`) 
  = (Expr)`$alg.Continue(<Expr label>)`
  when 
    Expr label := [Expr]"\"<x>\"";
  
Expr stm2cps((Stm)`continue;`) 
  = (Expr)`$alg.Continue()`;

Expr stm2cps((Stm)`break <Id x>;`) 
  = (Expr)`$alg.Break(<Expr label>)`
  when 
    Expr label := [Expr]"\"<x>\"";
  
Expr stm2cps((Stm)`break;`) 
  = (Expr)`$alg.Break()`;


Expr stm2cps((Stm)`for (<FormalParam f>: <Expr e>) <Stm s>`) 
  = (Expr)`$alg.For(<Expr ecps>, (<FormalParam f>) -\> {return <Expr scps>;})`
  when 
    Expr ecps := expr2cps(e),
    Expr scps := stm2cps(s);
 
    
Expr stm2cps((Stm)`throw <Expr e>;`) 
  = (Expr)`$alg.Throw(<Expr ecps>)`
  when
    Expr ecps := expr2cps(e);

Expr stm2cps((Stm)`return <Expr e>;`) 
  = (Expr)`$alg.Return(<Expr ecps>)`
  when
    Expr ecps := expr2cps(e);


Expr stm2cps((Stm)`return;`) 
  = (Expr)`$alg.Return()`;

Expr stm2cps((Stm)`;`) = (Expr)`$alg.Empty()`;

Expr stm2cps((Stm)`<Block b>`) = block2cps(b);

// TODO: ClassDec as blockstm
Expr block2cps((Block)`{}`) = (Expr)`$alg.Empty()`;

Expr block2cps((Block)`{<Stm s>}`) = stm2cps(s);

Expr block2cps((Block)`{<Stm s> <BlockStm+ ss>}`) 
  = (Expr)`$alg.Seq(<Expr scps>, <Expr sscps>)`
  when
    Expr scps := stm2cps(s),
    Expr sscps := block2cps((Block)`{<BlockStm* ss>}`);

// todo: annos/modifiers
Expr block2cps((Block)`{<Type t> <{VarDec ","}+ vs>; <BlockStm+ ss>}`) 
  = varDecs2cps(t, vs, sscps)
  when
    Expr sscps := block2cps((Block)`{<BlockStm* ss>}`);

Expr varDecs2cps(Type t, {VarDec ","}+ vs, Expr k) {
   for (VarDec vd <- reverse([ v | v <- vs])) {
     k = varDec2cps(t, vd, k);
   }
   return k;
}   

Expr varDec2cps(Type t, (VarDec)`<Id x>`, Expr k) 
  = (Expr)`$alg.\<<Type t>\>Decl(null, <Id x> -\> {return <Expr k>;})`;
  
Expr varDec2cps(Type t, (VarDec)`<Id x> = <VarInit e>`, Expr k) 
  = (Expr)`$alg.\<<Type t>\>Decl(<Expr ecps>, <Id x> -\> {return <Expr k>;})`
  when
    Expr ecps := varInit2cps(t, e);

Expr varInit2cps(Type t, (VarInit)`<Expr e>`)
  = expr2cps(e);

Expr varInit2cps(Type t, (VarInit)`{<{VarInit ","}* inits>,}`)
  = TODO; 

Expr varInit2cps(Type t, (VarInit)`{<{VarInit ","}* inits>}`)
  = TODO; 

Expr stm2cps((Stm)`<Id l>: <Stm s>`)
   = (Expr)`$alg.Labeled(<Expr label>, <Expr scps>)`
   when 
     Expr label := [Expr]"\"<l>\"",
     Expr scps := stm2cps(s);
     
Expr stm2cps((Stm)`if (<Expr c>) <Stm s>`) 
  = (Expr)`$alg.If(<Expr ecps>, <Expr scps>)`
  when 
    Expr ecps := expr2cps(c),
    Expr scps := stm2cps(s);

Expr stm2cps((Stm)`if (<Expr c>) <Stm s1> else <Stm s2>`) 
  = (Expr)`$alg.If(<Expr ecps>, <Expr s1cps>, <Expr s2cps>)`
  when 
    Expr ecps := expr2cps(c),
    Expr s1cps := stm2cps(s1),
    Expr s2cps := stm2cps(s2);
  
Expr stm2cps((Stm)`while (<Expr c>) <Stm s>`) 
  = (Expr)`$alg.While(<Expr ecps>, <Expr scps>)`
  when 
    Expr ecps := expr2cps(c),
    Expr scps := stm2cps(s);

    
Expr stm2cps((Stm)`do <Stm s> while (<Expr c>);`) 
  = (Expr)`$alg.Do(<Expr scps>, <Expr ecps>)`
  when 
    Expr scps := stm2cps(s),
    Expr ecps := expr2cps(c);

Expr stm2cps((Stm)`<Expr e>;`) 
  = (Expr)`$alg.ExpStat(<Expr ecps>)`
  when
    Expr ecps := expr2cps(e);

Expr stm2cps((Stm)`try <Block body> <CatchClause* catches> finally <Block fin>`)
  = (Expr)`$alg.TryCatchFinally(<Expr bodycps>, <Expr catchescps>, <Expr fincps>)`
  when 
    Expr bodycps := block2cps(body),
    Expr catchescps := catches2cps(catches),
    Expr fincps := block2cps(body); 

Expr stm2cps((Stm)`try <Block body> <CatchClause+ catches>`)
  = (Expr)`$alg.TryCatch(<Expr bodycps>, <Expr catchescps>)`
  when 
    Expr bodycps := block2cps(body),
    Expr catchescps := catches2cps(catches);


// "catch"  "(" FormalParam ")" Block

Expr catch2cps((CatchClause)`catch (<Type t> <Id x>) <Block b>`)
  =  (Expr)`$alg.Catch(<Type t>.class, (<Type t> <Id x>) -\> {return <Expr bcps>;})`
  when
    Expr bcps := block2cps(b);

Expr expr2cps(Expr e)
  = (Expr)`$alg.Exp(() -\> { return <Expr e>; })`;

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



  
