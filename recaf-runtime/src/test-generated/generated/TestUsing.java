package generated;

import recaf.core.direct.IExec;
import recaf.demo.direct.Using;
import java.io.BufferedReader;
import java.io.FileReader;

public class TestUsing {

	private static Using<String, IExec> alg = new Using<String, IExec>() {
	};

	static String usingExample(String path) {
		recaf.core.Ref<String> $path = new recaf.core.Ref<String>(path);
		return (String) alg
				.Method(alg.Using(() -> new BufferedReader(new FileReader($path.value)), (BufferedReader br) -> {
					return alg.Return(() -> br.readLine());	
				}));
	}

	public static void main(String args[]) {
		System.out.println(new TestUsing().usingExample("src/test/resources/test"));
	}
}