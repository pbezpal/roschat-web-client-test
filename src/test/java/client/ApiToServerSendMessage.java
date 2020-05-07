package client;

public class ApiToServerSendMessage extends Thread  {

    private APIToServer apiToServer;
    private String IDForReceivingMessageUser;
    private String message;

    public ApiToServerSendMessage(String host, String apiUser, String clientUser, String password, String message){
        this.apiToServer = new APIToServer(host, apiUser, password);
        this.IDForReceivingMessageUser = apiToServer.getContactIDBySurnameFromListOfContacts(clientUser, 60);
        this.message = message;
    }

    @Override
    public void run(){
        apiToServer.SendTextMessageToUser(
                "user",
                IDForReceivingMessageUser,
                "text",
                message,
                60
        );
        apiToServer.disconnect();
        interrupt();
    }

}
