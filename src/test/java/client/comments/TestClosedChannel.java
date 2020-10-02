/***
 * Тестирование закрытого канала на WEB-клиенте
 * клиент А - 7000@ros.chat
 * клиент B - 7001@ros.chat
 * Проверяется:
 * 1. Создание закрытого канала клиентом А
 * 2. Поиск канала клиентом B
 * 3. Поделиться ссылкой на канал с клиентом B
 * 4. Добавление подписчиков и администраторов в канал
 * 5. Подписаться клиентом B на канал
 * 6. Создание публикации клиентом А
 * 7. Поделиться публикацие с клиентом B
 * 8. Видит ли клиента B пубьликацию
 * 9. Изменение названия и описания канала
 * 10. Поиск на втором клиенте канала после изменения названия и описания
 * 11. Удаление канала клиентом А
 * 12. Поиск на втором пользователе канала после удаления канала
 */

package client.comments;

import client.*;
import client.tools.APIToServer;
import client.tools.SSHGetCommand;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.*;

import static chat.ros.testing2.data.ContactsData.*;
import static data.CommentsData.*;
import static org.junit.jupiter.api.Assertions.*;

@Epic(value = "Каналы")
@Feature(value = "Закрытый канал")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(RecourcesTests.class)
@ExtendWith(WatcherTests.class)
public class TestClosedChannel extends chat.ros.testing2.server.administration.ChannelsPage implements CommentsPage, Helper, StartParallelTest {

    private final String client_A = CLIENT_7000 + "@ros.chat";
    private final String client_B = CLIENT_7001 + "@ros.chat";
    private static APIToServer apiToServer = null;
    private static String CIDUser = SSHGetCommand.isCheckQuerySSH(
            "sudo -u roschat psql -c \"select cid, login from users;\" " +
                    "| grep " + CLIENT_7000 + " | awk '{print $1}'"
    );
    private static ChannelsPage clientChannelsPage = new ChannelsPage();
    private String[] admins = {CLIENT_7007, CLIENT_7001, CLIENT_7002};
    private String[] subscribers = {CLIENT_7003, CLIENT_7004, CLIENT_7005, CLIENT_7006};
    private static String nameChannel = "CHC" + System.currentTimeMillis();
    private static String newNameChannel = nameChannel + System.currentTimeMillis();
    private static boolean status_create = false;
    private static boolean status_change = false;
    private static boolean status_delete = false;
    private String channelName = null;

    @Story(value = "Создаём новый закрытый канал")
    @Description(value = "Авторизуемся под клиентом А и создаём новый закрытый канал")
    @Order(1)
    @Test
    void test_Create_Channel(){
        openClient(client_A, false);
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
        status_create = true;
    }

    @Story(value = "Ищем под клиентом B закрытый канал")
    @Description(value = "Авторизуемся под учётной записью клиента B и вводим в поле поиска имя закрытого канала." +
            " Проверяем, что канал не отображается в списке каналов")
    @Order(2)
    @Test
    void test_Search_Closed_Channel(){
        assertTrue(status_create, "Канал не был создан");
        openClient(client_B, false);
        assertTrue(
                clientChannelsPage.searchChannel(
                        nameChannel,
                        CLIENT_TYPE_CHANNEL_CLOSED),
                "Канал отображается в списке бесед");
    }

