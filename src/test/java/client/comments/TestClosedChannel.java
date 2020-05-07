package client.comments;

import client.APIToServer;
import client.RecourcesTests;
import client.WatcherTests;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.*;

import static chat.ros.testing2.data.ContactsData.*;
import static chat.ros.testing2.data.LoginData.HOST_SERVER;
import static com.codeborne.selenide.Selenide.sleep;
import static data.CommentsData.*;
import static org.junit.jupiter.api.Assertions.*;

@Epic(value = "Каналы")
@Feature(value = "Закрытый канал")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(RecourcesTests.class)
@ExtendWith(WatcherTests.class)
public class TestClosedChannel extends chat.ros.testing2.server.administration.ChannelsPage implements CommentsPage{

    private static String apiHost = "https://" + HOST_SERVER + ":8080";
    private static String apiUser = CONTACT_NUMBER_7013 + "@ros.chat";
    private static APIToServer apiToServer = new APIToServer(apiHost, apiUser, USER_ACCOUNT_PASSWORD);;
    private static String IDForReceivingMessageUser = apiToServer.getContactIDBySurnameFromListOfContacts(CONTACT_NUMBER_7012, 60);
    private ChannelsPage clientChannelsPage = new ChannelsPage();
    private String[] admins = {CLIENT_USER_A, CLIENT_USER_B, CLIENT_USER_C};
    private String[] subscribers = {CLIENT_USER_D, CLIENT_USER_E, CLIENT_USER_F, CONTACT_NUMBER_7013};

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
    @Description(value = "Авторизуемся в СУ, переходим в раздел Администрирование->Каналы и проверяем, отображается ли " +
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
        assertAll("Проверяем новое название и описание канала",
                () -> assertTrue(isExistComments(
                        CLIENT_NEW_NAME_CHANNEL_CLOSED, true),
                        "Новое название не найдено в списке бесед"),
                () -> assertEquals(clientChannelsPage.getNameMainHeaderChannel(CLIENT_NEW_NAME_CHANNEL_CLOSED),
                        CLIENT_NEW_NAME_CHANNEL_CLOSED,
                        "Новое название канала не найдено в заголовке канала"),
                () -> assertEquals(clientChannelsPage.
                                getDescriptionMainHeaderChannel(CLIENT_NEW_DESCRIPTION_CHANNEL_CLOSED),
                        CLIENT_NEW_DESCRIPTION_CHANNEL_CLOSED,
                        "Новое описание канала не найдено в заголовке канала"),
                () -> assertEquals(clientChannelsPage.getTitleName(CLIENT_NEW_NAME_CHANNEL_CLOSED),
                        CLIENT_NEW_NAME_CHANNEL_CLOSED,
                        "Новое название канала не найдено в разделе информация о канале"),
                () -> assertEquals(clientChannelsPage.getDescriptionChannel(),
                        CLIENT_NEW_DESCRIPTION_CHANNEL_CLOSED,
                        "Новое описание канала не найдено в разделе информация о канале")
        );

    }

    @Story(value = "Копируем и делимся ссылкой на канал")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7012, переходим в раздел информации о канале," +
            "нажимаем 'Копировать ссылку' и делимся ссылкой. Проверяем, что ссылка дошла до адресата.")
    @Order(5)
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
        assertAll("Проверяем, совпадает ли ID и сообщение с ссылкой на канал",
                () -> assertEquals(apiGetMessageResult[0], IDForReceivingMessageUser, "Сообщение пришло " +
                        "не от пользователя " + CONTACT_NUMBER_7012),
                () -> assertTrue(apiGetMessageResult[1].contains(CLIENT_NEW_NAME_CHANNEL_CLOSED),
                        "Ссылка на канал " + CLIENT_NEW_NAME_CHANNEL_CLOSED + " не пришла")
        );

        //sleep(60000);
    }

    @Story(value = "Делимся ссылкой на канал")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7012, переходим в раздел информации о канале" +
            " и нажимаем 'Поделиться ссылкой'. Проверяем, что сохранения применились.")
    @Order(6)
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
        assertAll("Проверяем, совпадает ли ID и сообщение с ссылкой на канал",
                () -> assertEquals(apiGetMessageResult[0], IDForReceivingMessageUser, "Сообщение пришло " +
                        "не от пользователя " + CONTACT_NUMBER_7012),
                () -> assertTrue(apiGetMessageResult[1].contains(CLIENT_NEW_NAME_CHANNEL_CLOSED),
                        "Ссылка на канал " + CLIENT_NEW_NAME_CHANNEL_CLOSED + " не пришла")
        );

        //sleep(60000);
    }

    @Story(value = "Добавляем администраторов и подписчиков")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7012, переходим в раздел информации о канале," +
            "нажимаем 'Администраторы' и добавляем администраторов в канал. Проверяем, что администарторы добавились." +
            " Затем нажимаем 'Подписчики' и добавляем подписчиков в канал. Проверяем, что подписчики добавились.")
    @Order(7)
    @Test
    void test_Add_User_Closed_Channel_7012() {
        assertAll("Проверяем, что добавляются администраторы и подписчики",
                () -> assertTrue(clientChannelsPage.
                            addUsersChannel(CLIENT_NEW_NAME_CHANNEL_CLOSED, CLIENT_INFO_ITEM_ADMIN_CHANNEL, admins).
                            isCountUsersChannel() == admins.length + 1,
                    "Количество добавленных администраторов меньше " + admins.length),
                () -> assertTrue(clientChannelsPage.
                                addUsersChannel(CLIENT_NEW_NAME_CHANNEL_CLOSED, CLIENT_INFO_ITEM_USER_CHANNEL, subscribers).
                                isCountUsersChannel() == subscribers.length,
                        "Количество добавленных подписчиков меньше " + subscribers.length)
        );
    }

    @Story(value = "Пользователь подписывается на канал")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7013, переходим в раздел информации о канале," +
            "нажимаем 'Подписаться на канал'. Проверяем, что пользователь подписался на канал. Проверяем сколько" +
            "администраторов и подписчиков в канале отображается у подписчика")
    @Order(8)
    @Test
    void test_Subscriber_Closed_Channel_7013() {
        assertAll("Подписываемся на канал и проверяем сколько администраторов и подписчиков" +
                " отображается у подписчика",
                () -> assertTrue(clientChannelsPage.
                                userSubscriberChannel(CLIENT_NEW_NAME_CHANNEL_CLOSED).
                                isActionInfoWrapper(CLIENT_INFO_EXIT_CHANNEL, true),
                        "Пользователь не подписался на закрытый канал"),
                () -> assertTrue(clientChannelsPage.actionInfoWrapper(CLIENT_INFO_ITEM_ADMIN_CHANNEL).
                                isCountUsersChannel() == admins.length + 1,
                        "Количество администраторов у пользователя отображается меньше " + admins.length + 1),
                () -> assertTrue(clientChannelsPage.actionInfoWrapper(CLIENT_INFO_ITEM_USER_CHANNEL).
                        isCountUsersChannel() == subscribers.length,
                        "Количество подписчиков у пользователя отображается меньше " + subscribers.length)
        );
    }

    @AfterAll
    static void tearDown(){
        apiToServer.disconnect();
    }
}
