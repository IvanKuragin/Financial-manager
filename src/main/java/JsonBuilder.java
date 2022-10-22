import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonBuilder {

    public static Map<String, String> jsonConverter(String data) {
        Map<String, String> mapa = new HashMap<>();
        String[] dataArray = data.split(",");
        String firstCell = dataArray[0];
        String[] firstCellArray = firstCell.split(" ");
        mapa.put(firstCellArray[0], firstCellArray[1]);
        String secondCell = dataArray[1];
        String[] secondCellArray = secondCell.split(" ");
        mapa.put(secondCellArray[0], secondCellArray[1]);
        String thirdCell = dataArray[2];
        String[] thirdCellArray = thirdCell.split(" ");
        mapa.put(thirdCellArray[0], thirdCellArray[1]);
        return mapa;
    }

    public static JSONObject tsvComparator(Map<String, String> mapa, File file) {
        JSONObject sumAndCategory = new JSONObject();
        sumAndCategory.put("sum", mapa.get("sum"));
        TsvParserSettings settings = new TsvParserSettings();
        TsvParser parser = new TsvParser(settings);
        try {
            List<String[]> list = parser.parseAll(new FileReader(file));
            for (int i = 0; i < list.size(); i++) {
                String[] row = list.get(i);
                String row1 = Arrays.toString(row);
                String row2 = row1.replace("[", "");
                String row3 = row2.replace("]", "");
                String[] row4 = row3.split(" ");
                if (mapa.get("title").equals(row4[0])) {
                    sumAndCategory.put("category", row4[1]);
                    break;
                } else {
                    sumAndCategory.put("category", "другое");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return sumAndCategory;
    }
}
