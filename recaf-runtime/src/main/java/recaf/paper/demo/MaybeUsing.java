package recaf.paper.demo;

import recaf.paper.methods.ToOptional;

//BEGIN_MAYBE_USING
interface MaybeUsing<R> 
  extends Maybe<R>, ToOptional<R>, Using<R> { }
//END_MAYBE_USING

class Test {
	
	public static void main(String[] args) {
		MaybeUsing<String> x = new MaybeUsing<String>() {
		};
	}
}
