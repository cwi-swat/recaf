package recaf.demo.direct;

public class StudentPolicy implements Policy{

	@Override
	public boolean check(int op, Object obj, String name) {
		if (op == Policy.READ){
			if (obj instanceof Student){
				return ((Student) obj).grade != "F";
			}
		}
		else if (op == Policy.UPDATE){
			if (obj instanceof Student){
				return ((Student) obj).section == 1;
			}
		}
		return true;
	}

}
