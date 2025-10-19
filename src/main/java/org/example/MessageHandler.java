package org.example;

import org.example.keyboards.LocationKeyboard;
import org.example.commands.HelpCommand;
import org.example.commands.StartCommand;
import org.example.commands.Command;
import org.example.commands.GetLocationCommand;
import org.example.service.UserStateService;
import org.example.states.UserState;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

import static org.example.states.UserState.*;

public class MessageHandler {

    private final Map<String, Command> commands = new HashMap<>();
    private final UserStateService userStateService = new UserStateService();
    private final GetLocationCommand getLocationCommand = new GetLocationCommand(userStateService);

    public MessageHandler() {
        registerCommands();
    }

    private void registerCommands() {
        Command startCommand = new StartCommand(userStateService);
        commands.put(startCommand.getCommandName(), startCommand);

        Command helpCommand = new HelpCommand(commands);
        commands.put(helpCommand.getCommandName(), helpCommand);

        commands.put(getLocationCommand.getCommandName(), getLocationCommand);

        System.out.println("Зарегистрированы команды: " + commands.keySet());
    }

    public void handleLocation(Update update, AbsSender absSender) {
        Message message = update.getMessage();
        User user = message.getFrom();
        Long userId = user.getId();

        UserState currentState = userStateService.getUserState(userId);

        // здесь мы принимаем гео от пользователя
        if (message.hasLocation()) {
            handleLocationInput(userId, message.getChatId(), message.getLocation(), absSender);
        }
        // здесь мы обрабатываем вводимый город вручную
        else if (message.hasText()) {
            handleTextLocationInput(userId, message.getChatId(), message.getText(), currentState, absSender);
        }
    }

        private void handleLocationInput(Long userId, Long chatId, Location location, AbsSender absSender){
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            try {
                String city = getLocationCommand.getCityFromCoordinates(latitude, longitude);
                handleSuccessfulCityDetection(userId, chatId, city, absSender);
            } catch (Exception e){
                handleCityDetectionError(userId, chatId, e, absSender);
            }
        }

        private void handleTextLocationInput(Long userId, Long chatId, String text,
                                             UserState currentState, AbsSender absSender){
        /*
        здесь логика простая: пользователь может ввести текст сразу или после нажатия
        на кнопку, оба состояния будут одинаково обрабатываться и на состояния не повлияют
         */
        switch (currentState){
            case AWAITING_LOCATION:
                if (text.equals("\uD83D\uDC49 ввести город вручную")){
                    handleTextCityRequest(userId, chatId, absSender);
                }
                else {
                    handleTextCityInput(userId, chatId, text, absSender);
                }
                break;
            case AWAITING_MANUAL_CITY:
                handleTextCityInput(userId, chatId, text, absSender);
                break;
        }
        }
        private void handleTextCityRequest(Long userId, Long chatId, AbsSender absSender){
        userStateService.setUserState(userId, UserState.AWAITING_MANUAL_CITY);

        SendMessage response = new SendMessage();
        response.setChatId(chatId.toString());
        response.setText("🏙️ Пожалуйста, введите название вашего города:");
        try{
            absSender.execute(response);
        } catch (TelegramApiException e){
            System.err.println("сообщение не отправилось, " + e.getMessage());
        }
        }

        private void handleTextCityInput(Long userId, Long chatId, String cityName, AbsSender absSender){
        try{

            // здесь добавим проверку, есть ли такой город в апи или нет

            handleSuccessfulCityDetection(userId, chatId, cityName, absSender);
        } catch (Exception e){
            System.err.println("ошибка ввода города (скорее всего такого не существует)");
            SendMessage errResponse = new SendMessage();
            errResponse.setChatId(chatId.toString());
            errResponse.setText("❌ Произошла ошибка при ручном вводе города. " +
                    "попробуйте ввести снова или воспользуйтесь автоматическим вводом геолокации");
            try {
                absSender.execute(errResponse);
            } catch (TelegramApiException ex){
                System.err.println("ну если даже это не отправится.." + ex.getMessage());
            }
        }
        }

