package client.comments;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static data.CommentsData.CLIENT_ITEM_NEW_CHAT;

public class ChatsPage implements CommentsPage {

    private ElementsCollection spanMessage = $$("#message-list div.message-list-item span");
    private SelenideElement divInputMessage = $("#message-input div.custom-textarea");
    private SelenideElement buttonSendMessage = $("i.fa.fa-paper-plane");
    private SelenideElement inputSelectContacts = $("div.select-contact input.search-input");
    private ElementsCollection listContacts = $$("div.contact-list div.fio.name");

    public ChatsPage() {}

    @Step(value = "Проверяем, пришло ли сообщение")
    protected boolean isExistMessage(String message){
        try{
            spanMessage.findBy(Condition.text(message)).waitUntil(Condition.visible, 60000);
        }catch (ElementNotFound elementNotFound){
            return false;
        }

        return true;
    }

    @Step(value = "Нажимаем на участника новой беседы")
    protected ChatsPage clickNewChat(String user){
        itemsListChat.findBy(Condition.text(user)).waitUntil(Condition.visible, 10000).click();
        return this;
    }

    @Step(value = "Ищем контакт для новой беседы")
    private ChatsPage searchContactForNewChat(String fio){
        inputSelectContacts.sendKeys(fio);
        return this;
    }

    @Step(value = "Выбираем контакт для беседы")
    private ChatsPage clickContactForChat(String contact){
        listContacts.findBy(Condition.text(contact)).shouldBe(Condition.visible).click();
        return this;
    }

    @Step(value = "Вводим сообщение")
    protected ChatsPage sendInputMessage(String message){
        divInputMessage.sendKeys(Keys.CONTROL + "a");
        divInputMessage.sendKeys(Keys.BACK_SPACE);
        divInputMessage.sendKeys(message);
        return this;
    }

    @Step(value = "Нажимаем кнопку отправить")
    protected ChatsPage clickButtonSendMessage(){
        buttonSendMessage.click();
        return this;
    }

    public boolean isGetNewMessage(String contact, String message){
        clickItemComments();
        clickNewChat(contact);
        return isExistMessage(message);
    }

    public void sendNewMessage(String contact, String message){
        clickItemComments();
        if(isExistComments(contact, false)){
            clickContextMenu().clickItemContextMenu(CLIENT_ITEM_NEW_CHAT);
            searchContactForNewChat(contact).clickContactForChat(contact);
        }else clickNewChat(contact);
        sendInputMessage(message).clickButtonSendMessage();
    }

}
