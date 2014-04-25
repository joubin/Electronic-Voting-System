package Server;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by joubin on 4/16/14.
 */
public class EncryptDB {

    public static void main(String args[]) throws Exception {

            DB_handler db = new DB_handler();
            VSCrypt toolkit = new VSCrypt();
            String sql = "select * from voters_of_america;";
            ResultSet rs = db.getResult(sql);
            String key = "1234";
            ArrayList<String> todos = new ArrayList<String>();
            while (rs.next()){
                String vid_hash = rs.getString("VID_HASH");
                String vid = new String(toolkit.encrypt(key.getBytes(), rs.getString("VID").toString()));
                String ssn = new String(toolkit.encrypt(key.getBytes(),  rs.getString("ssn").toString()));
                String full_name = new String(toolkit.encrypt(key.getBytes(),  rs.getString("full_name").toString()));
                String address = new String(toolkit.encrypt(key.getBytes(),  rs.getString("address").toString()));
                sql = " update voters_of_america set VID = \""+vid+"\", SSN = \""+ssn+"\", full_name = \""+full_name+"\", address = \""+address+"\", allow_to_vote = 1 where VID_HASH = \""+vid_hash+"\"";
                todos.add(sql);


            }
        db.cleanup();
        db = new DB_handler();

        for(String s : todos){
            sql = s;

            boolean pass = db.setValues(sql);
            if (pass){
                System.out.println("winning");
            }else{
                System.out.println("Failing");
            }
        }
        db.cleanup();

////
    }
}