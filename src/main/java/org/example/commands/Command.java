package org.example.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;


public interface Command {

    /**
     * Возвращает имя команды (то, что пользователь вводит после /)
     * @return имя команды
     */
    String getCommandName();

    /**
     * Возвращает описание команды для справки
     * @return описание команды
     */
    String getDescription();

    /**
     * Выполняет логику команды
     *
     * @param absSender объект для отправки сообщений
     * @param message   входящее сообщение от пользователя
     */
    Object execute(AbsSender absSender, Message message);

}