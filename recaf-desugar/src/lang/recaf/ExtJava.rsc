module lang::recaf::ExtJava

extend lang::java::\syntax::Java15;

syntax KId
  = @category="MetaKeyword" Id
  ; 

syntax Stm
   // return -like
   = KId "!" Expr ";" // ! is needed, otherwise amb with local var dec.
   
   // for, while and try like
   | KId "(" FormalParam ":" Expr ")" Stm
   | KId "(" Expr ")" Block // block otherwise amb with method call and empty
   | KId Block 
 
   // with continuation blocks (not implemented)
   | KId "(" FormalParam ":" Expr ")" Stm Rest+
   | KId "(" Expr ")" Block Rest+
   | KId Block Rest+

   // switch case like
   | KId "(" FormalParam ":" Expr ")" "{" Item+ "}"
   | KId "(" Expr ")" "{" Item+ "}"
   | KId "{" Item+ "}"
   ;
   
syntax Item
  = KId Expr ":" Stm
  ;
   
syntax Rest
   = "." KId  Block
   | "." KId "(" Expr ")" Block
   | "." KId "(" FormalParam ":" Expr ")" Block
   ;

syntax Expr
  = right cond: Expr CondMid Expr 
  > "(" {FormalParam ","}* ")" "-\>" Expr
  | "(" {FormalParam ","}* ")" "-\>" Block
  | "(" {Id ","}+ ")" "-\>" Expr
  | "(" {Id ","}+ ")" "-\>" Block
  | Id "-\>" Expr
  | Id "-\>" Block
  ;


