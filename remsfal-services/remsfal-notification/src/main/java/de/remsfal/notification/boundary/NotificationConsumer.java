package de.remsfal.notification.boundary;

import de.remsfal.core.json.eventing.EmailEventJson;
import de.remsfal.notification.control.MailingController;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class NotificationConsumer {

    @Inject
    Logger logger;

    @Inject
    MailingController mailingController;

    @Incoming("user-notification-consumer")
    public CompletionStage<Void> consumeUserNotification(Message<EmailEventJson> msg) {
        EmailEventJson mail = msg.getPayload();

        String email = mail.getUser().getEmail();
        String link = mail.getLink();
        Locale locale = mail.getLocale() != null ? Locale.forLanguageTag(mail.getLocale()) : Locale.GERMAN;

        logger.infov("Received user-notification for user email: {0}", email);
        logger.infov("Type: {0}", mail.getType());

        return CompletableFuture.runAsync(() -> {
            switch (mail.getType()) {
                case PROJECT_ADMISSION:
                    mailingController.sendNewMembershipEmail(mail.getUser(), link, locale);
                    break;
                case USER_REGISTRATION:
                    mailingController.sendWelcomeEmail(mail.getUser(), link, locale);
                    break;
            }
        })
        .thenCompose(v -> msg.ack());
    }
}
