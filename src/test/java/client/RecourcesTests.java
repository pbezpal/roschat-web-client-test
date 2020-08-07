package client;

import chat.ros.testing2.helpers.SSHManager;
import chat.ros.testing2.server.LoginPage;
import chat.ros.testing2.server.contacts.ContactsPage;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.stqa.selenium.factory.WebDriverPool;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.logging.Level;

import static chat.ros.testing2.data.ContactsData.*;
import static chat.ros.testing2.data.LoginData.*;
import static com.codeborne.selenide.Selenide.*;
import static data.CommentsData.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RecourcesTests implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, ClientPage {

    private LoginPage loginPage = new LoginPage();
    private String hostServer;
    private String hostClient;
    private String classTest = "";
    private WebDriver driver = null;
    private String sshCommandIsContact = "sudo -u roschat psql -c \"select cid, login from users;\" | grep ";
    private String[] users = {CLIENT_USER_A, CLIENT_USER_B, CLIENT_USER_C, CLIENT_USER_D, CLIENT_USER_E, CLIENT_USER_F,
            CONTACT_A, CONTACT_B, CONTACT_C};

    public RecourcesTests() {
        hostServer = "https://" + HOST_SERVER + ":" + PORT_SERVER;
        hostClient = "https://" + HOST_SERVER;
    }

    @Override
    public void beforeAll(ExtensionContext context){

        classTest = String.valueOf(context.getTestClass());

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("chrome");
        capabilities.setVersion("80.0");
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", false);
        capabilities.setCapability("acceptInsecureCerts", true);


        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
        capabilities.setCapability("goog:loggingPrefs", logPrefs);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        try {
            driver = WebDriverPool.DEFAULT.getDriver(URI.create("http://" + HOST_HUB + ":4444/wd/hub").toURL(), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.manage().window().setPosition(new Point(2,2));
        WebDriverRunner.setWebDriver(driver);

        Configuration.screenshots = false;

        if(classTest.contains("TestLogin")){
            assertTrue(SSHManager.isCheckQuerySSH(sshCommandIsContact + CLIENT_USER_A),
                    "Отсутствует пользователь " + CLIENT_USER_A + " в БД, невозможно начать тестирование");
            Configuration.baseUrl = hostClient;
            open("/");
        }

        if(classTest.contains("TestChatsPage")) {
            assertTrue(SSHManager.isCheckQuerySSH(sshCommandIsContact + CLIENT_USER_A),
                    "Отсутствует пользователь " + CLIENT_USER_A + " в БД, невозможно начать тестирование");
            assertTrue(SSHManager.isCheckQuerySSH(sshCommandIsContact + CLIENT_USER_B),
                    "Отсутствует пользователь " + CLIENT_USER_B + " в БД, невозможно начать тестирование");
        }

        if(classTest.contains("Channel")){
            for(int i = 0; i < users.length; i++){
                assertTrue(SSHManager.isCheckQuerySSH(sshCommandIsContact + users[i]),
                        "В БД нет учётной записи " + users[i] + " невозможно начать тестирование каналов");
            }
        }
    }

    @Override
    public void beforeEach(ExtensionContext context){
        sleep(2000);
        String method = context.getTestMethod().toString();
        if(classTest.contains("TestChatsPage")){
            if(method.contains("test_Get_New_Message")) openClient( CLIENT_USER_A + "@ros.chat", false);
            else openClient(CLIENT_USER_B + "@ros.chat", false);
        }
    }

    private void openClient(String login, boolean staySystem){
        Configuration.baseUrl = hostClient;
        open("/");
        if(ClientPage.isLoginWindow()) {
            assertTrue(loginClientClickButtonEnter(login, USER_ACCOUNT_PASSWORD, staySystem, true).isSuccessAuthClient(), "Ошибка при " +
                    "авторизации");
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
        if(classTest.contains("TestLogin")){
            refresh();
        }
    }
}
