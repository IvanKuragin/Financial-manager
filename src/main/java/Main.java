import org.json.simple.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

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
        if(Person.loadFromJson(jsonFile) == null) {
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
                    System.out.println(data);
                    String data1 = data.replace("\"", "");
                    String data2 = data1.replace(":", " ");
                    String data3 = data2.replace("{", "");
                    String data4 = data3.replace("}", "");
                    Map<String, String> mapa = JsonBuilder.jsonConverter(data4);
                    JSONObject added = JsonBuilder.tsvComparator(mapa, person.getTsvFile());
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
