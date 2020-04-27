package client.comments;

import client.RecourcesTests;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic(value = "Каналы")
@Feature(value = "Публичный проверенный канал")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(RecourcesTests.class)
public class TestProvenPublicChannel extends chat.ros.testing2.server.administration.ChannelsPage implements CommentsPage{

    private ChannelsPage clientChannelsPage = new ChannelsPage();

    @Story(value = "Создаём новый публичный канал")
    @Description(value = "Авторизуемся под пользователем 7012 и создаём новый публичный канал")
    @Order(1)
    @Test
    void test_Create_Public_Proven_Channel_7012(){
        assertTrue(
                clientChannelsPage.createNewChannel(
                        CLIENT_NAME_CHANNEL_PUBLIC_PROVEN,
                        CLIENT_DESCRIPTION_CHANNEL_PUBLIC_PROVEN,
                        CLIENT_ITEM_NEW_CHANNEL,
                        CLIENT_TYPE_CHANNEL_PUBLIC).
                        isExistComments(CLIENT_NAME_CHANNEL_PUBLIC_PROVEN, true),
                "Канал не найден в списке бесед");
    }

    @Story(value = "Делаем проверенным публичный канал")
    @Description(value = "Авторизуемся в СУ, переходим в раздел Администрирование->Каналы, делаем публичный канал проверенным")
    @Order(2)
    @Test
    void test_Do_Proven_Channel(){
        assertTrue(isShowChannel(CLIENT_NAME_CHANNEL_PUBLIC_PROVEN, true));
        doTestedChannel(CLIENT_NAME_CHANNEL_PUBLIC_PROVEN);
    }

    @Story(value = "Проверяем статус публичного канала под учётной записью 7012")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7012. Проверяем, что у канала появился статус Проверенный")
    @Order(3)
    @Test
    void test_Check_Status_Public_Proven_Channel_7012(){
        clientChannelsPage.clickItemComments();
        assertTrue(clientChannelsPage.isStatusTestedChannelListChat(CLIENT_NAME_CHANNEL_PUBLIC_PROVEN),
                "Отсутствует статус Проверенный у канала в разделе Беседы");
        assertTrue(clientChannelsPage.isStatusTestedChannelMainHeader(CLIENT_NAME_CHANNEL_PUBLIC_PROVEN),
                "Отсутствует статус Проверенный в заголовке канала");
        assertTrue(clientChannelsPage.isStatusTestedInfoChannel(CLIENT_NAME_CHANNEL_PUBLIC_PROVEN),
                "Отсутствует статус Проверенный в разделе информация о канале");
    }

    @Story(value = "Ищем на клиенте 7013 публичный канал")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7013 и вводим в поле поиска имя публичного канала." +
            " Проверяем, что у канала статус Проверенный")
    @Order(4)
    @Test
    void test_Search_Public_Proven_Channel_7013(){
        assertTrue(
                clientChannelsPage.searchChannel(
                        CLIENT_NAME_CHANNEL_PUBLIC_PROVEN,
                        CLIENT_TYPE_CHANNEL_PUBLIC),
                "Канал не найден");
        assertTrue(clientChannelsPage.isStatusTestedChannelListChat(CLIENT_NAME_CHANNEL_PUBLIC_PROVEN),
                "Отсутствует статус Проверенный у канала в разделе Беседы");
        assertTrue(clientChannelsPage.isStatusTestedChannelMainHeader(CLIENT_NAME_CHANNEL_PUBLIC_PROVEN),
                "Отсутствует статус Проверенный в заголовке канала");
        assertTrue(clientChannelsPage.isStatusTestedInfoChannel(CLIENT_NAME_CHANNEL_PUBLIC_PROVEN),
                "Отсутствует статус Проверенный в разделе информация о канале");
    }

    @Story(value = "Меняем название и описание публичного проверенного канала")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7012, переходим в раздел информации о канале" +
            " и правим название и описание канала. Проверяем, что сохранения применились.")
    @Order(5)
    @Test
    void test_Edit_Name_And_Description_Public_Proven_Channel_7012(){
        clientChannelsPage.editNameAndDescriptionChannel(
                CLIENT_NAME_CHANNEL_PUBLIC_PROVEN,
                CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN,
                CLIENT_NEW_DESCRIPTION_PUBLIC_CHANNEL_PROVEN);
        assertTrue(isExistComments(
                CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN, true),
                "Новое название не найдено в списке бесед");
        assertEquals(
                clientChannelsPage.getNameMainHeaderChannel(CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN),
                CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN,
                "Новое название канала не найдено в заголовке канала");
        assertEquals(
                clientChannelsPage.getDescriptionMainHeaderChannel(CLIENT_NEW_DESCRIPTION_PUBLIC_CHANNEL_PROVEN),
                CLIENT_NEW_DESCRIPTION_PUBLIC_CHANNEL_PROVEN,
                "Новое описание канала не найдено в заголовке канала");
        assertEquals(
                clientChannelsPage.getTitleName(CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN),
                CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN,
                "Новое название канала не найдено в разделе информация о канале");
        assertEquals(
                clientChannelsPage.getDescriptionChannel(),
                CLIENT_NEW_DESCRIPTION_PUBLIC_CHANNEL_PROVEN,
                "Новое описание канала не найдено в разделе информация о канале");
    }
}
