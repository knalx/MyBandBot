package com.knalx.service;


import com.knalx.commands.ChatCommand;
import com.knalx.commands.NewBandCommand;
import com.knalx.commands.RepCommand;
import com.knalx.dao.MongoDao;
import com.knalx.model.Repetition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by knalx on 13.06.16.
 */
public class MyBandBot extends TelegramLongPollingBot {

    private MongoDao mongoDao;
    private String userName;
    private String token;


    Logger logger = LoggerFactory.getLogger(MyBandBot.class);

    public static List<Repetition> repetitionList = new ArrayList<>();

    public static volatile Map<String, ChatCommand> chatCommandList = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        logger.debug("Update call : " + update.getMessage().getFrom().getFirstName() + "  " + update.getMessage().getChatId().toString() + " - " + update.toString());
        String chatId = update.getMessage().getChatId().toString();
        // если есть активный диалог в этом чате
        if (chatCommandList.containsKey(chatId)) {
            ChatCommand command = chatCommandList.get(chatId);
            if (update.getMessage().getText().equals(RepCommand.cancelButton)) {
                chatCommandList.remove(chatId);
                sendMessageToClient(command.handleCancel());
                return;
            }

            sendMessageToClient(command.handleRequest(update));

            if (command.isFinished()) {
                chatCommandList.remove(chatId);
                return;
            }

        }
        String inputText = update.getMessage().getText();
        if (inputText.equals(Cmd.TRACKS)) {
            logger.debug("TRACKS");
        } else if (inputText.equals(Cmd.REP)) {
            logger.debug("REP");
            RepCommand repCommand = new RepCommand(chatId);
            chatCommandList.put(chatId, repCommand);
            sendMessageToClient(repCommand.handleRequest(update));
        } else if (inputText.startsWith(Cmd.TRACK)) {
            logger.debug("TRACK");
        }

    }


    public void sendMessageToClient(SendMessage sm) {
        try {
            this.sendMessage(sm);
        } catch (TelegramApiException e) {
            logger.error("Can't send message ", e);
        }
    }

    public MongoDao getMongoDao() {
        return mongoDao;
    }

    public void setMongoDao(MongoDao mongoDao) {
        this.mongoDao = mongoDao;
    }


    private static class Cmd {
        /**
         * Print list ofr all available tracks
         */
        static String TRACKS = "/tracks";
        /**
         * Show time and place of the next rehearsal
         */
        static String REP = "/rep";
        /**
         * Show information about track - lyrics and tempo - may be notes
         */
        static String TRACK = "/track";
    }

    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
