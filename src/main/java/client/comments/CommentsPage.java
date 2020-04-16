package client.comments;

import client.ClientPage;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public interface CommentsPage extends ClientPage {

    SelenideElement itemComments = $("i.fa.fa-comments");
    SelenideElement contextMenu = $("div.chat-list.content svg");
    ElementsCollection itemsContextMenu = $$("div#context-menu li");
    ElementsCollection itemsListChat = $$("div.chat-list-item div.name");

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
    default boolean isExistComments(String comments, String show){
        switch(show){
            case "Yes":
                try{
                    System.out.println(itemsListChat.find(Condition.text(comments)));
                    itemsListChat.find(Condition.text(comments)).shouldBe(visible);
                }catch (ElementNotFound e){
                    return false;
                }
                break;
            case "No":
                try{
                    System.out.println(itemsListChat.find(Condition.not(Condition.text(comments))));
                    itemsListChat.find(Condition.text(comments)).shouldBe(not(visible));
                }catch (ElementNotFound e){
                    return false;
                }
                break;
        }

        return true;
    }

    @Override
    default Object getInstanceClient(String typeComments){
        switch(typeComments){
            case "Каналы":
                return new ChannelsPage();
            case "Беседы":
                return new ChatsPage();
        }
        return null;
    }

}
