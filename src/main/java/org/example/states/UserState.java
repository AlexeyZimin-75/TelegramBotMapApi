package org.example.states;

/**
 * Состояния пользователя в FSM
 */
public enum UserState {
    START,
    AWAITING_LOCATION,
    AWAITING_MANUAL_CITY,
    AWAITING_DESTINATION_CITY,
    AWAITING_FOR_DATE,
    READY_TO_SEARCH
}