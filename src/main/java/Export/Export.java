package Export;

import Classes.Division;
import Collections.Linked.LinkedUnorderedList;
import Game.SimulationResult;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Export {

    public static void saveActionsToJSON(SimulationResult result, String resultsFilename) {
        JSONArray resultsList = new JSONArray();

        try (FileReader reader = new FileReader(resultsFilename)) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            if (obj instanceof JSONArray) {
                resultsList = (JSONArray) obj;
            }
        } catch (IOException e) {

            System.out.println("File not found, creating a new one.");
        } catch (ParseException e) {
            System.out.println("File is empty or malformed, starting with an empty list.");
        }


        JSONObject resultDetails = new JSONObject();
        resultDetails.put("codMission: ", result.getMissionVersion());
        resultDetails.put("remainingLifePoints: ", result.getRemainingLifePoints());
        resultDetails.put("totalDamageDealt: ", result.getTotalDamages());
        resultDetails.put("healthItemsUsed: ", result.getHealthItemsUsed());
        resultDetails.put("vestsUsed: ", result.getVestUsed());
        resultDetails.put("missionSuccess: ", result.isMissionSuccess());

        resultsList.add(resultDetails);


        try (FileWriter file = new FileWriter(resultsFilename)) {
            file.write(resultsList.toJSONString().replace(",", ",\n"));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void savePathToJSON(String mission, LinkedUnorderedList<Division> trajectory, String resultsFilename) {
        JSONArray resultsList = new JSONArray();

        try (FileReader reader = new FileReader(resultsFilename)) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            if (obj instanceof JSONArray) {
                resultsList = (JSONArray) obj;
            }
        } catch (IOException e) {

            System.out.println("File not found, creating a new one.");
        } catch (ParseException e) {
            System.out.println("File is empty or malformed, starting with an empty list.");
        }

        JSONArray missionArray = new JSONArray();
        missionArray.add(mission);

        JSONArray divisionArray = new JSONArray();
        for (Division division : trajectory) {
            divisionArray.add(division.getName());
        }
        missionArray.add(divisionArray);

        resultsList.add(missionArray);

        try (FileWriter file = new FileWriter(resultsFilename)) {
            file.write(resultsList.toJSONString().replace(",", ",\n"));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
