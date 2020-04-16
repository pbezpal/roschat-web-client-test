package client.radio;

import client.ClientPage;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public interface RadioPage extends ClientPage {

    SelenideElement itemRadio = $("i.fa.fa-walkie-talkie");
    SelenideElement divSelectTypeRadio = $("div.selection-is-available").find("h4");
    ElementsCollection divRadioList = $$("div.radio-list span");


 /*   @Override
    default Object getInstanceClient(String login, String password, String type) {
        getLoginClient(login, password).shouldBe(Condition.visible);
        switch(type){
            case "Рация":
                return null;
        }
        return null;
    }*/

    @Step(value = "Проверяем, появился ли раздел Рация")
    default boolean isItemRadio(){
        try{
            itemRadio.should(Condition.visible);
        }catch (ElementNotFound element){
            return false;
        }

        return true;
    }

    @Step(value = "Нажимаем на иконку Рация")
    default RadioPage clickItemRadio(){
        itemRadio.click();
        return this;
    }

    @Step(value = "Нажимаем на элемент выбора типа Рации")
    default RadioPage clickSelectTypeRadio(){
        divSelectTypeRadio.click();
        return this;
    }

    @Step(value = "Выбираем тип Рации {type}")
    default RadioPage selectTypeRadio(String type){
        divRadioList.findBy(Condition.text(type)).click();
        return this;
    }

    @Step(value = "Проверяем, что отображается тип рации {type}")
    default boolean isRadioType(String type){
        return divSelectTypeRadio.text().equals(type);
    }
}
