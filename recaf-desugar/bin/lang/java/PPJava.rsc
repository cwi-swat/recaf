module lang::java::PPJava

import lang::java::ExtJava;
import ParseTree;
import IO;

start[CompilationUnit] ppjava(start[CompilationUnit] cu) {

  Expr indent(Expr parent, Expr e) {
     return indent(parent@\loc.begin.column, e);
  }

  return visit (cu) {
   case t:(Expr)`(<{FormalParam ","}* fs>) -\> <Expr e>`
     => indent(t, (Expr)`(<{FormalParam ","}* fs>) -\> 
              '  <Expr e>`)

   case t:(Expr)`(<{FormalParam ","}* fs>) -\> {<BlockStm* stms>}`
     => indent(t, (Expr)`(<{FormalParam ","}* fs>) -\> {
              '  <BlockStm* stms>
              '}`)

   case t:(Expr)`(<{Id ","}+ fs>) -\> <Expr e>`
     => indent(t, (Expr)`(<{Id ","}+ fs>) -\> 
              '  <Expr e>`)

   case t:(Expr)`(<{Id ","}+ fs>) -\> {<BlockStm* stms>}`
     => indent(t, (Expr)`(<{Id ","}+ fs>) -\> {
              '  <BlockStm* stms>
              '}`)
   
   case t:(Expr)`<Id x> -\> <Expr e>`
     => indent(t, (Expr)`<Id x> -\> 
              '  <Expr e>`)

   case t:(Expr)`<Id x> -\> {<BlockStm* stms>}`
     => indent(t, (Expr)`<Id x> -\> {
              '  <BlockStm* stms>
              '}`)
   
  }
 
}

/*
[appl(
          regular(\iter-star(lex("LAYOUT"))),
          [appl(
              prod(
                lex("LAYOUT"),
                [\char-class([
                      range(9,10),
                      range(12,13),
                      range(32,32)
                    ])],
                {}),
              [char(10)])[
              @loc=|prompt:///|(12,1)
            ],appl(
              prod(
                lex("LAYOUT"),
                [\char-class([
                      range(9,10),
                      range(12,13),
                      range(32,32)
                    ])],
                {}),
              [char(32)])[
              @loc=|prompt:///|(14,1)
            ]])[
          @loc=|prompt:///|(12,2)
        ]])[
      @loc=|prompt:///|(12,2)
    ],
    
    */
    
&T<:Tree indent(int n, (&T<:Tree) tree) {
  return visit (tree) {
    case t:appl(regular(\iter-star(lex("LAYOUT"))), args): {
      println("Found layout: <t>");
      newArgs = [];
      for (a <- args) {
        newArgs += [a];
        if (appl(p, [char(10)]) := a) {
          println("found newline: <p>");
          newArgs += [ appl(p, [char(32)])[@\loc=a@\loc] | i <- [0..n] ];
        }
      }
      r = appl(regular(\iter-star(lex("LAYOUT"))), newArgs)[@\loc=t@\loc];
      println("New tree: `<r>`");
      println("size <size(r.args)>");
      println("size <size(newArgs)>");
      insert r;
    }
  }
}
