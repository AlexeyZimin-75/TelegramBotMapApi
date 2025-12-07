package org.example.states;

/**
 * Состояния пользователя в FSM
 */
public enum UserState {
    START,
    AWAITING_LOCATION,
    AWAITING_MANUAL_CITY,
    AWAITING_DESTINATION_CITY,
    AWAITING_ARRIVAL_DATE_RESPONSE,
    AWAITING_DEPARTURE_DATE_RESPONSE,
    READY_TO_SEARCH
}