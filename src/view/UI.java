package view;

import controller.Controller;
import exceptions.ValidatorException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Course;
import model.Student;
import model.Teacher;
import repository.CourseFileRepository;
import repository.ICrudRepository;
import repository.StudentFileRepository;
import repository.TeacherFileRepository;

import java.io.FileNotFoundException;
import java.io.IOException;

public class UI {
    ICrudRepository<Student> studentRepo = new StudentFileRepository("src/StudentsFile");
    ICrudRepository<Teacher> teacherRepo = new TeacherFileRepository("src/TeachersFile");
    ICrudRepository<Course> courseRepo = new CourseFileRepository("src/CoursesFile", studentRepo, teacherRepo);

    private Controller ctrl = new Controller(courseRepo, studentRepo, teacherRepo);

    Integer ID = null;
    Integer courseID = null;

    public UI() throws IOException {
    }

    public GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Add Column Constraints

        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);

        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200, 200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        return gridPane;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private void registerForCourseUIControls(GridPane gridPane) {
        // Add Header
        Label headerLabel = new Label("Registrieren Sie sich für einen Kurs");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));


        // Add ID Label
        Label courseIDLabel = new Label("Kurs ID : ");
        gridPane.add(courseIDLabel, 0, 1);

        // Add ID Text Field
        TextField courseIDField = new TextField();
        courseIDField.setPrefHeight(30);
        courseIDField.setPrefWidth(40);
        gridPane.add(courseIDField, 1, 1);

        // Add ID Label
        Label studentIDLabel = new Label("Student ID : ");
        gridPane.add(studentIDLabel, 0, 2);

        // Add ID Text Field
        TextField studentIDField = new TextField();
        studentIDField.setPrefHeight(30);
        studentIDField.setPrefWidth(40);
        gridPane.add(studentIDField, 1, 2);



        // Add Registration Button
        Button submitButton = new Button("Anmelden");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 4, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0, 20, 0));

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Course c = ctrl.getCourseRepo().findOne((long) Integer.parseInt(courseIDField.getText()));
                Student s=ctrl.getStudentRepo().findOne((long) Integer.parseInt(studentIDField.getText()));
                if (c == null || s==null)
                    showAlert(Alert.AlertType.ERROR,
                            "Registration Error!", "Course or student doesn't exist!");
                else {
                    int res = 0;
                    try {
                        res = ctrl.register(s, c);
                    } catch (FileNotFoundException | ValidatorException e) {
                        e.printStackTrace();
                    }

                    if (res == 3)
                        showAlert(Alert.AlertType.CONFIRMATION,
                                "Registration Successful!", "Student " + s.getFirstName() +" "+s.getLastName()+ " was registered for course with ID " + courseIDField.getText());
                    if (res == 2)
                        showAlert(Alert.AlertType.ERROR,
                                "Registration Error!", "Student already takes this course!");
                    if (res == 0)
                        showAlert(Alert.AlertType.ERROR,
                                "Registration Error!", "You can't choose this course. Course doesn't have any free places!");
                    if (res == 1)
                        showAlert(Alert.AlertType.ERROR,
                                "Registration Error!", "You can't choose this course. Maximum number of credits exceeded!");
                }

            }
        });
    }

    private void freeSpotsUI(GridPane gridPane){
        Label headerLabel = new Label("Freie Plätze Kurse");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));
       // System.out.println(ctrl.retrieveCoursesWithFreePlaces());

        // Add Label
        Label creditLabel = new Label("Kurse Namen : ");
        gridPane.add(creditLabel, 0, 2);

        // Add Label
        Label numberLabel = new Label();
        gridPane.add(numberLabel, 1, 2);

        Button showCredit = new Button("Kurse zeigen");
        gridPane.add(showCredit, 1, 4);
        GridPane.setHalignment(showCredit, HPos.CENTER);
        GridPane.setMargin(showCredit, new Insets(20, 0, 20, 70));

        // Add Label
        Label creditLabel2 = new Label("Freie Platze : ");
        gridPane.add(creditLabel2, 0, 3);

        // Add Label
        Label numberLabel2 = new Label();
        gridPane.add(numberLabel2, 1, 3);

        showCredit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    String str = ctrl.courseToString(ctrl.retrieveCoursesWithFreePlaces());
                    numberLabel.setText(str);
                    String str2 = ctrl.placesToString(ctrl.retrieveCoursesWithFreePlaces());
                    numberLabel2.setText(str2);
            }
        });
    }

    private void noStudentsCourseUI(GridPane gridPane){
        Label headerLabel = new Label("Kurse ohne Studenten");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));
        // System.out.println(ctrl.retrieveCoursesWithFreePlaces());

        // Add Label
        Label creditLabel = new Label("Kurse Namen : ");
        gridPane.add(creditLabel, 0, 2);

        // Add Label
        Label numberLabel = new Label();
        gridPane.add(numberLabel, 1, 2);

        Button showCredit = new Button("Kurse zeigen");
        gridPane.add(showCredit, 1, 4);
        GridPane.setHalignment(showCredit, HPos.CENTER);
        GridPane.setMargin(showCredit, new Insets(20, 0, 20, 70));

        showCredit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String str = ctrl.courseToString(ctrl.retrieveCoursesWithoutStud());
                numberLabel.setText(str);
            }
        });
    }


    public void addMenuUIControls1(GridPane gridPane) {
        // Add Header
        Label headerLabel = new Label("STUDENT MENU");
        headerLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 24));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

        Button studentButton2 = new Button("Anmelden an Kurs");
        Button studentButton3 = new Button("Kurse mit freien Plätzen");
        Button studentButton4 = new Button("Kurse ohne Studenten");


        studentButton2.setMinSize(170, 30);
        studentButton3.setMinSize(170, 30);
        studentButton4.setMinSize(170, 30);

        gridPane.add(studentButton2, 0, 2, 2, 1);
        gridPane.add(studentButton3, 0, 3, 2, 1);
        gridPane.add(studentButton4, 0, 4, 2, 1);

        GridPane.setHalignment(studentButton2, HPos.CENTER);
        GridPane.setMargin(studentButton2, new Insets(0, 0, 0, 0));
        GridPane.setHalignment(studentButton3, HPos.CENTER);
        GridPane.setMargin(studentButton3, new Insets(0, 0, 0, 0));
        GridPane.setHalignment(studentButton4, HPos.CENTER);
        GridPane.setMargin(studentButton4, new Insets(0, 0, 0, 0));

        studentButton2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Stage thirdStage = new Stage();
                thirdStage.setTitle("Kursanmeldung");
                // Create the student registration form grid pane
                GridPane registerForCourseGridPane = createGridPane();
                // Add UI controls to the registration form grid pane
                registerForCourseUIControls(registerForCourseGridPane);

                // Create a scene with registration form grid pane as the root node
                Scene registerCourseScene = new Scene(registerForCourseGridPane, 500, 300);
                // Set the scene in primary stage
                thirdStage.setScene(registerCourseScene);
                thirdStage.show();
            }
        });

        studentButton3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Stage thirdStage = new Stage();
                thirdStage.setTitle("Kurse mit freien Plätzen");
                // Create the student registration form grid pane
                GridPane CreditGridPane = createGridPane();
                // Add UI controls to the registration form grid pane
                freeSpotsUI(CreditGridPane);

                // Create a scene with registration form grid pane as the root node
                Scene creditScene = new Scene(CreditGridPane, 500, 300);
                // Set the scene in primary stage
                thirdStage.setScene(creditScene);
                thirdStage.show();
            }
        });

        studentButton4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Stage thirdStage = new Stage();
                thirdStage.setTitle("Kurse ohne Studenten");
                // Create the student registration form grid pane
                GridPane CreditGridPane = createGridPane();
                // Add UI controls to the registration form grid pane
                noStudentsCourseUI(CreditGridPane);

                // Create a scene with registration form grid pane as the root node
                Scene creditScene = new Scene(CreditGridPane, 500, 300);
                // Set the scene in primary stage
                thirdStage.setScene(creditScene);
                thirdStage.show();
            }
        });
    }

    private void showStudentsUIControls(GridPane gridPane){
        Label headerLabel = new Label("Angemeldet Studenten");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

        // Add ID Label
        Label IDLabel = new Label("Kurse ID : ");
        gridPane.add(IDLabel, 0, 1);

        // Add ID Text Field
        TextField IDField = new TextField();
        IDField.setPrefHeight(30);
        IDField.setMaxWidth(100);
        gridPane.add(IDField, 1, 1);

        // Add Label
        Label creditLabel = new Label("Studenten IDs : ");
        gridPane.add(creditLabel, 0, 2);

        // Add Label
        Label numberLabel = new Label();
        gridPane.add(numberLabel, 1, 2);


        Button showCredit = new Button("Studenten zeigen");
        gridPane.add(showCredit, 1, 1);
        GridPane.setHalignment(showCredit, HPos.CENTER);
        GridPane.setMargin(showCredit, new Insets(20, 0, 20, 70));

        showCredit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (IDField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR,
                            "Registration Error!", "Please enter all the required data!");
                    return;
                }

                ID = Integer.parseInt(IDField.getText());
                Course c = ctrl.getCourseRepo().findOne((long) ID);
                int credits;
                if (c == null)
                    showAlert(Alert.AlertType.ERROR,
                            "Error!", "This course doesn't exist!");
                else {
                        String str = c.studentsIDToString();
                        numberLabel.setText(str);
                }
            }
        });
    }

    private void changeCreditsUI(GridPane gridPane){
        Label headerLabel = new Label("Kredite Änderung");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));


        // Add ID Label
        Label courseIDLabel = new Label("Kurse ID : ");
        gridPane.add(courseIDLabel, 0, 1);

        // Add ID Text Field
        TextField courseIDField = new TextField();
        courseIDField.setPrefHeight(30);
        courseIDField.setPrefWidth(40);
        gridPane.add(courseIDField, 1, 1);

        // Add ID Label
        Label cred = new Label("Neue Kredite : ");
        gridPane.add(cred, 0, 2);

        // Add ID Text Field
        TextField credField = new TextField();
        credField.setPrefHeight(30);
        credField.setPrefWidth(40);
        gridPane.add(credField, 1, 2);

        // Add Registration Button
        Button submitButton = new Button("Ändern");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 4, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0, 20, 0));

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Course c=ctrl.getCourseRepo().findOne((long) Integer.parseInt(courseIDField.getText()));

                if (c==null)
                    showAlert(Alert.AlertType.ERROR,
                            "Error!", "Course doesn't exist!");
                else {
                    try {
                        ctrl.updateCourse(c.getId(),c.getName(),c.getTeacher(),c.getMaxEnrollment(),Integer.parseInt(credField.getText()));
                    } catch (ValidatorException | FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void deleteCourseUI(GridPane gridPane){

// Add Header
        Label headerLabel = new Label("Kurs löschen");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));


        // Add ID Label
        Label courseIDLabel = new Label("Kurse ID : ");
        gridPane.add(courseIDLabel, 0, 1);

        // Add ID Text Field
        TextField courseIDField = new TextField();
        courseIDField.setPrefHeight(30);
        courseIDField.setPrefWidth(40);
        gridPane.add(courseIDField, 1, 1);

        // Add Registration Button
        Button submitButton = new Button("Löschen");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 3, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0, 20, 0));

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Course c=ctrl.getCourseRepo().findOne((long) Integer.parseInt(courseIDField.getText()));

                if (c==null)
                    showAlert(Alert.AlertType.ERROR,
                            "Error!", "Course doesn't exist!");
                else {
                    try {
                        ctrl.getCourseRepo().delete((long) Integer.parseInt(courseIDField.getText()));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void sortStudentsUI(GridPane gridPane){
        Label headerLabel = new Label("Sortieren nach First Namen");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));
        // System.out.println(ctrl.retrieveCoursesWithFreePlaces());

        // Add Label
        Label creditLabel = new Label("Studenten : ");
        gridPane.add(creditLabel, 0, 2);

        // Add Label
        Label numberLabel = new Label();
        gridPane.add(numberLabel, 1, 2);

        Button showCredit = new Button("Sortieren");
        gridPane.add(showCredit, 1, 4);
        GridPane.setHalignment(showCredit, HPos.CENTER);
        GridPane.setMargin(showCredit, new Insets(20, 0, 20, 70));

        showCredit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String str = ctrl.studToString(ctrl.sortStudents());
                numberLabel.setText(str);
            }
        });
    }

    private void filterStudentsUI(GridPane gridPane){
        Label headerLabel = new Label("Studenten Filtern");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

        // Add ID Label
        Label IDLabel = new Label("Mit 1-15 Kredite : ");
        gridPane.add(IDLabel, 0, 1);
        // Add Label
        Label numberLabel = new Label();
        gridPane.add(numberLabel, 1, 1);


        // Add Label
        Label creditLabel = new Label("Mit 15-30 Kredite: ");
        gridPane.add(creditLabel, 0, 2);
        // Add Label
        Label numberLabel2 = new Label();
        gridPane.add(numberLabel2, 1, 2);


        Button showCredit = new Button("Studenten zeigen");
        gridPane.add(showCredit, 1, 3);
        GridPane.setHalignment(showCredit, HPos.CENTER);
        GridPane.setMargin(showCredit, new Insets(20, 0, 20, 70));

        showCredit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    String str = ctrl.studToString(ctrl.filterStudents1());
                    numberLabel.setText(str);
                String str2 = ctrl.studToString(ctrl.filterStudents2());
                numberLabel2.setText(str2);
            }
        });

    }


    public void addMenuUIControls2(GridPane gridPane) {
        // Add Header
        Label headerLabel = new Label("TEACHERS MENU");
        headerLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 24));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

        Button studentButton2 = new Button("Studenten zeigen");
        Button studentButton3 = new Button("Credits ändern");
        Button studentButton4 = new Button("Kurs löschen");
        Button studentButton5 = new Button("Studenten sortieren");
        Button studentButton6 = new Button("Studenten filtern");


        studentButton2.setMinSize(170, 30);
        studentButton3.setMinSize(170, 30);
        studentButton4.setMinSize(170, 30);
        studentButton5.setMinSize(170, 30);
        studentButton6.setMinSize(170, 30);


        gridPane.add(studentButton2, 0, 2, 2, 1);
        gridPane.add(studentButton3, 0, 3, 2, 1);
        gridPane.add(studentButton4, 0, 4, 2, 1);
        gridPane.add(studentButton5, 0, 5, 2, 1);
        gridPane.add(studentButton6, 0, 6, 2, 1);

        GridPane.setHalignment(studentButton2, HPos.CENTER);
        GridPane.setMargin(studentButton2, new Insets(0, 0, 0, 0));
        GridPane.setHalignment(studentButton3, HPos.CENTER);
        GridPane.setMargin(studentButton3, new Insets(0, 0, 0, 0));
        GridPane.setHalignment(studentButton4, HPos.CENTER);
        GridPane.setMargin(studentButton4, new Insets(0, 0, 0, 0));
        GridPane.setHalignment(studentButton5, HPos.CENTER);
        GridPane.setMargin(studentButton5, new Insets(0, 0, 0, 0));
        GridPane.setHalignment(studentButton6, HPos.CENTER);
        GridPane.setMargin(studentButton6, new Insets(0, 0, 0, 0));

        studentButton2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Stage thirdStage = new Stage();
                thirdStage.setTitle("Studenten zeigen");
                // Create the student registration form grid pane
                GridPane registerForCourseGridPane = createGridPane();
                // Add UI controls to the registration form grid pane
                showStudentsUIControls(registerForCourseGridPane);

                // Create a scene with registration form grid pane as the root node
                Scene registerCourseScene = new Scene(registerForCourseGridPane, 500, 300);
                // Set the scene in primary stage
                thirdStage.setScene(registerCourseScene);
                thirdStage.show();
            }
        });

        studentButton3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Stage thirdStage = new Stage();
                thirdStage.setTitle("Credits ändern");
                // Create the student registration form grid pane
                GridPane CreditGridPane = createGridPane();
                // Add UI controls to the registration form grid pane
                changeCreditsUI(CreditGridPane);

                // Create a scene with registration form grid pane as the root node
                Scene creditScene = new Scene(CreditGridPane, 500, 300);
                // Set the scene in primary stage
                thirdStage.setScene(creditScene);
                thirdStage.show();
            }
        });

        studentButton4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Stage thirdStage = new Stage();
                thirdStage.setTitle("Kurs löschen");
                // Create the student registration form grid pane
                GridPane CreditGridPane = createGridPane();
                // Add UI controls to the registration form grid pane
                deleteCourseUI(CreditGridPane);

                // Create a scene with registration form grid pane as the root node
                Scene creditScene = new Scene(CreditGridPane, 500, 300);
                // Set the scene in primary stage
                thirdStage.setScene(creditScene);
                thirdStage.show();
            }
        });

        studentButton5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Stage thirdStage = new Stage();
                thirdStage.setTitle("Studenten sortieren");
                // Create the student registration form grid pane
                GridPane CreditGridPane = createGridPane();
                // Add UI controls to the registration form grid pane
                sortStudentsUI(CreditGridPane);

                // Create a scene with registration form grid pane as the root node
                Scene creditScene = new Scene(CreditGridPane, 500, 300);
                // Set the scene in primary stage
                thirdStage.setScene(creditScene);
                thirdStage.show();
            }
        });

        studentButton6.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Stage thirdStage = new Stage();
                thirdStage.setTitle("Studenten filtern");
                // Create the student registration form grid pane
                GridPane CreditGridPane = createGridPane();
                // Add UI controls to the registration form grid pane
                filterStudentsUI(CreditGridPane);

                // Create a scene with registration form grid pane as the root node
                Scene creditScene = new Scene(CreditGridPane, 500, 300);
                // Set the scene in primary stage
                thirdStage.setScene(creditScene);
                thirdStage.show();
            }
        });


    }
}