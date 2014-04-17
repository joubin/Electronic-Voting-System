package Main;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by joubin on 4/16/14.
 */

public class Json {

    private Map<Object, Object> localMap = null;

    public Json()
    {
        localMap =  new HashMap<Object, Object>();
    }

    public void  setState(String s)
    {
            localMap.put("state", s);
    }


    public void setVIDHash(String hash)
    {
        localMap.put("vid_hash", hash);
    }

    public void addItem(Object key, Object value)
    {
        localMap.put(key, value.toString());
    }

    public JSONObject getLocalMap() {
        JSONObject js = new JSONObject(localMap);
        return js;

    }

}
