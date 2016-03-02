module lang::recaf::desugar::Util

import lang::recaf::Recaf;

alias Names = tuple[set[Id] refs, map[Id, Id] renaming];

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

Type boxed((Type)`int`) = (Type) `Integer`;
Type boxed((Type)`long`) = (Type)`Long`;
Type boxed((Type)`boolean`) = (Type)`Boolean`;
Type boxed((Type)`double`) = (Type)`Double`;
Type boxed((Type)`float`) = (Type)`Float`;
Type boxed((Type)`char`) = (Type)`Character`;

default Type boxed(Type t) = t;

