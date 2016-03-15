module lang::recaf::Recaf

extend lang::java::Java8;

syntax KId
  = @category="MetaKeyword" Id
  ; 


syntax Expr
  = "#" KId "(" { Expr ","}*  ")"
  | "#" KId Block
  ;

syntax Stm
   // return -like
   = KId "!" Expr ";" // ! is needed, otherwise amb with local var dec.
   | KId "!" ";" // ugly, but hey
   
   // for, while and try like, and combinations
   | KId "(" FormalParam ":" Expr ")" Stm
   // todo, fix this, it can be stm!empty
   | KId "(" {Expr ","}+ ")" Block // block otherwise amb with method call and empty
   | KId "(" {Expr ","}+ "," FormalParam ":" Expr ")" Stm
   | KId Stm!exprStm!emptyBlock 
 
   // with continuation blocks (not implemented)
   | KId "(" FormalParam ":" Expr ")" Stm Rest+
   | KId "(" Expr ")" Block Rest+
   | KId Block Rest+
   // TODO: augment with all the other forms

   // switch case like
   | KId "(" FormalParam ":" Expr ")" "{" Item+ "}"
   | KId "(" {Expr ","}+ ")" "{" Item+ "}"
   | KId "{" Item+ "}"
   
   // decl like
   | KId FormalParam ";"
   | KId FormalParam "=" "(" Expr ")" Block
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
  = "recaf" !>> "f"
  | "recaff"
  ;

syntax FieldMod
  = "recaf"
  ;
   

syntax MethodMod
  = "recaf" !>> "f"
  | "recaff"
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
  


