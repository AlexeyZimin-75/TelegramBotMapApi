package org.example.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import java.util.ArrayList;
import java.util.List;

public class LocationKeyboard {
    public ReplyKeyboardMarkup createLocationKeyboard() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(true);
        keyboard.setSelective(true);

        List<KeyboardRow> buttons = new ArrayList<>();
        KeyboardButton autoLocationButton = new KeyboardButton("📍 Отправить локацию");
        autoLocationButton.setRequestLocation(true);
        KeyboardButton manuallyLocationButton = new KeyboardButton("\uD83D\uDC49 ввести город вручную");
        KeyboardRow row1 = new KeyboardRow();
        row1.add(autoLocationButton);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(manuallyLocationButton);
        buttons.add(row1);
        buttons.add(row2);
        keyboard.setKeyboard(buttons);
        return keyboard;
    }
}
