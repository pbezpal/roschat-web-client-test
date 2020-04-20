package client.comments;

import client.APIToServer;
import client.RecourcesTests;
import com.codeborne.selenide.Selenide;
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
@Feature(value = "Чаты")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(RecourcesTests.class)
public class TestChatsPage implements CommentsPage {

    private static APIToServer apiToServer = new APIToServer("https://" + HOST_SERVER + ":8080", CONTACT_NUMBER_7013 + "@ros.chat", USER_ACCOUNT_PASSWORD);
    private static String IDForReceivingMessageUser = apiToServer.getContactIDBySurnameFromListOfContacts(CONTACT_NUMBER_7012, 60);

    private static ChatsPage chatsPage = new ChatsPage();

    @Story(value = "Проверка приёма сообщений")
    @Description(value = "Авторизуемся под пользователем 7012, пользователь 7013 отправляет сообщение, проверяем, " +
            "появилось ли сообщение у пользователя 7012")
    @Test
    void test_Get_New_Message_7012(){
        Runnable getMessage = () -> {
            assertTrue(chatsPage.isGetNewMessage(CONTACT_NUMBER_7013, CLIENT_CHATS_SEND_MESSAGE));
        };

        Thread sendMessage = new Thread() {
            @Override
            public void run() {
                Selenide.sleep(3000);
                apiToServer.SendTextMessageToUser(
                        "user",
                        IDForReceivingMessageUser,
                        "text",
                        CLIENT_CHATS_SEND_MESSAGE,
                        60
                );
                apiToServer.disconnect();
            }
        };

        sendMessage.start();
        getMessage.run();
    }
}
