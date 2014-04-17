package Main;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by joubin on 4/16/14.
 */
public class DictionaryPlay {

    public static void main(String args[]){

        Map<Object, Object> hm = new HashMap<Object, Object>();
        Map<Object, Object> hm2 = new HashMap<Object, Object>();

        hm.put("asd", "asd");
        hm2.put("123", "234");

        hm.put("asd2", hm2);
        Set<Map.Entry<Object, Object>> set = hm.entrySet();

        for (Map.Entry<Object, Object> me : set) {
            System.out.println(me.getKey() + " " + me.getValue());
        }
       }
}
