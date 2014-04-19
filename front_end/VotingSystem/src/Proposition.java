
public class Proposition {
    public String proposition_number;
    public String question;
    public int id;
    
    public void setPropositionNumber(String s){
    	this.proposition_number = s;
    }
    
    public String getPropositionNumber(){
    	return proposition_number;
    }
    
    public void setQuestion(String s){
    	this.question = s;
    }
    
    public String getQuestion(){
    	return question;
    }
    
    public void setID(int i){
    	this.id = i;
    }
    
    public int getID(){
    	return id;
    }
    
}