    @Story(value = "Копируем и делимся ссылкой на канал")
    @Description(value = "Авторизуемся под учётной записью клиента А, переходим в раздел информации о канале," +
            "нажимаем 'Копировать ссылку' и делимся ссылкой. Проверяем, что ссылка дошла до адресата.")
    @Order(3)
    @Test
    void test_Copy_And_Paste_Link_Closed_Channel() throws ExecutionException, InterruptedException {
        String[] apiGetMessageResult;
        assertTrue(status_create, "Канал не был создан");

        Runnable clientShareLinkChannel = () -> {
            openClient(client_A, false);
            clientChannelsPage.copyLinkChannel(
                    nameChannel,
                    CLIENT_7001
            );
        };

        CompletableFuture<String[]> socketGetMessage = CompletableFuture.supplyAsync(() ->{
            assertTrue(status_create, "Канал не был создан");
            apiToServer = getApiToServer(hostApiServer, client_B, apiToServer);
            String[] getMessageResult = apiToServer.GetTextMessageFromUser(CLIENT_CHATS_SEND_EVENT, "data",60);
            return getMessageResult;
        });

        clientShareLinkChannel.run();
        apiGetMessageResult = socketGetMessage.get();
        assertAll("Проверяем, есть ли иконка, совпадает ли ID и сообщение с ссылкой на канал",
                () -> assertTrue(clientChannelsPage.isIconCopyLinkChannel(nameChannel),
                        "Нет иконки 'Копировать ссылку'"),
                () -> assertEquals(apiGetMessageResult[0], CIDUser, "Сообщение пришло " +
                        "не от пользователя " + CLIENT_7000),
                () -> assertTrue(apiGetMessageResult[1].contains(nameChannel),
                        "Ссылка на канал " + nameChannel + " не пришла")
        );
    }

    @Story(value = "Делимся ссылкой на канал")
    @Description(value = "Авторизуемся под учётной записью клиента A, переходим в раздел" +
            " информации о канале и нажимаем 'Поделиться ссылкой'. Проверяем, что сохранения применились.")
    @Order(4)
    @Test
    void test_Share_Link_Closed_Channel() throws ExecutionException, InterruptedException {
        String[] apiGetMessageResult;
        assertTrue(status_create, "Канал не был создан");
        Runnable clientShareLinkChannel = () -> {
            openClient(client_A, false);
            clientChannelsPage.shareLinkChannel(nameChannel, CLIENT_7001);
        };

        CompletableFuture<String[]> socketGetMessage = CompletableFuture.supplyAsync(() ->{
            apiToServer = getApiToServer(hostApiServer, client_B, apiToServer);
            String[] getMessageResult = apiToServer.GetTextMessageFromUser(CLIENT_CHATS_SEND_EVENT,
                    "data",
                    60);
            return getMessageResult;
        });

        clientShareLinkChannel.run();
        apiGetMessageResult = socketGetMessage.get();
        assertAll("Проверяем, есть ли иконка, совпадает ли ID и сообщение с ссылкой на канал",
                () -> assertTrue(clientChannelsPage.isIconShareChannel(),
                        "Нет иконки 'Поделиться ссылкой'"),
                () -> assertEquals(apiGetMessageResult[0], CIDUser, "Сообщение пришло " +
                        "не от пользователя " + CLIENT_7000),
                () -> assertTrue(apiGetMessageResult[1].contains(nameChannel),
                        "Ссылка на канал " + nameChannel + " не пришла")
        );
    }

