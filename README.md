
## Recaf
[![Gitter](https://badges.gitter.im/cwi-swat/recaf.svg)](https://gitter.im/cwi-swat/recaf?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

_Recaf_ is an open-source framework for authoring extensions for Java. It transforms code at compile time with a transparent set of rules and defines a basic set of runtime classes that allows the creation of extensions.

### Hello World!

Imagine we want to create our own try-with-resources statement for Java!
```Java
String method(String path)  {
  using (BufferedReader br : new BufferedReader(new FileReader(path))){ 
    return br.readLine();
  }
}
```

The code above is not valid Java, but it can, most certainly, be through Recaf! Using two small additions we enable the generic transformation of that snippet of code into something that we can override and define. In our case, our goal is to define what
```using``` does. So first we decorate ```method``` with the ```recaf``` keyword. That enables the generic translation of our code. Secondly we use a special field that we also decorate with the same keyword. By doing that we enable an extension for the scope of the whole compilation unit (the are other ways as well).
```Java
recaf Using<String> alg = new Using<String>();

recaf String method(String path)  {
  using (BufferedReader br : new BufferedReader(new FileReader(path))){ 
    return br.readLine();
  }
}
```

The code is transformed as method invocations to the ```Using``` object above (named ```alg```) and it is valid Java now.
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
