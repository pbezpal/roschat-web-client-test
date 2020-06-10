package client.comments;

import client.APIToServer;
import client.Helper;
import client.RecourcesTests;
import client.WatcherTests;
import client.helper.SSHGetCommand;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.*;

import static chat.ros.testing2.data.ContactsData.*;
import static chat.ros.testing2.data.LoginData.HOST_SERVER;
import static data.CommentsData.*;
import static org.junit.jupiter.api.Assertions.*;

@Epic(value = "Каналы")
@Feature(value = "Закрытый канал")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(RecourcesTests.class)
@ExtendWith(WatcherTests.class)
public class TestClosedChannel extends chat.ros.testing2.server.administration.ChannelsPage implements CommentsPage, Helper {

    private static String apiHost = "https://" + HOST_SERVER + ":8080";
    private static String apiUser = CONTACT_NUMBER_7013 + "@ros.chat";
    private static APIToServer apiToServer = null;
    private static String CIDUser = SSHGetCommand.isCheckQuerySSH(
            "sudo -u roschat psql -c \"select cid, login from users;\" " +
                    "| grep " + CONTACT_NUMBER_7012 + " | awk '{print $1}'"
    );
    private static ChannelsPage clientChannelsPage = new ChannelsPage();
    private String[] admins = {CLIENT_USER_A, CLIENT_USER_B, CLIENT_USER_C};
    private String[] subscribers = {CLIENT_USER_D, CLIENT_USER_E, CLIENT_USER_F, CONTACT_NUMBER_7013};
    private static String nameChannel = "CHC%1$s";
    private static String newNameChannel = null;
    private boolean status = false;

    @BeforeAll
    static void setUp(){
        nameChannel = String.format(nameChannel,System.currentTimeMillis());
        newNameChannel = nameChannel + System.currentTimeMillis();
    }

    @Story(value = "Создаём новый закрытый канал")
    @Description(value = "Авторизуемся под пользователем 7012 и создаём новый закрытый канал")
    @Order(1)
    @Test
    void test_Create_Closed_Channel_7012(){
        assertTrue(clientChannelsPage.createNewChannel(
                nameChannel,
                CLIENT_DESCRIPTION_CHANNEL_CLOSED,
                CLIENT_ITEM_NEW_CHANNEL,
                CLIENT_TYPE_CHANNEL_CLOSED).
                        isExistComments(nameChannel, true),
                "Канал не найден в списке бесед");
        clickChat(nameChannel);
        assertTrue(clientChannelsPage.isTextInfoClosedChannel(true),"Отсутствует надпись Закрытый " +
                "в разделе 'Информация о канале'");
        status = true;
    }

