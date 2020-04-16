package client.comments;

import client.ClientApiToServer;
import client.RecourcesTests;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static chat.ros.testing2.data.ContactsData.*;
import static data.CommentsData.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic(value = "Беседы")
@Feature(value = "Чаты")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(RecourcesTests.class)
public class TestChatsPage implements CommentsPage {

    private String login7012 = CONTACT_NUMBER_7012 + "@ros.chat";
    private static ClientApiToServer clientApiToServer;
    private ChatsPage chatsPage;

    @Story(value = "Проверка приёма сообщений")
    @Description(value = "Авторизуемся под пользователем 7012, пользователь 7013 отправляет сообщение, проверяем, " +
            "появилось ли сообщение у пользователя 7012")
    @Test
    void test_Get_New_Message_7012(){
        clientApiToServer = new ClientApiToServer();
        clientApiToServer.start();
        if(clientApiToServer.isAlive()){
            try{
                clientApiToServer.join();	//Подождать пока оппонент закончит высказываться.
            }catch(InterruptedException e){}
            chatsPage = (ChatsPage) getInstanceClient(CLIENT_TYPE_COMMENTS_CHATS);
            assertTrue(chatsPage.isGetNewMessage("Это тестовое сообщение от пользователя 7013"),
                    "Канал не найден в списке бесед");
        }

        clientApiToServer.apiToServer.disconnect();

    }
}
