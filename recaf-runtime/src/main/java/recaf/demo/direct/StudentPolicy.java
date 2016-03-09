package recaf.demo.direct;

public class StudentPolicy implements Policy{

	@Override
	public boolean check(SecurityOperation op, Object obj, String name) {
		if (op.equals(SecurityOperation.READ)){
			if (obj instanceof Student){
				Student s = (Student) obj;
				return (s.grade != "F");
			}
			return true;
		}
		else return true;
	}

}
