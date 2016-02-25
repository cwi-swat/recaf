module lang::recaf::Recaf

extend lang::java::Java8;

syntax KId
  = @category="MetaKeyword" Id
  ; 

syntax Stm
   // return -like
   = KId "!" Expr ";" // ! is needed, otherwise amb with local var dec.
   
   // for, while and try like, and combinations
   | KId "(" FormalParam ":" Expr ")" Stm
   | KId "(" Expr ")" Block // block otherwise amb with method call and empty
   | KId "(" {Expr ","}+ "," FormalParam ":" Expr ")" Stm
   | KId Block 
 
   // with continuation blocks (not implemented)
   | KId "(" FormalParam ":" Expr ")" Stm Rest+
   | KId "(" Expr ")" Block Rest+
   | KId Block Rest+
   // TODO: augment with all the other forms

   // switch case like
   | KId "(" FormalParam ":" Expr ")" "{" Item+ "}"
   | KId "(" Expr ")" "{" Item+ "}"
   | KId "{" Item+ "}"
   
   // decl like
   | KId FormalParam ";"
   | KId {Expr ","}+ "," FormalParam ";"
   | KId FormalParam "=" Expr ";"
   | KId {Expr ","}+ "," FormalParam "=" Expr ";"
   | FormalParam "=" KId "!" Expr ";"
   
   // new style
   | KId "(" {FormalParam ","}+ ")" Stm
   // todo: KId "(" {Expr ","}+ "," {FormalParam ","}+ ")" Stm
   ;
   
syntax Item
  = KId Expr ":" BlockStm+
  | KId FormalParam ":" BlockStm+
  ;


syntax Anno
  = "[" Type "]"
  ;

syntax VarMod
  = "recaf"
  ;

syntax FieldMod
  = "recaf"
  ;
   

syntax MethodMod
  = "recaf"
  ;   
   
syntax Modifier
  = "default"
  ;

syntax Rest
   = KId ":"  Block
   | KId ":" "(" Expr ")" Block
   | KId ":" "(" FormalParam ")" Block
   | KId ":" "(" FormalParam ":" Expr ")" Block
   ;
  


