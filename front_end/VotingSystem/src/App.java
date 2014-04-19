import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App {

    public static class Foo {
        public int foo;
        public int foo2;
    }
    public static class Proposition {
        public String proposition_number;
        public String question;
        public int id;
    }

    public static void main(String[] args) {

        String json = "{\"someArray\":[{\"foo\":5,\"foo2\":1},{\"foo\":6,\"foo2\":2},{\"foo\":7,\"foo2\":3}]}";
        String json2 = "{\"proposition\":[{\"proposition_number\": \"prop5\",\"question\": \"move to legalaize marry  j\",\"id\": 51},{\"proposition_number\": \"prop6\", \"question\": \"move to legalaize monkey bars \",\"id\": 52},{\"proposition_number\": \"prop57\",\"question\": \"move to legalaize michael jackson \",\"id\": 53}]}";
        try {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json2);
        node = node.get("proposition");
        TypeReference<List<Proposition>> typeRef = new TypeReference<List<Proposition>>(){};
        List<Proposition> list = mapper.readValue(node.traverse(), typeRef);
        for (Proposition f : list) {
            System.out.printf("%s, %s, %d\n", f.proposition_number, f.question, f.id);
        }
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}