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

    SelenideElement divLogin = $("div#login");
    SelenideElement buttonPencil = $("i.fal.fa-pencil");
    SelenideElement inputEmail = $("input.input.non-border-input[type='text']");
    SelenideElement inputPassword = $("input.input.non-border-input[type='password']");
    SelenideElement buttonLogin = $("button#login-btn");
    SelenideElement divSide = $("div#side");

    //Раздел инструментов(Рация, Контакты, Беседы, Вызовы)
    ElementsCollection itemsToolbar = $$("div.toolbar-wrapper span");
    SelenideElement divMainHeader = $("div.main-header");
    SelenideElement inputSearch = $("div.search-wrapper input");

    @Step(value = "Проверяем, появилось ли окно авторизации на WEB-клиенте")
    default boolean isLoginWindow(){
        try{
            divLogin.waitUntil(Condition.visible, 30000);
        }catch (ElementNotFound elementNotFound){
            return false;
        }

        return true;
    }

    @Step(value = "Нажимаем кнопку 'Ввести логин и пароль'")
    default ClientPage clickButtonPencil(){
        buttonPencil.click();
        return this;
    }

    @Step(value = "Вводим адрес электронной почты {email}")
    default ClientPage sendInputEmail(String email){
        inputEmail.sendKeys(Keys.CONTROL + "a");
        inputEmail.sendKeys(Keys.BACK_SPACE);
        inputEmail.sendKeys(email);
        return this;
    }

    @Step(value = "Вводим пароль {password}")
    default ClientPage sendInputPassword(String password){
        inputPassword.sendKeys(Keys.CONTROL + "a");
        inputPassword.sendKeys(Keys.BACK_SPACE);
        inputPassword.sendKeys(password);
        return this;
    }

    @Step(value = "Нажимаем кнопку 'Войти'")
    default ClientPage clickButtonLogin(){
        buttonLogin.click();
        return this;
    }

    default SelenideElement getLoginClient(String email, String password){
        if(isLoginWindow()) {
            clickButtonPencil();
            sendInputEmail(email);
            sendInputPassword(password);
            clickButtonLogin();

            return divSide;
        }

        return null;
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

    public Object getInstanceClient(String login, String password, String type);

}
