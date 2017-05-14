package com.knalx.commands;

import com.knalx.model.Band;
import com.knalx.model.Repetition;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by knalx on 16.06.16.
 */
public class NewBandCommand implements ChatCommand {
    private String telegramChatId;
    private Band band = new Band();
    private Stage stage = Stage.NEW;

    private static String enterBandName= "Как называется ваша группа?";
    private static String joinBand= "Чтобы вступить в группу, Все участники должны нажать нажать /join \n Для окончания регистрации нажмите /finish";
    public static String cancelButton = String.valueOf('\u274C') + " Отмена";


    @Override
    public SendMessage handleRequest(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChat().getId()));
        if(!update.getMessage().getChat().isGroupChat()){
            sendMessage.setText("Команду необходимо вызывать в групповом чате");
            sendMessage.enableMarkdown(true);
            return sendMessage;
        }

        switch (this.stage) {
            case NEW: {
                sendMessage = buildResponseWithCancelButton(sendMessage,enterBandName);
                this.stage=Stage.NAME;
                break;
            }
            case NAME:{
                this.band.setName(update.getMessage().getText());
                sendMessage= buildResponseWithCancelButton(sendMessage,joinBand);
                this.stage=Stage.ADD_MEMBERS;
                break;
            }
            case ADD_MEMBERS:{
                if(update.getMessage().getText().equals("/join")){

                } else{

                }
                break;
            }

        }

        return sendMessage;

    }

    @Override
    public SendMessage handleCancel() {

        return null;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    private enum Stage {
        NEW,
        NAME,
        ADD_MEMBERS
    }

    private SendMessage buildResponseWithCancelButton (SendMessage sm, String text){
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardButtons = new KeyboardRow();
        keyboardButtons.add(0, cancelButton);
        keyboardRows.add(keyboardButtons);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboad(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
//        sm.setReplayMarkup(replyKeyboardMarkup);
        sm.setText(text);
        return sm;

    }

    private SendMessage buildResponseKeyboard(Stage stage) {
        SendMessage sendMessage = new SendMessage();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardButtons = new KeyboardRow();
        switch (stage) {
            case NAME:
                keyboardRows.add(keyboardButtons);
                break;
          }
        return sendMessage;
    }
}