        private void handleSuccessfulCityDetection(Long userId, Long chatId, String city, AbsSender absSender) {
        // Сохраняем город пользователя
        userStateService.getUserData(userId).setCurrentCity(city);

        // Переходим к следующему состоянию - ожидаем ввод города назначения
        userStateService.setUserState(userId, AWAITING_DESTINATION_CITY);

        SendMessage response = new SendMessage();
        response.setChatId(chatId.toString());
        response.setText("📍 Отлично! Ваш город: " + city +
                "\n\nТеперь введите город, в который хотите поехать:");

        try {
            absSender.execute(response);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки сообщения: " + e.getMessage());
        }
    }

    private void handleCityDetectionError(Long userId, Long chatId, Exception error, AbsSender absSender) {
        System.err.println("❌ Ошибка при определении города по координатам: " + error.getMessage());

        SendMessage errorResponse = new SendMessage();
        errorResponse.setChatId(chatId.toString());
        errorResponse.setText("❌ Не удалось определить город по вашим координатам. Пожалуйста, введите ваш город вручную:");

        userStateService.setUserState(userId, UserState.AWAITING_MANUAL_CITY);

        try {
            absSender.execute(errorResponse);
        } catch (TelegramApiException ex) {
            System.err.println("Ошибка отправки сообщения об ошибке: " + ex.getMessage());
        }
    }
    private void handleFSM(Message message, AbsSender absSender) {
        Long userId = message.getFrom().getId();
        UserState currentState = userStateService.getUserState(userId);
        String text = message.getText();

        if (currentState == AWAITING_LOCATION || currentState == AWAITING_MANUAL_CITY) {
            handleTextLocationInput(userId, message.getChatId(), text, currentState, absSender);
            return;
        }

        if (currentState == AWAITING_DESTINATION_CITY) {
            // Сохраняем город назначения
            userStateService.getUserData(userId).setDestinationCity(text);
            userStateService.setUserState(userId, UserState.READY_TO_SEARCH);

            String currentCity = userStateService.getUserData(userId).getCurrentCity();

            try {
                String landmarks = getLocationCommand.getCityLandmarks(text);

                String routeInfo = String.format(
                        "🎉 Отлично! Маршрут построен:\n📍 От: %s\n🎯 До: %s\n\n🏛️ Вот некоторые достопримечательности города %s:\n%s",
                        currentCity, text, text, landmarks
                );

                SendMessage response = new SendMessage();
                response.setChatId(message.getChatId().toString());
                response.setText(routeInfo);
                absSender.execute(response);
            } catch (Exception e) {
                System.err.println("❌ Ошибка при получении достопримечательностей: " + e.getMessage());

                // Если не удалось получить достопримечательности, отправляем без них
                String routeInfo = String.format(
                        "🎉 Отлично! Маршрут построен:\n📍 От: %s\n🎯 До: %s\n\nТеперь вы можете искать места для посещения в %s!",
                        currentCity, text, text
                );

                SendMessage response = new SendMessage();
                response.setChatId(message.getChatId().toString());
                response.setText(routeInfo);

                try {
                    absSender.execute(response);
                } catch (TelegramApiException ex) {
                    System.err.println("Ошибка отправки сообщения: " + ex.getMessage());
                }
            }
        }

    }

    public void handleMessage(Message message, AbsSender absSender) {
        String text = message.getText();
        User user = message.getFrom();
        Long userId = user.getId();

        if (text.startsWith("/")) {
            String commandName = text.substring(1).toLowerCase();
            Command command = commands.get(commandName);

            if (command != null) {
                System.out.println("   🔧 Выполняется команда: /" + commandName);
                command.execute(absSender, message);
            } else {
                System.out.println("   ❌ Неизвестная команда: /" + commandName);
                sendUnknownCommand(message, absSender);
            }
        } else {
            System.out.println("   📝 Обработка текстового сообщения в FSM");
            handleFSM(message, absSender);
        }
    }



    private void sendUnknownCommand(Message message, AbsSender absSender) {
        try {
            SendMessage response = new SendMessage();
            response.setChatId(message.getChatId().toString());
            response.setText("❌ Неизвестная команда. Используй /help для списка команд.");
            absSender.execute(response);
        } catch (Exception e) {
            System.err.println("Ошибка при отправке сообщения об ошибке: " + e.getMessage());
        }
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}