package data;

public interface CommentsData {

    /***** Общие параметры *****/
    String CLIENT_BUTTON_NEXT = "Далее";
    String CLIENT_BUTTON_ADD = "Добавить";

    /***** Параметры для создания канала на клиенте ******/
    String CLIENT_ITEM_NEW_CHANNEL = "Новый канал";
    String CLIENT_7000 = "7000";
    String CLIENT_7001 = "7001";
    String CLIENT_7002 = "7002";
    String CLIENT_7003 = "7003";
    String CLIENT_7004 = "7004";
    String CLIENT_7005 = "7005";
    String CLIENT_7006 = "7006";
    String CLIENT_7007 = "7007";
    String CLIENT_7008 = "7008";
    String CLIENT_7009 = "7009";
    String CLIENT_7010 = "7010";

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
    String CLIENT_BUTTON_SHARE_MESSAGE = "Поделиться";

    /****** Добавление подписчиков ******/
    String CLIENT_INFO_ITEM_ADMIN_CHANNEL = "Администраторы";
    String CLIENT_INFO_ITEM_USER_CHANNEL = "Подписчики";
    String CLIENT_INFO_SUBSCRIBER_CHANNEL = "Подписаться на канал";
    String CLIENT_INFO_EXIT_CHANNEL = "Выйти из канала";
    String CLIENT_PUBLICATION = "li#publication%1$s";
    String CLIENT_DELETE_CHANNEL_CONTEXT_MENU = "Удалить канал";

    /***** Параметры для проверки бесед ******/
    String CLIENT_ITEM_NEW_CHAT = "Новая беседа";
    String CLIENT_CHATS_RECEIVED_MESSAGE = "Это тестовое сообщение от пользователя 7013 отправленное пользователю 7012";
    String CLIENT_CHATS_SEND_EVENT = "message-event";
    String CLIENT_CHATS_FIRST_MESSAGE = "Это первое тестовое сообщение. Если оно отображается у всех пользователей, значит" +
            " тест прошёл успешно";
    String CLIENT_CHATS_TEXT_REPLY_MESSAGE = "Это ответное тестовое сообщение. Если оно отображается у всех пользователей, значит" +
            " тест прошёл успешно";
    String CLIENT_CHATS_ITEM_DELETE_CHAT = "Удалить беседу";

    /***** Параметры для проверки групповой беседы ******/
    String CLIENT_ITEM_NEW_GROUP_CHAT = "Новая группа";
    String CLIENT_GROUP_CHAT_NAME = "Тестовая групповая беседа";
    String CLIENT_GROUP_CHAT_AUTH = "7000  создал(а) беседу";
}
