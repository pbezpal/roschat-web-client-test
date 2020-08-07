/***
 * Тестируем отправку и приём сообщений на WEB-клиенте
 * Клиент 1 - 7000@ros.chat
 * Клиент 2 - 7001@ros.chat
 */

package client.comments;

import client.*;
import client.tools.APIToServer;
import client.tools.SSHGetCommand;
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
    private static String apiUser = CLIENT_USER_B + "@ros.chat";
    private static APIToServer apiToServer = null;
    private static String CIDUser = SSHGetCommand.isCheckQuerySSH(
            "sudo -u roschat psql -c \"select cid, login from users;\" | grep " + CLIENT_USER_A + " | awk '{print $1}'"
    );
    private ChatsPage chatsPage = new ChatsPage();

    @Story(value = "Проверка отправки сообщения")
    @Description(value = "1. Авторизуемся первым пользователем на WEB-клиенте. \n" +
            "2. Авторизуемся вторым пользователем через api. \n" +
            "3. Первый пользователь отправляет сообщение второму пользователю. \n" +
            "4. Второй пользователь проверяет, пришло ли ему сообщение от первого клиента.")
    @Order(1)
    @Test
    void test_Send_New_Message() throws ExecutionException, InterruptedException {
        String[] apiGetMessageResult;

        Runnable clientSendMessage = () -> {
            chatsPage.sendNewMessage(CLIENT_USER_B, CLIENT_CHATS_SEND_MESSAGE);
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
                        "не от пользователя " + CLIENT_USER_A),
                () -> assertEquals(apiGetMessageResult[1], CLIENT_CHATS_SEND_MESSAGE, "Текст сообщения не совпадает с тем," +
                        "которое отправил пользователь " + CLIENT_USER_A)
        );
    }

    @Story(value = "Проверка получения сообщения")
    @Description(value = "1. Авторизуемся вторым клиентом на WEB-клиенте\n." +
            "2. Проверяем WEB-клиенте, пришло ли сообщение от первого клиента")
    @Order(2)
    @Test
    void test_Get_New_Message(){
        assertTrue(chatsPage.isGetNewMessage(CLIENT_USER_A, CLIENT_CHATS_SEND_MESSAGE));
    }

    private APIToServer getApiToServer(APIToServer apiToServer){
        if(apiToServer == null){
            return new APIToServer(apiHost, apiUser, USER_ACCOUNT_PASSWORD);
        }else return apiToServer;
    }

    @AfterAll
    static void tearDown(){
        if(apiToServer != null) apiToServer.disconnect();
        Selenide.close();
    }
}
