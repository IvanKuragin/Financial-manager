import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import com.univocity.parsers.tsv.TsvWriter;
import com.univocity.parsers.tsv.TsvWriterSettings;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DisplayName("Тестирование класса JsonBuilder")
public class TsvComparatorTest {
    protected File TsvFile = new File("categoriesTest.tsv");

    @Test
    @DisplayName("Обработка TSV")
    void tsvComparatorTest() {
        String[][] categories = {{"булка еда"}};
        TsvWriterSettings settings = new TsvWriterSettings();
        TsvWriter writer = new TsvWriter(TsvFile, settings);
        for (int i = 0; i < categories.length; i++) {
            String[] e1 = categories[i];
            writer.writeRow(e1);
        }
        writer.close();
        String title = "булка";
        int sum = 200;
        Map<String, String> mapa = new HashMap<>();
        mapa.put("title", "булка");
        mapa.put("sum", "200");
        JSONObject sumAndCategory = new JSONObject();
        sumAndCategory.put("sum", sum);
        TsvParserSettings settings1 = new TsvParserSettings();
        TsvParser parser = new TsvParser(settings1);
        try {
            List<String[]> list = parser.parseAll(new FileReader(TsvFile));
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
            parser.stopParsing();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        Assertions.assertEquals(sumAndCategory, TsvComparator.tsvComparator(title, sum, TsvFile));
        TsvFile.delete();
    }
}
