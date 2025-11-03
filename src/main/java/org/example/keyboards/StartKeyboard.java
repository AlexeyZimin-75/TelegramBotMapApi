package org.example.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import java.util.ArrayList;
import java.util.List;

public class StartKeyboard {
    public ReplyKeyboardMarkup createStartKeyboard() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);

        keyboard.setOneTimeKeyboard(true);
        keyboard.setSelective(true);

        List<KeyboardRow> buttons = new ArrayList<>();
        KeyboardButton startLocationButton = new KeyboardButton("🗺️ Построить новый маршрут");
        KeyboardButton getLocationButton = new KeyboardButton("\uD83C\uDF0D Проложить маршрут");
        KeyboardButton getHelpButton = new KeyboardButton("🤖 Что умеет наш бот");



        KeyboardRow row1 = new KeyboardRow();
        row1.add(getLocationButton);
        buttons.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(startLocationButton);
        buttons.add(row2);

        KeyboardRow row3 = new KeyboardRow();
        row3.add(getHelpButton);
        buttons.add(row3);


        keyboard.setKeyboard(buttons);
        return keyboard;
    }
}
