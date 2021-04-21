package messaging;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

class MessagingAppTest {

    private static final String MESSAGE = "Hello world!";

    private static final String ENCRYPTED_MESSAGE = "dhfdsfds";

    private static final String TO = "Alice";

    private static final String FROM = "Bob";

    private static final String ATTACKER = "Mr. Hacker";

    private MessagingApp underTest;

    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        encryptionService = Mockito.mock(EncryptionService.class);
        underTest = new MessagingApp(encryptionService);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testSendMessageShouldEncryptTextWhenSendingToAuthenticatedUser() {
        //Given
        Mockito.when(encryptionService.authenticateUser(TO)).thenReturn(true);
        Mockito.when(encryptionService.encrypt(MESSAGE)).thenReturn(ENCRYPTED_MESSAGE);
        //When
        underTest.sendMessage(TO, MESSAGE);
        //Then
        Mockito.verify(encryptionService).authenticateUser(TO);
        Mockito.verify(encryptionService).encrypt(MESSAGE);
        Mockito.verifyNoMoreInteractions(encryptionService);
    }

    @Test
    void testReceiveMessageShouldDecryptTextWhenReceivingFromAuthenticatedUser() {
        //Given
        Mockito.when(encryptionService.authenticateUser(FROM)).thenReturn(true);
        Mockito.when(encryptionService.decrypt(ENCRYPTED_MESSAGE)).thenReturn(MESSAGE);
        //When
        underTest.receiveMessage(FROM, ENCRYPTED_MESSAGE);
        //Then
        Mockito.verify(encryptionService).authenticateUser(FROM);
        Mockito.verify(encryptionService).decrypt(ENCRYPTED_MESSAGE);
        Mockito.verifyNoMoreInteractions(encryptionService);
    }

    @Test
    void testSendMessageShouldThrowIllegalArgumentExceptionWhenSendingTextToUnauthenticatedUser() {
        //Given
        Mockito.when(encryptionService.authenticateUser(ATTACKER)).thenReturn(false);
        Mockito.when(encryptionService.encrypt(MESSAGE)).thenReturn(ENCRYPTED_MESSAGE);
        //When
        try {
            underTest.sendMessage(ATTACKER, MESSAGE);
        } catch (IllegalArgumentException e) {
            assertEquals(ATTACKER, e.getMessage());
        } catch (Exception e) {
            fail();
        }
        //Then
        Mockito.verify(encryptionService).authenticateUser(ATTACKER);
        Mockito.verifyNoMoreInteractions(encryptionService);
    }

    @Test
    void receiveMessageShouldThrowIllegalArgumentExceptionWhenReceivingTextFromUnauthenticatedUser() {
        //Given
        Mockito.when(encryptionService.authenticateUser(ATTACKER)).thenReturn(false);
        Mockito.when(encryptionService.decrypt(ENCRYPTED_MESSAGE)).thenReturn(MESSAGE);
        //When
        try {
            underTest.receiveMessage(ATTACKER, ENCRYPTED_MESSAGE);
        } catch (IllegalArgumentException e) {
            assertEquals(ATTACKER, e.getMessage());
        } catch (Exception e) {
            fail();
        }
        //Then
        Mockito.verify(encryptionService).authenticateUser(ATTACKER);
        Mockito.verifyNoMoreInteractions(encryptionService);
    }
}