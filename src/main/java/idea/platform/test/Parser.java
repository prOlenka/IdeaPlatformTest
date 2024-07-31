package idea.platform.test;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Parser {

    public static JSONObject readFile(String path) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Reader reader = new FileReader(path);
        Object jsonObj = parser.parse(reader);
        reader.close();
        return (JSONObject) jsonObj;
    }
}
