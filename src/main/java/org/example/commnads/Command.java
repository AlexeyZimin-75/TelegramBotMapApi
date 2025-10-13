package org.example.commnads;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;


public interface Command {


    String getCommandName();

    String getDescription();

    void execute(AbsSender absSender, Message message);
}