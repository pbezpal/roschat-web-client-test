package client;

import static org.junit.jupiter.api.Assertions.*;

public class ApiToServerGetMessage extends Thread  {

    public APIToServer apiToServer;
    private String IDForReceivingMessageUser;
    private String event;
    private String message;
    private String clientUser;

    public ApiToServerGetMessage(String host, String apiUser, String clientUser, String password, String event, String message){
        this.apiToServer = new APIToServer(host, apiUser, password);
        this.IDForReceivingMessageUser = apiToServer.getContactIDBySurnameFromListOfContacts(clientUser, 60);
        this.event = event;
        this.message = message;
        this.clientUser = clientUser;
    }

    @Override
    public void run(){
        String[] getMessage = apiToServer.GetTextMessageFromUser(this.event,60);
        apiToServer.disconnect();
        assertAll("Проверяем, совпадает ли ID и сообщение с ссылкой на канал",
                () -> assertEquals(getMessage[0], this.IDForReceivingMessageUser, "Сообщение пришло " +
                "не от пользователя " + this.clientUser),
                () -> assertTrue(getMessage[1].contains(this.message), "Текст сообщения не совпадает с тем," +
                "которое отправил пользователь " + this.clientUser)
        );
    }

}
