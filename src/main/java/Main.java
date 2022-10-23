import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    protected static File jsonFile;

    public static void main(String[] args) {
        jsonFile = new File("money.json");
        try {
            jsonFile.createNewFile();
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
        Person person;
        if (Person.loadFromJson(jsonFile) == null) {
            person = new Person("Жорик");
            boolean isCreated = person.createTsvFile();
            person.setTsvFile();
        } else {
            person = Person.loadFromJson(jsonFile);
            person.setTsvFile();
        }
        try (ServerSocket serverSocket = new ServerSocket(8989)) {
            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream())
                ) {
                    System.out.printf("Соединение установлено! Привет, %s%n", person.getName());
                    String data = in.readLine();
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    JsonObject json = gson.fromJson(data, JsonObject.class);
                    System.out.println(data);
                    JSONObject added = TsvComparator.tsvComparator(json.getTitle(), json.getSum(), person.getTsvFile());
                    JSONObject max = person.maxCount(added);
                    out.println(max);
                    person.saveToJson(jsonFile);
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}
