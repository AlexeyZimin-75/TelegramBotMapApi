package org.example.service;

import java.util.concurrent.ConcurrentHashMap;

public class UserDataService {
    private final ConcurrentHashMap<Long, UserData> userDataMap;

    public UserDataService() {
        this.userDataMap = new ConcurrentHashMap<>();
    }

    public UserData getUserData(Long userId) {
        return userDataMap.computeIfAbsent(userId, k -> new UserData());
    }

    public void setUserData(Long userId, UserData userData) {
        userDataMap.put(userId, userData);
    }

    public void clearUserData(Long userId) {
        userDataMap.remove(userId);
    }
}