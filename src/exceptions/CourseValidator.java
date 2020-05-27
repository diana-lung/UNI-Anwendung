package exceptions;

import model.Course;

public class CourseValidator implements Validator<Course> {
    @Override
    public void validate(Course c) throws ValidatorException {
        if (c.getId() != (long) c.getId())
            throw new ValidatorException("Invalid ID! Only long type of numbers allowed.");
        if (!c.getName().chars().allMatch(Character::isLetter))
            throw new ValidatorException("Invalid first name! Only letters allowed.");
        if (c.getMaxEnrollment() != (int) c.getMaxEnrollment())
            throw new ValidatorException("Invalid number for maximum enrollment! Only integer numbers allowed.");
        if (c.getCredits() != (int) c.getCredits())
            throw new ValidatorException("Invalid number for maximum enrollment! Only integer numbers allowed.");
        if (c.getCredits() > 30)
            throw new ValidatorException("Invalid number of credits! Credits number for a course can't be greater than 30.");
    }
}
