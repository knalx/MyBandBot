package com.knalx.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import javax.annotation.PostConstruct;

/**
 * @since 08.02.17
 **/
@Component
public class TelegramService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MyBandBot myBandBot;

    @PostConstruct
    public void init() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(myBandBot);
        } catch (TelegramApiException e) {
            BotLogger.error("BOT", e);
        }
        log.info("Bot started.");
    }


}
