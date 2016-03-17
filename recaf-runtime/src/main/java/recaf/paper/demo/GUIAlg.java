package recaf.paper.demo;

import java.util.function.Supplier;

import recaf.paper.methods.MuJavaMethod;
import recaf.paper.stm.IExec;
import recaf.paper.stm.MuJavaBase;

//BEGIN_GUI_ALG
interface GUIAlg extends MuJavaBase<Void>, MuJavaMethod<Void, IExec> {
	IExec Tag(Supplier<String> t, IExec body);
	IExec Button(Supplier<String> label, IExec body);
	IExec Echo(Supplier<String> exp);	
}
//END_GUI_ALG