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

    private ElementsCollection liMessages = $$("li.series-message-start");
    private ElementsCollection spanMessage = $$("#message-list div.message-list-item span");
    private SelenideElement divInputMessage = $("#message-input div.custom-textarea");
    private SelenideElement buttonSendMessage = $("i.fa.fa-paper-plane");

    public ChatsPage() {}

    @Step(value = "Проверяем, пришло ли сообщение {message}")
    protected boolean isExistMessage(String message){
        try{
            liMessages.last().find("span").waitUntil(Condition.text(message), 60000);
        }catch (ElementNotFound elementNotFound){
            return false;
        }

        return true;
    }

    @Step(value = "Проверяем, отображается ли автор {auth} сообщения, на которое отвечали")
    public boolean isAuthReplyMessage(String auth){
        try{
            liMessages.last().find(".reply-info-name").shouldBe(Condition.text(auth));
        }catch (ElementNotFound elementNotFound){
            return false;
        }

        return true;
    }

    @Step(value = "Проверяем, отображается ли сообщение, на котрое отвечали")
    public boolean isReplyMessage(String message){
        try{
            liMessages.last().find(".reply-info-text").shouldBe(Condition.text(message));
        }catch (ElementNotFound elementNotFound){
            return false;
        }

        return true;
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

    /**
     * This method is checking exist message
     *
     * @param contact
     * @param message
     * @return exist message
     */
    public boolean isExistChatMessage(String contact, String message){
        clickItemComments();
        clickChat(contact);
        return isExistMessage(message);
    }

    /**
     * This method is send message
     *
     * @param contact
     * @param message
     */
    public void sendChatMessage(String contact, String message, boolean reply){
        clickItemComments();
        if(isExistComments(contact, false)){
            clickContextMenu().clickItemContextMenu(CLIENT_ITEM_NEW_CHAT);
            searchContactForAction(contact);
            selectFoundContact(contact);
        }else if(reply) {
            clickChat(contact);
            liMessages.last().find("span").contextClick();
            selectItemContextMenu("Ответить");
        }else clickChat(contact);
        sendInputMessage(message).clickButtonSendMessage();
    }

}
