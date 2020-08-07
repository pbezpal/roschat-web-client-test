package client;

import chat.ros.testing2.server.LoginPage;
import client.tools.APIToServer;
import client.tools.SSHGetCommand;

import static chat.ros.testing2.data.ContactsData.USER_ACCOUNT_PASSWORD;
import static chat.ros.testing2.data.LoginData.*;

public interface Helper {

    LoginPage loginPage = new LoginPage();
    String sshCommandIsContact = "sudo -u roschat psql -c \"select cid, login from users;\" | grep ";
    String hostServer = "https://" + HOST_SERVER + ":1110";
    String hostClient = "https://" + HOST_SERVER;

    default APIToServer getApiToServer(String host, String user, APIToServer apiToServer){
        if(apiToServer == null){
            return new APIToServer(host, user, USER_ACCOUNT_PASSWORD);
        }else return apiToServer;
    }

    default String getIDChannel(String channel){
        return SSHGetCommand.isCheckQuerySSH(
                "sudo -u roschat psql -c \"select * from channels;\" " +
                        "| grep '" + channel + "' | awk '{print $1}'"
        );
    }
}
