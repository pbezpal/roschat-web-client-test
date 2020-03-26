package client;

import client.comments.ChannelsPage;
import client.comments.CommentsPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.gen5.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static chat.ros.testing2.data.ContactsData.USER_ACCOUNT_PASSWORD;
import static data.CommentsData.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic(value = "Сервер")
@Feature(value = "Настройка -> Интеграция")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(RecourcesTests.class)
public class WC_ChannelsPage extends chat.ros.testing2.server.administration.ChannelsPage implements CommentsPage {

    private String login7012 = "7012@ros.chat";
    private String login7013 = "7013@ros.chat";
    private ChannelsPage clientChannelsPage;

    @Story(value = "Создаём новый публичный канал")
    @Description(value = "Авторизуемся под пользователем 7012 и создаём новый публичный канал")
    @Order(1)
    @Test
    void test_Create_Public_Channel_7012(){
        clientChannelsPage = getClientChannelsPage(login7012);
        assertTrue(clientChannelsPage.createNewChannel(CLIENT_NAME_CHANNEL_PUBLIC, CLIENT_ITEM_NEW_CHANNEL, CLIENT_TYPE_CHANNEL_PUBLIC).isExistComments(CLIENT_NAME_CHANNEL_PUBLIC, "Yes"),
                "Канал не найден в списке бесед");
    }

    @Story(value = "Делаем публичному каналу статус Проверенный")
    @Description(value = "Авторизуемся в СУ, переходим в раздел Администрирование->Каналы, делаем публичный канал проверенным")
    @Order(2)
    @Test
    void test_Do_Tested_Channel(){
        doTestedChannel(CLIENT_NAME_CHANNEL_PUBLIC);
    }

    @Story(value = "Проверяем статус публичного канала под учёткой 7012")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7012. Проверяем, что у канала появился статус Проверенный")
    @Order(3)
    @Test
    void test_Check_Status_Public_Tested_Channel_7012(){
        clientChannelsPage = getClientChannelsPage(login7012);
        clientChannelsPage.clickItemComments();
        Assertions.assertTrue(clientChannelsPage.isStatusTestedChannelListChat(),
                "Отсутствует статус Проверенный у канала в разделе Беседы");
        Assertions.assertTrue(clientChannelsPage.isStatusTestedChannelMainHeader(),
                "Отсутствует статус Проверенный в заголовке канала");
    }

    @Story(value = "Ищем на клиенте 7013 публичный канал")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7013 и вводим в поле поиска имя публичного канала. Проверяем, что у канала статус Проверенный")
    @Order(4)
    @Test
    void test_Search_Public_Channel_7013(){
        clientChannelsPage = getClientChannelsPage(login7013);
        assertTrue(clientChannelsPage.searchChannel(CLIENT_NAME_CHANNEL_PUBLIC, CLIENT_TYPE_CHANNEL_PUBLIC),
                "У канала отсутствует статус проверенный");
    }

    @Story(value = "Создаём новый закрытый канал")
    @Description(value = "Авторизуемся под пользователем 7012 и создаём новый закрытый канал")
    @Order(5)
    @Test
    void test_Create_Closed_Channel_7012(){
        clientChannelsPage = getClientChannelsPage(login7012);
        assertTrue(clientChannelsPage.createNewChannel(CLIENT_NAME_CHANNEL_CLOSED, CLIENT_ITEM_NEW_CHANNEL, CLIENT_TYPE_CHANNEL_CLOSED).isExistComments(CLIENT_NAME_CHANNEL_CLOSED, "Yes"),
                "Канал не найден в списке бесед");
    }

    @Story(value = "Ищем на клиенте 7013 закрытый канал")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7013 и вводим в поле поиска имя закрытого канала. Проверяем, что канал не отображается в списке каналов")
    @Order(6)
    @Test
    void test_Search_Closed_Channel_7013(){
        clientChannelsPage = getClientChannelsPage(login7013);
        assertTrue(clientChannelsPage.searchChannel(CLIENT_NAME_CHANNEL_CLOSED, CLIENT_TYPE_CHANNEL_CLOSED),
                "Канал отображается в списке бесед");
    }

    private ChannelsPage getClientChannelsPage(String login){
        return (ChannelsPage) getInstanceClient(login, USER_ACCOUNT_PASSWORD, CLIENT_TYPE_COMMENTS_CHANNELS);
    }

}
