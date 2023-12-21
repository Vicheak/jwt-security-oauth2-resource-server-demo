package com.vicheak.coreapp.mail;

import jakarta.mail.MessagingException;

public interface MailService {

    void sendMail(Mail<?> mail) throws MessagingException;

}
