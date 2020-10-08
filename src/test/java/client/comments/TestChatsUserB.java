/***
 * Тестируем отправку и приём сообщений пользователем 7001
 */

package client.comments;

import client.RecourcesTests;
import client.WatcherTests;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static data.CommentsData.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic(value = "Беседы")
@Feature(value = "Беседа")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(RecourcesTests.class)
@ExtendWith(WatcherTests.class)
public class TestChatsUserB implements CommentsPage {

    private ChatsPage chatsPage = new ChatsPage();

    @Story(value = "Проверка отправки сообщения")
    @Description(value = "1. Авторизуемся пользователем 7001. \n" +
            "2. Пользователь 7001 отправляет сообщение пользователю 7000. \n" +
            "3. Проверяем, отображается ли отправленое сообщение на клиенте")
    @Test
    void test_Send_Second_Message() {
        chatsPage.sendChatMessage(CLIENT_7000, CLIENT_CHATS_SECOND_MESSAGE);
        assertTrue(chatsPage.isExistChatMessage(CLIENT_7000, CLIENT_CHATS_SECOND_MESSAGE),
                "Не найдено сообщение \"" + CLIENT_CHATS_SECOND_MESSAGE + "\" в беседе " + CLIENT_7000);
    }

    @Story(value = "Проверка получения сообщения")
    @Description(value = "1. Авторизуемся пользователем 7001.\n." +
            "2. Проверяем, что приишло сообщение от пользователя 7000")
    @Test
    void test_Get_First_Message(){
        assertTrue(chatsPage.isExistChatMessage(CLIENT_7000, CLIENT_CHATS_FIRST_MESSAGE),
                "Не найдено сообщение \"" + CLIENT_CHATS_FIRST_MESSAGE + "\" в беседе " + CLIENT_7000);
    }

    @AfterAll
    static void tearDown(){
        Selenide.close();
    }
}
