package client.comments;

import client.ClientPage;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public interface CommentsPage extends ClientPage {

    SelenideElement itemComments = $("i.fa.fa-comments");
    SelenideElement contextMenu = $("div.chat-list.content svg");
    ElementsCollection itemsContextMenu = $$("div#context-menu li");
    SelenideElement itemListChat = $("div.chat-list-item div.name");

    ElementsCollection itemsToolbar = $$("div.toolbar-wrapper span");

    @Step(value = "Переходим в раздел Беседы")
    default CommentsPage clickItemComments(){
        itemComments.click();
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
    default boolean isExistComments(String comments, String status){
        switch(status){
            case "Yes":
                try{
                    itemListChat.find("span").text().contains(comments);
                }catch (ElementNotFound e){
                    return false;
                }
                break;
            case "No":
                try{
                    itemListChat.find("span").shouldNotHave(Condition.text(comments));
                }catch (ElementNotFound e){
                    return false;
                }
                break;
        }

        return true;
    }

    @Override
    default Object getInstanceClient(String login, String password, String typeComments){
        getLoginClient(login, password).shouldBe(Condition.visible);
        switch(typeComments){
            case "Каналы":
                return new ChannelsPage();
        }
        return null;
    }

}
