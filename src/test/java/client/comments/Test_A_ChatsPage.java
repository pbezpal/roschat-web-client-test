package client.comments;

import client.*;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static chat.ros.testing2.data.ContactsData.*;
import static chat.ros.testing2.data.LoginData.HOST_SERVER;
import static data.CommentsData.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic(value = "Беседы")
@Feature(value = "Беседа")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(RecourcesTests.class)
@ExtendWith(WatcherTests.class)
public class Test_A_ChatsPage implements CommentsPage {

    private ApiToServerSendMessage apiToServerSendMessage;
    private ApiToServerGetMessage apiToServerGetMessage;
    private String apiHost = "https://" + HOST_SERVER + ":8080";
    private String apiUser = CONTACT_NUMBER_7013 + "@ros.chat";
    private ChatsPage chatsPage = new ChatsPage();

    @Story(value = "Проверка получения сообщения")
    @Description(value = "Авторизуемся под пользователем 7012, пользователь 7013 отправляет сообщение, проверяем, " +
            "появилось ли сообщение у пользователя 7012")
    @Test
    void test_Get_New_Message_7012(){
        apiToServerSendMessage = new ApiToServerSendMessage(
                apiHost,
                apiUser,
                CONTACT_NUMBER_7012,
                USER_ACCOUNT_PASSWORD,
                CLIENT_CHATS_RECEIVED_MESSAGE);
        apiToServerSendMessage.start();

        assertTrue(chatsPage.isGetNewMessage(CONTACT_NUMBER_7013, CLIENT_CHATS_RECEIVED_MESSAGE));
    }

    @Story(value = "Проверка отправки сообщения")
    @Description(value = "Авторизуемся под пользователем 7012, пользователь 7013 отправляет сообщение, проверяем, " +
            "появилось ли сообщение у пользователя 7012")
    @Test
    void test_Send_New_Message_7012() {
        apiToServerGetMessage = new ApiToServerGetMessage(
                apiHost,
                apiUser,
                CONTACT_NUMBER_7012,
                USER_ACCOUNT_PASSWORD,
                CLIENT_CHATS_SEND_EVENT,
                CLIENT_CHATS_SEND_MESSAGE
        );

        apiToServerGetMessage.start();
        chatsPage.sendNewMessage(CONTACT_NUMBER_7013, CLIENT_CHATS_SEND_MESSAGE);
    }
}
