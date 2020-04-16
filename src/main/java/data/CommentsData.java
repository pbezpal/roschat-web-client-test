package data;

public interface CommentsData {

    String config = "Comments.properties";

    /***** Параметры для создания канала на клиенте ******/
    String CLIENT_TYPE_COMMENTS_CHANNELS = "Каналы";
    String CLIENT_ITEM_NEW_CHANNEL = "Новый канал";
    String CLIENT_NAME_CHANNEL_PUBLIC = "Public test channel";
    String CLIENT_TYPE_CHANNEL_PUBLIC = "Публичный";
    String CLIENT_NAME_CHANNEL_CLOSED = "Closed test channel";
    String CLIENT_TYPE_CHANNEL_CLOSED = "Закрытый";

    /***** Параметры для проверки бесед ******/
    String CLIENT_TYPE_COMMENTS_CHATS = "Беседы";
    String CLIENT_CHATS_SEND_MESSAGE = "Это тестовое сообщение от пользователя 7013";

}
