package recaf.demo.direct;

public interface Policy {
	boolean check(SecurityOperation op, Object obj, String name);
}
