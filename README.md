## Recaf
Recaf is a source transformation system for Java that liberates the programmer from fixed semantics. 

### Tests

```shell
> git clone git@github.com:cwi-swat/recaf.git
> cd recaf
> mvn -DGEN=true clean dependency:copy-dependencies test
```

In the maven command:

- ```clean``` cleans all generated files
- ```-DGEN``` is used in case you don't want to regenerate all files (Boolean)
- ```dependency:copy-dependencies``` copies all declared dependencies to a directory in order to be discovered from our programmatic invocation of the ```javac``` compiler.
- ```test``` runs all the tests
