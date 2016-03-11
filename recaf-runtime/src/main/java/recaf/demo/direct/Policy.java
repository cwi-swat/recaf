package recaf.demo.direct;

public interface Policy {
	public final int READ = 0;
	public final int UPDATE = 1;
	boolean check(int op, Object obj, String name);
}
