package models.commonUtils.Exceptions;

/**
 * @author Jana Wengenroth
 */
public class InvitationNotFoundException extends Exception{
    public InvitationNotFoundException(){
        super("Invitation not found");
    }
}