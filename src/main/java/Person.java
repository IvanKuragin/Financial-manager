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
    protected Integer max1;
    protected String name;
    protected Map<String, Integer> customerData = new HashMap<>();

    protected String savedCategory;
    protected int summary;
    protected static String host = "127.0.0.1";
    protected static int port = 8989;

    public Person(String name) {
        this.name = name;
        TsvFile = new File("categories.tsv");
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
        int sum = (int) json.get("sum");
        if (savedCategory == null) {
            savedCategory = category;
            summary += sum;
        } else if (category.equals(savedCategory)) {
            summary += sum;
        } else if (!category.equals(savedCategory)) {
            summary = 0;
            summary += sum;
            savedCategory = category;
        }
        customerData.put(category, summary);
        Collection<Integer> list = customerData.values();
        max1 = Collections.max(list);
        String key = null;
        for (Map.Entry<String, Integer> entry : customerData.entrySet()) {
            if (entry.getValue().equals(max1)) {
                key = entry.getKey();
            }
        }
        json.put("category", key);
        json.put("sum", max1);
        JSONObject max = new JSONObject();
        max.put("maxCategory", json);
        return max;
    }

    public void saveToJson(File jsonFile) {
        this.jsonFile = jsonFile;
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(this);
        try (PrintWriter out = new PrintWriter(jsonFile)) {
            jsonFile.createNewFile();
            out.println(json);
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
    }

    public static Person loadFromJson(File jsonFile) {
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
                GsonBuilder builder = new GsonBuilder();
                Gson json = builder.create();
                String output = json.toJson(new JsonObject("мыло", "2022.02.08", 200));
                System.out.println(output);
                out.println(output);
                String data = in.readLine();
                System.out.println(data);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            Thread.sleep(5000);
        }
    }
}