    @Story(value = "Ищем на клиенте 7013 закрытый канал")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7013 и вводим в поле поиска имя закрытого канала." +
            " Проверяем, что канал не отображается в списке каналов")
    @Order(2)
    @Test
    void test_Search_Closed_Channel_7013(){
        assertTrue(status, "Канал не был создан");
        assertTrue(
                clientChannelsPage.searchChannel(
                        nameChannel,
                        CLIENT_TYPE_CHANNEL_CLOSED),
                "Канал отображается в списке бесед");
    }

    @Story(value = "Копируем и делимся ссылкой на канал")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7012, переходим в раздел информации о канале," +
            "нажимаем 'Копировать ссылку' и делимся ссылкой. Проверяем, что ссылка дошла до адресата.")
    @Order(3)
    @Test
    void test_Copy_And_Paste_Link_Closed_Channel_7012() throws ExecutionException, InterruptedException {
        assertTrue(status, "Канал не был создан");
        String[] apiGetMessageResult;

        Runnable clientShareLinkChannel = () -> {
            clientChannelsPage.copyLinkChannel(
                    nameChannel,
                    CONTACT_NUMBER_7013
            );
        };

        CompletableFuture<String[]> socketGetMessage = CompletableFuture.supplyAsync(() ->{
            apiToServer = getApiToServer(apiHost, apiUser, apiToServer);
            String[] getMessageResult = apiToServer.GetTextMessageFromUser(CLIENT_CHATS_SEND_EVENT, "data",60);
            return getMessageResult;
        });

        clientShareLinkChannel.run();
        apiGetMessageResult = socketGetMessage.get();
        assertAll("Проверяем, есть ли иконка, совпадает ли ID и сообщение с ссылкой на канал",
                () -> assertTrue(clientChannelsPage.isIconCopyLinkChannel(nameChannel),
                        "Нет иконки 'Копировать ссылку'"),
                () -> assertEquals(apiGetMessageResult[0], CIDUser, "Сообщение пришло " +
                        "не от пользователя " + CONTACT_NUMBER_7012),
                () -> assertTrue(apiGetMessageResult[1].contains(nameChannel),
                        "Ссылка на канал " + nameChannel + " не пришла")
        );
    }

    @Story(value = "Делимся ссылкой на канал")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7012, переходим в раздел информации о канале" +
            " и нажимаем 'Поделиться ссылкой'. Проверяем, что сохранения применились.")
    @Order(4)
    @Test
    void test_Share_Link_Closed_Channel_7012() throws ExecutionException, InterruptedException {
        assertTrue(status, "Канал не был создан");
        String[] apiGetMessageResult;

        Runnable clientShareLinkChannel = () -> {
            clientChannelsPage.shareLinkChannel(nameChannel, CONTACT_NUMBER_7013);
        };

        CompletableFuture<String[]> socketGetMessage = CompletableFuture.supplyAsync(() ->{
            apiToServer = getApiToServer(apiHost, apiUser, apiToServer);
            String[] getMessageResult = apiToServer.GetTextMessageFromUser(CLIENT_CHATS_SEND_EVENT, "data",60);
            return getMessageResult;
        });

        clientShareLinkChannel.run();
        apiGetMessageResult = socketGetMessage.get();
        assertAll("Проверяем, есть ли иконка, совпадает ли ID и сообщение с ссылкой на канал",
                () -> assertTrue(clientChannelsPage.isIconShareChannel(),
                        "Нет иконки 'Поделиться ссылкой'"),
                () -> assertEquals(apiGetMessageResult[0], CIDUser, "Сообщение пришло " +
                        "не от пользователя " + CONTACT_NUMBER_7012),
                () -> assertTrue(apiGetMessageResult[1].contains(nameChannel),
                        "Ссылка на канал " + nameChannel + " не пришла")
        );
    }

    @Story(value = "Добавляем администраторов и подписчиков")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7012, переходим в раздел информации о канале," +
            "нажимаем 'Администраторы' и добавляем администраторов в канал. Проверяем, что администарторы добавились." +
            " Затем нажимаем 'Подписчики' и добавляем подписчиков в канал. Проверяем, что подписчики добавились.")
    @Order(5)
    @Test
    void test_Add_User_Closed_Channel_7012() {
        assertTrue(status, "Канал не был создан");
        clientChannelsPage.addUsersChannel(nameChannel, CLIENT_INFO_ITEM_ADMIN_CHANNEL, admins);
        assertAll("Проверяем, что есть иконка и добавляются администраторы",
                () -> assertTrue(clientChannelsPage.isIconUserPlus(),
                        "Нет иконки добавления добавления администраторов"),
                () -> assertTrue(clientChannelsPage.getCountUsersChannel() == admins.length + 1,
                    "Количество добавленных администраторов меньше " + admins.length)
        );
        clientChannelsPage.addUsersChannel(nameChannel, CLIENT_INFO_ITEM_USER_CHANNEL, subscribers);
        assertAll("Проверяем, что есть иконка и добавляются подписчики",
                () -> assertTrue(clientChannelsPage.isIconUserPlus(),
                        "Нет иконки добавления добавления подписчиков"),
                () -> assertTrue(clientChannelsPage.getCountUsersChannel() == subscribers.length,
                        "Количество добавленных администраторов меньше " + subscribers.length)
        );
    }

    @Story(value = "Пользователь подписывается на канал")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7013, переходим в раздел информации о канале," +
            "нажимаем 'Подписаться на канал'. Проверяем, что пользователь подписался на канал. Проверяем сколько" +
            "администраторов и подписчиков в канале отображается у подписчика")
    @Order(6)
    @Test
    void test_Subscriber_Closed_Channel_7013() {
        assertTrue(status, "Канал не был создан");
        assertAll("Проверяем, что есть иконка," +
                          "подписываемся на канал," +
                          "проверяем сколько администраторов и подписчиков отображается у подписчика",
                () -> assertTrue(clientChannelsPage.isIconSignOut(nameChannel),
                        "Нет иконки Подписаться на канал"),
                () -> assertTrue(clientChannelsPage.
                                userSubscriberChannel(nameChannel).
                                isActionInfoWrapper(CLIENT_INFO_EXIT_CHANNEL, true),
                        "Пользователь не подписался на закрытый канал"),
                () -> assertTrue(clientChannelsPage.isIconSignOut(nameChannel),
                        "Нет иконки Выйти из канала"),
                () -> assertTrue(clientChannelsPage.actionInfoWrapper(CLIENT_INFO_ITEM_ADMIN_CHANNEL).
                                getCountUsersChannel() == admins.length + 1,
                        "Количество администраторов у пользователя отображается меньше " + admins.length + 1),
                () -> assertTrue(clientChannelsPage.actionInfoWrapper(CLIENT_INFO_ITEM_USER_CHANNEL).
                                getCountUsersChannel() == subscribers.length,
                        "Количество подписчиков у пользователя отображается меньше " + subscribers.length)
        );
    }

    @Story(value = "Новая публикация")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7012, в контекстном меню выбираем 'Новая публикация'" +
            " опубликовываем публикацию. Проверяем, что публикация была успешно опубликована.")
    @Order(7)
    @Test
    void test_New_Publication_Closed_Channel_7012() throws ExecutionException, InterruptedException {
        assertTrue(status, "Канал не был создан");
        String[] apiGetMessageResult;

        Runnable clientNewPublicationChannel = () -> {
            clientChannelsPage.newPublication(
                    nameChannel,
                    CLIENT_TITLE_PUBLICATION_CHANNEL,
                    CLIENT_DESCRIPTION_PUBLICATION_CLOSED_CHANNEL);
        };

        CompletableFuture<String[]> socketGetMessage = CompletableFuture.supplyAsync(() ->{
            apiToServer = getApiToServer(apiHost, apiUser, apiToServer);
            String[] getMessageResult = apiToServer.GetTextMessageFromUser(
                    CLIENT_PUBLICATION_EVENT,
                    "title",
                    60
            );
            return getMessageResult;
        });

        clientNewPublicationChannel.run();
        apiGetMessageResult = socketGetMessage.get();
        assertAll("Проверяем, опубликована ли новая публикация",
                () -> assertEquals(apiGetMessageResult[0], CIDUser, "Сообщение пришло " +
                        "не от пользователя " + CONTACT_NUMBER_7012),
                () -> assertEquals(apiGetMessageResult[1], CLIENT_TITLE_PUBLICATION_CHANNEL,"Текст заголовка " +
                        "публикации не совпадает с эталоном "  + CLIENT_TITLE_PUBLICATION_CHANNEL),
                () -> assertEquals(clientChannelsPage.getAuthorPublication("1"),
                        CONTACT_NUMBER_7012,
                        "В публикации указан неправильный автор"),
                () -> assertTrue(clientChannelsPage.isShowTitlePublication("1", CLIENT_TITLE_PUBLICATION_CHANNEL),
                        "Заголовок публикации не совпадает с " + CLIENT_TITLE_PUBLICATION_CHANNEL),
                () -> assertTrue(clientChannelsPage.
                                isShowDescriptionPublication("1", CLIENT_DESCRIPTION_PUBLICATION_CLOSED_CHANNEL, true),
                        "Описание публикации не совпадает с " + CLIENT_DESCRIPTION_PUBLICATION_CLOSED_CHANNEL)
        );
    }

    @Story(value = "Проверяем, видна ли публикация у подписчика")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7013 и проверяем, что новая публикация" +
            "видна у подписчика")
    @Order(8)
    @Test
    void test_Check_New_Publication_Closed_Channel_7013(){
        assertTrue(status, "Канал не был создан");
        clientChannelsPage.clickItemComments();
        clientChannelsPage.clickChat(nameChannel);
        assertAll("Проверяем, отображается ли публикация у подписчика",
                () -> assertEquals(clientChannelsPage.getAuthorPublication("1"),
                        CONTACT_NUMBER_7012,
                        "В публикации указан неправильный автор"),
                () -> assertTrue(clientChannelsPage.isShowTitlePublication("1", CLIENT_TITLE_PUBLICATION_CHANNEL),
                        "Заголовок публикации не совпадает с " + CLIENT_TITLE_PUBLICATION_CHANNEL),
                () -> assertTrue(clientChannelsPage.
                                isShowDescriptionPublication("1", CLIENT_DESCRIPTION_PUBLICATION_CLOSED_CHANNEL, true),
                        "Описание публикации не совпадает с " + CLIENT_DESCRIPTION_PUBLICATION_CLOSED_CHANNEL)
        );
    }

    @Story(value = "Переходим в публикацию, которой с нами поделились")
    @Description(value = "Авторизуемся под кчётной записью 7012. Пользователь 7013 деится публикацией канала через API." +
            "Проверяем, что пользователь 7012 видит публикацию и может перейти к ней")
    @Order(9)
    @Test
    void test_Get_Share_Publication_Closed_channel_7012(){
        assertTrue(status, "Канал не был создан");
        String message = "{\"type\":\"publication\",\"chId\":" + getIDChannel(nameChannel) + ",\"pubId\":1}";

        Runnable clientGetMessage = () -> {
            clientChannelsPage.clickItemComments();
            clientChannelsPage.clickChat(CONTACT_NUMBER_7013);
            clientChannelsPage.clickSharePublicationChannel(nameChannel);
            assertAll("Проверяем, отображается ли публикация после перехода к публикации",
                    () -> assertEquals(clientChannelsPage.getAuthorPublication("1"),
                            CONTACT_NUMBER_7012,
                            "В публикации указан неправильный автор"),
                    () -> assertTrue(clientChannelsPage.isShowTitlePublication("1", CLIENT_TITLE_PUBLICATION_CHANNEL),
                            "Заголовок публикации не совпадает с " + CLIENT_TITLE_PUBLICATION_CHANNEL),
                    () -> assertTrue(clientChannelsPage.
                                    isShowDescriptionPublication("1", CLIENT_DESCRIPTION_PUBLICATION_CLOSED_CHANNEL, true),
                            "Описание публикации не совпадает с " + CLIENT_DESCRIPTION_PUBLICATION_CLOSED_CHANNEL)
            );
        };

        Thread apiSendMessage = new Thread() {
            @Override
            public void run() {
                apiToServer = getApiToServer(apiHost, apiUser, apiToServer);
                Selenide.sleep(3000);
                apiToServer.SendTextMessageToUser(
                        "user",
                        CIDUser,
                        "data",
                        message,
                        30
                );
            }
        };

        apiSendMessage.start();
        clientGetMessage.run();
    }

    @Story(value = "Делимся публикацией")
    @Description(value = "Авторизуемся под учётной записью 7012 и делимся публикацией канала с пользователем 7013." +
            "Проверяем при помощи API, что успешно поделились публикацией.")
    @Order(10)
    @Test
    void test_Send_Share_Publication_Closed_Channel_7012() throws ExecutionException, InterruptedException {
        assertTrue(status, "Канал не был создан");
        String[] apiGetMessageResult;
        String message = "{\"type\":\"publication\",\"chId\":" + getIDChannel(nameChannel) + ",\"pubId\":1}";

        Runnable clientSharePublicationChannel = () -> {
            clientChannelsPage.sharePublicationChannel(
                    nameChannel,
                    "1",
                    CONTACT_NUMBER_7013);
        };

        CompletableFuture<String[]> socketGetMessage = CompletableFuture.supplyAsync(() ->{
            apiToServer = getApiToServer(apiHost, apiUser, apiToServer);
            String[] getMessageResult = apiToServer.GetTextMessageFromUser(
                    CLIENT_CHATS_SEND_EVENT,
                    "data",
                    60
            );
            return getMessageResult;
        });

        clientSharePublicationChannel.run();
        apiGetMessageResult = socketGetMessage.get();
        assertAll("Проверяем, поделились ли публикацией",
                () -> assertEquals(apiGetMessageResult[0], CIDUser, "Сообщение пришло " +
                        "не от пользователя " + CONTACT_NUMBER_7012),
                () -> assertEquals(apiGetMessageResult[1], message,"Сообщение не соответствует ожидаемому" +
                        "" + message)
        );
    }

    @Story(value = "Меняем название и описание закрытого канала")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7012, переходим в раздел информации о канале" +
            " и правим название и описание канала. Проверяем, что сохранения применились.")
    @Order(11)
    @Test
    void test_Edit_Name_And_Description_Closed_Channel_7012(){
        assertTrue(status, "Канал не был создан");
        status = false;
        clientChannelsPage.changeDataChannel(nameChannel,true,true,false,
                newNameChannel,
                CLIENT_NEW_DESCRIPTION_CHANNEL_CLOSED);
        assertTrue(isExistComments(newNameChannel, true),
                "Новое название не найдено в списке бесед");
          assertAll("Проверяем новое название и описание канала",
                () -> assertEquals(clientChannelsPage.getNameMainHeaderChannel(newNameChannel),
                        newNameChannel,
                        "Новое название канала не найдено в заголовке канала"),
                () -> assertEquals(clientChannelsPage.
                                getDescriptionMainHeaderChannel(CLIENT_NEW_DESCRIPTION_CHANNEL_CLOSED),
                        CLIENT_NEW_DESCRIPTION_CHANNEL_CLOSED,
                        "Новое описание канала не найдено в заголовке канала"),
                () -> assertEquals(clientChannelsPage.getTitleName(newNameChannel),
                        newNameChannel,
                        "Новое название канала не найдено в разделе информация о канале"),
                () -> assertEquals(clientChannelsPage.getDescriptionChannel(),
                        CLIENT_NEW_DESCRIPTION_CHANNEL_CLOSED,
                        "Новое описание канала не найдено в разделе информация о канале")
        );
        status = true;
    }

    @Story(value = "Ищем на клиенте 7013 закрытый канал после изменения названия")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7013 и вводим в поле поиска имя закрытого канала." +
            " Проверяем, что канал не отображается в списке каналов")
    @Order(12)
    @Test
    void test_Search_Closed_Channel_7013_After_Edit_Name_Channel(){
        assertTrue(status, "Канал не был создан или измененно навзание");
        assertTrue(
                clientChannelsPage.searchChannel(
                        newNameChannel,
                        CLIENT_TYPE_CHANNEL_CLOSED),
                "Канал отображается в списке бесед");
    }

    @Story(value = "Удаляем закрытый канал")
    @Description(value = "Авторизуемся под учётно записью администратора канала и удалем канал")
    @Order(13)
    @Test
    void test_Delete_Channel_7012(){
        assertTrue(status, "Канал не был создан");
        assertTrue(clientChannelsPage.deleteChannel(nameChannel).
                        isExistComments(newNameChannel, false),
                "Канал найден в списке бесед после удаления");
    }

    @AfterAll
    static void tearDown(){
        if(apiToServer != null) apiToServer.disconnect();
    }
}
