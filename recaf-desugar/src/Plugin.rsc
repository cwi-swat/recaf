module Plugin

import lang::recaf::Recaf;
import lang::recaf::Desugar;
import lang::recaf::TypeCheck;
import ParseTree;

import IO;
import Message;
import util::IDE;

private str LANG_NAME = "Java Recaffeinated";

void main() {
  registerLanguage(LANG_NAME, "recaf", start[CompilationUnit](str src, loc org) {
    return parse(#start[CompilationUnit], src, org);
  });
  
  registerContributions(LANG_NAME, {
    //annotator(Tree (Tree tree) {
    //  if (start[CompilationUnit] cu := tree) {
    //    return tree[@messages=typeCheck(cu)];
    //  }
    //  return tree[@messages={error("Not a <LANG_NAME> program", tree@\loc)}];
    //}),
    builder(set[Message] (Tree tree) {
      if (start[CompilationUnit] cu := tree) {
        loc l = cu@\loc.top;
        l.extension = "java";
        newLoc = |project://recaf-runtime/src/test-generated/generated/<l.file>|;
        newCU = desugar(cu);
        writeFile(newLoc, newCU);
        return typeCheck(newCU);
      }
      return {error("Not a <LANG_NAME> program", tree@\loc)};
    })
  });
}