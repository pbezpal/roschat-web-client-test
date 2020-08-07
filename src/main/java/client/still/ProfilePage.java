package client.still;

import client.ClientPage;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static data.StillData.STILL_PROFILE_BUTTON_LOGOUT;

public class ProfilePage implements StillPage, ProfilePageInterface {

    protected final SelenideElement confirmLogoutForm = $(locatorConfirmFormLogout);
    protected final ElementsCollection buttonConfirmLogoutForm = confirmLogoutForm.$$(locatorButtonConfirmFormLogout);

    public ProfilePage() {}

    @Step(value = "Проверяем, что мы авторизованы под пользователем {user}")
    @Override
    public boolean isUserLogin(String user){
        try{
            spanItemTitle.findBy(Condition.text(user)).shouldBe(Condition.visible);
        }catch (ElementNotFound e){
            return false;
        }
        return true;
    }

    @Step(value = "Нажимаем кнопку Выйти из профиля")
    @Override
    public ProfilePage logoutUser(){
        clickItemStill().clickSectionStill().spanItemTitle.
                findBy(Condition.text(STILL_PROFILE_BUTTON_LOGOUT)).click();
        return this;
    }

    @Step(value = "Нажимаем кнопку {button} в форме подтверждения выхода из профиля")
    @Override
    public ClientPage clickButtonConfirmFormLogout(String button) {
        buttonConfirmLogoutForm.findBy(Condition.text(button)).click();
        return this;
    }

    @Step(value = "Переходим в раздел Профиль")
    @Override
    public StillPage clickSectionStill() {
        itemProfileInfo.click();
        return this;
    }
}
