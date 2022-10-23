import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TsvComparator {

    public static JSONObject tsvComparator(String title, int sum, File file) {
        JSONObject sumAndCategory = new JSONObject();
        sumAndCategory.put("sum", sum);
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
                if (title.equals(row4[0])) {
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
