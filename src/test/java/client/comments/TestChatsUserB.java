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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic(value = "Беседы")
@Feature(value = "Беседа")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(RecourcesTests.class)
@ExtendWith(WatcherTests.class)
public class TestChatsUserB implements CommentsPage {

    private ChatsPage chatsPage = new ChatsPage();

    @Story(value = "Проверка отправки ответа на сообщение")
    @Description(value = "1. Авторизуемся под пользователем 7001. \n" +
            "2. Пользователь 7001 отправляет ответное сообщение пользователю 7000. \n" +
            "3. Проверяем, отображается ли ответное сообщение на клиенте")
    @Test
    void test_Send_Reply_Message() {
        chatsPage.sendChatMessage(CLIENT_7000, CLIENT_CHATS_TEXT_REPLY_MESSAGE, true);
        assertTrue(chatsPage.isExistChatMessage(CLIENT_7000, CLIENT_CHATS_TEXT_REPLY_MESSAGE),
                "Не найдено сообщение \"" + CLIENT_CHATS_TEXT_REPLY_MESSAGE + "\" в беседе " + CLIENT_7000);
        assertAll("Проверяем, отображается ли автор и текст сообщения, на которое отвечали",
                () -> assertTrue(chatsPage.isAuthReplyMessage(CLIENT_7000),
                        "Автор, сообщения, на которое отвечали не отображается или не совпадает"),
                () -> assertTrue(chatsPage.isReplyMessage(CLIENT_CHATS_FIRST_MESSAGE),
                        "Текст сообщение, на которое отвечали не совпадает или не отображается")
        );
    }

    @Story(value = "Проверка получения сообщения")
    @Description(value = "1. Авторизуемся под пользователем 7001. \n" +
            "2. Проверяем, что приишло сообщение от пользователя 7000")
    @Test
    void test_Get_Message(){
        assertTrue(chatsPage.isExistChatMessage(CLIENT_7000, CLIENT_CHATS_FIRST_MESSAGE),
                "Не найдено сообщение \"" + CLIENT_CHATS_FIRST_MESSAGE + "\" в беседе " + CLIENT_7000);
    }

    @Story(value = "Удаляем беседу")
    @Description(value = "1. Авторизуемся под пользователем 7001 \n" +
            "2. Удаляем беседу с пользователем 7000 \n" +
            "3. Проверяем, что беседа с пользователем 7000 была удалена")
    @Test
    void test_Delete_Chat(){
        assertTrue(deleteChat(CLIENT_7000, CLIENT_CHATS_ITEM_DELETE_CHAT, true).
                isExistComments(CLIENT_7000, false),
                "Беседа с пользователем " + CLIENT_7000 + " отображается в списке ебесед после удаления");
    }

    @AfterAll
    static void tearDown(){
        Selenide.close();
    }
}
