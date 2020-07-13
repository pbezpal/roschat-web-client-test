package client;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static chat.ros.testing2.data.ContactsData.USER_ACCOUNT_PASSWORD;
import static chat.ros.testing2.data.LoginData.PORT_SERVER;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(RecourcesTests.class)
public class TestLogin {

    String hostServer = "https://testing2.ros.chat:" + PORT_SERVER;
    String hostClient = "https://testing2.ros.chat";

    @Test
    @Disabled
    void testLogin(){
        Configuration.baseUrl = hostClient;
        open("/");
        if(ClientPage.isLoginWindow()) {
            assertTrue(ClientPage.loginClient("7012@ros.chat", USER_ACCOUNT_PASSWORD, false), "Ошибка при " +
                    "авторизации");
        }
    }
}
