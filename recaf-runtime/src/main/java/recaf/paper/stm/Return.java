package recaf.paper.stm;

public class Return extends RuntimeException { 
	public final Object value;
	Return(Object value) { this.value = value; }
}

