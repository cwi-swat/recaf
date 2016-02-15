module lang::recaf::TypeCheck

import lang::recaf::Recaf;
import lang::recaf::Desugar;

import Message;
import ParseTree;
import lang::java::jdt::m3::Core;
import IO;
import String;

set[Message] typeCheck(loc generatedFileLoc, list[loc] sourcePaths, start[CompilationUnit] desugaredCU, start[CompilationUnit] cu) {
  <_, ind> = posIndex(desugaredCU, desugaredCU@\loc);
  return { relocate(m, ind) | m <- createM3FromFile(generatedFileLoc, 
      sourcePath = sourcePaths, javaVersion="1.8")@messages };
}

Message relocate(error(str s, loc l), rel[loc src, loc origin] ind)
  = error(s, l2)
  when
    <loc l1, loc l2> <- ind,
    l1.offset == l.offset, l1.length == l.length; 

default Message relocate(Message m, rel[loc src, loc origin] ind) = m;

tuple[loc current, rel[loc src, loc origin] index] posIndex(Tree tree, loc current) {
   //cur = |<src.scheme>://<src.authority><src.path>|(0, 0, <1, 0>, <1,0>);
   ind = {};
   
   switch (tree) {
     case char(n): { 
       current.offset += 1;
       current.length = 1;
       return <current, ind>;
     }
     
     case t:appl(_, args): {
       if (t@\loc?) {
         current.length = t@\loc.length;
         ind += {<current, t@\loc>};
         for (Tree a <- args) {
           <current, subs> = posIndex(a, current);
           ind += subs;
         }
       }
       else {
         current.offset += size("<t>");
       }
     }
     case amb(_):
       throw "todo";
   }
   
   return <current, ind>;
   

}

