import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ballot {
	
	private String state;
	@JsonProperty("vid_hash")
	private String vid_hash;
	private List<Proposition> proposition;
	private List<PresidentialCandidates> presidential_candidates;
	
	public void setState(String s){
		state = s;
	}
	
	public String getState(){
		return state;
	}
	
	@JsonProperty("vid_hash")
	public void setVidHash(String vid){
		vid_hash = vid;
	}
	
	@JsonProperty("vid_hash")
	public String getVidHash(){
		return vid_hash;
	}
	
	public void setProposition(List<Proposition> props){
		this.proposition = props;
	}
	
	public List<Proposition> getProposition(){
		return proposition;
	}
	
	@JsonProperty("presidential_candidates")
	public void setPresidentialCandidate(List<PresidentialCandidates> pres){
		this.presidential_candidates = pres;
	}
	
	@JsonProperty("presidential_candidates")
	public List<PresidentialCandidates> getPresidentialCandidates(){
		return presidential_candidates;
	}
	
}
