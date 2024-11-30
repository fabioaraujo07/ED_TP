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

    public ImportJSON() {
    }

    public Map<Division> importBuilding(String filepath) {
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

    public LinkedUnorderedList<Enemy> importEnemy(String filepath) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader fileReader = new FileReader(filepath)){
            JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);

            JSONArray enemiesArray = (JSONArray) jsonObject.get("inimigos");
            LinkedUnorderedList<Enemy> enemies = new LinkedUnorderedList<>();

            for (int i = 0; i < enemiesArray.size(); i++) {
                JSONObject toObject = (JSONObject) enemiesArray.get(i);

                String name = (String) toObject.get("nome");
                long power = (long) toObject.get("poder");
                String divisionName = (String) toObject.get("divisao");
                Division division = new Division(divisionName);

                Enemy enemy = new Enemy( (int) power, name, division);
                enemies.addToFront(enemy);
            }

            return enemies;
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public Goal importGoal(String filepath) {
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

    public LinkedUnorderedList<Item> importItems(String filepath) {
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
                Division division = new Division(divisionName);

                Item item = new Item( division, it, (int) points);
                items.addToFront(item);
            }

            return items;
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
}
