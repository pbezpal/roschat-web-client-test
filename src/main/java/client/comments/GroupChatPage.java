package client.comments;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static data.CommentsData.*;

public class GroupChatPage extends ChatPage implements CommentsPage {

    private SelenideElement inputNameGroupChat = $(".chat-create input[type='text']");

    @Step(value = "Вводим имя групповой беседы {name}")
    private GroupChatPage sendNameGroupChat(String name){
        inputNameGroupChat.sendKeys(name);
        return this;
    }

    /**
     * Create new group chat
     * @param name
     * @param users
     * @return
     */
    public GroupChatPage createNewGroupChat(String name, String[] users){
        clickItemComments().clickContextMenu().clickItemContextMenu(CLIENT_ITEM_NEW_GROUP_CHAT);
        for (int i = 0; i < users.length; i++) {
            searchContactForAction(users[i]);
            selectFoundContact(users[i]);
        }
        clickButtonFooter(CLIENT_BUTTON_NEXT);
        sendNameGroupChat(name).clickButtonCreateOrSave();
        return this;
    }

    public void sendMessageToGroupChat(String chat, String message, boolean reply){
        clickItemComments();
        if(reply) {
            clickChat(chat);
            listMessages.last().find("span").contextClick();
            selectItemContextMenu("Ответить");
        }else clickChat(chat);
        sendInputMessage(message).clickButtonSendMessage();
    }

}
