package models.commonUtils.Exceptions;

/**
 * @author Jana Wengenroth
 */
public class TeamNotFoundException extends Exception {
    public TeamNotFoundException() {
        super("Membership request not found");
    }
}