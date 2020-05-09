package data;

public interface CommentsData {

    /***** Параметры для создания канала на клиенте ******/
    String CLIENT_ITEM_NEW_CHANNEL = "Новый канал";
    String CLIENT_USER_A = "7000";
    String CLIENT_USER_B = "7001";
    String CLIENT_USER_C = "7002";
    String CLIENT_USER_D = "7003";
    String CLIENT_USER_E = "7004";
    String CLIENT_USER_F = "7005";

    /***** Публичный проверенный канал ******/
    String CLIENT_NAME_CHANNEL_PUBLIC_PROVEN = "Публичный проверенный канал";
    String CLIENT_DESCRIPTION_CHANNEL_PUBLIC_PROVEN = "Описание публичного проверенного канала";
    String CLIENT_NEW_NAME_PUBLIC_CHANNEL_PROVEN = "Новое название публичного проверенного канала";
    String CLIENT_NEW_DESCRIPTION_PUBLIC_CHANNEL_PROVEN = "Новое описание публичного проверенного канала";
    String CLIENT_TYPE_CHANNEL_PUBLIC = "Публичный";

    String CLIENT_NAME_CHANNEL_CLOSED = "Закрытый тестовый канал";
    String CLIENT_DESCRIPTION_CHANNEL_CLOSED = "Описание закрытого тестового канала";
    String CLIENT_NEW_NAME_CHANNEL_CLOSED = "Новое название закрытого тестового канала";
    String CLIENT_NEW_DESCRIPTION_CHANNEL_CLOSED = "Новое описание закрытого тестового канала";
    String CLIENT_TYPE_CHANNEL_CLOSED = "Закрытый";

    /***** Публикации в канале ******/
    String CLIENT_NEW_PUBLICATION_CHANNEL_CONTEXT_MENU = "Новая публикация";
    String CLIENT_TITLE_PUBLICATION_CHANNEL = "Тестовая публикация";
    String CLIENT_DESCRIPTION_PUBLICATION_CLOSED_CHANNEL = "Текст описания первой публикации закрытого канала";
    String CLIENT_DESCRIPTION_PUBLICATION_PUBLIC_CHANNEL = "Текст описания первой публикации публичного канала";
    String CLIENT_PUBLICATION_EVENT = "publication-event";

    /***** Поделиться ссылкой ******/
    String CLIENT_INFO_SHARE_LINK_CHANNEL = "Поделиться ссылкой";
    String CLIENT_INFO_COPY_LINK_CHANNEL = "Копировать ссылку";
    String CLIENT_SHARE_LINK_CHANNEL_CONTEXT_MENU = "Поделиться";
    String CLIENT_BUTTON_SHARE_LINK_CHANNEL = "Поделиться";

    /****** Добавление подписчиков ******/
    String CLIENT_INFO_ITEM_ADMIN_CHANNEL = "Администраторы";
    String CLIENT_INFO_ITEM_USER_CHANNEL = "Подписчики";
    String CLIENT_ADD_USER_CHANNEL = "Добавить";
    String CLIENT_INFO_SUBSCRIBER_CHANNEL = "Подписаться на канал";
    String CLIENT_INFO_EXIT_CHANNEL = "Выйти из канала";

    /***** Параметры для проверки бесед ******/
    String CLIENT_ITEM_NEW_CHAT = "Новая беседа";
    String CLIENT_CHATS_RECEIVED_MESSAGE = "Это тестовое сообщение от пользователя 7013 отправленное пользователю 7012";
    String CLIENT_CHATS_SEND_EVENT = "message-event";
    String CLIENT_CHATS_SEND_MESSAGE = "Это тестовое сообщение от пользователя 7012 отправленное пользователю 7013";
}
