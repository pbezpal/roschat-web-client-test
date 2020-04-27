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
import static data.CommentsData.CLIENT_TYPE_CHANNEL_CLOSED;
import static data.CommentsData.CLIENT_TYPE_CHANNEL_PUBLIC;

public class ChannelsPage implements CommentsPage {

    private SelenideElement inputNameChannel = $("div.channel-create input[type='text']");
    private SelenideElement divDescriptionChannel = $("div.info.non-border-input.div-input");
    private SelenideElement buttonCreateOrSaveChannel = $("div.footer button.btn.btn-primary");
    private SelenideElement spanItemChannel = $("div.filter.channels span");
    private ElementsCollection radioTypeChannel = $$("form.custom-radio label");
    private SelenideElement divAddUser = $("div.btn-list-item.list-item");
    private SelenideElement divActionContainer = $("div.action-container");
    private SelenideElement pDescriptionChannel = $("div.additional-text p");
    private String statusTestedChannel = "i.fa-check";
    private String iShareChannel = "i.fal.fa-share";

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

    //Ищем канал
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
}
