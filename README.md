## recaf
Recaffeinating Java

### Ideas
* Rx
* Push/Pull
* Amp
* Async
* Parallel
* Iterator
* Cloud

### Papers

* Dart has an interesting approach with multiple continuation types (statements, expressions, exceptions) [link](http://queue.acm.org/detail.cfm?id=2747873)
* F# Computation Zoo: await in expressions means that we could write int ```i = 1 + await (async { some computation }) + 3```. F# uses continuation monad for asynchronous workflows. [link](http://research.microsoft.com/pubs/217375/computation-zoo.pdf)
* Yield: Mainstream Delimited Continuations: [link](http://parametricity.net/dropbox/yield.subc.pdf)
* Delimited control in OCaml/delimcc: [link](http://www.groundwater.com.au/media/W1siZiIsIjIwMTIvMDgvMjEvMTZfNTVfMjJfNTg5X2NhbWxfc2hpZnQucGRmIl1d/caml-shift.pdf), continue that ?: [link](https://github.com/biboudis/jdelimcc)
* Scala-Virtualized: scala-virtualized follows tagless interpreters approach (thus object algebras), not Filinski’s [link](http://infoscience.epfl.ch/record/197945/files/hosc2013.pdf)
* Representing Monads: seems that every monad boils down to the continuation monad
* Encoding monadic computations in C# using iterators: [link](http://ceur-ws.org/Vol-584/paper9.pdf)
* scala supports reset/shift: [link](http://infoscience.epfl.ch/record/149136/files/icfp113-rompf.pdf)
