module lang::recaf::DesugarFullMain

import lang::recaf::Recaf;
import lang::recaf::DesugarFull;
import ParseTree;
import IO;
import String;

void main(list[str] args) {
    str root = args[0];
    str tgt = args[1];
  	
    loc rootDir = toLocation(root);
    for (loc src <- rootDir.ls){
    	str content = readFile(src);
    	Tree tree = parse(#start[CompilationUnit], content);
		if (start[CompilationUnit] cu := tree) {
        	println(src);
        	newLoc = toLocation(tgt+"/<src[extension = "java"].file>");
        	writeFile(newLoc, desugar(cu));
      	}
    }
}