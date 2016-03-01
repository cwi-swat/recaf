package recaf.demo.pegs;

public interface Parser<T> {
	Result<T> parse(String src, int pos);
}
