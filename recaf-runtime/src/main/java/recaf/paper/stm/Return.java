package recaf.paper.stm;

public class Return extends RuntimeException { 
	public final Object value;
	public Return(Object value) { this.value = value; }
}

