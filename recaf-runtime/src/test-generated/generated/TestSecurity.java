package generated;

import java.util.Arrays;
import java.util.List;

import recaf.demo.direct.Student;
import recaf.demo.direct.StudentPolicy;
import recaf.demo.direct.Security;

class Printer2 {
	public Void print(Object o) {
		System.out.print(o);
		return null;
	}
}

public class TestSecurity{
	
	private static Security<Object> alg = new Security<Object>(new StudentPolicy());
	
	public static  Void  printGrades(List<Student> students) {
  recaf.core.Ref<List<Student>> $students = new recaf.core.Ref<List<Student>>(students);
  return (Void )alg.Method(alg.Decl(alg.New(Printer2.class), (recaf.core.Ref<Printer2 > p) -> {return alg.<Student >ForEach(alg.Ref("$students", $students), (recaf.core.Ref<Student > s) -> alg.Seq(alg.ExpStat(alg.Invoke(alg.Ref("p", p), "print", alg.Field(alg.Ref("s", s), "name"))), alg.Seq(alg.ExpStat(alg.Invoke(alg.Ref("p", p), "print", alg.Lit(" -> "))), alg.Seq(alg.ExpStat(alg.Invoke(alg.Ref("p", p), "print", alg.Field(alg.Ref("s", s), "grade"))), alg.ExpStat(alg.Invoke(alg.Ref("p", p), "print", alg.Lit("\n")))))));}));
}
	
	public static  Void  failStudent(Student s) {
  recaf.core.Ref<Student > $s = new recaf.core.Ref<Student >(s);
  return (Void )alg.Method(alg.ExpStat(alg.Assign(alg.Field(alg.Ref("$s", $s), "grade"), alg.Lit("F"))));
}
	
	public static void main(String[] args){
		List<Student> students =
			Arrays.asList(new Student[]{
				new Student("Alice", "A",1),
				new Student("Bob","B", 2),
				new Student("Cathy","F",1)});
		failStudent(students.get(0));
		failStudent(students.get(1));	
		printGrades(students);	
	}
}