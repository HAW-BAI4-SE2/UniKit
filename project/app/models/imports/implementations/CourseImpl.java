package models.imports.implementations;

import models.imports.interfaces.Course;

class CourseImpl implements Course {
    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }
	
	@Override	
	public String getShortcut() {
        return null;
    }

    @Override
    public int getMinTeamSize() {
        return 0;
    }

    @Override
    public int getMaxTeamSize() {
        return 0;
    }
	
	@Override
	public FieldOfStudy getFieldOfStudy() {
        return null;
    }
}
