package com.knalx.commands;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

/**
 * Created by knalx on 16.06.16.
 */
public interface ChatCommand {
    public SendMessage handleRequest(Update update);
    public SendMessage handleCancel();
    public boolean isFinished();
}
