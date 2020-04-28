package client.comments;

import client.APIToServer;
import client.RecourcesTests;
import io.qameta.allure.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static chat.ros.testing2.data.ContactsData.*;
import static chat.ros.testing2.data.LoginData.HOST_SERVER;
import static data.CommentsData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic(value = "Каналы")
@Feature(value = "Закрытый канал")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(RecourcesTests.class)
public class Test_B_ClosedChannel extends chat.ros.testing2.server.administration.ChannelsPage implements CommentsPage{

    private static APIToServer apiToServer = new APIToServer("https://" + HOST_SERVER + ":8080", CONTACT_NUMBER_7013 + "@ros.chat", USER_ACCOUNT_PASSWORD);;
    private static String IDForReceivingMessageUser = apiToServer.getContactIDBySurnameFromListOfContacts(CONTACT_NUMBER_7012, 60);
    private ChannelsPage clientChannelsPage = new ChannelsPage();

    @Story(value = "Создаём новый закрытый канал")
    @Description(value = "Авторизуемся под пользователем 7012 и создаём новый закрытый канал")
    @Order(1)
    @Test
    void test_Create_Closed_Channel_7012(){
        assertTrue(
                clientChannelsPage.createNewChannel(
                        CLIENT_NAME_CHANNEL_CLOSED,
                        CLIENT_DESCRIPTION_CHANNEL_CLOSED,
                        CLIENT_ITEM_NEW_CHANNEL,
                        CLIENT_TYPE_CHANNEL_CLOSED).
                        isExistComments(CLIENT_NAME_CHANNEL_CLOSED, true),
                "Канал не найден в списке бесед");
    }

    @Story(value = "Проверяем, отображается ли закрытый канал в СУ")
    @Story(value = "Авторизуемся в СУ, переходим в раздел Администрирование->Каналы и проверяем, отображается ли " +
            "закрытый канал в списке каналов")
    @Order(2)
    @Test
    void test_Show_Closed_Channel_In_MS(){
        assertTrue(isShowChannel(CLIENT_NAME_CHANNEL_CLOSED, false));
    }

    @Story(value = "Ищем на клиенте 7013 закрытый канал")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7013 и вводим в поле поиска имя закрытого канала." +
            " Проверяем, что канал не отображается в списке каналов")
    @Order(3)
    @Test
    void test_Search_Closed_Channel_7013(){
        assertTrue(
                clientChannelsPage.searchChannel(
                        CLIENT_NAME_CHANNEL_CLOSED,
                        CLIENT_TYPE_CHANNEL_CLOSED),
                "Канал отображается в списке бесед");
    }

    @Story(value = "Меняем название и описание закрытого канала")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7012, переходим в раздел информации о канале" +
            " и правим название и описание канала. Проверяем, что сохранения применились.")
    @Order(4)
    @Test
    void test_Edit_Name_And_Description_Closed_Channel_7012(){
        clientChannelsPage.editNameAndDescriptionChannel(
                CLIENT_NAME_CHANNEL_CLOSED,
                CLIENT_NEW_NAME_CHANNEL_CLOSED,
                CLIENT_NEW_DESCRIPTION_CHANNEL_CLOSED);
        assertTrue(isExistComments(
                CLIENT_NEW_NAME_CHANNEL_CLOSED, true),
                "Новое название не найдено в списке бесед");
        assertEquals(
                clientChannelsPage.getNameMainHeaderChannel(CLIENT_NEW_NAME_CHANNEL_CLOSED),
                CLIENT_NEW_NAME_CHANNEL_CLOSED,
                "Новое название канала не найдено в заголовке канала");
        assertEquals(
                clientChannelsPage.getDescriptionMainHeaderChannel(CLIENT_NEW_DESCRIPTION_CHANNEL_CLOSED),
                CLIENT_NEW_DESCRIPTION_CHANNEL_CLOSED,
                "Новое описание канала не найдено в заголовке канала");
        assertEquals(
                clientChannelsPage.getTitleName(CLIENT_NEW_NAME_CHANNEL_CLOSED),
                CLIENT_NEW_NAME_CHANNEL_CLOSED,
                "Новое название канала не найдено в разделе информация о канале");
        assertEquals(
                clientChannelsPage.getDescriptionChannel(),
                CLIENT_NEW_DESCRIPTION_CHANNEL_CLOSED,
                "Новое описание канала не найдено в разделе информация о канале");
    }

    @Story(value = "Делимся ссылкой на канал")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7012, переходим в раздел информации о канале" +
            " и нажимаем 'Поделиться ссылкой'. Проверяем, что сохранения применились.")
    @Order(5)
    @Test
    void test_Share_Link_Closed_Channel_7012() throws ExecutionException, InterruptedException {
        String[] apiGetMessageResult;

        Runnable clientShareLinkChannel = () -> {
            clientChannelsPage.shareLinkChannel(CLIENT_NEW_NAME_CHANNEL_CLOSED, CONTACT_NUMBER_7013);
        };

        CompletableFuture<String[]> socketGetMessage = CompletableFuture.supplyAsync(() ->{
            String[] getMessageResult = apiToServer.GetTextMessageFromUser(CLIENT_CHATS_SEND_EVENT,60);
            return getMessageResult;
        });

        clientShareLinkChannel.run();
        apiGetMessageResult = socketGetMessage.get();
        assertEquals(apiGetMessageResult[0], IDForReceivingMessageUser, "Сообщение пришло " +
                "не от пользователя " + CONTACT_NUMBER_7012);
        assertTrue(apiGetMessageResult[1].contains(CLIENT_NEW_NAME_CHANNEL_CLOSED),
                "Ссылка на канал " + CLIENT_NEW_NAME_CHANNEL_CLOSED + " не пришла");
    }

    @Story(value = "Копируем и делимся ссылкой на канал")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7012, переходим в раздел информации о канале," +
                         "нажимаем 'Копировать ссылку' и делимся ссылкой. Проверяем, что ссылка дошла до адресата.")
    @Order(6)
    @Test
    void test_Copy_And_Paste_Link_Closed_Channel_7012() throws ExecutionException, InterruptedException {
        String[] apiGetMessageResult;

        Runnable clientShareLinkChannel = () -> {
            clientChannelsPage.copyLinkChannel(CLIENT_NEW_NAME_CHANNEL_CLOSED, CONTACT_NUMBER_7013);
        };

        CompletableFuture<String[]> socketGetMessage = CompletableFuture.supplyAsync(() ->{
            String[] getMessageResult = apiToServer.GetTextMessageFromUser(CLIENT_CHATS_SEND_EVENT,60);
            return getMessageResult;
        });

        clientShareLinkChannel.run();
        apiGetMessageResult = socketGetMessage.get();
        assertEquals(apiGetMessageResult[0], IDForReceivingMessageUser, "Сообщение пришло " +
                "не от пользователя " + CONTACT_NUMBER_7012);
        assertTrue(apiGetMessageResult[1].contains(CLIENT_NEW_NAME_CHANNEL_CLOSED),
                "Ссылка на канал " + CLIENT_NEW_NAME_CHANNEL_CLOSED + " не пришла");
    }
}
