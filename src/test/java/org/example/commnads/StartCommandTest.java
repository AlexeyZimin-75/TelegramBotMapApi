package org.example.commnads;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.junit.jupiter.api.Assertions.*;
class StartCommandTest {

    private Command command = new StartCommand();


    @Test
    void getCommandName() {
        assertEquals("start",command.getCommandName());
    }

    @Test
    void getDescription() {
        assertEquals("Запустить бота и получить приветствие",command.getDescription());
    }

    @Test
    void execute( ) {

    }
}