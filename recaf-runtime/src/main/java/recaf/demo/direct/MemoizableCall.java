package recaf.demo.direct;

import java.util.Arrays;
import java.util.stream.Stream;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class MemoizableCall {
	private Object receiver;
	private String methodName;
	private Object[] evaluatedArguments;
	
	public MemoizableCall(Object receiver, String methodName, Object[] evaluatedArguments) {
		super();
		this.receiver = receiver;
		this.methodName = methodName;
		this.evaluatedArguments = evaluatedArguments;
	}
	
	public Object getReceiver() {
		return receiver;
	}
	public String getMethodName() {
		return methodName;
	}
	public Object[] getEvaluatedArguments() {
		return evaluatedArguments;
	}
	
	@Override
	public String toString(){
		Stream<String> as =  Arrays.asList(evaluatedArguments).stream().map(x -> x.toString());
		String args = String.join(", ", (Iterable<String>) as::iterator);
		return receiver+"."+methodName+"("+args+")";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MemoizableCall))
            return false;
        if (obj == this)
            return true;

        MemoizableCall rhs = (MemoizableCall) obj;
        return new EqualsBuilder().
            // if deriving: appendSuper(super.equals(obj)).
            append(receiver, rhs.receiver).
            append(methodName, rhs.methodName).
            append(evaluatedArguments, rhs.evaluatedArguments).
            isEquals();
	}
	
	@Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            append(receiver).
            append(methodName).
            append(evaluatedArguments).
            toHashCode();
    }
	
}
