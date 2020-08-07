package client.still;

import client.ClientPage;
import io.qameta.allure.Step;

public interface ProfilePageInterface {

    String locatorConfirmFormLogout = ".v--modal-box.v--modal";
    String locatorButtonConfirmFormLogout = ".footer .btn";

    boolean isUserLogin(String user);
    ProfilePage logoutUser();
    ClientPage clickButtonConfirmFormLogout(String button);
}
