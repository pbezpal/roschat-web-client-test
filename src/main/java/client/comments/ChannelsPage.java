package client.comments;

import client.ClientPage;
import com.codeborne.selenide.*;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.ElementShould;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.*;
import static data.CommentsData.*;

public class ChannelsPage extends ChatPage implements CommentsPage {

    private SelenideElement inputNameChannel = $(".channel-create input[type='text']");
    private SelenideElement divDescriptionChannel = $("div.info.non-border-input.div-input");
    private SelenideElement spanItemChannel = $("div.filter.channels span");
    private ElementsCollection radioTypeChannel = $$("form.custom-radio label");
    private SelenideElement divAddUser = $("div.btn-list-item.list-item");
    private ElementsCollection spansInfoChannel = $$("div.channel-info.info-content.sections span");
    private SelenideElement spanTypeClosedChannel = $(".channel-type .text.color-red");
    private SelenideElement pDescriptionChannel = $("div.additional-text p");
    private SelenideElement iShareChannel = $("i.fal.fa-share");
    private SelenideElement inputSearchChat = $("div.select-chat input.search-input");
    private ElementsCollection listChats = $$("div.select-chat div.fio.name");
    private ElementsCollection selectedChat = $$("div.selected span");
    private SelenideElement iCopyLinkChannel = $("i.fal.fa-external-link");
    private SelenideElement iUserPlus = $("i.fa-user-plus");
    private SelenideElement divAddUserChannel = $("div.btns.info-section");
    private ElementsCollection listUsersChannel = $$("div.members.info-section div.fio.name");
    private SelenideElement iFaArrowLeft = $("i.far.fa-arrow-left");
    private SelenideElement inputTitlePublication = $("div.publication-editor input[type='text']");
    private SelenideElement divDescriptionPublication = $("div.publication-editor div.info-input");
    private SelenideElement buttonOkPublication = $("div.modal-footer button._btnOk");
    private SelenideElement iSignOut = $("i.fal.fa-sign-out");
    private ElementsCollection listPublicationsMessage = $$("div.chat-publication-message .channel-name");
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
    private ChannelsPage sendDescriptionChannel(String description){
        divDescriptionChannel.setValue("");
        divDescriptionChannel.sendKeys(description);
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
        radioTypeChannel.findBy(Condition.text(type)).find("i").click();
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

    @Step(value = "Получаем описание {description} канала в разделе информации")
    public String getDescriptionChannel(){
        return pDescriptionChannel.text();
    }

    @Step(value = "Проверяем, есть ли надпись закрытого канала в разделе информации")
    public boolean isTextInfoClosedChannel(boolean show){
        if(isDivInfoWrapper(false)) clickMainHeaderText();
        if(show) {
            try {
                spanTypeClosedChannel.waitUntil(Condition.visible,30000);
            } catch (ElementNotFound e) {
                return false;
            }
            return spanTypeClosedChannel.text().equals(CLIENT_TYPE_CHANNEL_CLOSED);
        }else{
            try {
                spanTypeClosedChannel.waitUntil(Condition.not(Condition.visible),30000);
            } catch (ElementShould e) {
                return false;
            }
            return true;
        }
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

    @Step(value = "Проверяем, что есть иконка 'Копировать ссылку'")
    public boolean isIconCopyLinkChannel(String channel){
        try{
            clickItemComments().clickChat(channel);
            if(isDivInfoWrapper(false)) clickMainHeaderText();
            iCopyLinkChannel.shouldBe(Condition.visible);
        }catch (ElementNotFound e){
            return false;
        }
        return true;
    }

    @Step(value = "Добавляем подписчиков/администраторов в канал")
    private ChannelsPage clickAddUsersChannel(){
        divAddUserChannel.shouldBe(Condition.visible).click();
        return this;
    }

    @Step(value = "Проверяем, что есть иконка добавления подписчиков/администраторов")
    public boolean isIconUserPlus(){
        try {
            iUserPlus.shouldBe(Condition.visible);
        }catch (ElementNotFound e){
            return false;
        }

        return true;
    }

    @Step(value = "Получаем количество подписчиков/администраторов")
    public int getCountUsersChannel(){
        int count = listUsersChannel.shouldBe(CollectionCondition.sizeNotEqual(0), 10000).size();
        iFaArrowLeft.shouldBe(Condition.visible).click();
        return count;
    }

    @Step(value = "Вводим заголовок публикации {title}")
    private ChannelsPage sendInputTitlePublication(String title){
        for (Character c: title.toCharArray()) {
            inputTitlePublication.sendKeys(c.toString());
            sleep(500);
        }
        return this;
    }

    @Step(value = "Вводим описание публикации {description}")
    private ChannelsPage sendDescriptionPublication(String description){
        divDescriptionPublication.sendKeys(description);
        return this;
    }

    @Step(value = "Нажимаем кнопку Опубликовать")
    private ChannelsPage clickOkPublication(){
        buttonOkPublication.click();
        return this;
    }

    @Step(value = "Проверяем, что отображается правильно автор публикации")
    public String getAuthorPublication(String id){
            return $(String.format(CLIENT_PUBLICATION, id)).find("div.publisher-name").text();
    }

    @Step(value = "Проверяем, что отображается текст заголовка публикации")
    public boolean isShowTitlePublication(String id, String title){
        try{
            $("li#publication" + id).
                    find("div.title").
                    $$("span").
                    findBy(Condition.text(title)).
                    shouldBe(Condition.visible);
        }catch (ElementNotFound e){
            return false;
        }
        return true;
    }

    @Step(value = "Проверяем, что отображается текст описания публикации")
    public boolean isShowDescriptionPublication(String id, String description, boolean show){
        if(show){try{
            $("li#publication" + id).
                    find("div.info-text").
                    $$("span").
                    findBy(Condition.text(description)).
                    shouldBe(Condition.visible);
            } catch (ElementNotFound e) {
                return false;
            }
        }else{
            try{
            $("li#publication" + id).
                    find("div.info-text").
                    $$("span").
                    findBy(Condition.text(description)).
                    shouldBe(Condition.not(Condition.visible));
            } catch (ElementShould e) {
                return false;
            }
        }
        return true;
    }

    @Step(value = "Проверяем, есть ли иконка Подписаться на канал/Выйти из канала")
    public boolean isIconSignOut(String channel){
        try {
            clickItemComments().clickChat(channel);
            if(isDivInfoWrapper(false)) clickMainHeaderText();
            iSignOut.shouldBe(Condition.visible);
        }catch (ElementNotFound e){
            return false;
        }

        return true;
    }

    @Step(value = "Нажимаем иконку поделиться публикацией")
    private ChannelsPage clickIconSharePublication(String id){
        $("li#publication" + id + " i.fal.fa-share").click();
        return this;
    }

    @Step(value = "Переходим в публикацию канала {channel}, которой поделились")
    public ChannelsPage clickSharePublicationChannel(String channel){
        listPublicationsMessage.findBy(Condition.text(channel)).click();
        return this;
    }

    //Создаём канал
    public ChannelsPage createNewChannel(String name, String description, String item, String type){
        clickItemComments().clickContextMenu().clickItemContextMenu(item);
        sendInputNameChannel(name).
                sendDescriptionChannel(description).
                clickTypeChannel(type).
                clickButtonCreateOrSave();
        return this;
    }

    //Редактируем название, описание и тип канала канала
    public ChannelsPage changeDataChannel(String channel, boolean changeName, boolean changeDescription,
                                          boolean changeType, String... data){
        clickItemComments().clickChat(channel);
        if(isDivInfoWrapper(false)) clickMainHeaderText();
        clickPencilEdit();
        if(changeName && changeDescription && changeType) {
            sendInputNameChannel(data[0]);
            sendDescriptionChannel(data[1]);
            clickTypeChannel(data[2]);
        }else if(changeName && changeDescription) {
            sendInputNameChannel(data[0]);
            sendDescriptionChannel(data[1]);
        }else if(changeName && changeType) {
            sendInputNameChannel(data[0]);
            clickTypeChannel(data[1]);
        }else if(changeDescription && changeType) {
            sendDescriptionChannel(data[0]);
            clickTypeChannel(data[1]);
        }else if(changeName) sendInputNameChannel(data[0]);
        else if(changeDescription) sendInputNameChannel(data[0]);
        else if(changeType) clickTypeChannel(data[0]);
        clickButtonCreateOrSave();
        return this;
    }

    //Делимся ссылкой
    public ChannelsPage shareLinkChannel(String channel, String chat){
        clickItemComments().clickChat(channel);
        if(isDivInfoWrapper(false)) clickMainHeaderText();
        actionInfoWrapper(CLIENT_INFO_SHARE_LINK_CHANNEL).
                sendInputSearchChat(chat).
                selectChat(chat).isSelectChat(chat);
        clickButtonFooter(CLIENT_BUTTON_SHARE_MESSAGE);
        return this;
    }

    //Делимся ссылкой через контекстное меню
    public ChannelsPage shareLinkChannelContextMenu(String channel, String chat){
        clickItemComments().clickChat(channel);
        clickHeaderContextMenu().selectItemContextMenu(CLIENT_SHARE_LINK_CHANNEL_CONTEXT_MENU);
        sendInputSearchChat(chat).selectChat(chat).isSelectChat(chat);
        clickButtonFooter(CLIENT_BUTTON_SHARE_MESSAGE);
        return this;
    }

    //Копируем ссылку
    public ChannelsPage copyLinkChannel(String channel, String contact){
        clickItemComments().clickChat(channel);
        if(isDivInfoWrapper(false)) clickMainHeaderText();
        actionInfoWrapper(CLIENT_INFO_COPY_LINK_CHANNEL);
        sendMessageToChat(contact, Keys.CONTROL + "v", false);
        return this;
    }

    //Добавляем подписчиков
    public ChannelsPage addUsersChannel(String channel, String typeUsers, String[] contacts){
        clickItemComments().clickChat(channel);
        if(isDivInfoWrapper(false)) clickMainHeaderText();
        actionInfoWrapper(typeUsers).clickAddUsersChannel();
        for(int i = 0; i < contacts.length; i++) {
            searchContactForAction(contacts[i]);
            selectFoundContact(contacts[i]);
        }
        clickButtonFooter(CLIENT_BUTTON_ADD);
        return this;
    }

    //Подпись на канал
    public ChannelsPage userSubscriberChannel(String channel){
        clickItemComments().clickChat(channel);
        if(isDivInfoWrapper(false)) clickMainHeaderText();
        return actionInfoWrapper(CLIENT_INFO_SUBSCRIBER_CHANNEL);
    }

    public ChannelsPage newPublication(String channel, String title, String description){
        clickItemComments().clickChat(channel);
        clickHeaderContextMenu().selectItemContextMenu(CLIENT_NEW_PUBLICATION_CHANNEL_CONTEXT_MENU);
        return sendInputTitlePublication(title).sendDescriptionPublication(description).
                clickOkPublication();
    }

    public ChannelsPage sharePublicationChannel(String channel, String id, String chat){
        clickItemComments().clickChat(channel);
        clickIconSharePublication(id).
                sendInputSearchChat(chat).
                selectChat(chat).isSelectChat(chat);
        clickButtonFooter(CLIENT_BUTTON_SHARE_MESSAGE);
        return this;
    }
}
