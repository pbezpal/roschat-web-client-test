package client;

import chat.ros.testing2.helpers.SSHManager;
import chat.ros.testing2.server.contacts.ContactsPage;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
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
import static com.codeborne.selenide.Selenide.open;
import static data.CommentsData.*;
import static data.CommentsData.CLIENT_7005;
import static org.junit.jupiter.api.Assertions.assertTrue;

public interface StartParallelTest extends ClientPage {

    String HOST_SERVER = System.getProperty("hostserver");
    String PORT_SERVER = System.getProperty("portserver");

    String hostApiServer = "https://" + HOST_SERVER + ":8080";
    String hostServer = "https://" + HOST_SERVER + ":" + PORT_SERVER;
    String hostClient = "https://" + HOST_SERVER;
    String sshCommandIsContact = "sudo -u roschat psql -c \"select cid, login from users;\" | grep ";
    String[] users = {CLIENT_7000, CLIENT_7001, CLIENT_7002, CLIENT_7003, CLIENT_7004, CLIENT_7005,
            CLIENT_7006, CLIENT_7007};

    static void init(){
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

        WebDriver driver = getDriver(capabilities);

        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.manage().window().setPosition(new Point(2,2));
        WebDriverRunner.setWebDriver(driver);

        Configuration.screenshots = false;

        for(int i = 0; i < users.length; i++){
            addContactAndAccount(users[i]);
        }
    }

    default void openClient(String login, boolean staySystem){
        Configuration.baseUrl = hostClient;
        open("/");
        if(ClientPage.isLoginWindow()) {
            ClientPage.loginClientClickButtonOrEnter(login, USER_ACCOUNT_PASSWORD, staySystem, true);
            assertTrue(isSuccessAuthClient(), "Ошибка при " +
                    "авторизации");
        }
    }

    static void addContactAndAccount(String number){
        if (!SSHManager.isCheckQuerySSH(sshCommandIsContact + number)) {
            ContactsPage contactsPage = new ContactsPage();
            //openMS("/contacts");
            //contactsPage.addContact(number).addUserAccount(number, USER_ACCOUNT_PASSWORD, USER_ACOUNT_ITEM_MENU);
        }
    }

    static WebDriver getDriver(DesiredCapabilities capabilities){
        try {
            return WebDriverPool.DEFAULT.getDriver(URI.create("http://" + HOST_HUB + ":4444/wd/hub").toURL(), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

    }
}
