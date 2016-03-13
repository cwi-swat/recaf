package recaf.paper.access;

//BEGIN_POLICY
public interface Policy {
	boolean isAllowed(Object obj, String name);
}
//END_POLICY