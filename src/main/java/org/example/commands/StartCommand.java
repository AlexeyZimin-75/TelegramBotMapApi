package org.example.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.example.service.UserStateService;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.example.states.UserState;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Команда /start - приветствие пользователя при запуске бота
 */
public class StartCommand implements Command {
    private final UserStateService userStateService;

    public StartCommand(UserStateService userStateService) {
        this.userStateService = userStateService;
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

        userStateService.clearUserData(userId);
        userStateService.setUserState(userId, UserState.AWAITING_LOCATION);
        return sendWelcomeMessage(absSender, message.getChatId());
    }

    private SendPhoto sendWelcomeMessage(AbsSender absSender, long chatId) {
        try {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(String.valueOf(chatId));

            String imageURL = "https://media.easemytrip.com/media/Blog/International/637007769287754861/637007769287754861GltpKG.jpg";
            sendPhoto.setPhoto(new InputFile(imageURL));
            sendPhoto.setCaption("️Добро пожаловать в туристический бот! 🗺️\n" +
                    "Я помогу тебе построить путь в лучшие места для путешествий!\n\n" +
                    "Доступные команды:\n" +
                    "/start - начать работу\n" +
                    "/help - информация о боте\n" +
                    "/location - узнать информацию о твоем текущем местоположении");

            absSender.execute(sendPhoto);
            System.out.println("✅ Отправлено фото с приветствием");
            return sendPhoto;
        } catch (TelegramApiException e) {
            System.err.println("❌ Ошибка отправки фото: " + e.getMessage());
            throw new RuntimeException("Не удалось отправить фото",e);
        }

    }

}