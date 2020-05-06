package client.comments;

import client.APIToServer;
import client.RecourcesTests;
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

@Epic(value = "Каналы")
@Feature(value = "Публичный проверенный канал")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(RecourcesTests.class)
public class Test_B_PublicChannel extends chat.ros.testing2.server.administration.ChannelsPage implements CommentsPage{

    private static APIToServer apiToServer = new APIToServer("https://" + HOST_SERVER + ":8080", CONTACT_NUMBER_7013 + "@ros.chat", USER_ACCOUNT_PASSWORD);;
    private static String IDForReceivingMessageUser = apiToServer.getContactIDBySurnameFromListOfContacts(CONTACT_NUMBER_7012, 60);
    private ChannelsPage clientChannelsPage = new ChannelsPage();
    private String[] admins = {CLIENT_USER_A, CLIENT_USER_B, CLIENT_USER_C};
    private String[] subscribers = {CLIENT_USER_D, CLIENT_USER_E, CLIENT_USER_F, CONTACT_NUMBER_7013};

    @Story(value = "Создаём новый публичный канал")
    @Description(value = "Авторизуемся под пользователем 7012 и создаём новый публичный канал")
    @Order(1)
    @Test
    void test_Create_Public_Channel_7012(){
        assertTrue(
                clientChannelsPage.createNewChannel(
                        CLIENT_NAME_CHANNEL_PUBLIC_PROVEN,
                        CLIENT_DESCRIPTION_CHANNEL_PUBLIC_PROVEN,
                        CLIENT_ITEM_NEW_CHANNEL,
                        CLIENT_TYPE_CHANNEL_PUBLIC).
                        isExistComments(CLIENT_NAME_CHANNEL_PUBLIC_PROVEN, true),
                "Канал не найден в списке бесед");
    }

