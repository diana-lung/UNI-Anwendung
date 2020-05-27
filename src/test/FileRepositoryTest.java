package test;

import model.Student;
import repository.ICrudRepository;
import repository.StudentFileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Test Klase fur ICrudRepository Operationen
 */
class FileRepositoryTest {
    private ICrudRepository<Student> studentRepo;
    private Student s1;
    private Student s2;
    private List<Long> coursesId;

    @BeforeEach
    void setUp() throws IOException {
        this.coursesId = new ArrayList<>();
        this.coursesId.add((long) 1);
        this.coursesId.add((long) 2);
        this.studentRepo = new StudentFileRepository("testStudents");
        this.s1 = new Student((long) 6, "Diana", "Lung", 12, this.coursesId);
        this.s2 = new Student((long) 2, "Lisa", "Park", 12, this.coursesId);
    }

    @AfterEach
    void tearDown() {
        Object studentRepo = null;
    }

    /**
     * Wir testen, ob findOne die Entitat mit der angegebenen ID zuruckgibt, falls es existiert, und in anderen Fällen null
     */
    @Test
    void testFindOne() {
        Student s = this.studentRepo.findOne((long) 1);
        assert s != null; //pt ca exista
        assert this.studentRepo.findOne((long) 56) == null; //ID "56" existiert nicht
    }

    /**
     * Wir testen, ob die findAll-Funktion eine Liste mit der gleichen Große wie unser Kurse Repository zuruckgibt
     */
    @Test
    void testFindAll() {
        Iterator<Student> it = this.studentRepo.findAll().iterator();
        int i = 0;
        for (; it.hasNext(); ++i) it.next();
        assert i == 4;
    }

    /**
     * Wir testen, ob die bereits vorhandene Entität gespeichert wurde
     */
    @Test
    void testSave() throws FileNotFoundException {
        studentRepo.save(s1);
        Iterator<Student> it = this.studentRepo.findAll().iterator();
        int i = 0;
        for (; it.hasNext(); ++i) it.next();
        assert i == 5; //lista a fost marita
    }

    /**
     * Uberprufen Sie, ob die Entitat aktualisiert wurde
     */
    @Test
    void testUpdate() throws FileNotFoundException {
        Student s = this.studentRepo.update(s2);
        assert s == null; //die Entitat wurde aktualisiert
    }

    /**
     *Wir testen, ob nach dem Loschen der Entitat die Listengroße geandert wurde
     */
    @Test
    void testDelete() throws FileNotFoundException {
        this.studentRepo.delete((long) 4);
        Iterator<Student> it = this.studentRepo.findAll().iterator();
        int i = 0;
        for ( ; it.hasNext() ; ++i ) it.next();
        assert i==4;
    }
}