
public class Pharmacist {
    private String firstName,lastName,password;

    public Pharmacist(){
        firstName = "";
        lastName = "";
        password = "";
    }
    public Pharmacist(String firstName,String lastName,String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }public String getLastName() {
        return lastName;
    }public String getPassword() {
        return password;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }public void setLastName(String lastName) {
        this.lastName = lastName;
    }public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("Logging in as : %s %s",firstName,lastName);
    }
}
