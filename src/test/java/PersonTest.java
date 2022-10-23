import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

@DisplayName("Тестирование класса Person")
public class PersonTest {

    protected int foodSpent = 0;

    protected Integer max = 0;

    @Test
    @DisplayName("Подсчет максимума")
    void maxCountTest() {
        JSONObject test = new JSONObject();
        test.put("category", "еда");
        test.put("sum", 200);
        Person person = new Person("Чаки");
        JSONObject json = new JSONObject();
        json.put("category", "еда");
        json.put("sum", 200);
        int sum = (int) json.get("sum");
        int max1 = foodSpent += sum;
        List<Integer> listOfSpent = List.of(20, 60, 80, max1);
        max = Collections.max(listOfSpent);
        json.put("sum", max);
        JSONObject maximum = new JSONObject();
        maximum.put("maxCategory", json);
        Assertions.assertEquals(maximum, person.maxCount(test));
    }
}
