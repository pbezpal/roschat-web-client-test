package data;

public interface CommentsData {

    /***** Параметры для создания канала на клиенте ******/
    String CLIENT_ITEM_NEW_CHANNEL = "Новый канал";

    /***** Публичный канал ******/
    String CLIENT_NAME_CHANNEL_PUBLIC = "Публичный тестовый канал";
    String CLIENT_DESCRIPTION_CHANNEL_PUBLIC = "Описание публичного тестового канала";
    String CLIENT_NEW_NAME_PUBLIC_CHANNEL = "Новое название публичного тестового канала";
    String CLIENT_NEW_DESCRIPTION_PUBLIC_CHANNEL = "Новое описание публичного тестового канала";
    String CLIENT_TYPE_CHANNEL_PUBLIC = "Публичный";

    /***** Публичный проверенный канал ******/
    String CLIENT_NAME_CHANNEL_PUBLIC_PROVEN = "Публичный проверенный канал";
    String CLIENT_DESCRIPTION_CHANNEL_PUBLIC_PROVEN = "Описание публичного проверенного канала";
    String CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN = "Новое название публичного проверенного канала";
    String CLIENT_NEW_DESCRIPTION_PUBLIC_CHANNEL_PROVEN = "Новое описание публичного проверенного канала";

    String CLIENT_NAME_CHANNEL_CLOSED = "Закрытый тестовый канал";
    String CLIENT_DESCRIPTION_CHANNEL_CLOSED = "Описание закрытого тестового канала";
    String CLIENT_NEW_NAME_CHANNEL_CLOSED = "Новое название закрытого тестового канала";
    String CLIENT_NEW_DESCRIPTION_CHANNEL_CLOSED = "Новое описание закрытого тестового канала";
    String CLIENT_TYPE_CHANNEL_CLOSED = "Закрытый";


    /***** Параметры для проверки бесед ******/
    String CLIENT_ITEM_NEW_CHAT = "Новая беседа";
    String CLIENT_CHATS_RECEIVED_MESSAGE = "Это тестовое сообщение от пользователя 7013 отправленное пользователю 7012";
    String CLIENT_CHATS_SEND_EVENT = "message-event";
    String CLIENT_CHATS_SEND_MESSAGE = "Это тестовое сообщение от пользователя 7012 отправленное пользователю 7013";
}
