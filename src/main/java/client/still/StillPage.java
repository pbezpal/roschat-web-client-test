package client.still;

import client.ClientPage;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public interface StillPage extends ClientPage {

    SelenideElement itemStill = $("i.fa.fa-equals");
    SelenideElement itemProfileInfo = $(".profile-info");
    ElementsCollection spanItemTitle = $$("span.item-title");

    @Step(value = "Переходим в раздел Ещё")
    default StillPage clickItemStill(){
        itemStill.click();
        return this;
    }

    StillPage clickSectionStill();

    @Step(value = "Переходим в раздел {section}")
    default StillPage clickSectionStill(String section){
        spanItemTitle.findBy(Condition.text(section)).click();
        return this;
    }
}
