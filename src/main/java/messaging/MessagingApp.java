package messaging;

public class MessagingApp {

    private EncryptionService encryptionService;

    public MessagingApp(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    public String sendMessage(String to, String message) {
        if (encryptionService.authenticateUser(to)) {
            return encryptionService.encrypt(message);
        } else {
            throw new IllegalArgumentException(to);
        }
    }

    public String receiveMessage(String from, String message) {
        if (encryptionService.authenticateUser(from)) {
            return encryptionService.decrypt(message);
        } else {
            throw new IllegalArgumentException(from);
        }
    }
}
