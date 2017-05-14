package com.knalx.commands;

import com.knalx.model.Repetition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by knalx on 13.06.16.
 */
public class RepCommand implements ChatCommand {

    Logger logger = LoggerFactory.getLogger(RepCommand.class);

    private String telegramChatId;
    private Stage stage;
    private Repetition repetition;

    private static String calendarCymbol = String.valueOf(Character.toChars(Character.toCodePoint('\uD83D', '\uDCC5')));
    private static String pinCymbol = String.valueOf(Character.toChars(Character.toCodePoint('\uD83D', '\uDCCD')));

    public static String chooseDate = "Регистрация репетиции \n " + calendarCymbol + " Введите дату:";
    public static String choosePlace = pinCymbol + " Введите место:";
    public static String chooseTime = '\u23F0' + " Введите время:";
    public static String canceled = "Создание репетиции отменено";
    public static String cancelButton = String.valueOf('\u274C') + " Отмена";

    public RepCommand(String telegramChatId) {
        this.stage = Stage.NEW;
        this.telegramChatId = telegramChatId;
    }

    public SendMessage handleRequest(Update update) {
        switch (this.getStage()) {
            case NEW: {
                this.repetition = new Repetition();
                this.setStage(Stage.DAY);
                break;
            }
            case DAY: {
                this.setStage(Stage.PLACE);
                repetition.setDate(update.getMessage().getText());
                break;
            }
            case PLACE: {
                this.setStage(Stage.TIME);
                repetition.setPlaceName(update.getMessage().getText());
                break;
            }
            case TIME: {
                this.setStage(Stage.FINISHED);
                repetition.setTime(update.getMessage().getText());
                break;
            }
        }
        SendMessage sm = buildResponseKeyboard(this.getStage());
        return sm;
    }

    public SendMessage handleCancel() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(this.telegramChatId);
        sendMessage.setText(canceled);
        return sendMessage;
    }

    @Override
    public boolean isFinished() {
        return stage.equals(Stage.FINISHED);
    }


    private SendMessage buildResponseKeyboard(Stage stage) {
        SendMessage sendMessage = new SendMessage();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardButtons = new KeyboardRow();
        switch (stage) {
            case NEW:
                logger.error("Не должен опапдать в обработчик со статусом NEW");
                break;
            case DAY:
                keyboardButtons.add(0, cancelButton);
                keyboardRows.add(keyboardButtons);

                sendMessage.setText(chooseDate);
                break;
            case PLACE:
                keyboardButtons.add(0, cancelButton);
                keyboardRows.add(keyboardButtons);
                sendMessage.setText(choosePlace);
                break;
            case TIME:
                keyboardButtons.add(0, cancelButton);
                keyboardRows.add(keyboardButtons);
                sendMessage.setText(chooseTime);
                break;
            case FINISHED: {
                String repInfo = '\u2705' + " Оповещение отправлено всей группе \n"
                        + "*Дата:* " + repetition.getDate() + "\n"
                        + "*Место:* " + repetition.getPlaceName() + "\n"
                        + "*Время:* " + repetition.getTime() + "\n";
                sendMessage.setText(repInfo);
                sendMessage.enableMarkdown(true);
                sendMessage.setChatId(this.telegramChatId);
                sendMessageToAllBandMembers();
                return sendMessage;
            }
        }
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboad(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
//        sendMessage.setReplayMarkup(replyKeyboardMarkup);
        sendMessage.setChatId(this.telegramChatId);

        return sendMessage;
    }

    public void sendMessageToAllBandMembers() {

    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Repetition getRepetition() {
        return repetition;
    }

    public void setRepetition(Repetition repetition) {
        this.repetition = repetition;
    }

    public enum Stage {
        NEW,
        DAY,
        PLACE,
        TIME,
        FINISHED
    }

}
