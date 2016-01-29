package recaf.tests;

import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.StandardTextWriter;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.shell.ShellEvaluatorFactory;

public class RascalModuleRunner{

  private final Evaluator eval;

  public RascalModuleRunner(PrintWriter stdout, PrintWriter stderr) {
    eval = ShellEvaluatorFactory.getDefaultEvaluator(stdout, stderr);
  }

  public void addRascalSearchPathContributor(ISourceLocation loc){
	  eval.addRascalSearchPath(loc);
  }
  
  public void run(String module, String[] args) throws IOException {
    if (module.endsWith(".rsc")) {
      module = module.substring(0, module.length() - 4);
    }
    module = module.replaceAll("/", "::");

    eval.doImport(null, module);
    //eval.eval("")
    IValue v = eval.main(null, module, "main", args);

    if (v != null) {
      new StandardTextWriter(true).write(v, eval.getStdOut());
      eval.getStdOut().flush();
    }

    return;
  }

}