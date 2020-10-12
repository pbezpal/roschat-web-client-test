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
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic(value = "Беседы")
@Feature(value = "Групповая беседа")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(RecourcesTests.class)
@ExtendWith(WatcherTests.class)
public class TestGroupChatUser7000 extends GroupChatPage{

    String[] users = {CLIENT_7001, CLIENT_7002, CLIENT_7003};

    @Story(value = "Создаём групповую беседу")
    @Description(value = "1. Авторизуемся под пользователем 7000\n" +
            "2. Переходим в раздел беседы\n" +
            "3. Выбираем в контекстном меню 'Новая группа'\n" +
            "4. Создаём групповую беседу\n" +
            "5. Проверяем, что групповая беседа была создана и отображается автор созданной группы")
    @Test
    @Order(1)
    void test_Create_New_Group_Chat(){
        createNewGroupChat(CLIENT_GROUP_CHAT_NAME, users);
        assertTrue(clickItemComments().isExistComments(CLIENT_GROUP_CHAT_NAME, true),
                "Групповая беседа " + CLIENT_GROUP_CHAT_NAME + " не найдена в списке бесед");
        assertTrue(clickItemComments().clickChat(CLIENT_GROUP_CHAT_NAME).isSystemMessage(CLIENT_GROUP_CHAT_AUTH),
                "Не найдено системное сообщение " + CLIENT_GROUP_CHAT_NAME);
    }

    @Story(value = "Проверка отправки сообщения в групповой чат")
    @Description(value = "1. Авторизумеся под пользовтаелем 7000\n" +
            "2. Переходим в раздел беседы\n" +
            "3. Выбираем гурпповую беседу в списке бесед\"" +
            "4. Отправляем сообщение в групповую беседу\n" +
            "5. Проверяем, что сообщение отображается в писке сообщений")
    @Test
    @Order(2)
    void test_Send_First_Message_Group_Chat(){
        sendMessageToGroupChat(CLIENT_GROUP_CHAT_NAME, CLIENT_CHATS_FIRST_MESSAGE, false);
        assertTrue(isExistMessage(CLIENT_CHATS_FIRST_MESSAGE),
                "Не найдено сообщение " + CLIENT_CHATS_FIRST_MESSAGE + " " +
                        "в групповй беседе " + CLIENT_GROUP_CHAT_NAME);
    }

    @Story(value = "Удаляем беседу")
    @Description(value = "1. Авторизуемся под пользователем 7000 \n" +
            "2. Удаляем групповую беседу \n" +
            "3. Проверяем, что групповая беседа была удалена")
    @Test
    @Order(3)
    void test_Delete_Group_Chat(){
        assertTrue(deleteChat(CLIENT_GROUP_CHAT_NAME, CLIENT_CHATS_ITEM_DELETE_CHAT, false).
                        isExistComments(CLIENT_GROUP_CHAT_NAME, false),
                "Групповая беседа " + CLIENT_GROUP_CHAT_NAME + " отображается в списке ебесед после удаления");
    }

}
