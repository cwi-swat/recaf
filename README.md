[![Build Status](https://travis-ci.com/cwi-swat/recaf.svg?token=EYsxboxiFVSqpFARwkTX&branch=master)](https://travis-ci.com/cwi-swat/recaf)

## recaf
Recaffeinating Java

### notes

Currently the first test that is executed generates the files. This is obviously wrong, maven should handle this. 
An artifact of that is that we cannot execute benchmarks as part of the build process. The quick and dirty way is
to move the generated package to the recaf-runtie project and then:
```shell
> mvn clean install
> java -jar target/benchmarks.jar -i 5 -wi 5 -f 2 .*
```
