

public class Medicines {
    private String medicine_name;
    private Shelves shelf;
    private Availability availability;

    public Medicines(){
        medicine_name = "";
        shelf = Shelves.A1;
        availability = Availability.Yes;
    }

    public Medicines(String name, Shelves shelf1, Availability availability1){
        this.medicine_name = name;
        this.shelf = shelf1;
        this.availability = availability1;

    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public Shelves getShelf() {
        return shelf;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public void setShelf(Shelves shelf) {
        this.shelf = shelf;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return String.format("Patient Medication: %s\nShelf Number: %s\nMedication Availability: %s\n",medicine_name,getShelf().toString(),getAvailability().toString());
    }
}
