package client;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.ElementShould;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.*;

public interface ClientPage {

    SelenideElement buttonPencil = $("i.fal.fa-pencil");
    SelenideElement inputEmail = $("input.input.non-border-input[type='text']");
    SelenideElement inputPassword = $("input.input.non-border-input[type='password']");
    SelenideElement iStaySystem = $("i.fal.fa-check");
    SelenideElement buttonLogin = $("button#login-btn");
    SelenideElement divSide = $("div#side");

    //Раздел инструментов(Рация, Контакты, Беседы, Вызовы)
    ElementsCollection itemsToolbar = $$("div.toolbar-wrapper span");
    SelenideElement divMainHeader = $("div.main-header");
    SelenideElement inputSearch = $("div.search-wrapper input");
    SelenideElement divSuccessLogin = $("div.side div.section-header h4.header-text");

    @Step(value = "Проверяем, появилось ли окно авторизации на WEB-клиенте")
    default boolean isLoginWindow(){
        try{
            buttonPencil.waitUntil(Condition.visible, 30000);
        }catch (ElementNotFound elementNotFound){
            return false;
        }

        return true;
    }

    @Step(value = "Нажимаем кнопку 'Ввести логин и пароль'")
    static void clickButtonPencil(){
        buttonPencil.click();
    }

    @Step(value = "Вводим адрес электронной почты {email}")
    static void sendInputEmail(String email){
        inputEmail.sendKeys(Keys.CONTROL + "a");
        inputEmail.sendKeys(Keys.BACK_SPACE);
        inputEmail.sendKeys(email);
    }

    @Step(value = "Вводим пароль {password}")
    static void sendInputPassword(String password){
        inputPassword.sendKeys(Keys.CONTROL + "a");
        inputPassword.sendKeys(Keys.BACK_SPACE);
        inputPassword.sendKeys(password);
    }

    @Step(value = "Нажимаем челбокс, чтобы оставаться в системе")
    static void clickCheckboxStaySystem(){
        iStaySystem.click();
    }

    @Step(value = "Нажимаем кнопку 'Войти'")
    static void clickButtonLogin(){
        buttonLogin.click();
    }

    static boolean loginClient(String email, String password, boolean staySystem){
        clickButtonPencil();
        sendInputEmail(email);
        sendInputPassword(password);
        if(staySystem) clickCheckboxStaySystem();
        clickButtonLogin();
        return isLoginClient();
    }

    @Step(value = "Проверяем, что появился заголовок контакта/группы/канала")
    default boolean isSearchMainHeader() {
        try{
            divMainHeader.shouldNotBe(Condition.visible);
        }catch (ElementShould e){
            return false;
        }
        return true;
    }

    @Step(value = "Вводим в поле поиска {text}")
    default ClientPage sendInputSearch(String text){
        inputSearch.sendKeys(Keys.CONTROL + "a");
        inputSearch.sendKeys(Keys.BACK_SPACE);
        inputSearch.sendKeys(text);
        return this;
    }

    @Step(value = "Проверяем, авторизованы ли мы на клиенте")
    static boolean isLoginClient(){
        try{
            divSuccessLogin.shouldBe(Condition.visible);
        }catch (ElementNotFound elementNotFound){
            return false;
        }
        return true;
    }

    Object getInstanceClient(String type);

}
