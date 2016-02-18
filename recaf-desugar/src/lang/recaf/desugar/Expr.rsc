module lang::recaf::desugar::Expr

import lang::java::Java8;
extend lang::recaf::desugar::Stmt;
import ParseTree;
import List;
import String;

//
// TODO:
//  | ArrayCreationExpr 
// qNewInstance: Expr "." "new" TypeArgs? Id TypeArgs? "(" {Expr ","}* ")" ClassBody? 
//   exprName: AmbName "." Id // statics 
//  |  exprName: Id 
//  ;

// NB: cannot use expr2alg because of void expressions.
Expr stm2alg((Stm)`<Expr e>;`, Id alg, Names names) 
  = (Expr)`<Id alg>.ExpStat(<Expr e2>)`
  when 
    Expr e2 := expr2alg(e, alg, names);



Expr expr2alg((Expr)`(<{FormalParam ","}* fps>) -\> <Expr e>`, Id alg, Names names)
  = (Expr)`<Id alg>.Closure((<{FormalParam ","}* fps>) -\> <Expr e2>)`
  when
    e2 := expr2alg(e, alg, names);

Expr expr2alg((Expr)`(<{Id ","}+ fps>) -\> <Expr e>`, Id alg, Names names)
  = (Expr)`<Id alg>.Closure((<{Id ","}+ fps>) -\> <Expr e2>)`
  when
    e2 := expr2alg(e, alg, names);

Expr expr2alg((Expr)`<Id x> -\> <Expr e>`, Id alg, Names names)
  = expr2alg((Expr)`(<Id x>) -\> <Expr e>`, alg, names);

Expr expr2alg((Expr)`(<{FormalParam ","}* fps>) -\> <Block b>`, Id alg, Names names)
  = (Expr)`<Id alg>.Closure((<{FormalParam ","}* fps>) -\> <Expr e2>)`
  when
    e2 := block2alg(b, alg, names);

Expr expr2alg((Expr)`(<{Id ","}+ fps>) -\> <Block b>`, Id alg, Names names)
  = (Expr)`<Id alg>.Closure((<{Id ","}+ fps>) -\> <Expr e2>)`
  when
    e2 := block2alg(b, alg, names);

Expr expr2alg((Expr)`<Id x> -\> <Block b>`, Id alg, Names names)
  = expr2alg((Expr)`(<Id x>) -\> <Block b>`, alg, names);


Expr expr2alg((Expr)`<Id x>`, Id alg, Names names)
  = (Expr)`<Id alg>.Var(<Expr name>, <Id x>)`
  when
    Expr name := id2strExpr(x);

Expr expr2alg((Expr)`<Expr c> ? <Expr t> : <Expr e>`, Id alg, Names names)
  = (Expr)`<Id alg>.Cond(<Expr c2>, <Expr t2>, <Expr e2>)`
  when
    Expr c2 := expr2alg(c, alg, names),
    Expr t2 := expr2alg(t, alg, names),
    Expr e2 := expr2alg(e, alg, names);

Expr expr2alg((Expr)`<Expr a>[<Expr i>]`, Id alg, Names names)
  = (Expr)`<Id alg>.ArrayAccess(<Expr a2>, <Expr i2>)`
  when
    Expr a2 := expr2alg(a, alg, names),
    Expr i2 := expr2alg(i, alg, names);

Expr expr2alg((Expr)`new <TypeArgs? _> <ClassOrInterfaceType t>(<{Expr ","}* es>)`, Id alg, Names names)
  = (Expr)`<Id alg>.New(<ClassOrInterfaceType t>.class, <{Expr ","}* es2>)`
  when 
    {Expr ","}* es2 := args2alg(es, alg, names);

Expr expr2alg((Expr)`<Id m>(<{Expr ","}* es>)`, Id alg, Names names)
  = (Expr)`<Id alg>.Invoke(this, <Expr name>, <{Expr ","}* es2>)`
  when 
    name := id2strExpr(m),
    {Expr ","}* es2 := args2alg(es, alg, names);

Expr expr2alg((Expr)`super.<TypeArgs? _><Id m>(<{Expr ","}* es>)`, Id alg, Names names)
  = (Expr)`<Id alg>.InvokeSuper(this, <Expr name>, <{Expr ","}* es2>)`
  when 
    name := id2strExpr(m),
    {Expr ","}* es2 := args2alg(es, alg, names);

Expr expr2alg((Expr)`<TypeName tn>.super.<TypeArgs? _><Id m>(<{Expr ","}* es>)`, Id alg, Names names)
  = (Expr)`<Id alg>.InvokeSuper(this, <TypeName tn>.class, <Expr name>, <{Expr ","}* es2>)`
  when 
    name := id2strExp(m),
    {Expr ","}* es2 := args2alg(es, alg, names);


Expr expr2alg((Expr)`<Expr e>.<TypeArgs? _><Id m>(<{Expr ","}* es>)`, Id alg, Names names)
  = (Expr)`<Id alg>.Invoke(<Expr e2>, <Expr name>, <{Expr ","}* es2>)`
  when 
    Expr name := id2strExpr(m),
    Expr e2 := expr2alg(e, alg, names),
    {Expr ","}* es2 := args2alg(es, alg, names);

// TODO:
//  |  genericMethod: AmbName "." TypeArgs Id 

Expr id2strExpr(Id x) = [Expr]"\"<x>\"";

