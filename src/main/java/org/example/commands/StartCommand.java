package org.example.commands;

import org.example.keyboards.StartKeyboard;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.example.service.UserStateService;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.example.states.UserState;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.example.service.UserDataService;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.example.DataBaseManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Команда /start - приветствие пользователя при запуске бота
 */
public class StartCommand implements Command {
    private final UserStateService userStateService;
    private final UserDataService userDataService;
    private  Map<String,String> startTriggers;
    private final DataBaseManager dataBaseManager;

    public StartCommand(UserStateService userStateService, UserDataService userDataService) {
        this.userStateService = userStateService;
        this.userDataService = userDataService;
        this.dataBaseManager = new DataBaseManager();
        startTriggers = new HashMap<>();
        startTriggers.put("🗺️ Построить новый маршрут","/start");
    }

    @Override
    public String getCommandName() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Запустить бот и получить приветствие";
    }

    @Override
    public SendPhoto execute(AbsSender absSender, Message message) {
        Long userId = message.getFrom().getId();
        dataBaseManager.saveUser(userId);

        userStateService.clearUserData(userId);
        userStateService.setUserState(userId, UserState.AWAITING_LOCATION);
        return sendWelcomeMessage(absSender, message.getChatId());
    }

    private SendPhoto sendWelcomeMessage(AbsSender absSender, long chatId) {
        try {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(String.valueOf(chatId));

            ReplyKeyboardMarkup replyKeyboardMarkup = new StartKeyboard().createStartKeyboard();
            sendPhoto.setReplyMarkup(replyKeyboardMarkup);



            String imageURL = "https://media.easemytrip.com/media/Blog/International/637007769287754861/637007769287754861GltpKG.jpg";
            sendPhoto.setPhoto(new InputFile(imageURL));
            sendPhoto.setCaption("️Добро пожаловать в туристический бот! 🗺️\n" +
                    "Я помогу тебе построить путь в лучшие места для путешествий!\n\n");

            absSender.execute(sendPhoto);
            System.out.println("✅ Отправлено фото с приветствием");
            return sendPhoto;
        } catch (TelegramApiException e) {
            System.err.println("❌ Ошибка отправки фото: " + e.getMessage());
            throw new RuntimeException("Не удалось отправить фото",e);
        }

    }

    public  Map<String, String> getStartTriggers() {
        return startTriggers;
    }
}