package Import;

import Classes.*;
import Collections.Lists.LinkedUnorderedList;
import Enumerations.Items;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class ImportJSON {

    private String filepath;

    public ImportJSON(String filepath) {
        this.filepath = filepath;
    }

    public Map<Division> importBuilding() {
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

            //Enemies
            importEnemy(map);

            //Itens
            importItems(map);

            return map;
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void importEnemy(Map<Division> map) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader fileReader = new FileReader(filepath)){
            JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);

            JSONArray enemiesArray = (JSONArray) jsonObject.get("inimigos");

            for (int i = 0; i < enemiesArray.size(); i++) {
                JSONObject toObject = (JSONObject) enemiesArray.get(i);

                String name = (String) toObject.get("nome");
                long power = (long) toObject.get("poder");
                String divisionName = (String) toObject.get("divisao");

                Division division = getOrCreateDivision(map, divisionName);

                Enemy enemy = new Enemy( (int) power, name);
                division.addEnemy(enemy);
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public Goal importGoal() {
        JSONParser jsonParser = new JSONParser();

        try (FileReader fileReader = new FileReader(filepath)){
            JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);

            JSONObject GoalArray = (JSONObject) jsonObject.get("alvo");
            String tipo = (String) GoalArray.get("tipo");
            String divisionName = (String) GoalArray.get("divisao");
            Division division = new Division(divisionName);

            Goal goal = new Goal(division, tipo);
            return goal;
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void  importItems(Map<Division> map) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader fileReader = new FileReader(filepath)){
            JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);

            JSONArray itemsArray = (JSONArray) jsonObject.get("itens");
            LinkedUnorderedList<Item> items = new LinkedUnorderedList<>();

            for (int i = 0; i < itemsArray.size(); i++) {
                JSONObject toObject = (JSONObject) itemsArray.get(i);

                Items it = null;
                long points = 0;
                switch ((String) toObject.get("tipo")) {
                    case "kit de vida":
                        it = Items.KIT_VIDA;
                        points = (long) toObject.get("pontos-recuperados");
                        break;
                    default:
                        it = Items.COLETE;
                        points = (long) toObject.get("pontos-extra");

                }
                String divisionName = (String) toObject.get("divisao");
                Division division = getOrCreateDivision(map, divisionName);

                Item item = new Item(it, (int) points);
                division.addItem(item);
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public LinkedUnorderedList<Division> importInAndOut() {
        JSONParser jsonParser = new JSONParser();

        try (FileReader fileReader = new FileReader(filepath)) {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);

            LinkedUnorderedList<Division> inAndOut = new LinkedUnorderedList<>();
            JSONArray entrada_saida = (JSONArray) jsonObject.get("entradas-saidas");

            for (int i = 0; i < entrada_saida.size(); i++) {
                String divisionName = (String) entrada_saida.get(i);
                Division division = new Division(divisionName);
                inAndOut.addToFront(division);
            }

            return inAndOut;
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Division findDivisionByName(Map<Division> map, String name) {
        for (int i = 0; i < map.getNumVertices(); i++) {
            if (map.getVertex(i).getName().equals(name)) {
                return map.getVertex(i);
            }
        }
        return null;
    }

    private Division getOrCreateDivision(Map<Division> map, String divisionName){
        for (int i = 0; i < map.getNumVertices(); i++) {
            if (map.getVertex(i).getName().equalsIgnoreCase(divisionName)) {
                return map.getVertex(i);
            }
        }
        Division newDivision = new Division(divisionName);
        map.addVertex(newDivision);
        return newDivision;
    }
}
