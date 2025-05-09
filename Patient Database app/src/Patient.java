

public class Patient  {
    private String firstName,lastName, dob, street,city,state;
    private int id;
    private long phoneNumber;
    private String medicineName;
    Medicines medicines;

    public Patient(){
        firstName = "";
        lastName = "";
        dob = "";
        id = 0;
        dob = "";
        street = "";
        city = "";
        phoneNumber= 0;
        medicineName = "";
    }

    public Patient(String firstName,String lastName, int id, String dob, String street,String city,String state, long phoneNumber,String MedicineName){
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.dob = dob;
        this.street = street;
        this.city = city;
        this.state = state;
        this.phoneNumber = phoneNumber;
        this.medicineName = MedicineName;
    }
    public String getFirstName() {
        return firstName;
    }public String getLastName(){
        return lastName;
    }public int getId() {
        return id;
    }public String getDob() {
        return dob;
    }public String getStreet() {
        return street;
    }public String getCity(){
        return city;
    }public States getState() {
        return States.valueOf(state);
    }public long getPhoneNumber() {
        return phoneNumber;
    }public String getMedicineName(){
        return medicineName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }public void setLastName(String lastName) {
        this.lastName = lastName;
    }public void setId(int id) {
        this.id = id;
    }public void setDob(String dob) {
        this.dob = dob;
    }public void setStreet(String street) {
        this.street = street;
    }public void setCity(String city) {
        this.city = city;
    }public void setStates(States state){
        this.state = state.toString();
    }public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }public void setMedicineName(String medicineName){
        this.medicineName = medicineName;
    }

    public String toString() {
        return String.format("Name: %s %s\nID: %d\nDOB: %s\nAddress: %s %s, %s\nPhone Number: %d\nMedication: %s\n",
                firstName,lastName,id,dob,street,city,state.substring(0,1).toUpperCase()+state.substring(1).toLowerCase().replace("_"," "),phoneNumber,medicineName.replace("-"," and "));
    }
}
