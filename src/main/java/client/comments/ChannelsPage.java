package client.comments;

import client.ClientPage;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static data.CommentsData.*;

public class ChannelsPage implements CommentsPage {

    private SelenideElement inputNameChannel = $("div.channel-create input[type='text']");
    private SelenideElement divDescriptionChannel = $("div.info.non-border-input.div-input");
    private SelenideElement buttonCreateOrSaveChannel = $("div.footer button.btn.btn-primary");
    private SelenideElement spanItemChannel = $("div.filter.channels span");
    private ElementsCollection radioTypeChannel = $$("form.custom-radio label");
    private SelenideElement divAddUser = $("div.btn-list-item.list-item");
    private ElementsCollection spansInfoChannel = $$("div.channel-info.info-content.sections span");
    private SelenideElement pDescriptionChannel = $("div.additional-text p");
    private SelenideElement iShareChannel = $("i.fal.fa-share");
    private SelenideElement inputSearchChat = $("div.select-chat input.search-input");
    private ElementsCollection listChats = $$("div.select-chat div.fio.name");
    private ElementsCollection selectedChat = $$("div.selected span");
    private ElementsCollection buttonFooter = $$("div.footer button");
    private String statusTestedChannel = "i.fa-check";

    public ChannelsPage(){}

    @Step(value = "Вводим имя канала {channel}")
    private ChannelsPage sendInputNameChannel(String channel){
        inputNameChannel.sendKeys(Keys.CONTROL + "a");
        inputNameChannel.sendKeys(Keys.BACK_SPACE);
        inputNameChannel.sendKeys(channel);
        return this;
    }

    @Step(value = "Вводим описание канала {description}")
    private ChannelsPage sendDivDescriptionChannel(String description){
        divDescriptionChannel.setValue("");
        divDescriptionChannel.sendKeys(description);
        return this;
    }

    @Step(value = "Нажимаем кнопку Создать/Сохранить")
    private ChannelsPage clickButtonCreateOrSaveChannel(){
        buttonCreateOrSaveChannel.click();
        return this;
    }

    @Step(value = "Переходим в раздел Каналы")
    private ClientPage clickItemChannels(){
        inputSearch.click();
        spanItemChannel.click();
        return this;
    }

    @Step(value = "Проверяем, что в заголовке канала {channel} появился статус проверенного")
    public boolean isStatusTestedChannelMainHeader(String channel) {
        itemsListChat.findBy(Condition.text(channel)).click();
        try{
            divMainHeader.find(statusTestedChannel).shouldBe(Condition.visible);
        }catch (ElementNotFound e){
            return false;
        }
        return true;
    }

    @Step(value = "Проверяем текст названия {name} в заголовке канала")
    public String getNameMainHeaderChannel(String name){
        return divMainHeader.find("span").text();
    }

    @Step(value = "Проверяем текст описания {description} в заголовке канала")
    public String getDescriptionMainHeaderChannel(String description){
        return divMainHeader.find("div.channel-info").text();
    }

    @Step(value = "Проверяем, что в разделе информации о канале {channel} появился статус провереннеого")
    public boolean isStatusTestedInfoChannel(String channel){
        itemsListChat.findBy(Condition.text(channel)).click();
        if(isDivInfoWrapper(false)) clickMainHeaderText();
        try{
            divCommonText.find(statusTestedChannel).shouldBe(Condition.visible);
        }catch (ElementNotFound e){
            return false;
        }

        return true;
    }

    @Step(value = "Проверяем, что тип канала не выбран")
    public boolean isNotActiveRadioTypeChannel(String type){
        try{
            radioTypeChannel.findBy(Condition.text(type)).find("input[value='false']").isEnabled();
        }catch (ElementNotFound e){
            return false;
        }
        return true;
    }

    @Step(value = "Выбираем тип канала {type}")
    public ChannelsPage clickTypeChannel(String type){
        if(isNotActiveRadioTypeChannel(type)) radioTypeChannel.findBy(Condition.text(type)).find("i").click();
        return this;
    }

    @Step(value = "Ищем канал {channel}")
    public boolean searchChannel(String channel, String type){
        clickItemChannels();
        sendInputSearch(channel);
        if(type.equals(CLIENT_TYPE_CHANNEL_PUBLIC)) {
            return isExistComments(channel, true);
        }else if(type.equals(CLIENT_TYPE_CHANNEL_CLOSED)) {
            return isExistComments(channel, false);
        }
        return false;
    }

    @Step(value = "Проверяем, что в разделе Беседы у канала {channel} появился статус проверенного")
    public boolean isStatusTestedChannelListChat(String channel) {
        try{
            itemsListChat.findBy(Condition.text(channel)).find(statusTestedChannel).shouldBe(Condition.visible);
        }catch (ElementNotFound e){
            return false;
        }
        return true;
    }

    @Step(value = "Проверяем правильное ли описание {description} канала в разделе информации")
    public String getDescriptionChannel(){
        return pDescriptionChannel.text();
    }

    @Step(value = "Проверяем, что есть иконка 'Поделиться каналом'")
    public boolean isIconShareChannel(){
        try{
            iShareChannel.shouldBe(Condition.visible);
        }catch (ElementNotFound e){
            return false;
        }
        return true;
    }

    @Step(value = "Выбираем действие с каналом {action}")
    private ChannelsPage actionChannel(String action){
        spansInfoChannel.findBy(Condition.text(action)).click();
        return this;
    }

    @Step(value = "Вводим в поле поиска беседу {chat}")
    private ChannelsPage sendInputSearchChat(String chat){
        inputSearchChat.sendKeys(chat);
        return this;
    }

    @Step(value = "Выбираем беседу {chat}")
    private ChannelsPage selectChat(String chat){
        listChats.findBy(Condition.text(chat)).click();
        return this;
    }

    @Step(value = "Проверяем, что беседа {chat} была успешно выбрана")
    private boolean isSelectChat(String chat){
        try{
            selectedChat.findBy(Condition.text(chat)).shouldBe(Condition.visible);
        }catch (ElementNotFound e){
            return false;
        }
        return true;
    }

    @Step(value = "Нажимаем кнопку {button}")
    private ChannelsPage clickButtonFooter(String button){
        buttonFooter.findBy(Condition.text(button)).click();
        return this;
    }

    //Создаём канал
    public ChannelsPage createNewChannel(String name, String description, String item, String type){
        clickItemComments();
        clickContextMenu();
        clickItemContextMenu(item);
        sendInputNameChannel(name).
                sendDivDescriptionChannel(description).
                clickTypeChannel(type).
                clickButtonCreateOrSaveChannel();
        return this;
    }

    //Редактируем название и описание канала
    public ChannelsPage editNameAndDescriptionChannel(String channel, String name, String description){
        clickItemComments();
        clickChat(channel);
        if(isDivInfoWrapper(false)) clickMainHeaderText();
        clickPencilEdit();
        sendInputNameChannel(name).
                sendDivDescriptionChannel(description).
                clickButtonCreateOrSaveChannel();
        return this;
    }

    //Делимся ссылкой
    public ChannelsPage shareLinkChannel(String channel, String chat){
        clickItemComments();
        clickChat(channel);
        if(isDivInfoWrapper(false)) clickMainHeaderText();
        actionChannel(CLIENT_SHARE_LINK_CHANNEL).
                sendInputSearchChat(chat).
                selectChat(chat).isSelectChat(chat);
        clickButtonFooter(CLIENT_BUTTON_SHARE_LINK_CHANNEL);
        return this;
    }
}
