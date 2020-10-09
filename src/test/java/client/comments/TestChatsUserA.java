/***
 * Тестируем отправку и приём сообщений пользователем 7000
 */

package client.comments;

import client.*;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static data.CommentsData.*;
import static org.junit.jupiter.api.Assertions.*;

@Epic(value = "Беседы")
@Feature(value = "Беседа")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(RecourcesTests.class)
@ExtendWith(WatcherTests.class)
public class TestChatsUserA implements CommentsPage {

    private ChatsPage chatsPage = new ChatsPage();

    @Story(value = "Проверка отправки сообщения")
    @Description(value = "1. Авторизуемся пользователем 7000. \n" +
            "2. Пользователь 7000 отправляет сообщение пользователю 7001. \n" +
            "3. Проверяем, отображается ли отправленое сообщение на клиенте")
    @Test
    void test_Send_First_Message() {
        chatsPage.sendChatMessage(CLIENT_7001, CLIENT_CHATS_FIRST_MESSAGE, false);
        assertTrue(chatsPage.isExistChatMessage(CLIENT_7001, CLIENT_CHATS_FIRST_MESSAGE),
                "Не найдено сообщение \"" + CLIENT_CHATS_FIRST_MESSAGE + "\" в беседе " + CLIENT_7001);
    }

    @Story(value = "Проверка получения сообщения, на которое ответили")
    @Description(value = "1. Авторизуемся пользователем 7000. \n" +
            "2. Проверяем, что пришло ответное сообщение от пользователя 7001 ")
    @Test
    void test_Get_Reply_Message(){
        assertTrue(chatsPage.isExistChatMessage(CLIENT_7001, CLIENT_CHATS_TEXT_REPLY_MESSAGE),
                "Не найдено сообщение \"" + CLIENT_CHATS_TEXT_REPLY_MESSAGE + "\" в беседе " + CLIENT_7001);
        assertAll("Проверяем, отображается ли автор и текст сообщения, на которое отвечали",
                () -> assertTrue(chatsPage.isAuthReplyMessage(CLIENT_7000),
                        "Автор, сообщения, на которое отвечали не отображается или не совпадает"),
                () -> assertTrue(chatsPage.isReplyMessage(CLIENT_CHATS_FIRST_MESSAGE),
                        "Текст сообщение, на которое отвечали не совпадает или не отображается")
        );
    }

    @Story(value = "Удаляем беседу")
    @Description(value = "1. Авторизуемся под пользователем 7000 \n" +
            "2. Удаляем беседу с пользователем 7001 \n" +
            "3. Проверяем, что беседа с пользователем 7001 была удалена")
    @Test
    void test_Delete_Chat(){
        assertTrue(deleteChat(CLIENT_7001, CLIENT_CHATS_ITEM_DELETE_CHAT, false).
                isExistComments(CLIENT_7001, false),
                "Беседа с пользователем " + CLIENT_7001 + " отображается в списке ебесед после удаления");
    }

    @AfterAll
    static void tearDown(){
        Selenide.close();
    }
}
