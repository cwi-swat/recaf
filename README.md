
<img src="/resources/recaf.png" width="150">

_Recaf_ is an open-source framework for authoring extensions for Java as libraries. You can redine every syntactic element of the language, add new ones and create your own flavor of Java that matches your needs. It can be used to give syntactic support to libraries, to generate code, to instrument code and experiment with ideas that involve the manipulation of the semantics of Java programs. 

The key point is that recaf transforms code at compile time, with a transparent set of rules. The user does not get involved with parsers, language workbenchs and compilers.  

[![Gitter](https://badges.gitter.im/cwi-swat/recaf.svg)](https://gitter.im/cwi-swat/recaf?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

### Hello World!

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

Without diving into the gory details of Recaf, the body of the mothod is transformed as method invocations to the ```Using``` object above (named ```alg```). Note that this is valid Java now.
```Java
String method(String path) {
  return alg.Method(
    alg.Using(() -> new BufferedReader(new FileReader(path)), (BufferedReader br) -> {
      return alg.Return(() -> br.readLine());
    }));
}	
```

### Tests

```shell
> git clone git@github.com:cwi-swat/recaf.git
> cd recaf
> ./testgen # or testnogen to skip generation
```
