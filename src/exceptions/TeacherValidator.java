package exceptions;

import model.Teacher;

import java.util.List;
import java.util.stream.Collectors;

public class TeacherValidator implements Validator<Teacher> {

    @Override
    public void validate(Teacher t) throws ValidatorException {
        if (t.getId() != (long) t.getId())
            throw new ValidatorException("Invalid ID! Only long type of numbers allowed.");
        if (!t.getFirstName().chars().allMatch(Character::isLetter))
            throw new ValidatorException("Invalid first name! Only letters allowed.");
        if (!t.getLastName().chars().allMatch(Character::isLetter))
            throw new ValidatorException("Invalid last name! Only letters allowed.");
        List<Long> invalidCourses = t.getCourses().stream().filter(courseId -> false).
                collect(Collectors.toList());
        if (!invalidCourses.isEmpty())
            throw new ValidatorException("Courses IDs should be of type long! Please check the courses names list.");
    }
}
