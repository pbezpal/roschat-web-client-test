package client.comments;

import client.RecourcesTests;
import client.WatcherTests;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static data.CommentsData.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic(value = "Беседы")
@Feature(value = "Групповая беседа")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(RecourcesTests.class)
@ExtendWith(WatcherTests.class)
public class TestGroupChatUser7001 extends GroupChatPage{

    @Story(value = "Ищем групповую беседу после создания на другом клиенте")
    @Description(value = "1. Авторизуемся под пользователем 7001\n" +
            "2. Проверяем, что групповая беседа была создана и отображается автор созданной группы")
    @Test
    @Order(1)
    void test_Check_Exist_Group_Chat(){
        assertTrue(clickItemComments().isExistComments(CLIENT_GROUP_CHAT_NAME, true),
                "Групповая беседа " + CLIENT_GROUP_CHAT_NAME + " не найдена в списке бесед");
        assertTrue(clickItemComments().clickChat(CLIENT_GROUP_CHAT_NAME).isSystemMessage(CLIENT_GROUP_CHAT_AUTH),
                "Не найдено системное сообщение " + CLIENT_GROUP_CHAT_NAME);
    }

    @Test
    @Order(2)
    void test_Reply_Message_Group_Chat(){
        sendMessageToGroupChat(CLIENT_GROUP_CHAT_NAME, CLIENT_CHATS_TEXT_REPLY_MESSAGE, true);
        assertAll("Проверяем, отображается ли автор и текст сообщения, на которое отвечали",
                () -> assertTrue(clickItemComments().
                                clickChat(CLIENT_GROUP_CHAT_NAME).
                                isAuthReplyMessage(CLIENT_7000),
                        "Автор, сообщения, на которое отвечали не отображается или не совпадает"),
                () -> assertTrue(clickItemComments().
                                clickChat(CLIENT_GROUP_CHAT_NAME).
                                isReplyMessage(CLIENT_CHATS_FIRST_MESSAGE),
                        "Текст сообщение, на которое отвечали не совпадает или не отображается"),
                () -> assertTrue(clickItemComments().
                        clickChat(CLIENT_GROUP_CHAT_NAME).
                        isExistMessage(CLIENT_CHATS_TEXT_REPLY_MESSAGE),
                        "Текст ответного сообщения не совпадает с тем, которое отправили")
        );
    }

    @Story(value = "Удаляем беседу")
    @Description(value = "1. Авторизуемся под пользователем 7001 \n" +
            "2. Удаляем групповую беседу \n" +
            "3. Проверяем, что групповая беседа была удалена")
    @Test
    @Order(3)
    void test_Delete_Group_Chat(){
        assertTrue(deleteChat(CLIENT_GROUP_CHAT_NAME, CLIENT_CHATS_ITEM_DELETE_CHAT, true).
                        isExistComments(CLIENT_GROUP_CHAT_NAME, false),
                "Групповая беседа " + CLIENT_GROUP_CHAT_NAME + " отображается в списке ебесед после удаления");
    }

}
