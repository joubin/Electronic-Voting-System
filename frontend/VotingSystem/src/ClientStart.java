import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class ClientStart {

	public static void main(String[] args) {
		Login lg = new Login();
		JSONObject finalBallot = new JSONObject();
		JSONArray jsa = new JSONArray();
		for(int i = 0; i < 10; i++){
			JSONObject a = new JSONObject();
			a.put("id", ""+i);
			a.put("question", "test question stuff number"+i);
			a.put("answer", "yes");
			jsa.add(a);
		}
		
		finalBallot.put("state", "ballot_response");
		finalBallot.put("proposition",  jsa);
		System.out.println(finalBallot.toJSONString());
		
		JSONObject data = new JSONObject();
		data.put("vid_hash", "asdkfjwfj32oifjr0jf1;2j3fweijfaisjdfasdjfawefjaweajf");
		data.put("data", finalBallot);
		System.out.println(data.toJSONString());
	}

}
