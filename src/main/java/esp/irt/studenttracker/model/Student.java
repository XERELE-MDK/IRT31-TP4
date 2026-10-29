package esp.irt.studenttracker.model;

public class Student {
    private String firstName;
    private String lastName;
    public Student() { } // constructeur sans argument (requis par Jackson

    public Student(String firstName, String lastName) {
        this.firstName =firstName;
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }



    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }
}
