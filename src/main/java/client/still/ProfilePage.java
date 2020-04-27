package client.still;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ex.ElementNotFound;
import io.qameta.allure.Step;

import static data.StillData.STILL_PROFILE_BUTTON_LOGOUT;

public class ProfilePage implements StillPage {

    public ProfilePage() {}

    @Step(value = "Проверяем, что мы авторизованы под пользователем {user}")
    protected boolean isUserLogin(String user){
        try{
            spanItemTitle.findBy(Condition.text(user)).shouldBe(Condition.visible);
        }catch (ElementNotFound e){
            return false;
        }
        return true;
    }

    public void logoutUser(String user){
        if(isUserLogin(user)){
            clickSectionStill(user).spanItemTitle.findBy(Condition.text(STILL_PROFILE_BUTTON_LOGOUT)).click();
        }
    }
}
