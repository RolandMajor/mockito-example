package messaging;

public interface EncryptionService {

    public boolean authenticateUser(String username);

    public String encrypt(String message);

    public String decrypt(String message);

}
