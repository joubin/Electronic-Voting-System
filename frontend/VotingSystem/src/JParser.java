import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JParser {
	private Ballot ballot;
	
    public JParser() {
        //JParser read = new JParser();
        //readJSONFile();
    	
    	try {
    		BufferedReader fileReader = new BufferedReader(new FileReader("./sample.json"));
    		ObjectMapper mapper = new ObjectMapper();
    		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    		ballot = mapper.readValue(fileReader, Ballot.class);
    		System.out.println(ballot.getState());
    		System.out.println(ballot.getVidHash());
    		for (Proposition prop : ballot.getProposition()) {
    		  System.out.printf("%s\t%s\t%d\n", prop.getPropositionNumber(), prop.getQuestion(), prop.getID());
    		}
    		for (PresidentialCandidates pres : ballot.getPresidentialCandidates()) {
      		  System.out.printf("%s\t%s\t%d\n", pres.getPartyAffiliation(), pres.getFullName(), pres.getID());
      		}
			
    		
    		
    		/*JsonNode node = mapper.readTree(fileReader);
			String state = node.path("state").textValue();
			String vid = node.path("vid_hash").textValue();
			//String prop = node.path("proposition").textValue();
			System.out.printf("%s\n%s\n", state, vid);
			
			JsonNode propNode = node.get("proposition");
			TypeReference<List<Proposition>> typeRef = new TypeReference<List<Proposition>>(){};
	        List<Proposition> list = mapper.readValue(propNode.traverse(), typeRef);
	        for (Proposition f : list) {
	            System.out.printf("%s\n%s\n%d\n", f.proposition_number, f.question, f.id);
	        }*/
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public String getState(){
    	return ballot.getState();
    }
    
    public List<Proposition> getProposition(){
		return ballot.getProposition();
	}
    
    public List<PresidentialCandidates> getPresidentialCandidates(){
		return ballot.getPresidentialCandidates();
	}
}
