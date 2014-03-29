import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JParser {
	
	public static class Proposition {
        public String proposition_number;
        public String question;
        public int id;
    }
	
	String testObjects;
	
    public JParser() {
        //JParser read = new JParser();
        //readJSONFile();
    	
    	try {
    		BufferedReader fileReader = new BufferedReader(new FileReader("./test.json"));
    		ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(fileReader);
			String state = node.path("state").textValue();
			String vid = node.path("vid_hash").textValue();
			//String prop = node.path("proposition").textValue();
			System.out.printf("%s\n%s\n", state, vid);
			
			JsonNode propNode = node.get("proposition");
			TypeReference<List<Proposition>> typeRef = new TypeReference<List<Proposition>>(){};
	        List<Proposition> list = mapper.readValue(propNode.traverse(), typeRef);
	        for (Proposition f : list) {
	            System.out.printf("%s\n%s\n%d\n", f.proposition_number, f.question, f.id);
	        }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
}