    @Story(value = "Меняем название и описание публичного проверенного канала")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7012, переходим в раздел информации о канале" +
            " и правим название и описание канала. Проверяем, что сохранения применились.")
    @Order(2)
    @Test
    void test_Edit_Name_And_Description_Public_Channel_7012(){
        clientChannelsPage.editNameAndDescriptionChannel(
                CLIENT_NAME_CHANNEL_PUBLIC_PROVEN,
                CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN,
                CLIENT_NEW_DESCRIPTION_PUBLIC_CHANNEL_PROVEN);
        assertAll("Проверяем новое название и описание канала",
                () -> assertTrue(isExistComments(
                        CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN, true),
                        "Новое название не найдено в списке бесед"),
                () -> assertEquals(
                        clientChannelsPage.getNameMainHeaderChannel(CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN),
                        CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN,
                        "Новое название канала не найдено в заголовке канала"),
                () -> assertEquals(
                        clientChannelsPage.getDescriptionMainHeaderChannel(CLIENT_NEW_DESCRIPTION_PUBLIC_CHANNEL_PROVEN),
                        CLIENT_NEW_DESCRIPTION_PUBLIC_CHANNEL_PROVEN,
                        "Новое описание канала не найдено в заголовке канала"),
                () -> assertEquals(
                        clientChannelsPage.getTitleName(CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN),
                        CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN,
                        "Новое название канала не найдено в разделе информация о канале"),
                () -> assertEquals(
                        clientChannelsPage.getDescriptionChannel(),
                        CLIENT_NEW_DESCRIPTION_PUBLIC_CHANNEL_PROVEN,
                        "Новое описание канала не найдено в разделе информация о канале")
        );

    }

    @Story(value = "Копируем и делимся ссылкой на канал")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7012, переходим в раздел информации о канале," +
            "нажимаем 'Копировать ссылку' и делимся ссылкой. Проверяем, что ссылка дошла до адресата.")
    @Order(3)
    @Test
    void test_Copy_And_Paste_Link_Public_Channel_7012() throws ExecutionException, InterruptedException {
        String[] apiGetMessageResult;

        Runnable clientShareLinkChannel = () -> {
            clientChannelsPage.copyLinkChannel(CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN, CONTACT_NUMBER_7013);
        };

        CompletableFuture<String[]> socketGetMessage = CompletableFuture.supplyAsync(() ->{
            String[] getMessageResult = apiToServer.GetTextMessageFromUser(CLIENT_CHATS_SEND_EVENT,60);
            return getMessageResult;
        });

        clientShareLinkChannel.run();
        apiGetMessageResult = socketGetMessage.get();
        assertAll("Проверяем пришла ли ссылка на канал и от какого пользователя",
                () -> assertEquals(apiGetMessageResult[0], IDForReceivingMessageUser, "Сообщение пришло " +
                        "не от пользователя " + CONTACT_NUMBER_7012),
                () -> assertTrue(apiGetMessageResult[1].contains(CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN),
                        "Ссылка на канал " + CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN + " не пришла")
        );
    }

    @Story(value = "Делимся ссылкой на канал")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7012, переходим в раздел информации о канале" +
            " и нажимаем 'Поделиться ссылкой'. Проверяем, что ссылка дошла до адресата.")
    @Order(4)
    @Test
    void test_Share_Link_Public_Channel_7012() throws ExecutionException, InterruptedException {
        String[] apiGetMessageResult;

        Runnable clientShareLinkChannel = () -> {
            clientChannelsPage.shareLinkChannel(CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN, CONTACT_NUMBER_7013);
        };

        CompletableFuture<String[]> socketGetMessage = CompletableFuture.supplyAsync(() ->{
            String[] getMessageResult = apiToServer.GetTextMessageFromUser(CLIENT_CHATS_SEND_EVENT,60);
            return getMessageResult;
        });

        clientShareLinkChannel.run();
        apiGetMessageResult = socketGetMessage.get();
        assertAll("Проверяем пришла ли ссылка на канал и от какого пользователя",
                () -> assertEquals(apiGetMessageResult[0], IDForReceivingMessageUser, "Сообщение пришло " +
                        "не от пользователя " + CONTACT_NUMBER_7012),
                () -> assertTrue(apiGetMessageResult[1].contains(CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN),
                        "Ссылка на канал " + CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN + " не пришла")
        );
    }

    @Story(value = "Делаем проверенным публичный канал")
    @Description(value = "Авторизуемся в СУ, переходим в раздел Администрирование->Каналы, делаем публичный канал проверенным")
    @Order(5)
    @Test
    void test_Do_Proven_Channel(){
        assertTrue(isShowChannel(CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN, true));
        doTestedChannel(CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN);
    }

    @Story(value = "Проверяем статус публичного канала под учётной записью 7012")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7012. Проверяем, что у канала появился статус Проверенный")
    @Order(6)
    @Test
    void test_Check_Status_Public_Channel_7012(){
        clientChannelsPage.clickItemComments();
        assertAll("Проверяем статус канала - проверенный",
                () -> assertTrue(clientChannelsPage.isStatusTestedChannelListChat(CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN),
                        "Отсутствует статус Проверенный у канала в разделе Беседы"),
                () -> assertTrue(clientChannelsPage.isStatusTestedChannelMainHeader(CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN),
                        "Отсутствует статус Проверенный в заголовке канала"),
                () -> assertTrue(clientChannelsPage.isStatusTestedInfoChannel(CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN),
                        "Отсутствует статус Проверенный в разделе информация о канале")
        );
    }

    @Story(value = "Ищем на клиенте 7013 публичный канал")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7013 и вводим в поле поиска имя публичного канала." +
            " Проверяем, что у канала статус Проверенный")
    @Order(7)
    @Test
    void test_Search_Public_Channel_7013(){
        assertTrue(
                clientChannelsPage.searchChannel(
                        CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN,
                        CLIENT_TYPE_CHANNEL_PUBLIC),
                "Канал не найден");
        assertAll("Проверяем статус канала - проверенный",
                () -> assertTrue(clientChannelsPage.isStatusTestedChannelListChat(CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN),
                        "Отсутствует статус Проверенный у канала в разделе Беседы"),
                () -> assertTrue(clientChannelsPage.isStatusTestedChannelMainHeader(CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN),
                        "Отсутствует статус Проверенный в заголовке канала"),
                () -> assertTrue(clientChannelsPage.isStatusTestedInfoChannel(CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN),
                        "Отсутствует статус Проверенный в разделе информация о канале")
        );
    }

    @Story(value = "Добавляем администраторов и подписчиков")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7012, переходим в раздел информации о канале," +
            "нажимаем 'Администраторы' и добавляем администраторов в канал. Проверяем, что администарторы добавились." +
            " Затем нажимаем 'Подписчики' и добавляем подписчиков в канал. Проверяем, что подписчики добавились.")
    @Order(8)
    @Test
    void test_Add_User_Public_Proven_Channel_7012() {
        assertAll("Проверяем, что добавляются администраторы и подписчики",
                () -> assertTrue(clientChannelsPage.
                                addUsersChannel(CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN,
                                        CLIENT_INFO_ITEM_ADMIN_CHANNEL,
                                        admins).
                                isCountUsersChannel() == admins.length + 1,
                        "Количество добавленных администраторов меньше " + admins.length),
                () -> assertTrue(clientChannelsPage.
                                addUsersChannel(CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN,
                                        CLIENT_INFO_ITEM_USER_CHANNEL,
                                        subscribers).
                                isCountUsersChannel() == subscribers.length,
                        "Количество добавленных подписчиков меньше " + subscribers.length)
        );
    }

    @Story(value = "Пользователь подписывается на канал")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7013, переходим в раздел информации о канале," +
            "нажимаем 'Подписаться на канал'. Проверяем, что пользователь подписался на канал. Проверяем сколько" +
            "администраторов и подписчиков в канале отображается у подписчика")
    @Order(9)
    @Test
    void test_Subscriber_Public_Proven_Channel_7013() {
        assertAll("Подписываемся на канал и проверяем сколько администраторов и подписчиков" +
                        " отображается у подписчика",
                () -> assertTrue(clientChannelsPage.
                                userSubscriberChannel(CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN).
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
    static void afterAllTests(){
        apiToServer.disconnect();
    }
}
