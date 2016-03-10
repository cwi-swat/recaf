package recaf.paper;

import java.util.Optional;

//BEGIN_MAYBE_USING
interface MaybeUsing<R> 
  extends Maybe<R>, ToOptional<R>, Using<Optional<R>> { }
//END_MAYBE_USING
