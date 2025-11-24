package org.example.commands;

import org.example.keyboards.LastKeyboard;
import org.example.DataBaseManager;
import org.example.service.UserData;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class UserHistoryCommand implements Command {
    private final DataBaseManager dataBaseManager;

    public UserHistoryCommand() {
        this.dataBaseManager = new DataBaseManager();
    }

    @Override
    public Object execute(AbsSender absSender, Message message) {
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();

        List<UserData> lastRoutes = dataBaseManager.getLastRoutes(userId, 5);
        int totalRoutes = dataBaseManager.getUserRouteCount(userId);

        SendMessage response = new SendMessage();
        response.setChatId(chatId.toString());
        response.setText(buildHistoryMessage(lastRoutes, totalRoutes));
        response.setReplyMarkup(new LastKeyboard().createStartKeyboard());

        try {
            absSender.execute(response);
            return "История маршрутов успешно отправлена";
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки истории: " + e.getMessage());
            return "Ошибка при отправке истории: " + e.getMessage();
        }
    }

    @Override
    public String getCommandName() {
        return "history";
    }

    @Override
    public String getDescription() {
        return "Показать историю маршрутов";
    }

    private String buildHistoryMessage(List<UserData> routes, int totalCount) {
        if (routes.isEmpty()) {
            return "📊 У вас пока нет сохраненных маршрутов.\n" +
                    "Начните поиск с помощью команды /start!";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("📊 **История ваших маршрутов**\n\n");
        sb.append("Всего маршрутов: ").append(totalCount).append("\n\n");
        sb.append("Последние 5 маршрутов:\n");

        for (int i = 0; i < routes.size(); i++) {
            UserData route = routes.get(i);
            sb.append(i + 1).append(". ")
                    .append(route.getCurrentCity() != null ? route.getCurrentCity() : "не указан")
                    .append(" → ")
                    .append(route.getDestinationCity())
                    .append("\n   📅 ")
                    .append(route.getDepartureDate() != null ? route.getDepartureDate() : "не указана")
                    .append(" - ")
                    .append(route.getArrivalDate() != null ? route.getArrivalDate() : "не указана")
                    .append("\n\n");
        }
        return sb.toString();
    }
}