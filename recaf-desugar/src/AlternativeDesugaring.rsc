module AlternativeDesugaring

import lang::recaf::Recaf;
import lang::recaf::Desugar2;
import ParseTree;
import IO;

import util::IDE;

public Tree alternativeDesugar(Tree tree) {
  if (start[CompilationUnit] cu := tree) {
        loc l = cu@\loc.top;
        l.extension = "java";
        newLoc = |project://recaf-runtime/src/test-generated/generated/<l.file>|;
        newCU = desugar(cu);
        writeFile(newLoc, newCU);
        return newCU;
   }
}