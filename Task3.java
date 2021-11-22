import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Task3 {
    public static Map<Integer, String> valuesMap = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter path to 'values.json' and 'tests.json'");
        valuesParser(new File(scanner.nextLine()));
        JSONObject testsJsonObject = testsParser(new File(scanner.nextLine()));
        JSONObject reportJson = mergeFiles((JSONArray) testsJsonObject.get("tests"));
        myFileWriter(reportJson);
        System.out.println("File 'report.json' successful created!");
    }

    public static void valuesParser(File path) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;
        JSONArray jsonArray;
        Reader reader;
        try {
            reader = new FileReader(path.toString());
            jsonObject = (JSONObject) parser.parse(reader);
            jsonArray = (JSONArray) jsonObject.get("values");

            for (Object o : jsonArray) {
                JSONObject temp = (JSONObject) o;
                valuesMap.put(Integer.parseInt(temp.get("id").toString()), temp.get("value").toString());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static JSONObject testsParser(File path) {
        JSONParser parser = new JSONParser();
        Reader reader;
        try {
            reader = new FileReader(path.toString());
            return (JSONObject) parser.parse(reader);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static JSONObject mergeFiles(JSONArray jsonArray) {
        JSONObject jsonObject = new JSONObject();
        for (Object o : jsonArray) {
            JSONObject temp = (JSONObject) o;
            if (valuesMap.containsKey(Integer.parseInt(temp.get("id").toString()))) {
                temp.put("value", valuesMap.get(Integer.parseInt(temp.get("id").toString())));
            }
            if (temp.containsKey("values")) {
                mergeFiles((JSONArray) temp.get("values"));
            }
        }
        jsonObject.put("tests", jsonArray);
        return jsonObject;
    }

    public static void myFileWriter(JSONObject reportJson) {
        try {
            FileWriter file = new FileWriter("report.json");
            file.write(reportJson.toJSONString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
