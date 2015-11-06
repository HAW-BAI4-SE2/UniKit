package models.studentComponent.FormModels;

/**
 * This model is wrapped by a TeamStateModel-FormObject to signal a state change for a team.
 * It's either used to add a student to the list of registrations, invitations or membership requests or used to delete
 * a student for either of these.
 * The respective method-call indicates what will happen
 * @author Thomas Bednorz
 **/
public class TeamStateChangeFormModel {

    //The student number of the student for which the change happens
    public String studentNumber;

    //The team ID for which the change happens
    public int teamID;

    public TeamStateChangeFormModel(){}

}
