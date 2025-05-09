import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Database {
    public static void main(String[] args) throws NoSuchElementException {
        ArrayList<Patient> patients = CreatePatients("Patients.txt");
        ArrayList<Medicines> medicines = CreateMedicine("Meds.txt");
        ArrayList<Pharmacist> pharmacists = CreatePharmacist("Pharmacist.txt");
        Scanner sc1 = new Scanner(System.in);

        //Program login
        boolean found = false;
        String passwordUI = "";
        Pharmacist info = null;
        while(!found){ //checks for inputs, if incorrect login it requests inputs again
            System.out.print("First Name: ");
            String fNameUI = sc1.nextLine();
            System.out.print("Last Name: ");
            String lNameUI = sc1.nextLine();
            System.out.print("Password: ");
            passwordUI = sc1.nextLine();
            for(Pharmacist pharmacist: pharmacists){
                if (fNameUI.equalsIgnoreCase(pharmacist.getFirstName()) && lNameUI.equalsIgnoreCase(pharmacist.getLastName())
                        && passwordUI.equalsIgnoreCase(pharmacist.getPassword())) {
                    found = true;
                    info = pharmacist;
                    break;
                }
            }
            if (found) {
                System.out.println("Logging in as " + fNameUI.substring(0,1).toUpperCase()+fNameUI.substring(1).toLowerCase()
                        + " " + lNameUI.substring(0,1).toUpperCase()+lNameUI.substring(1).toLowerCase() + "\n");
            } else {
                System.out.println("Not a valid user, please try again\n");
            }
        }

        //start of program option list
        int choices = 0;
        while (choices != 8) {
            System.out.println("What would you like to do?\n" +
                        "1. Program Settings\n"+
                        "2. Display all patients\n" +
                        "3. Search a patient by ID\n" +
                        "4. Authenticate Patient Data\n" +
                        "5. Edit patient information\n" +
                        "6. Check patient medication availability\n" +
                        "7. Add a patient");
            choices = sc1.nextInt();
            switch (choices) {
                case 1:
                    System.out.println("Setting options:\n" +
                            "1. Change user password\n" +
                            "2. Logout user");
                    int option = sc1.nextInt();
                    if(option == 1){
                        changePassword(pharmacists,passwordUI,"sprint/Pharmacist.txt",info);
                        break;
                    } else if (option == 2) {
                        System.out.println("Logging out, have a great day!");
                        choices = 8;
                    }
                    break;
                case 2:
                    for (Patient patient : patients) {
                        System.out.println(patient.toString());
                    }
                    break;
                case 3:
                    String skip = sc1.nextLine();
                    System.out.println("Enter patient ID");
                    int ID = getValidId(sc1);
                    Patient patient = searchID(patients, ID);
                    assert patient != null;
                    System.out.println("\n" + patient.toString());
                    break;
                case 4:
                    findPatient(patients);
                    break;
                case 5:
                    updatePatient(patients);
                    break;
                case 6:
                    System.out.println("Enter patient ID to check medication availability");
                    int id = getValidId(sc1);
                    CheckAvailability(patients, medicines, id);
                    break;
                case 7:
                    addPatient(patients);
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        }
    }
    public static void changePassword(ArrayList<Pharmacist> pharmacists,String currPassword,String fileName,Pharmacist loginInfo){
        Scanner input = new Scanner(System.in);
        boolean found = false;
        while(!found){
            System.out.println("Please enter current password for verification: ");
            String curr = input.nextLine();
            if(curr.equals(currPassword)){
                found = true;
            }
            if(!found){
                System.out.println("Incorrect Password, try again\n");
            }
        }
        System.out.println("\nPassword requirements:\n" +
                "- Have a minimum of 8 characters in length\n" +
                "- Consist of only letters and digits\n" +
                "- Must contain be a mix of upper and lower case letters\n" +
                "Please input new password");
        String newPassword = getPasswordValidity(input,currPassword);
        System.out.println("New password is now "+newPassword);
        boolean changed = false;
        for(Pharmacist pharmacist:pharmacists){
            if(loginInfo.equals(pharmacist)){
                pharmacist.setPassword(newPassword);
                changed = true;
            }
        }
        if(changed){
            try(PrintWriter writer = new PrintWriter(fileName)){
                for(Pharmacist pharmacist:pharmacists){
                    writer.println(pharmacist.getFirstName()+","+pharmacist.getLastName()+","+pharmacist.getPassword());
                }
                System.out.println("Password updated! Password is now "+newPassword+"\n");
            }catch(FileNotFoundException e){
                System.out.println("Error writing to file: "+e.getMessage());
            }
        }
    }
    public static String getPasswordValidity(Scanner input,String curr){
        boolean valid = false;
        String newPassword = "";
        boolean isUpper=false,isLower=false,isDigit=false;
        Pattern specialChar = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        while(!valid){
            System.out.println("Enter New password: ");
            newPassword = input.next();
            Matcher matcher = specialChar.matcher(newPassword);
            boolean specialFound = matcher.find();
            if (newPassword.length() < 8){
                System.out.println("Password must be at least 8 characters in length");
            }else if(newPassword.equals(curr)){
                System.out.println("New Password can not be the same as previous password");
            }else if(specialFound){
                System.out.println("Password must only contain letters and digits, no special characters");
            } else{
                for(int i=0;i<newPassword.length();i++){
                    if(Character.isUpperCase(newPassword.charAt(i))){
                        isUpper = true;
                    }else if(Character.isLowerCase(newPassword.charAt(i))){
                        isLower = true;
                    }else if(Character.isDigit(newPassword.charAt(i))){
                        isDigit = true;
                    }if(isUpper && isLower && isDigit){
                        valid = true;
                        break;
                    }
                }if(!isUpper || !isLower){
                    System.out.println("Password must be a mix of both upper and lower case characters");
                }if(!isDigit){
                    System.out.println("Password must contain at least one digit");
                }
            }
        }return newPassword;
    }
    public static Patient searchID(ArrayList<Patient> patients, int ID){
        for (Patient patient : patients) {
            if (patient.getId() == ID) {
                return patient;
            }
        }return null;
    }
    public static void CheckAvailability(ArrayList<Patient> patients,ArrayList<Medicines> medicines,int id){
        Patient foundPatient = searchID(patients,id);
        assert foundPatient != null;
        String[] medications = foundPatient.getMedicineName().split("-");
        boolean found_med = false;
        for (String medication : medications) {
            for (Medicines medicine : medicines) {
                if (medicine.getMedicine_name().equalsIgnoreCase(medication)) {
                    if(medicine.getAvailability().equals(Availability.No)){
                        System.out.println(medicine.getMedicine_name()+" is currently not available, please come back at a later time");
                    }
                    System.out.println(medicine.toString());
                    found_med = true;
                }
            }
        }
        if(!found_med){
            System.out.println("Medication not found, Please Check again\n");
        }
    }
    public static void findPatient(ArrayList<Patient> patients){
        Scanner sc1 = new Scanner(System.in);
        System.out.print("First Name: ");
        String fNameUI = sc1.nextLine();
        System.out.print("Last Name: ");
        String lNameUI = sc1.nextLine();
        int idUI = getValidId(sc1);
        System.out.print("Date of birth: (00/00/0000 d/m/y)");
        String dobUI = sc1.nextLine();
        System.out.print("Address: ");
        String addressA = sc1.nextLine();
        System.out.print("City: ");
        String cityA = sc1.nextLine();
        States stateA = getValidState(sc1);
        long phone_numberUI = getValidPhoneNumber(sc1);

        boolean found = false;
        for(Patient patient:patients){
            if (fNameUI.equalsIgnoreCase(patient.getFirstName()) && lNameUI.equalsIgnoreCase(patient.getLastName()) ) {
                if((idUI == patient.getId()) && dobUI.equalsIgnoreCase(patient.getDob()) && phone_numberUI == patient.getPhoneNumber() ){
                    if(addressA.equalsIgnoreCase(patient.getStreet()) && cityA.equalsIgnoreCase(patient.getCity()) && stateA==patient.getState()){
                        found = true;
                        break;
                    }
                }
            }
        }
        if (found) {
            System.out.println("Patient with specified information is in the database\n");
        } else {
            System.out.println("There is no patient listed with that information\n");
        }
    }
    public static ArrayList<Medicines> CreateMedicine(String inputFile){
        ArrayList<Medicines> list = new ArrayList<>();
        try{
            List<String> lines = Files.readAllLines(java.nio.file.Paths.get(inputFile));
            for(String line : lines){
                if(!line.equals("")){
                    Medicines medicines = new Medicines();
                    String[] tokens = line.split(",");
                    medicines.setMedicine_name(tokens[0]);
                    medicines.setShelf(Shelves.valueOf(tokens[1]));
                    medicines.setAvailability(Availability.valueOf(tokens[2]));
                    list.add(medicines);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }return list;
    }
    public static ArrayList<Pharmacist> CreatePharmacist(String inputFile){
        ArrayList<Pharmacist> list = new ArrayList<>();
        try{
            List<String> lines = Files.readAllLines(java.nio.file.Paths.get(inputFile));
            for(String line : lines){
                if(!line.equals("")) {
                    Pharmacist pharmacist = new Pharmacist();
                    String[] tokens = line.split(",");
                    pharmacist.setFirstName(tokens[0]);
                    pharmacist.setLastName(tokens[1]);
                    pharmacist.setPassword(tokens[2]);
                    list.add(pharmacist);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }return list;
    }
    public static ArrayList<Patient> CreatePatients(String inputFile){
        ArrayList<Patient> list = new ArrayList<>();
        try{
            List<String> lines = Files.readAllLines(java.nio.file.Paths.get(inputFile));
            for(String line : lines){
                if(!line.equals("")) {
                    Patient patient = new Patient();
                    String[] tokens = line.split(",");
                    patient.setFirstName(tokens[0]);
                    patient.setLastName(tokens[1]);
                    patient.setId(Integer.parseInt(tokens[2]));
                    patient.setDob(tokens[3]);
                    patient.setStreet(tokens[4]);
                    patient.setCity(tokens[5]);
                    patient.setStates(States.valueOf(tokens[6].toUpperCase()));
                    patient.setPhoneNumber(Long.parseLong(tokens[7]));
                    patient.setMedicineName(tokens[8]);
                    list.add(patient);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return list;
    }
    public static void updatePatient(ArrayList<Patient> patients) {
        System.out.println("Enter patient ID to update:");
        Scanner sc = new Scanner(System.in);
        int id = getValidId(sc);
        boolean found = false;
        for (Patient patient : patients) {
            if (patient.getId() == id) {
                found = true;
                System.out.println("Patient found. Enter updated information:");
                System.out.print("First Name: ");
                patient.setFirstName(sc.next());
                System.out.print("Last Name: ");
                patient.setLastName(sc.next());
                System.out.print("Address: ");
                sc.nextLine();
                patient.setStreet(sc.nextLine());
                System.out.print("City: ");
                String cityInput = sc.nextLine();
                patient.setCity(cityInput);
                patient.setStates(getValidState(sc));
                long phoneNumber = getValidPhoneNumber(sc);
                patient.setPhoneNumber(phoneNumber);
                System.out.println("Patient information updated successfully.\n");
                break;
            }
        }
        if (!found) {
            System.out.println("Patient with ID " + id + " not found.\n");
        } else {
            writePatientsToFile(patients, "sprint/Patients.txt");
        }
    }
    public static void writePatientsToFile(ArrayList<Patient> patients, String filename) {
        try (PrintWriter writer = new PrintWriter(filename)) {
            for (Patient patient : patients) {
                writer.println(patient.getFirstName() + "," +
                        patient.getLastName() + "," +
                        patient.getId() + "," +
                        patient.getDob() + "," +
                        patient.getStreet() + "," +
                        patient.getCity() + "," +
                        patient.getState() + "," +
                        patient.getPhoneNumber()+","+
                        patient.getMedicineName());
            }
            System.out.println("Patient information updated in the file.\n");
        } catch (FileNotFoundException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    public static void addPatient(ArrayList<Patient> patients) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter new patient details:");
        System.out.print("First Name: ");
        String firstName = sc.nextLine();
        System.out.print("Last Name: ");
        String lastName = sc.nextLine();

        int id;
        // Validate the ID to ensure it does not exist in the Patients.txt file
        while (true) {
            id = getValidId(sc);
            // Check if the ID already exists in the patients list
            if (isExistingId(patients, id)) {
                System.out.println("ID already exists in the database. Please use the edit option to update patient information.\n");
                return;
            } else {
                break; // Exit the loop if ID is unique
            }
        }
        System.out.print("Date of Birth (00/00/0000 m/d/y): ");
        String dob = sc.nextLine();
        System.out.print("Address: ");
        String street = sc.nextLine();
        System.out.print("City: ");
        String city = sc.nextLine();
        States state = getValidState(sc);
        long phoneNumber = getValidPhoneNumber(sc);

        // Validate the number of medicines
        int numMedicines = 0;
        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.print("How many medicines do you have? ");
                numMedicines = sc.nextInt();
                sc.nextLine(); // Consume newline character after nextInt()
                if (numMedicines == 0){
                    System.out.println("Patient needs to have at least one medication");
                }
                else if (numMedicines > 0) {
                    validInput = true; // Set flag to exit the loop
                } else {
                    System.out.println("Invalid number of medicines. Please enter a non-negative number.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input for number of medicines. Please enter a valid integer.");
                sc.nextLine(); // Consume invalid input
            }
        }
        // Prompt for medication names and format them with '-'
        StringBuilder formattedMedications = new StringBuilder();
        for (int i = 0; i < numMedicines; i++) {
            System.out.print("Medication Name #" + (i + 1) + ": ");
            String medicineName = sc.nextLine().trim(); // Read medication name

            formattedMedications.append(medicineName);
            if (i < numMedicines - 1) {
                formattedMedications.append("-"); // Add '-' between medications
            }
        }
        // Create new Patient object
        Patient newPatient = new Patient();
        newPatient.setFirstName(firstName);
        newPatient.setLastName(lastName);
        newPatient.setId(id);
        newPatient.setDob(dob);
        newPatient.setStreet(street);
        newPatient.setCity(city);
        newPatient.setStates(state);
        newPatient.setPhoneNumber(phoneNumber);
        newPatient.setMedicineName(formattedMedications.toString());

        patients.add(newPatient);

        writePatientsToFile(patients, "sprint/Patients.txt");

        System.out.println("Patient added successfully.\n");
    }
    private static boolean isExistingId(ArrayList<Patient> patients, int id) {
        // Check if the ID already exists in the patients list
        for (Patient patient : patients) {
            if (patient.getId() == id) {
                return true; // ID already exists
            }
        }return false; // ID does not exist
    }
    public static States CheckForUnderscore(String state1){
        char[] state_char = state1.toCharArray();
        for(int i = 0; i < state_char.length ; i++){
            if(state_char[i] == ' '){
                state_char[i] = '_';
            }
        }
        String new_state = String.valueOf(state_char);
        return States.valueOf(new_state);
    }
    private static int getValidId(Scanner sc) {
        int id = 0;
        boolean validId = false;
        while (!validId) {
            try {
                System.out.print("ID number (9 digits): ");
                id = sc.nextInt();
                sc.nextLine(); // Consume newline character

                // Convert ID to string to count digits
                String idString = String.valueOf(id);

                // Check if the ID length is exactly 9 digits
                if (idString.length() == 9) {
                    validId = true; // Set flag to exit loop
                } else {
                    System.out.println("ID must have exactly 9 digits. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input for ID. Please enter a valid 9-digit number.");
                sc.nextLine(); // Consume invalid input
            }
        }return id;
    }
    private static long getValidPhoneNumber(Scanner sc) {
        long phoneNumber = 0;
        while (true) {
            try {
                System.out.print("Phone Number: ");
                phoneNumber = Long.parseLong(sc.nextLine());
                // Check if the phone number has exactly 10 digits
                if (String.valueOf(phoneNumber).length() == 10) {
                    break;
                } else {
                    System.out.println("Phone number must have exactly 10 digits. Please enter again");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for phone number. Please enter a valid number");
            }
        }return phoneNumber;
    }
    private static States getValidState(Scanner sc) {
        States stateInput = null;
        while (stateInput == null) {
            try {
                System.out.print("State: ");
                stateInput = CheckForUnderscore(sc.nextLine().toUpperCase());
            } catch (Exception e) {
                System.out.println("Invalid input for state. Please enter a valid state");
            }
        }return stateInput;
    }
}