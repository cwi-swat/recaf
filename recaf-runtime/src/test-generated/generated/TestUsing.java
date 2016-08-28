package generated; 

import recaf.demo.direct.Using;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TestUsing {
 
  private static Using<String> alg = new Using<String>() {};
   
  static  String  usingExample(String path) {
  recaf.core.Ref<String > $path = new recaf.core.Ref<String >(path);
  return (String )alg.Method(alg.Using(() -> new BufferedReader(new FileReader($path.value)), (BufferedReader br) -> {return alg.Return(() -> br.readLine());}));
}
  
  public static void main(String args[])  {
     System.out.println(new TestUsing().usingExample("src/test-generated/README.md"));
  }
}