{Expr ","}* args2alg({Expr ","}* es, Id alg, Names names) {
  // hack
  i = 0;
  while (i < size(es.args)) {
    if (Expr e := es.args[i]) {
      es.args[i] = expr2alg(e, alg, names);
    } 
    else {
      throw "Non expression in expression list: <es.args[i]>";
    }
    i += 2;
  }
  return es;
}

Expr expr2alg((Expr)`<Expr x>.<Id f>`, Id alg, Names names)
  = (Expr)`<Id alg>.Field(<Expr x2>, <Expr name>)`
  when
    Expr x2 := expr2alg(x, alg, name),
    Expr name := id2strExpr(f);

Expr expr2alg((Expr)`super.<Id x>`, Id alg, Names names)
  = (Expr)`<Id alg>.SuperField(<Expr name>, this)`
  when
     Expr name := id2strExpr(x);

Expr expr2alg((Expr)`<TypeName tn>.super.<Id x>`, Id alg, Names names)
  = (Expr)`<Id alg>.SuperField(<Expr tname>, <Expr name>, this)`
  when
     Expr tname := id2strExpr(tn),
     Expr name := id2strExpr(x);


Expr expr2alg((Expr)`(<Expr e>)`, Id alg, Names names)
  = expr2alg(e, alg, names);


Expr expr2alg((Expr)`this`, Id alg, Names names)
  = (Expr)`<Id alg>.This(this)`;

Expr expr2alg((Expr)`<TypeName tn>.this`, Id alg, Names names)
  = (Expr)`<Id alg>.This(<TypeName tn>.this)`;

Expr expr2alg((Expr)`<Literal lit>`, Id alg, Names names)
  = (Expr)`<Id alg>.Lit(<Literal lit>)`;

Expr expr2alg((Expr)`(<PrimType t>)<Expr e>`, Id alg, Names names)
  = (Expr)`<Id alg>.CastPrim(<Type t2>.class, <Expr e2>)`
  when 
    t2 := boxed((Type)`<PrimType t>`),
    e2 := expr2alg(e, alg, names);

Expr expr2alg((Expr)`(<RefType t>)<Expr e>`, Id alg, Names names)
  = (Expr)`<Id alg>.CastPrim(<RefType t>.class, <Expr e2>)`
  when 
    e2 := expr2alg(e, alg, names);

default Expr expr2alg(Expr e, Id alg, Names names) {
   if (isPrefix(e)) {
     a = expr2alg(getPrefixArg(e), alg, names);
     m = methodName(e);
     return (Expr)`<Id alg>.<Id m>(<Expr a>)`;
   }
   if (isPostfix(e)) {
     a = expr2alg(getPostfixArg(e), alg, names);
     m = methodName(e);
     return (Expr)`<Id alg>.<Id m>(<Expr a>)`;
   }
   if (isInfix(e)) {
     l = expr2alg(getLhs(e), alg, names);
     r = expr2alg(getRhs(e), alg, names);
     m = methodName(e);
     return (Expr)`<Id alg>.<Id m>(<Expr l>, <Expr r>)`;
   }
   if (isAssign(e)) {
     l = lhs2alg(getLHS(e), alg, names);
     r = expr2alg(getRhs(e), alg, names);
     return (Expr)`<Id alg>.<Id m>(<Expr l>, <Expr r>)`; 
   }
   throw "Unsupported expression: <e>";
}

Expr lhs2alg((LHS)`<ExprName x>`, Id alg, Names names) 
  = exp2alg((Expr)`<ExprName x>`, alg, names);

Expr lhs2alg((LHS)`(<LHS l>)`, Id alg, Names names) 
  = lhs2alg(l, alg, names);

Expr lhs2alg((LHS)`<ArrayAccess x>`, Id alg, Names names) 
  = exp2alg((Expr)`<ArrayAccess x>`, alg, names);

Expr lhs2alg((LHS)`<FieldAccess x>`, Id alg, Names names) 
  = exp2alg((Expr)`<FieldAccess x>`, alg, names);


Id methodName(Expr e) = [Id]"<capitalize(getLabel(e))>";

bool isAssign(Expr e) 
  = size(e.prod.symbols) == 5
  && sort("LHS") := e.prod.symbols[0]
  && /lit(_) := e.prod.symbols[2] 
  && sort("Expr") := e.prod.symbols[4]; 

LHS getLHS(Expr e) = lhs
  when LHS lhs := e.args[0];

Expr getLhs(Expr e) = lhs
  when Expr lhs := e.args[0];
  
Expr getRhs(Expr e) = rhs
  when Expr rhs := e.args[4];

Expr getPrefixArg(Expr e) = arg
  when Expr arg := e.args[2]; 
  
Expr getPostfixArg(Expr e) = arg
  when Expr arg := e.args[0]; 
  
bool isPrefix(Expr e) 
  = size(e.prod.symbols) == 3
  && /lit(_) := e.prod.symbols[0] 
  && sort("Expr") := e.prod.symbols[2];

bool isPostfix(Expr e) 
  = size(e.prod.symbols) == 3
  && sort("Expr") := e.prod.symbols[0]
  && /lit(_) := e.prod.symbols[2]; 
  
   
bool isInfix(Expr e) 
  = size(e.prod.symbols) == 5
  && sort("Expr") := e.prod.symbols[0]
  && /lit(_) := e.prod.symbols[2] 
  && sort("Expr") := e.prod.symbols[4]; 

str getLabel(Tree t) = getLabel(t.prod);

str getLabel(prod(label(str l, _), _, _)) = l;

default str getLabel(Production _) = "UNKNOWN";






