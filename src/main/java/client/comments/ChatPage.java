package client.comments;

import static data.CommentsData.CLIENT_ITEM_NEW_CHAT;

public class ChatPage implements CommentsPage {

    public ChatPage() {}

    /**
     * This method send message
     *
     * @param contact
     * @param message
     */
    public void sendMessageToChat(String contact, String message, boolean reply){
        clickItemComments();
        if(isExistComments(contact, false)){
            clickContextMenu().clickItemContextMenu(CLIENT_ITEM_NEW_CHAT);
            searchContactForAction(contact);
            selectFoundContact(contact);
        }else if(reply) {
            clickChat(contact);
            listMessages.last().find("span").contextClick();
            selectItemContextMenu("Ответить");
        }else clickChat(contact);
        sendInputMessage(message).clickButtonSendMessage();
    }

}
