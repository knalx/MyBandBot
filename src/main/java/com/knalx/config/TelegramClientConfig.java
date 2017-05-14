package com.knalx.config;

import com.knalx.dao.MongoDao;
import com.knalx.service.MyBandBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;

import java.io.IOException;

/**
 * @since 08.02.17
 **/
@Configuration
public class TelegramClientConfig {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${telegram.bot.username}")
    private String userName;

    @Value("${telegram.bot.token}")
    private String token;

    @Autowired
    MongoDao mongoDao;

    @Bean
    public MyBandBot myBandBot() throws IOException {
        ApiContextInitializer.init();
        MyBandBot myBandBot = new MyBandBot();
        log.debug("init bot");
        myBandBot.setMongoDao(mongoDao);
        myBandBot.setToken(token);
        myBandBot.setUserName(userName);
        return myBandBot;
    }


}
