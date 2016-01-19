module Plugin

import lang::java::ExtJava;
import lang::java::CPSJava;
import lang::java::PPJava;
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
    builder(set[Message] (Tree tree) {
      if (start[CompilationUnit] cu := tree) {
        loc l = cu@\loc.top;
        l.extension = "java";
        newLoc = |project://compalg/src/test-src/<l.file>|;
        println(newLoc);
        writeFile(newLoc, desugar(cu));
        return {};
      }
      return {error("Not a <LANG_NAME> program", tree@\loc)};
    })
  });
}