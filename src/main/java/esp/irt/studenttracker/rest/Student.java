package esp.irt.studenttracker.rest;

public class Student {
    private String nom;
    private String prenom;

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Student(String nom, String prenom){
        this.nom = nom;
        this.prenom = prenom;
    }
}
