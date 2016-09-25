
<img src="/resources/recaf.png" width="100">

[![Build Status](https://travis-ci.org/cwi-swat/recaf.svg?branch=master)](https://travis-ci.org/cwi-swat/recaf) [![Join the chat at https://gitter.im/cwi-swat/recaf](https://badges.gitter.im/cwi-swat/recaf.svg)](https://gitter.im/cwi-swat/recaf?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

### What is Recaf?

_Recaf_ is an open-source framework for authoring extensions (_dialects_) as libraries for Java. You can redefine every major syntactic element of the language, either add new ones or create your own flavor of Java that matches your needs. It can be used to give syntactic support to libraries, to generate and instrument code. Last but not least you can experiment with the design and implementation of Java extensions in plain Java.

The key point is that Recaf transforms code at compile time, applying a predefined set of rewrite rules (no need to hack around it or even to know anything about it). The user does not get involved with parsers, language workbenchs and compilers. The code is transformed into method calls of a certain interface. The runtime instance of that structure defines the whole operational behavior of the program.

For example, the statement ```return 1 + 1``` is transformed in the following nested method calls chain: ```alg.Return(alg.Plus(alg.Lit(1), alg.Lit(1)))```.  What happens in reality depends on the implemention of the runtime instance of ```alg```. With Recaf we are able to override not only expressions but also the control flow of the program and orchestrate it with libraries for a certain style of execution e.g., for asynchronous computing, reactive computing. 

> Recaf: Java Dialects as Libraries ([pdf](https://biboudis.github.io/papers/recaf-gpce16.pdf)) will be presented at the _15th International Conference on Generative Programming: Concepts & Experience_ ([GPCE'16](http://conf.researchr.org/home/gpce-2016)) in Amsterdam.

### Getting Started

```shell
> git clone git@github.com:cwi-swat/recaf.git
> cd recaf
> ./testnogen # or testgen to regenerate all test files
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

## Language extension gymnastics with Recaf
In the following section we present some extensions of Java developed with _Recaf_. For demonstration purposes you can browse all extensions in the [directory with the recaf files](recaf-desugar/input) and examine the [generated java code](recaf-runtime/src/test-generated/generated).

### Spicing up Java (controlling the flow)

We support three new syntactic constructs that manipulate the control flow of the program. We can add support for generators, async and async* operations by programming the basic denotations of each operation as described in the [Spicing up Dart with Side Effects](https://queue.acm.org/detail.cfm?id=2747873) article.

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
recaf Future<Integer> task(String url) {
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

```Java
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

### Program your UI with a Java-Swul like embedding
We have implemented [SWUL](http://strategoxt.org/Stratego/Java-Swul) as an extension of the direct style implementation of Java, without virtualized expressions. For example part of the following demonstration UI can be generated by the snippet that follows:

<img src="/resources/SWUL.png" width="100">

```Java
recaf JPanel example1() {
  panel { 
   label text! "Welcome!";
   panel border {
      center label text! "Hello world!";
      south panel grid {
          row 2:  
            button {
              text! "cancel";
              action { System.out.println("Cancel"); }
            }
            button text! "ok";
        }
   }
  }
}
```

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
  - [SWING userinterface language](https://github.com/cwi-swat/recaf/blob/master/recaf-runtime/src/main/java/recaf/demo/swul/SWUL.java)
- Fully Generic 
  - [Times/Unless/Until](https://github.com/cwi-swat/recaf/tree/master/recaf-runtime/src/main/java/recaf/demo/generic)

### Bugs and Feedback

To discuss bugs, improvements and post questions please use our [Github Issues](https://github.com/cwi-swat/recaf/issues). Also, join the chat at https://gitter.im/cwi-swat/recaf! 

### Team
- Aggelos Biboudis [@biboudis](https://twitter.com/biboudis)
- Pablo Inostroza [@metalinguist](https://twitter.com/metalinguist)
- Tijs van der Storm [@tvdstorm](https://twitter.com/tvdstorm)

### Powered by Rascal 
Under the hood we use the [Rascal Metaprogramming Language](http://www.rascal-mpl.org/). It is included as a runtime dependency in the project. 


