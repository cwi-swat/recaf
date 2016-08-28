package generated;

import recaf.paper.demo.ToJS;
import recaf.paper.demo.HTTPResponse;
import recaf.paper.demo.HTTPRequest;

public class TestWeb extends ToJS.Browser {
  
   ToJS  alg = new ToJS();

  //BEGIN_HELLOWORLD_WEB  
    HTTPResponse  helloWorld(HTTPRequest req) {
  recaf.core.Ref<HTTPRequest > $req = new recaf.core.Ref<HTTPRequest >(req);
  return (HTTPResponse )alg.Method(alg.ExpStat(alg.Invoke(alg.Var("document", document), "write", alg.Plus(alg.Plus(alg.Lit("<h1>Hello world "), alg.Invoke(alg.Field(alg.Ref("$req", $req), "params"), "get", alg.Lit("name"))), alg.Lit("</h1>")))));
}
  //END_HELLOWORLD_WEB
  
  public static void main(String args[]) {
     HTTPResponse r = new TestWeb().helloWorld(new HTTPRequest());
     System.out.println(r.getJs());
  }

}