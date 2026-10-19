package org.example.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import java.util.ArrayList;
import java.util.List;

public class LastKeyboard {
    public ReplyKeyboardMarkup createStartKeyboard() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);

        keyboard.setOneTimeKeyboard(true);
        keyboard.setSelective(true);

        List<KeyboardRow> buttons = new ArrayList<>();
        KeyboardButton startLocationButton = new KeyboardButton("🗺️ Построить новый маршрут");
        KeyboardButton myRoutesButton = new KeyboardButton("📊 Мои маршруты");
        KeyboardButton getHelpButton = new KeyboardButton("🤖 Что умеет наш бот");



        KeyboardRow row2 = new KeyboardRow();
        row2.add(startLocationButton);
        row2.add(myRoutesButton);
        buttons.add(row2);

        KeyboardRow row3 = new KeyboardRow();
        row3.add(getHelpButton);
        buttons.add(row3);


        keyboard.setKeyboard(buttons);
        return keyboard;
    }
}
