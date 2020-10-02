package client;

import client.still.ProfilePage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static chat.ros.testing2.data.ContactsData.USER_ACCOUNT_PASSWORD;
import static com.codeborne.selenide.Selenide.refresh;
import static data.CommentsData.CLIENT_7000;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(RecourcesTests.class)
@ExtendWith(WatcherTests.class)
@Epic(value = "Окно авторизации")
@Feature(value = "")
public class TestLogin extends ProfilePage {

    private final String login = CLIENT_7000 + "@ros.chat";
    private final String wrongLogin = CLIENT_7000 + "ros.chat";
    private final String wrongPassword = USER_ACCOUNT_PASSWORD + "qwerty";

    @Story(value = "Проверяем авторизацию на клиенте без опции 'Остаться в системе'")
    @Description(value = "Авторизовываемся на клиенте без опции 'Остаться в системе'\n" +
            "Перезагружаем страницу клиента и проверяем, что после перезагрузки появилась окно авторизации")
    @Test
    void test_Login_Without_Stay_System(){
        if(ClientPage.isLoginWindow()) {
            ClientPage.loginClientClickButtonOrEnter(login, USER_ACCOUNT_PASSWORD, false, true);
            assertTrue(isSuccessAuthClient(),
                    "Ошибка при авторизации");
            refresh();
            assertTrue(ClientPage.isVisibleElement(ClientPage.buttonPencil, true) &&
                    ClientPage.isLoginWindow(), "После перезагрузки остались авторизованными на клиенте");
        }
    }

    @Story(value = "Проверяем авторизацию на клиенте c опцией 'Остаться в системе'")
    @Description(value = "1. Авторизовываемся на клиенте с опцией 'Остаться в системе'\n" +
            "2. Перезагружаем страницу клиента и проверяем, что остались авторизованными\n" +
            "3. Выходим их профиля и проверяем, что разлогинились")
    @Test
    void test_Login_With_Stay_System(){
        if(ClientPage.isLoginWindow()) {
            ClientPage.loginClientClickButtonOrEnter(login, USER_ACCOUNT_PASSWORD, true, true);
            assertTrue(isSuccessAuthClient(),
                    "Ошибка при авторизации");
            refresh();
            assertTrue(isSuccessAuthClient(), "После перезагрузки браузера разлогинились");
            logoutUser().clickButtonConfirmFormLogout("Ок");
            assertTrue(ClientPage.isVisibleElement(ClientPage.buttonPencil, true) &&
                    ClientPage.isLoginWindow(), "После перезагрузки остались авторизованными на клиенте");
        }
    }

    @Story(value = "Проверяем авторизацию на клиенте по нажатию клавиши Enter")
    @Description(value = "1. Вводим почту и пароль и нажимаем Enter\n" +
            "Проверяем, что успешно авторизовались на сервере")
    @Test
    void test_Login_Press_Enter(){
        if(ClientPage.isLoginWindow()) {
            ClientPage.loginClientClickButtonOrEnter(login, USER_ACCOUNT_PASSWORD, false, false);
            assertTrue(isSuccessAuthClient(),
                    "Ошибка при авторизации");
        }
    }

    @Story(value = "Проверяем авторизацию на клиенте c невалидным логином")
    @Description(value = "1. Вводим невалидную почту и валидный пароль и пробуем авторизоваться\n" +
            "2. Проверяем, что появилось окно об ошибке авторизации")
    @Test
    void test_Reg_With_Wrong_Login(){
        if(ClientPage.isLoginWindow()) {
            ClientPage.loginClientClickButtonOrEnter(wrongLogin, USER_ACCOUNT_PASSWORD, false, true);
            assertTrue(isConfirmErrorModalWindow(), "Не появилось модальное окно об ошибке при авторизации");
            assertEquals(getTextConfirmErrorModalWindow(), "Неверное имя пользователя или пароль",
                    "Надпись в модальном окне не совпадает с эталонной");
            clickButtonConfirmModalWindow();
        }
    }

    @Story(value = "Проверяем авторизацию на клиенте c невалидным паролем")
    @Description(value = "1. Вводим валидную почту и невалидный пароль и пробуем авторизоваться\n" +
            "2. Проверяем, что появилось окно об ошибке авторизации")
    @Test
    void test_Reg_With_Wrong_Password(){
        if(ClientPage.isLoginWindow()) {
            ClientPage.loginClientClickButtonOrEnter(login, wrongPassword, false, true);
            assertTrue(isConfirmErrorModalWindow(),
                    "Не появилось модальное окно об ошибке при авторизации");
            assertEquals(getTextConfirmErrorModalWindow(), "Неверное имя пользователя или пароль",
                    "Надпись в модальном окне не совпадает с эталонной");
            clickButtonConfirmModalWindow();
        }
    }
}
