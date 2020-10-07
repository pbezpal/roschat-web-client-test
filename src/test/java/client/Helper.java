package client;

import client.tools.APIToServer;
import client.tools.SSHGetCommand;

import static chat.ros.testing2.data.ContactsData.USER_ACCOUNT_PASSWORD;

public interface Helper {

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
