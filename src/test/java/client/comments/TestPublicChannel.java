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
@Feature(value = "Публичный канал")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(RecourcesTests.class)
public class TestPublicChannel extends chat.ros.testing2.server.administration.ChannelsPage implements CommentsPage {

    private ChannelsPage clientChannelsPage = new ChannelsPage();

    @Story(value = "Создаём новый публичный канал")
    @Description(value = "Авторизуемся под пользователем 7012 и создаём новый публичный канал")
    @Order(1)
    @Test
    void test_Create_Public_Channel_7012(){
        assertTrue(
                clientChannelsPage.createNewChannel(
                        CLIENT_NAME_CHANNEL_PUBLIC,
                        CLIENT_DESCRIPTION_CHANNEL_PUBLIC,
                        CLIENT_ITEM_NEW_CHANNEL,
                        CLIENT_TYPE_CHANNEL_PUBLIC).
                        isExistComments(CLIENT_NAME_CHANNEL_PUBLIC, true),
                "Канал не найден в списке бесед");
    }

    @Story(value = "Ищем на клиенте 7013 публичный канал")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7013 и вводим в поле поиска имя публичного канала." +
            " Проверяем, что канал отображается в списке найденных каналов")
    @Order(2)
    @Test
    void test_Search_Public_Channel_7013(){
        assertTrue(
                clientChannelsPage.searchChannel(
                        CLIENT_NAME_CHANNEL_PUBLIC,
                        CLIENT_TYPE_CHANNEL_PUBLIC),
                "Канал не найден");
    }

    @Story(value = "Меняем название и описание публичного проверенного канала")
    @Description(value = "Авторизуемся на клиенте под учётной записью 7012, переходим в раздел информации о канале" +
            " и правим название и описание канала. Проверяем, что сохранения применились.")
    @Order(3)
    @Test
    void test_Edit_Name_And_Description_Public_Channel_7012(){
        clientChannelsPage.editNameAndDescriptionChannel(
                CLIENT_NAME_CHANNEL_PUBLIC,
                CLIENT_NEW_NAME_PUBLIC_CHANNEL,
                CLIENT_NEW_DESCRIPTION_PUBLIC_CHANNEL);
        assertTrue(isExistComments(
                CLIENT_NEW_NAME_PUBLIC_CHANNEL, true),
                "Новое название не найдено в списке бесед");
        assertEquals(
                clientChannelsPage.getNameMainHeaderChannel(CLIENT_NEW_NAME_PUBLIC_CHANNEL),
                CLIENT_NEW_NAME_PUBLIC_CHANNEL,
                "Новое название канала не найдено в заголовке канала");
        assertEquals(
                clientChannelsPage.getDescriptionMainHeaderChannel(CLIENT_NEW_DESCRIPTION_PUBLIC_CHANNEL),
                CLIENT_NEW_DESCRIPTION_PUBLIC_CHANNEL,
                "Новое описание канала не найдено в заголовке канала");
        assertEquals(
                clientChannelsPage.getTitleName(CLIENT_NEW_NAME_PUBLIC_CHANNEL),
                CLIENT_NEW_NAME_PUBLIC_CHANNEL,
                "Новое название канала не найдено в разделе информация о канале");
        assertEquals(
                clientChannelsPage.getDescriptionChannel(),
                CLIENT_NEW_DESCRIPTION_PUBLIC_CHANNEL,
                "Новое описание канала не найдено в разделе информация о канале");
    }
}
