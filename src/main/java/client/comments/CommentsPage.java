package client.comments;

import client.ClientPage;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.ElementShould;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static data.CommentsData.CLIENT_ITEM_NEW_CHAT;

public interface CommentsPage extends ClientPage {

    //Раздел беседы
    SelenideElement itemComments = $("i.fa.fa-comments");
    //Кнопка вызова контекстного меню
    SelenideElement contextMenu = $("div.chat-list.content svg");
    //Пункты контекстного меню
    ElementsCollection itemsContextMenu = $$("div#context-menu li");
    //Список бесед
    ElementsCollection itemsListChat = $$("div.chat-list-item div.name");

    ElementsCollection itemsToolbar = $$("div.toolbar-wrapper span");
    //Кнопка подтверждения действия в беседе
    ElementsCollection buttonFooterConfirm = $$(".footer .btn");
    //Кнопка создания или сохранения изменений в беседе
    SelenideElement buttonCreateOrSave = $(".footer .btn-primary");
    //Список сообщений в беседе
    ElementsCollection listMessages = $$("li.series-message-start");
    //Список системных сообщений в беседе
    ElementsCollection listSystemMessages = $$(".system-message span:not([class='text-indent'])");
    //Поле ввода сообщения
    SelenideElement divInputMessage = $("#message-input div.custom-textarea");
    //Кнопка отправки сообщения
    SelenideElement buttonSendMessage = $("i.fa.fa-paper-plane");

    @Step(value = "Переходим в раздел Беседы")
    default CommentsPage clickItemComments(){
        itemComments.waitUntil(visible, 30000).click();
        return this;
    }

    @Step(value = "Вызываем контекстное меню")
    default CommentsPage clickContextMenu(){
        contextMenu.click();
        return this;
    }

    @Step(value = "Выбираем элемент контекстного меню {item}")
    default CommentsPage clickItemContextMenu(String item){
        itemsContextMenu.findBy(Condition.text(item)).click();
        return this;
    }

    @Step(value = "Проверяем, существует ли беседа {comments}")
    default boolean isExistComments(String comments, boolean show){
        if(show) {
            try {
                itemsListChat.find(Condition.text(comments)).waitUntil(visible, 30000);
            } catch (ElementNotFound e) {
                return false;
            }
        } else {
            try {
                itemsListChat.find(Condition.text(comments)).waitUntil(disappear,30000);
            } catch (ElementShould e) {
                return false;
            }
        }
        return true;
    }

    @Step(value = "Выбираем беседу {chat}")
    default CommentsPage clickChat(String chat){
        itemsListChat.findBy(Condition.text(chat)).waitUntil(Condition.visible, 10000).click();
        return this;
    }

    @Step(value = "Вызываем контекстное меню у беседы {chat} нажатием правой кнопкой мыши по беседе")
    default CommentsPage clickContextMenuInChat(String chat){
        itemsListChat.findBy(Condition.text(chat)).waitUntil(Condition.visible, 10000).contextClick();
        return this;
    }


    @Step(value = "Нажимаем кнопку {button} в форме подтверждения")
    default CommentsPage clickButtonFooterConfirm(String button){
        buttonFooterConfirm.findBy(text(button)).click();
        return this;
    }

    @Step(value = "Нажимаем кнопку Создать/Сохранить")
    default CommentsPage clickButtonCreateOrSave(){
        buttonCreateOrSave.click();
        return this;
    }

    @Step(value = "Ищем сообщение {message} в беседе")
    default boolean isExistMessage(String message){
        try{
            listMessages.last().find("span").waitUntil(Condition.text(message), 60000);
        }catch (ElementNotFound elementNotFound){
            return false;
        }

        return true;
    }

    @Step(value = "Проверяем, отображается ли автор {auth} сообщения, на которое отвечали")
    default boolean isAuthReplyMessage(String auth){
        try{
            listMessages.last().find(".reply-info-name").shouldBe(Condition.text(auth));
        }catch (ElementNotFound elementNotFound){
            return false;
        }

        return true;
    }

    @Step(value = "Проверяем, отображается ли сообщение {message}, на котрое отвечали")
    default boolean isReplyMessage(String message){
        try{
            listMessages.last().find(".reply-info-text").shouldBe(Condition.text(message));
        }catch (ElementNotFound elementNotFound){
            return false;
        }

        return true;
    }

    @Step(value = "Ищем системное сообщение {message} в беседе")
    default boolean isSystemMessage(String message){
        try{
            listSystemMessages.last().shouldBe(Condition.text(message));
        }catch (ElementNotFound elementNotFound){
            return false;
        }

        return true;
    }

    @Step(value = "Вводим сообщение")
    default CommentsPage sendInputMessage(String message){
        divInputMessage.sendKeys(Keys.CONTROL + "a");
        divInputMessage.sendKeys(Keys.BACK_SPACE);
        divInputMessage.sendKeys(message);
        return this;
    }

    @Step(value = "Нажимаем кнопку отправить")
    default CommentsPage clickButtonSendMessage(){
        buttonSendMessage.click();
        return this;
    }

    /**
     * This method is checking exist message
     *
     * @param chat
     * @param message
     * @return exist message
     */
    default boolean isExistChatMessage(String chat, String message){
        clickItemComments();
        clickChat(chat);
        return isExistMessage(message);
    }

    default CommentsPage deleteChat(String chat, String itemDelete, boolean clickRightButtonMouse){
        if(clickRightButtonMouse){
            clickItemComments().clickContextMenuInChat(chat);
        }else{
            clickItemComments().clickChat(chat);
            clickHeaderContextMenu();
        }
        selectItemContextMenu(itemDelete);
        clickButtonFooterConfirm("Ок");
        return this;
    }
}
