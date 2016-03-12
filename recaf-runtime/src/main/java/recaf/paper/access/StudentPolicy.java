package recaf.paper.access;

import recaf.demo.direct.Student;

//BEGIN_STUDENT_POLICY
public class StudentPolicy implements Policy {
	@Override
	public boolean checkRead(Object obj, String fldName) {
		if (obj instanceof Student)
			return ((Student) obj).grade != "F";
		return true;
	}
}
//END_STUDENT_POLICY