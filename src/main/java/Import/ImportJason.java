package Import;

import Classes.Division;
import Classes.Map;
import Exceptions.DivisionNotFound;
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

        try (FileReader fileReader = new FileReader(filepath)) {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);

            Map<Division> map = new Map<>();
            JSONArray edificioArray = (JSONArray) jsonObject.get("edificio");

            // Adiciona as divisões ao grafo
            for (int i = 0; i < edificioArray.size(); i++) {
                String divisionName = (String) edificioArray.get(i);
                Division division = new Division(divisionName);
                map.addVertex(division);  // Adiciona a divisão ao grafo
            }

            JSONArray ligacoesArray = (JSONArray) jsonObject.get("ligacoes");

            for (int i = 0; i < ligacoesArray.size(); i++) {
                JSONArray ligacao = (JSONArray) ligacoesArray.get(i);
                String origemName = (String) ligacao.get(0);
                String destinoName = (String) ligacao.get(1);

                Division origemDivision = findDivisionByName(map, origemName);
                Division destinoDivision = findDivisionByName(map, destinoName);

                    map.addEdge(origemDivision, destinoDivision);
            }

            return map;
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Division findDivisionByName(Map<Division> map, String name) throws DivisionNotFound {
        for (int i = 0; i < map.getNumVertices(); i++) {
            if (map.getVertex(i).getName().equals(name)) {
                return map.getVertex(i);
            }
        }
        throw new DivisionNotFound(name);
    }
}
