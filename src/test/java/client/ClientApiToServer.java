package client;

import static chat.ros.testing2.data.ContactsData.*;
import static chat.ros.testing2.data.LoginData.HOST_SERVER;
import static data.CommentsData.CLIENT_CHATS_SEND_MESSAGE;

public class ClientApiToServer extends Thread {

    public APIToServer apiToServer = new APIToServer("https://" + HOST_SERVER + ":8080", CONTACT_NUMBER_7013 + "@ros.chat", USER_ACCOUNT_PASSWORD);
    private String IDForReceivingMessageUser = apiToServer.getContactIDBySurnameFromListOfContacts(CONTACT_NUMBER_7012, 60);

    @Override
    public void run(){
        apiToServer.SendTextMessageToUser(
                "user",
                IDForReceivingMessageUser,
                "text",
                CLIENT_CHATS_SEND_MESSAGE,
                60
        );
    }
}
