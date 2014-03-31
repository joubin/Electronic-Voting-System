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
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class ClientStart {

	public static void main(String[] args) {
		PythonInterpreter interpreter = new PythonInterpreter();	// SETS UP THE INTERPRETER
		interpreter.exec("from test import tmp");									// FROM TEST.PY WE ARE IMPORTING THE DEF TMP
		PyObject someFunc = interpreter.get("tmp");								// NOW WE PUT THE FUNCTION INTO AN ACTUAL OBJECT SO WE CAN USE IT
		// WE SIMPLY CALL IT AND PLACE THE RESULT IN ANOTHER OBJECT
		// THE FUNCTION TMP REQUIRES 2 INT ARGS, SO THATS WHY WE HAVE TO MAKE 2 NEW PYTHON INTEGERS, PASSING JAVA INTS WON'T WORK
		PyObject result = someFunc.__call__(new PyInteger(5), new PyInteger(7));
		// SIMPLY CONVERT RESULT FROM PYTHON TO JAVA, SINCE WE EXPECT AN INT, WE USE INTEGER.CLASS
		int realResult = (int) result.__tojava__(Integer.class);
		System.out.println(realResult);
	}

}
