package recaf.paper.demo;

import java.util.function.Supplier;

import recaf.paper.stm.IExec;
import recaf.paper.stm.MuJavaBase;

//BEGIN_GUI_ALG
interface GUI extends MuJavaBase<Void> {
	IExec Tag(Supplier<String> t, IExec b);
	IExec Button(Supplier<String> l, IExec b);
}
//END_GUI_ALG