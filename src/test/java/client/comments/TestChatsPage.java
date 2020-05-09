package client.comments;

import client.*;
import client.helper.SSHGetCommand;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static chat.ros.testing2.data.ContactsData.*;
import static chat.ros.testing2.data.LoginData.HOST_SERVER;
import static data.CommentsData.*;
import static org.junit.jupiter.api.Assertions.*;

@Epic(value = "Беседы")
@Feature(value = "Беседа")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(RecourcesTests.class)
@ExtendWith(WatcherTests.class)
public class TestChatsPage implements CommentsPage {

    private static String apiHost = "https://" + HOST_SERVER + ":8080";
    private static String apiUser = CONTACT_NUMBER_7013 + "@ros.chat";
    private static APIToServer apiToServer = null;
    private static String CIDUser = SSHGetCommand.isCheckQuerySSH(
            "sudo -u roschat psql -c \"select cid, login from users;\" | grep " + CONTACT_NUMBER_7012 + " | awk '{print $1}'"
    );
    private ChatsPage chatsPage = new ChatsPage();

    @Story(value = "Проверка получения сообщения")
    @Description(value = "Авторизуемся под пользователем 7012, пользователь 7013 отправляет сообщение, проверяем, " +
            "появилось ли сообщение у пользователя 7012")
    @Test
    void test_Get_New_Message_7012(){
        Runnable clientGetMessage = () -> {
            assertTrue(chatsPage.isGetNewMessage(CONTACT_NUMBER_7013, CLIENT_CHATS_RECEIVED_MESSAGE));
        };

        Thread apiSendMessage = new Thread() {
            @Override
            public void run() {
                apiToServer = getApiToServer(apiToServer);
                Selenide.sleep(3000);
                apiToServer.SendTextMessageToUser(
                        "user",
                        CIDUser,
                        "text",
                        CLIENT_CHATS_RECEIVED_MESSAGE,
                        30
                );
            }
        };

        apiSendMessage.start();
        clientGetMessage.run();
    }

    @Story(value = "Проверка отправки сообщения")
    @Description(value = "Авторизуемся под пользователем 7012, пользователь 7013 отправляет сообщение, проверяем, " +
            "появилось ли сообщение у пользователя 7012")
    @Test
    void test_Send_New_Message_7012() throws ExecutionException, InterruptedException {
        String[] apiGetMessageResult;

        Runnable clientSendMessage = () -> {
            chatsPage.sendNewMessage(CONTACT_NUMBER_7013, CLIENT_CHATS_SEND_MESSAGE);
        };

        CompletableFuture<String[]> socketGetMessage = CompletableFuture.supplyAsync(() ->{
            apiToServer = getApiToServer(apiToServer);
            String[] getMessageResult = apiToServer.GetTextMessageFromUser(CLIENT_CHATS_SEND_EVENT, "data",60);
            return getMessageResult;
        });

        clientSendMessage.run();
        apiGetMessageResult = socketGetMessage.get();
        assertAll("Проверяем, совпадает ли ID и сообщение с ссылкой на канал",
                () -> assertEquals(apiGetMessageResult[0], CIDUser, "Сообщение пришло " +
                        "не от пользователя " + CONTACT_NUMBER_7012),
                () -> assertEquals(apiGetMessageResult[1], CLIENT_CHATS_SEND_MESSAGE, "Текст сообщения не совпадает с тем," +
                        "которое отправил пользователь " + CONTACT_NUMBER_7012)
        );
    }

    private APIToServer getApiToServer(APIToServer apiToServer){
        if(apiToServer == null){
            return new APIToServer(apiHost, apiUser, USER_ACCOUNT_PASSWORD);
        }else return apiToServer;
    }

    @AfterAll
    static void tearDown(){
        apiToServer.disconnect();
    }
}