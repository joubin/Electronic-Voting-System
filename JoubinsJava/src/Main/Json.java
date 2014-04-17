package Main;

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

    public void addItem(String key, Map m)
    {
        localMap.put(key, m);
    }

    public Map<Object, Object> getLocalMap() {
        return localMap;
    }

}
