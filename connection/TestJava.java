import org.python.util.PythonInterpreter;
import org.python.core.*;

public class SimpleEmbedded {
	public static void main(Stirng []args) throws PyException
	{
		PythonInterpreter interpreter = new PythonInterpreter();

		interpreter.exec("from client import connect");
		PyObject someFunc = interpreter.get("connect");
		System.out.println(someFunc);
		//PyObject result = someFunc.__call__();
		//String realResult = (String) result.__tojava__(String.class);
		//System.out.println(realResult);

//PythonInterpreter interpreter = new PythonInterpreter();
interpreter.exec("from client import set");
PyObject someFunc = interpreter.get("set");
System.out.println(someFunc);
PyObject result = someFunc.__call__();
String realResult = (String) result.__tojava__(String.class);
System.out.println(realResult);

//PythonInterpreter interpreter = new PythonInterpreter();
interpreter.exec("from client import get");
PyObject someFunc = interpreter.get("get");
System.out.println(someFunc);
PyObject result = someFunc.__call__();
String realResult = (String) result.__tojava__(String.class);
System.out.println(realResult);