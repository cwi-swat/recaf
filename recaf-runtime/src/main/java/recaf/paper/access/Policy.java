package recaf.paper.access;

//BEGIN_POLICY
public interface Policy {
	boolean checkRead(Object obj, String name);
}
//END_POLICY