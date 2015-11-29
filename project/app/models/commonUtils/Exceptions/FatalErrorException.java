package models.commonUtils.Exceptions;

/**
 * This exception gets thrown whenever a fatal error occurs in the system and no other exception is appropriate. The message specifies the error.
 * @author Thomas Bednorz
 */
public class FatalErrorException extends Exception {
    public FatalErrorException(String message) {
        super(message);
    }
}
