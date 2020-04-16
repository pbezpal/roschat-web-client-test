package client.comments;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ChatsPage implements CommentsPage {

    private ElementsCollection spanMessage = $$("#message-list div.message-list-item span");
    private SelenideElement divInputMessage = $("message-input");
    private SelenideElement buttonSendMessage = $("i.fa.fa-paper-plane");

    public ChatsPage() {}

    @Step(value = "Проверяем, пришло сообщение")
    protected boolean isExistMessage(String message){
        try{
            spanMessage.findBy(Condition.text(message)).waitUntil(Condition.visible, 60000);
        }catch (ElementNotFound elementNotFound){
            return false;
        }

        return true;
    }

    @Step(value = "Нажимаем на участника новой беседы")
    protected ChatsPage clickNewChat(){
        itemsListChat.first().click();
        return this;
    }

    @Step(value = "Вводим сообщение")
    protected ChatsPage sendNewMassage(String message){
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

    public boolean isGetNewMessage(String message){
        clickItemComments();
        clickNewChat();
        return isExistMessage(message);
    }

}
