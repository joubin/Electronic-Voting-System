import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class ClientStart {

	public static void main(String[] args) {
		//Login lg = new Login();
		//VotingBallot vb = new VotingBallot();
		PythonInterpreter interpreter = new PythonInterpreter();
		interpreter.exec("from test import tmp");
		PyObject someFunc = interpreter.get("tmp");
		//System.out.println(someFunc);
		PyObject result = someFunc.__call__(new PyInteger(5), new PyInteger(7));
		int realResult = (int) result.__tojava__(Integer.class);
		System.out.println(realResult);
	}

}
