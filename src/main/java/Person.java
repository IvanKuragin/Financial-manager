import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.univocity.parsers.tsv.TsvWriter;
import com.univocity.parsers.tsv.TsvWriterSettings;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class Person {

    protected File TsvFile;
    protected File jsonFile;
    protected int[] money;
    protected int foodSpent = 0;
    protected int clothesSpent = 0;
    protected int financialSpent = 0;
    protected int houseHoldSpent = 0;
    protected int otherSpent = 0;
    protected String categorySaved;
    protected Integer max1 = 0;
    protected String name;
    protected static String host = "127.0.0.1";
    protected static int port = 8989;

    public Person(String name) {
        this.name = name;
        TsvFile = new File("categories.tsv");
        money = new int[5];
    }

    public boolean createTsvFile() {
        boolean isCreated = false;
        try {
            isCreated = TsvFile.createNewFile();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return isCreated;
    }

    public void setTsvFile() {
        String[][] categories = {{"булка еда"}, {"колбаса еда"}, {"сухарики еда"},
                {"курица еда"}, {"тапки одежда"}, {"шапка одежда"},
                {"мыло быт"}, {"акции финансы"}};
        TsvWriterSettings settings = new TsvWriterSettings();
        TsvWriter writer = new TsvWriter(TsvFile, settings);
        for (int i = 0; i < categories.length; i++) {
            String[] e1 = categories[i];
            writer.writeRow(e1);
        }
        writer.close();
    }

    public File getTsvFile() {
        return TsvFile;
    }

    public String getName() {
        return name;
    }

    public JSONObject maxCount(JSONObject json) {
        String category = (String) json.get("category");
        String sum = (String) json.get("sum");
        String sum1 = sum.replace("\"", "");
        int sum2 = Integer.parseInt(sum1);
        if (categorySaved == null) {
            categorySaved = (String) json.get("category");
        }
        switch (category) {
            case ("еда"):
                foodSpent += sum2;
                money[0] = foodSpent;
                if (money[0] > max1 & money[0] > 0) {
                    json.put("category", category);
                    categorySaved = (String) json.get("category");
                } else {
                    json.put("category", categorySaved);
                }
                break;
            case ("одежда"):
                clothesSpent += sum2;
                money[1] = clothesSpent;
                if (money[1] > max1 & money[1] > 0) {
                    json.put("category", category);
                    categorySaved = (String) json.get("category");
                } else {
                    json.put("category", categorySaved);
                }
                break;
            case ("быт"):
                houseHoldSpent += sum2;
                money[2] = houseHoldSpent;
                if (money[2] > max1 & money[2] > 0) {
                    json.put("category", category);
                    categorySaved = (String) json.get("category");
                } else {
                    json.put("category", categorySaved);
                }
                break;
            case ("финансы"):
                financialSpent += sum2;
                money[3] = financialSpent;
                if (money[3] > max1 & money[3] > 0) {
                    json.put("category", category);
                    categorySaved = (String) json.get("category");
                } else {
                    json.put("category", categorySaved);
                }
                break;
            default:
                otherSpent += sum2;
                money[4] = otherSpent;
                if (money[4] > max1 & money[4] > 0) {
                    json.put("category", category);
                    categorySaved = (String) json.get("category");
                } else {
                    json.put("category", categorySaved);
                }
                break;
        }
        List<Integer> listOfSpent = Arrays.stream(money).boxed().collect(Collectors.toList());
        max1 = Collections.max(listOfSpent);
        json.put("sum", max1);
        JSONObject max = new JSONObject();
        max.put("maxCategory", json);
        return max;
    }

    public void saveToJson (File jsonFile) {
        this.jsonFile = jsonFile;
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json =gson.toJson(this);
        try(PrintWriter out = new PrintWriter(jsonFile)) {
            jsonFile.createNewFile();
            out.println(json);
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
    }

    public static Person loadFromJson (File jsonFile) {
        Person person = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
            String jsonText = reader.readLine();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            person = gson.fromJson(jsonText, Person.class);
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
        return person;
    }

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            try (Socket clientSocket = new Socket(host, port);
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                System.out.println("Соединение установлено");
                JSONObject json = new JSONObject();
                json.put("date", "2022.02.08");
                json.put("title", "мыло");
                json.put("sum", "200");
                out.println(json);
                String data = in.readLine();
                System.out.println(data);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            Thread.sleep(5000);
        }
    }
}
