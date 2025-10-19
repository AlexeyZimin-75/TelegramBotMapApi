package org.example.service;

import org.example.states.UserState;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class UserStateService {
    private final Map<Long, UserState> userStates = new ConcurrentHashMap<>();
    private final Map<Long, UserData> userData = new ConcurrentHashMap<>();

    public UserState getUserState(Long userId) {
        return userStates.getOrDefault(userId, UserState.START);
    }

    public void setUserState(Long userId, UserState state) {
        userStates.put(userId, state);
    }

    public UserData getUserData(Long userId) {
        return userData.computeIfAbsent(userId, k -> new UserData());
    }

    public void clearUserData(Long userId) {
        userStates.remove(userId);
        userData.remove(userId);
    }
}
