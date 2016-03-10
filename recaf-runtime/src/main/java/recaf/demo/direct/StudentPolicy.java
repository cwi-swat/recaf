package recaf.demo.direct;

public class StudentPolicy implements Policy{

	@Override
	public boolean check(SecurityOperation op, Object obj, String name) {
		if (op.equals(SecurityOperation.READ)){
			if (obj instanceof Student){
				return ((Student) obj).grade != "F";
			}
		}
		else if (op.equals(SecurityOperation.UPDATE)){
			if (obj instanceof Student){
				return ((Student) obj).section == 1;
			}
		}
		return true;
	}

}
