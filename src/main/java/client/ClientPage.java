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

    ElementsCollection loginWindow = $$("div.component-footer span");
    SelenideElement buttonPencil = $("i.fal.fa-pencil");
    SelenideElement inputEmail = $("input.input.non-border-input[type='text']");
    SelenideElement divValueInputEmail = $(".non-border-input[type='text']").parent();
    SelenideElement inputPassword = $("input.input.non-border-input[type='password']");
    SelenideElement divValueInputPassword = $(".non-border-input[type='password']").parent();
    SelenideElement iStaySystem = $("i.fal.fa-check");
    SelenideElement buttonLogin = $("button#login-btn");
    SelenideElement confirmErrorModalWindow = $(".v--modal-box");
    SelenideElement confirmErrorModalWindowText = confirmErrorModalWindow.$(".body");
    SelenideElement confirmErrorModalWindowButton = confirmErrorModalWindow.$("button");
    SelenideElement buttonConfirmError = $("div.alert-confirm button");
    SelenideElement successAuthClient = $("div.columns-layout:not([style='display: none;'])");

    SelenideElement inputSelectContacts = $("div.select-contact input.search-input");
    ElementsCollection listContacts = $$("div.contact-list div.fio.name");

    //Раздел инструментов(Рация, Контакты, Беседы, Вызовы, Ещё)
    ElementsCollection itemsToolbar = $$("div.toolbar-wrapper span");
    SelenideElement divMainHeader = $("div.main-header");
    SelenideElement divMainHeaderContextMenu = divMainHeader.find("div.circle-animation[title] svg");
    SelenideElement inputSearch = $("div.search-wrapper input");
    SelenideElement divSuccessLogin = $("div.side div.section-header h4.header-text");

    //Раздел инфо
    SelenideElement divInfoWrapper = $("div.info-wrapper");
    SelenideElement iEditName = $("i.fas.fa-pencil-alt");
    SelenideElement divCommonText = $("div.common-text");

    @Step(value = "Проверяем, появилось ли окно авторизации на WEB-клиенте")
    static boolean isLoginWindow(){
        try{
            loginWindow.findBy(Condition.text("Ввести логин и пароль")).waitUntil(Condition.visible, 30000);
        }catch (ElementNotFound elementNotFound){
            return false;
        }

        return true;
    }

    @Step(value = "Нажимаем кнопку 'Ввести логин и пароль'")
    static void clickButtonPencil(){
        for(int i = 0; i < 3 && isVisibleElement(buttonPencil, true); i++){
            buttonPencil.click();
            sleep(500);
        }
    }

    @Step(value = "Проверяем, виден ли элемент {element}")
    static boolean isVisibleElement(SelenideElement element, boolean show) {
        if (show){
            try {
                element.shouldBe(Condition.visible);
            } catch (ElementNotFound e) {
                return false;
            }
        }else{
            try {
                element.shouldBe(Condition.not(Condition.visible));
            } catch (ElementShould e) {
                return false;
            }
        }

        return true;
    }

    @Step(value = "Вводим адрес электронной почты {email}")
    static void sendInputEmail(String email){
        for(int i = 0; i < 3 && divValueInputEmail.getAttribute("input_text").isEmpty(); i++){
            inputEmail.sendKeys(Keys.CONTROL + "a");
            inputEmail.sendKeys(Keys.BACK_SPACE);
            inputEmail.sendKeys(email);
            sleep(500);
        }
    }

    @Step(value = "Вводим пароль {password}")
    static void sendInputPassword(String password){
        for(int i = 0; i < 3 && divValueInputPassword.getAttribute("input_text").isEmpty(); i++){
            inputPassword.sendKeys(Keys.CONTROL + "a");
            inputPassword.sendKeys(Keys.BACK_SPACE);
            inputPassword.sendKeys(password);
            sleep(500);
        }
    }

    @Step(value = "Нажимаем чекбокс, чтобы оставаться в системе")
    static void clickCheckboxStaySystem(){
        iStaySystem.click();
    }

    @Step(value = "Нажимаем кнопку 'Войти'")
    static void clickButtonLogin(){
        buttonLogin.click();
    }

    static void loginClientClickButtonOrEnter(String email, String password, boolean staySystem, boolean clickButton){
        clickButtonPencil();
        sendInputEmail(email);
        sendInputPassword(password);

        if(staySystem) clickCheckboxStaySystem();
        //Вход по кнопке
        if(clickButton) clickButtonLogin();
        //Вход по нажатию Enter
        else inputPassword.pressEnter();
    }

    @Step(value = "Проверяем, что авторизованы на клиенте")
    default boolean isSuccessAuthClient(){
        try {
            successAuthClient.waitUntil(Condition.visible,60000);
        }catch (ElementNotFound e){
            return false;
        }

        return true;
    }

    @Step(value = "Проверяем, появилось ли модальное окно об ошибке авторизации")
    default boolean isConfirmErrorModalWindow(){
        try{
            confirmErrorModalWindow.should(Condition.visible);
        }catch (ElementNotFound e){
            return false;
        }

        return true;
    }

    @Step(value = "Проверяем текст в модальном окне об ошбке авторизации")
    default String getTextConfirmErrorModalWindow(){
        return confirmErrorModalWindowText.text();
    }

    @Step(value = "Нажимаем кнопку Ок в модальном окне об ошибке авторизации")
    default void clickButtonConfirmModalWindow(){
        confirmErrorModalWindowButton.click();
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

    @Step(value = "Нажимаем на заголовок контакта/группы/канала")
    default ClientPage clickMainHeaderText(){
        divMainHeader.find("div.header-user-block").click();
        return this;
    }

    @Step(value = "Вводим имя пользователя в поле поиска")
    default ClientPage searchContactForAction(String fio){
        inputSelectContacts.sendKeys(Keys.CONTROL + "a");
        inputSelectContacts.sendKeys(Keys.BACK_SPACE);
        inputSelectContacts.sendKeys(fio);
        return this;
    }

    /** Раздел информации **/

    @Step(value = "Проверяем, отображается ли раздел информации")
    default boolean isDivInfoWrapper(boolean show){
        if(show){
            try{
                divInfoWrapper.shouldBe(Condition.visible);
            }catch (ElementNotFound e){
                return false;
            }
        } else {
            try{
                divInfoWrapper.shouldBe(Condition.not(Condition.visible));
            }catch (ElementShould e){
                return false;
            }
        }

        return true;
    }

    @Step(value = "Нажимаем карандаш для редактирования")
    default void clickPencilEdit(){ iEditName.click(); }

    @Step(value = "Проверяем название/имени {name} в разделе информации")
    default String getTitleName(String name){
        return $("div[title='" + name + "']").text();
    }

    @Step(value = "Выбираем найденный контакт")
    default ClientPage selectFoundContact(String contact){
        listContacts.findBy(Condition.text(contact)).shouldBe(Condition.visible).click();
        return this;
    }

    @Step(value = "Вводим в поле поиска {text}")
    default ClientPage sendInputSearch(String text){
        inputSearch.sendKeys(Keys.CONTROL + "a");
        inputSearch.sendKeys(Keys.BACK_SPACE);
        inputSearch.sendKeys(text);
        return this;
    }

    @Step(value = "Проверяем, что не появилось сообщение об ошибке")
    static boolean isNotShowConfirmError(){
        try{
            buttonConfirmError.shouldNotBe(Condition.visible);
        }catch (ElementShould elementShould){
            return false;
        }
        return true;
    }

}
