package com.knalx;

import com.knalx.commands.ChatCommand;
import com.knalx.commands.NewBandCommand;
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

    public static volatile Map<String, ChatCommand> chatCommandList = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        logger.debug(update.getMessage().getFrom().getFirstName() + "  " + update.getMessage().getChatId().toString());
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

        switch (update.getMessage().getText()) {
            case "/rep": {
                RepCommand repCommand = new RepCommand(chatId);
                chatCommandList.put(chatId, repCommand);
                sendMessageToClient(repCommand.handleRequest(update));
                break;
            }
            case "/newband": {
                NewBandCommand newBandCommand = new NewBandCommand();
                chatCommandList.put(chatId, newBandCommand);
                sendMessageToClient(newBandCommand.handleRequest(update));
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