    @Story(value = "Добавляем администраторов и подписчиков")
    @Description(value = "Авторизуемся под учётной записью клиента А, переходим в раздел" +
            " информации о канале, нажимаем 'Администраторы' и добавляем администраторов в канал. Проверяем, " +
            "что администарторы добавились. Затем нажимаем 'Подписчики' и добавляем подписчиков в канал. Проверяем, " +
            "что подписчики добавились.")
    @Order(5)
    @Test
    void test_Add_User_Closed_Channel() {
        assertTrue(status_create, "Канал не был создан");
        openClient(client_A, false);
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
    @Description(value = "Авторизуемся под учётной записью клиента B, переходим в раздел" +
            " информации о канале, нажимаем 'Подписаться на канал'. Проверяем, что пользователь подписался на канал. " +
            "Проверяем сколько администраторов и подписчиков в канале отображается у подписчика")
    @Order(6)
    @Test
    void test_Subscriber_Closed_Channel() {
        assertTrue(status_create, "Канал не был создан");
        openClient(client_B, false);
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
    @Description(value = "Авторизуемся под учётной записью клиента А, в контекстном меню выбираем" +
            " 'Новая публикация' опубликовываем публикацию. Проверяем, что публикация была успешно опубликована.")
    @Order(7)
    @Test
    void test_New_Publication_Closed_Channel() throws ExecutionException, InterruptedException {
        assertTrue(status_create, "Канал не был создан");
        String[] apiGetMessageResult;

        Runnable clientNewPublicationChannel = () -> {
            openClient(client_A, false);
            clientChannelsPage.newPublication(
                    nameChannel,
                    CLIENT_TITLE_PUBLICATION_CHANNEL,
                    CLIENT_DESCRIPTION_PUBLICATION_CLOSED_CHANNEL);
        };

        CompletableFuture<String[]> socketGetMessage = CompletableFuture.supplyAsync(() ->{
            apiToServer = getApiToServer(hostApiServer, client_B, apiToServer);
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
                        "не от пользователя " + CLIENT_7000),
                () -> assertEquals(apiGetMessageResult[1], CLIENT_TITLE_PUBLICATION_CHANNEL,"Текст заголовка " +
                        "публикации не совпадает с эталоном "  + CLIENT_TITLE_PUBLICATION_CHANNEL),
                () -> assertEquals(clientChannelsPage.getAuthorPublication("1"),
                        CLIENT_7000,
                        "В публикации указан неправильный автор"),
                () -> assertTrue(clientChannelsPage.isShowTitlePublication("1", CLIENT_TITLE_PUBLICATION_CHANNEL),
                        "Заголовок публикации не совпадает с " + CLIENT_TITLE_PUBLICATION_CHANNEL),
                () -> assertTrue(clientChannelsPage.
                                isShowDescriptionPublication("1", CLIENT_DESCRIPTION_PUBLICATION_CLOSED_CHANNEL,
                                        true),
                        "Описание публикации не совпадает с " + CLIENT_DESCRIPTION_PUBLICATION_CLOSED_CHANNEL)
        );
    }

    @Story(value = "Проверяем, видна ли публикация у подписчика")
    @Description(value = "Авторизуемся под учётной записью клиента B и проверяем, что новая публикация" +
            "видна у подписчика")
    @Order(8)
    @Test
    void test_Check_New_Publication_Closed_Channel(){
        assertTrue(status_create, "Канал не был создан");
        openClient(client_B, false);
        clientChannelsPage.clickItemComments();
        clientChannelsPage.clickChat(nameChannel);
        assertAll("Проверяем, отображается ли публикация у подписчика",
                () -> assertEquals(clientChannelsPage.getAuthorPublication("1"),
                        CLIENT_7000,
                        "В публикации указан неправильный автор"),
                () -> assertTrue(clientChannelsPage.isShowTitlePublication("1", CLIENT_TITLE_PUBLICATION_CHANNEL),
                        "Заголовок публикации не совпадает с " + CLIENT_TITLE_PUBLICATION_CHANNEL),
                () -> assertTrue(clientChannelsPage.
                                isShowDescriptionPublication("1", CLIENT_DESCRIPTION_PUBLICATION_CLOSED_CHANNEL,
                                        true),
                        "Описание публикации не совпадает с " + CLIENT_DESCRIPTION_PUBLICATION_CLOSED_CHANNEL)
        );
    }

    @Story(value = "Переходим в публикацию, которой с нами поделились")
    @Description(value = "Авторизуемся под учётной записью клиента А. Клиент B делится публикацией канала через API." +
            "Проверяем, что клиент А видит публикацию и может перейти к ней")
    @Order(9)
    @Test
    void test_Get_Share_Publication_Closed_channel(){
        assertTrue(status_create, "Канал не был создан");
        String message = "{\"type\":\"publication\",\"chId\":" + getIDChannel(nameChannel) + ",\"pubId\":1}";

        Runnable clientGetMessage = () -> {
            openClient(client_A, false);
            clientChannelsPage.clickItemComments();
            clientChannelsPage.clickChat(CLIENT_7001);
            clientChannelsPage.clickSharePublicationChannel(nameChannel);
            assertAll("Проверяем, отображается ли публикация после перехода к публикации",
                    () -> assertEquals(clientChannelsPage.getAuthorPublication("1"),
                            CLIENT_7000,
                            "В публикации указан неправильный автор"),
                    () -> assertTrue(clientChannelsPage.isShowTitlePublication("1", CLIENT_TITLE_PUBLICATION_CHANNEL),
                            "Заголовок публикации не совпадает с " + CLIENT_TITLE_PUBLICATION_CHANNEL),
                    () -> assertTrue(clientChannelsPage.
                                    isShowDescriptionPublication("1", CLIENT_DESCRIPTION_PUBLICATION_CLOSED_CHANNEL,
                                            true),
                            "Описание публикации не совпадает с " + CLIENT_DESCRIPTION_PUBLICATION_CLOSED_CHANNEL)
            );
        };

        Thread apiSendMessage = new Thread() {
            @Override
            public void run() {
                apiToServer = getApiToServer(hostApiServer, client_B, apiToServer);
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
    @Description(value = "Авторизуемся под учётной записью клиента А и делимся публикацией канала с клиентом B." +
            "Проверяем при помощи API, что успешно поделились публикацией.")
    @Order(10)
    @Test
    void test_Send_Share_Publication_Closed_Channel() throws ExecutionException, InterruptedException {
        String[] apiGetMessageResult;
        String message = "{\"type\":\"publication\",\"chId\":" + getIDChannel(nameChannel) + ",\"pubId\":1}";
        assertTrue(status_create, "Канал не был создан");
        Runnable clientSharePublicationChannel = () -> {
            openClient(client_A, false);
            clientChannelsPage.sharePublicationChannel(
                    nameChannel,
                    "1",
                    CLIENT_7001);
        };

        CompletableFuture<String[]> socketGetMessage = CompletableFuture.supplyAsync(() ->{
            apiToServer = getApiToServer(hostApiServer, client_B, apiToServer);
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
                        "не от пользователя " + CLIENT_7000),
                () -> assertEquals(apiGetMessageResult[1], message,"Сообщение не соответствует ожидаемому" +
                        "" + message)
        );
    }

    @Story(value = "Меняем название и описание закрытого канала")
    @Description(value = "Авторизуемся под учётной записью клиента А, переходим в раздел информации о канале" +
            " и правим название и описание канала. Проверяем, что сохранения применились.")
    @Order(11)
    @Test
    void test_Edit_Name_And_Description_Closed_Channel(){
        assertTrue(status_create, "Канал не был создан");
        openClient(client_A, false);
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
        status_change = true;
    }

    @Story(value = "Ищем под учетной записью клиента B закрытый канал после изменения названия")
    @Description(value = "Авторизуемся под учётной записью клиента B и вводим в поле поиска имя закрытого канала." +
            " Проверяем, что канал не отображается в списке каналов")
    @Order(12)
    @Test
    void test_Search_Closed_Channel_After_Edit_Name_Channel(){
        assertTrue(status_create, "Канал не был создан или не изменены данные");
        assertTrue(status_change, "Не поменялось название и/или описание канала");
        openClient(client_B, false);
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
    void test_Delete_Channel(){
        assertTrue(status_create, "Канал не был создан");
        if(status_change) channelName = newNameChannel;
        else channelName = nameChannel;
        openClient(client_A, false);
        assertTrue(clientChannelsPage.deleteChannel(channelName).
                        isExistComments(channelName, false),
                "Канал найден в списке бесед после удаления");
        status_delete = true;
    }

    @Story(value = "Ищем клиентом B закрытый канал после удаления канала")
    @Description(value = "Авторизуемся под учётной записью клиента B и вводим в поле поиска имя канала." +
            " Проверяем, что канал не отображается в списке каналов")
    @Order(14)
    @Test
    void test_Search_Closed_Channel_After_Delete_Channel(){
        assertTrue(status_create, "Канал не был создан");
        assertTrue(status_delete, "Канал не удалён");
        if(status_change) channelName = newNameChannel;
        else channelName = nameChannel;
        openClient(client_B, false);
        assertTrue(
                isExistComments(
                        channelName,
                        false),
                "Канал отображается в списке бесед после удаления");
    }

    @AfterAll
    static void tearDown(){
        if(apiToServer != null) apiToServer.disconnect();
        Selenide.close();
    }
}
