
import recaf.gui.GUIExtension;
import recaf.gui.RenderGUI;
import recaf.gui.HandleGUI;
import java.util.stream.IntStream;
import java.util.Iterator;

public class TestGUI {

	static Void println(Object o) {
		System.out.println(o);
		return null;
	}

	Void h2(GUIExtension alg, String title) {
		return (Void) alg.Method(alg.Tag(alg.Exp(() -> {
			return "h2";
		}), alg.Echo(alg.Exp(() -> {
			return title;
		}))));
	}

	Iterable<Integer> range(int n) {
		return new Iterable<Integer>() {
			public Iterator<Integer> iterator() {
				return IntStream.range(0, n).iterator();
			}
		};
	}

	Void theGUI(GUIExtension alg, int n) {
		return (Void) alg.Method(alg.Seq(alg.ExpStat(alg.Exp(() -> {
			return h2(alg, "Demo");
		})), alg.Tag(alg.Exp(() -> {
			return "div";
		}), alg.For(alg.Exp(() -> {
			return range(n);
		}), (Integer i) -> {
			return alg.Tag(alg.Exp(() -> {
				return "p";
			}), alg.Seq(alg.Echo(alg.Exp(() -> {
				return "Hello & goodbye " + i + "!\n";
			})), alg.Button(alg.Exp(() -> {
				return "Click " + i;
			}), alg.If(alg.Exp(() -> {
				return i % 2 == 0;
			}), alg.ExpStat(alg.Exp(() -> {
				return println("clicked even button " + i);
			})), alg.ExpStat(alg.Exp(() -> {
				return println("clicked odd button " + i);
			}))))));
		}))));
	}

	public static void main(String args[]) {
		RenderGUI alg = new RenderGUI();
		TestGUI tg = new TestGUI();
		tg.theGUI(alg, 10);
		String html = alg.getHTML();
		System.out.println(html);
		HandleGUI handle = new HandleGUI("id2");
		tg.theGUI(handle, 10);
	}

}