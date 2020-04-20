package client;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class APIToServer {

    //Параметры для подключения по soket-io
    private final String API_CLIENT_CURRENT = "0";
    private final String VALUE_FOR_SILENT_MODE = "false";
    private final String CLIENT = "pc";

    public Socket socket;

    //Для каждого публичного метода данного класса подразумевается выполнение одного действия (отправка сообщения, отправка звонка и т.д)
    //После завершения действия флаг переходит в true, и главноя нить прекращает ожидать (также главная нить перестаёт ждать по таймеру )
    private Boolean isActionFinish = false;
    private String serverURL;
    private String userLogin;
    private String userPassword;

    //Так как не разобрался как правильно исопльзольвать библиотеку при вызове именно тестов использую данную переменную
    //Для того, чтобы вернуть id пользователя из базы данных при его поиске по имени
    private String userID;

    public APIToServer(String serverURL, String userLogin, String userPassword) {
        this.serverURL = serverURL;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        connect();
    }

    /**
     * Установка соединения с сервером. После установки соединения запускается аутентификация
     * пользователя (указывается при создании объекта класса)
     * Также в методе описываются действия при получении различных событий и сообщений описанных в библиотеке
     * socket-io
     */
    private void connect() {
        disconnect();
        try {
            IO.Options options = new IO.Options();
            options.forceNew = false; // не разрешаем переподключать web сокет, чтобы не пропали данные сессии
            options.timeout = 10000; //таймаут на переподключение при разрыве соединения
            if (socket == null)
                socket = IO.socket(serverURL, options);
            System.out.println("WebSocket is connected to " + serverURL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Cannot create websocket : " + e.getMessage());
        }

        socket.on(Socket.EVENT_CONNECT, args -> {
            System.out.println("EVENT_CONNECT");
            login();
        });

        socket.on(Socket.EVENT_DISCONNECT, args -> {
            disconnect();
            System.out.println("Полученно событие EVENT_DISCONNECT, инициировано разрушение сокета.");
        });

        socket.on(Socket.EVENT_CONNECT_ERROR, args -> {
            disconnect();
            System.out.println("Полученно событие EVENT_CONNECT_ERROR, инициировано разрушение сокета.");
        });

        socket.on(Socket.EVENT_CONNECT_TIMEOUT, args -> {
            disconnect();
            System.out.println("Полученно событие EVENT_CONNECT_TIMEOUT, инициировано разрушение сокета.");
        });

        socket.on(Socket.EVENT_ERROR, args -> {
            disconnect();
            System.out.println("Полученно событие EVENT_ERROR, инициировано разрушение сокета.");
        });

        socket.on(Socket.EVENT_RECONNECT_FAILED, args -> {
            disconnect();
            System.out.println("Полученно событие EVENT_RECONNECT_FAILED, инициировано разрушение сокета.");
        });
        socket.connect();
    }

    /**
     * Прохождение процедуры аутентификации данные берутся из полей,
     * которые инициализируются при создании объекта или являются константами
     */
    private void login() {
        if (socket == null) return;
        JSONObject obj = new JSONObject();
        try {
            obj.put("login", userLogin);
            obj.put("password", userPassword);
            obj.put("client", CLIENT);
            obj.put("APILevel", API_CLIENT_CURRENT);
            obj.put("silentMode", VALUE_FOR_SILENT_MODE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("login" + obj);
        socket.emit("login", obj, (Ack) args -> {
            System.out.println("ack from server on connection" + args[0]);
            JSONObject data = (JSONObject) args[0];
            int userId = data.optInt("uid");
            int api_server = data.optInt("APILevel", 0);
            String error = data.optString("error");
            String status = data.optString("status");
        });

    }

    /**
     * Отправка сообщения
     */
    private void sendMessage(String cidType, String cid, String dataType, String data) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("cidType", cidType);
            obj.put("cid", cid);
            obj.put("dataType", dataType);
            obj.put("data", data);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("send-message :" + obj);
        socket.emit("send-message", obj, (Ack) args1 -> {
            System.out.println("ack from server " + args1[0]);
            JSONObject dataJson = (JSONObject) args1[0];
            int messageId = dataJson.optInt("id");
            System.out.println(messageId);
            isActionFinish = true;
        });

    }

    /**
     * Получение списка контактов и поиск id абонента по фамилии
     * Найденный id пользователя передаётся в поле userID
     */
    private void getContacts(String surnameOfUser) {
        if (socket == null) return;
        JSONObject obj = new JSONObject();
        try {
            obj.put("type", "global");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("get-contacts" + obj);
        socket.emit("get-contacts", obj, (Ack) args -> {
            System.out.println("get-contacts ack");
            JSONArray jsonArray = (JSONArray) args[0];
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.getString("surname").equals(surnameOfUser)) {
                        userID = jsonObject.getString("cid");
                        isActionFinish = true;
                        break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Cant get list of contact because: " + e.getMessage());
                }
            }
        });

    }

    /**
     * Получение из списка контактов id абонента по фамилии
     */
    public String getContactIDBySurnameFromListOfContacts(String surnameOfUser, int maxIntervalInSecToDoThisActions) {
        isActionFinish = false;
        Thread connectionThread = new Thread(() -> {
            getContacts(surnameOfUser);
        });

        connectionThread.start();

        //Завершаем ожидание главного потока по таймеру или по выполнению действия
        for (int i = 0; i < maxIntervalInSecToDoThisActions; i++) {
            if (isActionFinish) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        return userID;
    }

    /**
     * Отправка пользователю сообщения
     */

    public void SendTextMessageToUser(String cidType, String idOfUserReceivingMessage, String dataType, String data,
                                      int maxIntervalInSecToDoThisActions) {
        isActionFinish = false;
        Thread connectionThread = new Thread(() -> {
            sendMessage(cidType, idOfUserReceivingMessage, dataType, data);
        });

        connectionThread.start();

        //Завершаем ожидание главного потока по таймеру или по выполнению действия
        for (int i = 0; i < maxIntervalInSecToDoThisActions; i++) {
            if (isActionFinish) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Разрушение соединения
     */
    public void disconnect() {
        if (socket == null) return;
        if (socket.connected()) {
            socket.disconnect();
            socket.off();
            System.out.println("socket disconnect");
        }
    }
}
