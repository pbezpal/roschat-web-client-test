package client.radio;

import client.ClientPage;
import com.codeborne.selenide.Condition;

public interface RadioPage extends ClientPage {

    @Override
    default Object getInstanceClient(String login, String password, String type) {
        getLoginClient(login, password).shouldBe(Condition.visible);
        switch(type){
            case "Рация":
                return null;
        }
        return null;
    }
}
