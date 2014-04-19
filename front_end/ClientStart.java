/*
IMPORTANT!!!

1) I AM ASSUMING YOU HAVN'T DOWNLOADED THE JYTHON JAR FILES AND MADE A BUILD PATH IN ECLIPSE, 
		THAT'S WHAT THE BELOW COMPILE AND RUN COMMANDS ARE FOR.

	TO COMPILE: javac -cp jython-2.5.3.jar Example.java
	TO RUN: java -cp .:jython-2.5.3.jar Example (USE ';' INSTEAD OF ':' IN THIS LINE IF IN WINDOWS!! - GO PAO!)

2) I HAVN'T FIGURED OUT HOW TO GIVE INTERPRETER.EXEC THE PATH TO THE PYTHON FILE, SO I HAD TO PUT IT IN THE
		SAME DIRECTORY AS THE CLIENTSTART CLASS FILE.

3) THIS IS ABOUT THE EXTENT TO WHICH I KNOW ABOUT JYTHON, MOST OF THE COMMENTS BELOW ARE JUST STUFF I THOUGHT WAS STRAIGHTFORWARD.
		HAD TO READ THE DOCUMENTATION ON HOW TO PASS A PYTHON INT, THAT'S ABOUT AS MUCH AS I ACTUALLY DID, THE REST WAS STACKOVERFLOW.
*/

//NECESSARY IMPORTS
import org.python.core.PyInteger;
import org.python.core.PyString;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class ClientStart {

	public static void main(String[] args) {
		PythonInterpreter interpreter = new PythonInterpreter();	// SETS UP THE INTERPRETER
		interpreter.exec("from hello import encrypt_RSA");									// FROM TEST.PY WE ARE IMPORTING THE DEF TMP
		PyObject someFunc = interpreter.get("encrypt_RSA");
		PyObject result = someFunc.__call__(new PyString("./"), new PyString("hello world"));
		// SIMPLY CONVERT RESULT FROM PYTHON TO JAVA, SINCE WE EXPECT AN INT, WE USE INTEGER.CLASS
		String realResult = (String) result.__tojava__(String.class);
		System.out.println(realResult);
	}

}
