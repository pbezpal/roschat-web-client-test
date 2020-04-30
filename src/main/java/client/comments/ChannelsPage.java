package client.comments;

import client.ClientPage;
import com.codeborne.selenide.CollectionCondition;
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
    private SelenideElement iCopyLinkChannel = $("i.fal.fa-external-link");
    private SelenideElement divHeaderInfo = $("div[title='Информация']");
    private ElementsCollection divContextMenu = $$("div#context-menu li");
    private SelenideElement iUserPlus = $("i.fa-user-plus");
    private SelenideElement divAddUserChannel = $("div.btns.info-section");
    private ElementsCollection listUsersChannel = $$("div.members.info-section div.fio.name");
    private SelenideElement iFaArrowLeft = $("i.far.fa-arrow-left");
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

    @Step(value = "Нажимаем {action} в Информационном разделе канала")
    public ChannelsPage actionInfoWrapper(String action){
        spansInfoChannel.findBy(Condition.text(action)).shouldBe(Condition.visible).click();
        return this;
    }

    @Step(value = "Проверяем, есть ли элемент в Информационном разделе канала")
    public boolean isActionInfoWrapper(String span, boolean show){
        if(show) {
            try {
                spansInfoChannel.findBy(Condition.text(span)).shouldBe(Condition.visible);
            } catch (ElementNotFound e) {
                return false;
            }
        } else {
            try {
                spansInfoChannel.findBy(Condition.text(span)).shouldBe(Condition.not(Condition.visible));
            } catch (ElementNotFound e) {
                return false;
            }
        }
        return true;
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

    @Step(value = "Проверяем, что есть иконка 'Копировать ссылку'")
    public boolean isIconCopyLinkChannel(){
        try{
            iCopyLinkChannel.shouldBe(Condition.visible);
        }catch (ElementNotFound e){
            return false;
        }
        return true;
    }

    @Step(value = "Выбираем элемент контекстного меню {item}")
    private ChannelsPage selectItemContextMenu(String item){
        divHeaderInfo.shouldBe(Condition.visible).click();
        divContextMenu.findBy(Condition.text(item)).shouldBe(Condition.visible).click();
        return this;
    }

    @Step(value = "Добавляем подписчиков в канал")
    private ChannelsPage clickAddUsersChannel(){
        divAddUserChannel.shouldBe(Condition.visible).click();
        return this;
    }

    @Step(value = "Проверяем, что количество подписчиков = {users}")
    public int isCountUsersChannel(){
        int count = listUsersChannel.shouldBe(CollectionCondition.sizeNotEqual(0)).size();
        iFaArrowLeft.shouldBe(Condition.visible).click();
        return count;
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
        actionInfoWrapper(CLIENT_INFO_SHARE_LINK_CHANNEL).
                sendInputSearchChat(chat).
                selectChat(chat).isSelectChat(chat);
        clickButtonFooter(CLIENT_BUTTON_SHARE_LINK_CHANNEL);
        return this;
    }

    //Делимся ссылкой через контекстное меню
    public ChannelsPage shareLinkChannelContextMenu(String channel, String chat){
        clickItemComments();
        clickChat(channel);
        selectItemContextMenu(CLIENT_SHARE_LINK_CHANNEL_CONTEXT_MENU).
                sendInputSearchChat(chat).
                selectChat(chat).isSelectChat(chat);
        clickButtonFooter(CLIENT_BUTTON_SHARE_LINK_CHANNEL);
        return this;
    }

    //Копируем ссылку
    public ChannelsPage copyLinkChannel(String channel, String contact){
        ChatsPage chatsPage = new ChatsPage();
        clickItemComments();
        clickChat(channel);
        if(isDivInfoWrapper(false)) clickMainHeaderText();
        actionInfoWrapper(CLIENT_INFO_COPY_LINK_CHANNEL);
        chatsPage.sendNewMessage(contact, Keys.CONTROL + "v");
        return this;
    }

    //Добавляем подписчиков
    public ChannelsPage addUsersChannel(String channel, String typeUsers, String[] contacts){
        clickItemComments();
        clickChat(channel);
        if(isDivInfoWrapper(false)) clickMainHeaderText();
        actionInfoWrapper(typeUsers).clickAddUsersChannel();
        for(int i = 0; i < contacts.length; i++) {
            searchContactForAction(contacts[i]);
            selectFoundContact(contacts[i]);
        }
        clickButtonFooter(CLIENT_ADD_USER_CHANNEL);
        return this;
    }

    //Подпись на канал
    public ChannelsPage userSubscriberChannel(String channel){
        clickItemComments();
        clickChat(channel);
        if(isDivInfoWrapper(false)) clickMainHeaderText();
        actionInfoWrapper(CLIENT_INFO_SUBSCRIBER_CHANNEL);
        return this;
    }
}
