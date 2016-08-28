
<img src="/resources/recaf.png" width="150">

[![Build Status](https://travis-ci.org/cwi-swat/recaf.svg?branch=master)](https://travis-ci.org/cwi-swat/recaf) [![Join the chat at https://gitter.im/cwi-swat/recaf](https://badges.gitter.im/cwi-swat/recaf.svg)](https://gitter.im/cwi-swat/recaf?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Our paper _Recaf: Java Dialects as Libraries_ will be presented at the _15th International Conference on Generative Programming: Concepts & Experience_ ([GPCE'16](http://conf.researchr.org/home/gpce-2016)) in Amsterdam (preprint to be available soon).

_Recaf_ is an open-source framework for authoring extensions (let's call them _dialects_) as libraries for Java,. You can redine every syntactic element of the language, add new ones and create your own flavor of Java that matches your needs. It can be used to give syntactic support to libraries, to generate code, to instrument code and experiment with ideas that involve the manipulation of the semantics of Java programs. 

The key point is that recaf transforms code at compile time, applying a predefined set of rewrite rules (no need to hack around it). The user does not get involved with parsers, language workbenchs and compilers.

### Getting Started

```shell
> git clone git@github.com:cwi-swat/recaf.git
> cd recaf
> ./testgen # or testnogen to skip generation
```

### Hello World with a simple example!

Imagine we want to create our own try-with-resources statement for Java! Let's call it ```using```.
```Java
String method(String path)  {
  using (BufferedReader br : new BufferedReader(new FileReader(path))){ 
    return br.readLine();
  }
}
```

The code above is not valid Java, but it can, most certainly be through Recaf! Using two small additions, we enable the generic transformation of that snippet of code into something that we can override and define. In our case, our goal is to define what ```using``` does. So firstly, we decorate ```method``` with the ```recaf``` keyword. That enables the generic translation of our code. Secondly, we use a special field that we also decorate with the same keyword. By doing that we enable an extension for the scope of the whole compilation unit (the are other ways as well). Now every recaffeinated method uses the ```Using``` extension. As you may have guessed this object defines the behavior of our new keyword.

```Java
recaf Using<String> alg = new Using<String>();

recaf String method(String path)  {
  using (BufferedReader br : new BufferedReader(new FileReader(path))){ 
    return br.readLine();
  }
}
```

Without diving into the gory details of Recaf, the body of the method is transformed into method invocations to the ```Using``` object above (named ```alg```). Note that this is valid Java now.
```Java
String method(String path) {
  return alg.Method(
    alg.Using(() -> new BufferedReader(new FileReader(path)), (BufferedReader br) -> {
      return alg.Return(() -> br.readLine());
    }));
}	
```

### 

#### Spicing up Java (controlling the flow)

We were able to support three new syntactic constructs that manipulate the control flow of the program. We can add support for generators, async and async* operations by programming the basic denotations of each operation as described in the [Spicing up Dart with Side Effects](https://queue.acm.org/detail.cfm?id=2747873) article.

_Generators_
```Java
recaf Iterable<Integer> range(int s, int n) {
  if (n > 0) {
    yield! s;
    yieldFrom! range(s + 1, n - 1);
  }
}
```

_Async_
```Java
recaf Future<Integer> task(String url)
  await String html = fetchAsync(url);
  return html.length();
}
```

_Async*_
```Java
recaf <X> Observable<X> print(Observable<X> src) {
  awaitFor (X x: src) {
    System.out.println(x);
    yield! x;
  }
}
```

#### Parsing Expression Grammars (PEGs)

The following example demonstrates language embedding and aspect-oriented language customization. We have defined a DSL for Parsing Expression Grammars (PEGs). The ```lit!``` construct parses an atomic string, and ignores the result. ```let``` is used to bind intermediate parsing results. For terminal symbols, the ```regexp``` construct can be used. The language overloads the standard sequencing and return constructs of Java to encode sequential composition and the result of a parsing process. The constructs ```choice```, ```opt```, ```star```, and ```plus``` correspond to the usual regular EBNF operators. The ```choice``` combinator accepts a list of alternatives. The following parser implements parsing for primary expressions.

```
recaf Parser<Exp> primary() {
   choice {
      alt "value":
        regexp String n = "[0-9]+";
        return new Int(n);
      alt "bracket":
        lit! "("; let Exp e = addSub(); lit! ")";
        return e;
    }   
}
```

### Constraint solving as a language

In this example we demonstrate deep embedding of a simple constraint solving language. We have developed a Recaf embedding which translates a subset of Java expressions to the internal constraints of [Choco](http://choco-solver.org/) solver, which can then be solved. Note the use of ```recaff``` as we enable java expression overriding in this example.

```Java
recaf Solve alg = new Solve();
recaff Iterable<Map<String,Integer>> example() {
  var 0, 5, IntVar x;
  var 0, 5, IntVar y;
  solve! x + y < 5;
}
```


### Denotations using Java

We enumerate all the small extensions we developed with Recaf. The code is in plain Java and each file corresponds to one extension.

- Manipulating control flow
  - [Async](https://github.com/cwi-swat/recaf/blob/master/recaf-runtime/src/main/java/recaf/demo/cps/Async.java)
  - [Come From](https://github.com/cwi-swat/recaf/blob/master/recaf-runtime/src/main/java/recaf/demo/cps/ComeFrom.java)
  - [Backtrack](https://github.com/cwi-swat/recaf/blob/master/recaf-runtime/src/main/java/recaf/demo/cps/Backtrack.java)
  - [Coroutines](https://github.com/cwi-swat/recaf/blob/master/recaf-runtime/src/main/java/recaf/demo/cps/Coroutine.java)
  - [Yield](https://github.com/cwi-swat/recaf/blob/master/recaf-runtime/src/main/java/recaf/demo/cps/Iter.java) (Semi-coroutines)
  - [Rx/Observable](https://github.com/cwi-swat/recaf/blob/master/recaf-runtime/src/main/java/recaf/demo/cps/StreamExt.java)
- Direct
  - [Memoization](https://github.com/cwi-swat/recaf/blob/master/recaf-runtime/src/main/java/recaf/demo/direct/Memo.java)
  - [Security](https://github.com/cwi-swat/recaf/blob/master/recaf-runtime/src/main/java/recaf/demo/direct/Security.java)
  - [Constraints](https://github.com/cwi-swat/recaf/blob/master/recaf-runtime/src/main/java/recaf/demo/constraint/Solve.java)
- Fully Generic 
  - [Times/Unless/Until](https://github.com/cwi-swat/recaf/tree/master/recaf-runtime/src/main/java/recaf/demo/generic)

### Team
- Aggelos Biboudis [@biboudis](https://twitter.com/biboudis)
- Pablo Inostroza [@metalinguist](https://twitter.com/metalinguist)
- Tijs van der Storm [@tvdstorm](https://twitter.com/tvdstorm)

### Powered by Rascal 
Under the hood we use the [Rascal Metaprogramming Language](http://www.rascal-mpl.org/). It is included as a runtime dependency in the project. 


