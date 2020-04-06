package data;


import static chat.ros.testing2.data.GetDataTests.getProperty;

public interface CommentsData {

    String config = "Comments.properties";

    /***** Параметры для создания канала на клиенте ******/
    String CLIENT_TYPE_COMMENTS_CHANNELS = "Каналы";
    String CLIENT_ITEM_NEW_CHANNEL = "Новый канал";
    String CLIENT_NAME_CHANNEL_PUBLIC = "Public test channel";
    String CLIENT_TYPE_CHANNEL_PUBLIC = "Публичный";
    String CLIENT_NAME_CHANNEL_CLOSED = "Closed test channel";
    String CLIENT_TYPE_CHANNEL_CLOSED = "Закрытый";

}
