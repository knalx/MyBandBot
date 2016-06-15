package com.knalx;

import com.knalx.commands.RepCommand;
import com.knalx.model.Repetition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by knalx on 13.06.16.
 */
public class MyBandBot extends TelegramLongPollingBot {

    Logger logger = LoggerFactory.getLogger(MyBandBot.class);

    public static List<Repetition> repetitionList = new ArrayList<>();

    public static volatile Map<String, RepCommand> repCommandList = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        logger.debug(update.getMessage().getChatId().toString());
        String chatId = update.getMessage().getChatId().toString();

        if (repCommandList.containsKey(chatId)) {
            RepCommand repCommand = repCommandList.get(chatId);
            if (update.getMessage().getText().equals(RepCommand.cancelButton)) {
                repCommandList.remove(chatId);
                sendMessageToClient(repCommand.handleCancel());
                return;
            }
            sendMessageToClient(repCommand.handleRequest(update));
            if (repCommand.getStage().equals(RepCommand.Stage.FINISHED)) {
                repCommandList.remove(chatId);
                return;
            }

        }

        switch (update.getMessage().getText()) {
            case "/rep": {
                RepCommand repCommand;
                if (!repCommandList.containsKey(chatId)) {
                    repCommand = new RepCommand(chatId);
                    repCommandList.put(chatId, repCommand);
                } else {
                    repCommand = repCommandList.get(chatId);
                }
                sendMessageToClient(repCommand.handleRequest());
                break;
            }
            case "/init":{
                break;
            }
        }
    }


    public void sendMessageToClient(SendMessage sm) {
        try {
            this.sendMessage(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getBotUsername() {
        return Configuration.NANE;
    }

    @Override
    public String getBotToken() {
        return Configuration.TOKEN;
    }


}
