package Import;

import Classes.Division;
import Classes.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class ImportJason {

    public ImportJason() {
    }

    public Map<Division> importJason(String filepath) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader fileReader = new FileReader(filepath)){
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) jsonParser.parse(fileReader);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            Map<Division> map = new Map<>();
            JSONArray edificioArray =  (JSONArray) jsonObject.get("edificio");

            for (int i = 0; i < edificioArray.size(); i++) {
                String divisionName = (String) edificioArray.get(i);
                Division division = new Division(divisionName);
                map.addVertex(division);
            }

            return map;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
