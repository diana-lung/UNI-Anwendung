package exceptions;

import model.Student;

import java.util.List;
import java.util.stream.Collectors;

public class StudentValidator implements Validator<Student> {

    @Override
    public void validate(Student s) throws ValidatorException {
        if (s.getId() != (long) s.getId())
            throw new ValidatorException("Invalid ID! Only long type of numbers allowed.");
        if (!s.getFirstName().chars().allMatch(Character::isLetter))
            throw new ValidatorException("Invalid first name! Only letters allowed.");
        if (!s.getLastName().chars().allMatch(Character::isLetter))
            throw new ValidatorException("Invalid last name! Only letters allowed.");
        List<Long> invalidCourses = s.getEnrolledCourses().stream().filter(courseId-> false).
                collect(Collectors.toList());
        if (!invalidCourses.isEmpty())
            throw new ValidatorException("Courses IDs should be of type long! Please check the courses names list.");
        if (s.getTotalCredits() != (int) s.getTotalCredits())
            throw new ValidatorException("Invalid number of credits! Only integer numbers allowed.");
        if (s.getTotalCredits() > 30)
            throw new ValidatorException("Invalid number of credits! Credits number can't be greater than 30.");
    }
}
