package models.commonUtils.Exceptions;

/**
 * @author Jana Wengenroth
 */
public class MembershipRequestNotFoundException extends Exception{
    public MembershipRequestNotFoundException(){
        super("Membership request not found");
    }
}
