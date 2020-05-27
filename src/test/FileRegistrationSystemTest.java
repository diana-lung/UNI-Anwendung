package test;
import controller.Controller;
import exceptions.ValidatorException;
import model.Course;
import model.Student;
import model.Teacher;
import repository.CourseFileRepository;
import repository.ICrudRepository;
import repository.StudentFileRepository;
import repository.TeacherFileRepository;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class FileRegistrationSystemTest {

    private Controller registrationSystem;
    private Student s1;
    private Course c1;
    private ICrudRepository<Student> studentRepo;
    private ICrudRepository<Course> courseRepo;
    private ICrudRepository<Teacher> teacherRepo;
    private List<Long> coursesId;
    private List<Student> students;
/**
 * Richten Sie die Eigenschaften ein, bevor der Junit-Test gestartet wird.
 */
    @org.junit.jupiter.api.BeforeEach
    void setUp() throws IOException {
        this.studentRepo = new StudentFileRepository("testStudents");
        this.teacherRepo=new TeacherFileRepository("testTeachers");
        this.courseRepo = new CourseFileRepository("testCourses",studentRepo, teacherRepo);
        this.registrationSystem=new Controller(courseRepo,studentRepo,teacherRepo);

    }

    /**
     * Testen Sie, dass sich ein Student nicht mehr fur einen Kurs anmelden kann, den er bereits besucht
     */
   @Test
    void register() throws FileNotFoundException, ValidatorException {
       int ok=this.registrationSystem.register(this.studentRepo.findOne((long)1), this.courseRepo.findOne((long)4));
       assert this.courseRepo.findOne((long)4).getStudentsEnrolled().size()==3;
   }

    /**
     * Uberprufen Sie die Anzahl der freien Platze
     */
   @Test
    void freePlaces(){
        assert this.registrationSystem.freePlaces(this.courseRepo.findOne((long)3))==22;
   }

    /**
     * Verificam ca exista un singur student cu acest nume
     */
   @Test
   void filterStudentsByFirstName(){
       Set<Student> students=this.registrationSystem.filterStudentsByFirstName("Hi");
       assert students.size()==1;
   }

   @Test
    void sortCourses(){
       assert this.registrationSystem.sortCourses().get(1)==this.courseRepo.findOne((long)4);
   }

}

