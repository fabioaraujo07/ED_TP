package Import;

import Classes.*;
import Collections.Linked.LinkedUnorderedList;
import Enumerations.Items;
import Exceptions.ImportException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class ImportJSON {

    private String filepath;
    private LinkedUnorderedList<Division> inAndOut;

    /**
     * Constructs an ImportJSON with the specified file path.
     *
     * @param filepath the path to the JSON file
     */
    public ImportJSON(String filepath) {
        this.filepath = filepath;
    }

    /**
     * Imports the building data from the JSON file.
     *
     * @return the map of divisions
     * @throws ImportException if an error occurs during import
     */
    public Map<Division> importBuilding() throws ImportException {
        JSONObject jsonObject = parseJSONFile(filepath);

        Map<Division> map = new Map<>();
        importDivisions(map, jsonObject);
        importConnections(map, jsonObject);
        importEnemy(map, jsonObject);
        importItems(map, jsonObject);
        this.inAndOut = importInAndOut(map);

        return map;
    }

    /**
     * Imports the goal data from the JSON file.
     *
     * @return the goal
     * @throws ImportException if an error occurs during import
     */
    public Goal importGoal() throws ImportException {
        JSONObject jsonObject = parseJSONFile(filepath);

        JSONObject goalObject = (JSONObject) jsonObject.get("alvo");
        if (goalObject == null) {
            throw new ImportException("JSON file is missing 'alvo' key.");
        }

        String tipo = (String) goalObject.get("tipo");
        String divisionName = (String) goalObject.get("divisao");
        Division division = new Division(divisionName);

        return new Goal(division, tipo);
    }

    /**
     * Returns the list of entrance and exit divisions.
     *
     * @return the list of entrance and exit divisions
     */
    public LinkedUnorderedList<Division> getInAndOut() {
        return this.inAndOut;
    }

    /**
     * Imports the entrance and exit divisions from the JSON file.
     *
     * @param map the map of divisions
     * @return the list of entrance and exit divisions
     * @throws ImportException if an error occurs during import
     */
    private LinkedUnorderedList<Division> importInAndOut(Map<Division> map) throws ImportException {
        JSONObject jsonObject = parseJSONFile(filepath);
        LinkedUnorderedList<Division> inAndOut = new LinkedUnorderedList<>();
        JSONArray entrada_saida = (JSONArray) jsonObject.get("entradas-saidas");
        if (entrada_saida == null) {
            throw new ImportException("JSON file is missing 'entradas-saidas' key.");
        }

        for (Object obj : entrada_saida) {
            if (!(obj instanceof String)) {
                throw new ImportException("Invalid entrance/exit format.");
            }
            String divisionName = (String) obj;
            Division division = getOrCreateDivision(map, divisionName);
            inAndOut.addToRear(division);
        }
        return inAndOut;
    }

    /**
     * Parses the JSON file.
     *
     * @param filepath the path to the JSON file
     * @return the parsed JSON object
     * @throws ImportException if an error occurs during parsing
     */
    private JSONObject parseJSONFile(String filepath) throws ImportException {
        JSONParser jsonParser = new JSONParser();
        try (FileReader fileReader = new FileReader(filepath)) {
            return (JSONObject) jsonParser.parse(fileReader);
        } catch (IOException | ParseException e) {
            throw new ImportException("Error reading or parsing JSON file: " + e.getMessage());
        }
    }

    /**
     * Imports the divisions from the JSON file.
     *
     * @param map the map of divisions
     * @param jsonObject the parsed JSON object
     * @throws ImportException if an error occurs during import
     */
    private void importDivisions(Map<Division> map, JSONObject jsonObject) throws ImportException {
        JSONArray edificioArray = (JSONArray) jsonObject.get("edificio");
        if (edificioArray == null) {
            throw new ImportException("JSON file is missing 'edificio' key.");
        }

        for (Object obj : edificioArray) {
            if (!(obj instanceof String)) {
                throw new ImportException("Invalid division name format.");
            }
            String divisionName = (String) obj;
            Division division = new Division(divisionName);
            map.addVertex(division);
        }
    }

    /**
     * Imports the connections between divisions from the JSON file.
     *
     * @param map the map of divisions
     * @param jsonObject the parsed JSON object
     * @throws ImportException if an error occurs during import
     */
    private void importConnections(Map<Division> map, JSONObject jsonObject) throws ImportException {
        JSONArray ligacoesArray = (JSONArray) jsonObject.get("ligacoes");
        if (ligacoesArray == null) {
            throw new ImportException("JSON file is missing 'ligacoes' key.");
        }

        for (Object obj : ligacoesArray) {
            if (!(obj instanceof JSONArray)) {
                throw new ImportException("Invalid connection format.");
            }
            JSONArray ligacao = (JSONArray) obj;
            if (ligacao.size() != 2 || !(ligacao.get(0) instanceof String) || !(ligacao.get(1) instanceof String)) {
                throw new ImportException("Invalid connection format.");
            }
            String origemName = (String) ligacao.get(0);
            String destinoName = (String) ligacao.get(1);

            Division origemDivision = findDivisionByName(map, origemName);
            Division destinoDivision = findDivisionByName(map, destinoName);

            map.addEdge(origemDivision, destinoDivision);
        }
    }

    /**
     * Imports the enemies from the JSON file.
     *
     * @param map the map of divisions
     * @param jsonObject the parsed JSON object
     * @throws ImportException if an error occurs during import
     */
    private void importEnemy(Map<Division> map, JSONObject jsonObject) throws ImportException {
        JSONArray enemiesArray = (JSONArray) jsonObject.get("inimigos");
        if (enemiesArray == null) {
            throw new ImportException("JSON file is missing 'inimigos' key.");
        }

        for (Object obj : enemiesArray) {
            if (!(obj instanceof JSONObject)) {
                throw new ImportException("Invalid enemy format.");
            }
            JSONObject toObject = (JSONObject) obj;

            String name = (String) toObject.get("nome");
            long power = (long) toObject.get("poder");
            String divisionName = (String) toObject.get("divisao");

            Division division = getOrCreateDivision(map, divisionName);

            Enemy enemy = new Enemy((int) power, name);
            division.addEnemy(enemy);
        }
    }

    /**
     * Imports the items from the JSON file.
     *
     * @param map the map of divisions
     * @param jsonObject the parsed JSON object
     * @throws ImportException if an error occurs during import
     */
    private void importItems(Map<Division> map, JSONObject jsonObject) throws ImportException {
        JSONArray itemsArray = (JSONArray) jsonObject.get("itens");
        if (itemsArray == null) {
            throw new ImportException("JSON file is missing 'itens' key.");
        }

        for (Object obj : itemsArray) {
            if (!(obj instanceof JSONObject)) {
                throw new ImportException("Invalid item format.");
            }
            JSONObject toObject = (JSONObject) obj;

            Items it;
            long points;
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
    }

    /**
     * Finds a division by name in the map.
     *
     * @param map the map of divisions
     * @param name the name of the division
     * @return the division, or null if not found
     */
    private Division findDivisionByName(Map<Division> map, String name) {
        for (int i = 0; i < map.getNumVertices(); i++) {
            if (map.getVertex(i).getName().equals(name)) {
                return map.getVertex(i);
            }
        }
        return null;
    }

    /**
     * Gets or creates a division by name in the map.
     *
     * @param map the map of divisions
     * @param divisionName the name of the division
     * @return the division
     */
    private Division getOrCreateDivision(Map<Division> map, String divisionName) {
        for (int i = 0; i < map.getNumVertices(); i++) {
            if (map.getVertex(i).getName().equalsIgnoreCase(divisionName)) {
                return map.getVertex(i);
            }
        }
        Division newDivision = new Division(divisionName);
        map.addVertex(newDivision);
        return newDivision;
    }

    /**
     * Returns the file path of the JSON file.
     *
     * @return the file path
     */
    public String getFilename() {
        return this.filepath;
    }
}