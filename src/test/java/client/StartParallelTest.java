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
import static client.Helper.loginPage;
import static com.codeborne.selenide.Selenide.open;
import static data.CommentsData.*;
import static data.CommentsData.CLIENT_USER_F;
import static org.junit.jupiter.api.Assertions.assertTrue;

public interface StartParallelTest {

    String hostApiServer = "https://" + HOST_SERVER + ":8080";
    String hostServer = "https://" + HOST_SERVER + ":" + PORT_SERVER;
    String hostClient = "https://" + HOST_SERVER;
    String sshCommandIsContact = "sudo -u roschat psql -c \"select cid, login from users;\" | grep ";
    String[] users = {CLIENT_USER_A, CLIENT_USER_B, CLIENT_USER_C, CLIENT_USER_D, CLIENT_USER_E, CLIENT_USER_F,
            CONTACT_NUMBER_7012, CONTACT_NUMBER_7013};

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

    static void openMS(String page){
        Configuration.baseUrl = hostServer;
        if( ! WebDriverRunner.getWebDriver().getCurrentUrl().contains(hostServer)) open("/");
        if( ! loginPage.isLoginMS()) loginPage.loginOnServer(LOGIN_ADMIN_MS, PASSWORD_ADMIN_MS);
        assertTrue(loginPage.isLoginMS(), "Не удалось авторизоваться");
        open(page);
    }

    default void openClient(String login, boolean staySystem){
        Configuration.baseUrl = hostClient;
        open("/");
        if(ClientPage.isLoginWindow()) {
            assertTrue(ClientPage.loginClient(login, USER_ACCOUNT_PASSWORD, staySystem), "Ошибка при " +
                    "авторизации");
        }
    }

    static void addContactAndAccount(String number){
        if (!SSHManager.isCheckQuerySSH(sshCommandIsContact + number)) {
            ContactsPage contactsPage = new ContactsPage();
            openMS("/contacts");
            contactsPage.addContact(number).addUserAccount(number, USER_ACCOUNT_PASSWORD, USER_ACOUNT_ITEM_MENU);
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
