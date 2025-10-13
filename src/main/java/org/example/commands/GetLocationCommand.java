package org.example.commands;


import org.example.apiMethods.YandexMapsClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;


import java.util.ArrayList;
import java.util.List;


public class GetLocationCommand implements Command{
    @Override
    public String getCommandName() {
        return "location";
    }

    @Override
    public String getDescription() {
        return "Геолокация ";
    }

    @Override //функция запрашивает доступ к геолокации
    public void execute(AbsSender absSender, Message message) {



        System.out.println("началось выполнение");

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("📍 Пожалуйста, разрешите доступ к вашей геолокации\n\nНажмите кнопку ниже:");

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        KeyboardButton locationButton = new KeyboardButton();
        locationButton.setText("Разрешить доступ к геолокации");
        locationButton.setRequestLocation(true);

        row.add(locationButton);
        keyboard.add(row);
        replyKeyboardMarkup.setKeyboard(keyboard);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);


        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки сообщения: " + e.getMessage());
        }

    }

    public void showDataOfLocation(Update update,AbsSender absSender) throws Exception {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        Double longitude = update.getMessage().getLocation().getLongitude();
        Double latitude = update.getMessage().getLocation().getLatitude();
//        sendMessage.setText("Ваши данные: " + update.getMessage().getLocation().getLongitude() + " " + update.getMessage().getLocation().getLatitude());
        sendMessage.setText(getCityFromCoordinates(latitude, longitude));

        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCityFromCoordinates(double latitude, double longitude) throws Exception {

        YandexMapsClient yandexMapsClient = new YandexMapsClient();

        String city = yandexMapsClient.getCityName(longitude, latitude);
        return yandexMapsClient.getLandmarks(city);
    }
}





