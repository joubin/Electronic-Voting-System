package Main;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by joubin on 4/16/14.
 */
public class DictionaryPlay {

    public static void main(String args[]) throws NoSuchAlgorithmException {
//        JSONObject myJson = new JSONObject();
//
//            myJson.put("state", "register");
//            myJson.put("vid_hash", "mayhash");
//        JSONObject userInfo = new JSONObject();
//            userInfo.put("vid", "user_id");
//            userInfo.put("ssn", "123-12-2345");
//            userInfo.put("pin", "1234");
//            myJson.put("userInfo", userInfo);
//
//            System.out.println(myJson.toJSONString());
//
//        String send = myJson.toJSONString();
//
//        try {
//            JSONObject myNewString =   (JSONObject)new JSONParser().parse(send);
//            System.out.print(myNewString.get("vid_hash"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update("hello".getBytes());
        digest.update("asd".getBytes());
        digest.update("qwe".getBytes());
        char[] shareKey = Hex.encodeHex(digest.digest());
        System.out.println(shareKey);
    }
}