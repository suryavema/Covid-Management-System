import java.io.*;
import java.util.*;

public class Menu {
	static Scanner in = new Scanner(System.in);
    static boolean admin;
    static User user;

    static User login(int userID, String passwd) throws UserNotFoundException
    {
        for(User usr : UserManagement.UserData)
        {
            if(userID == usr.getUserId())
            {
                if(passwd.equals(usr.getPassword())) return usr;
                else throw new UserNotFoundException("Invalid Credentials");
            }
        }
        for(QuarantineUser usr : QuarantineManagement.quarantineUserList)
        {
            if(userID == usr.getUserId())
            {
                if(passwd.equals(usr.getPassword())) return usr;
                else throw new UserNotFoundException("Invalid Credentials");
            }
        }
        throw new UserNotFoundException("User not found with userID " + userID);
    }

    static void admin_menu() {
        boolean loop = true;
        while(loop)
        {
            System.out.println("\n\n\n-------------------------------------");
            System.out.print("Covid Management System\n-------------------------------------\n");
            System.out.println("Main Menu     @Admin");
            System.out.println("-------------------------------------\n");
            System.out.println("1. User Management");
            System.out.println("2. Quarantine Management");
            System.out.println("3. Donations");
            System.out.println("4. Manage help Requests");
            System.out.println("5. Quit");
            System.out.print("Enter your choice: ");
            int choice = in.nextInt();
            if (choice == 1) {
                // usermanagement(username,passwrd);
                UserManagement.adminMenu();
            } else if (choice == 2) {
                //quarantine_management(username,passwrd);
                QuarantineManagement.admin_menu();
            } else if (choice == 3) {
                //Donation(username,passwrd);
                DonationMenu.menu_admin();
            } else if (choice == 4) {
                help.mng_hlp_requests();
            } else if (choice == 5) {
                System.out.println("\n\nThank you");
                System.out.println("The Program is being closed");
                loop = false;
            } else {
                System.out.println("Please Enter A Valid Input");
            }
        }
    }

    static void user_menu() {
        boolean loop = true;
        while(loop)
            {
            System.out.println("\n\n\n-------------------------------------");
            System.out.println("Covid Management System\n------------------------------------");
            System.out.println("Main Menu     @User");
            System.out.println("-------------------------------------\n");
            
            System.out.println("1. User Management");
            System.out.println("2. Quarantine Management");
            System.out.println("3. Donate");
            System.out.println("4. Request Help");
            System.out.println("5. Quit");
            System.out.print("Enter your choice : ");
            int choice = in.nextInt();
            if (choice == 1) {
                //usermanagement(username,passwrd);
                UserManagement.userMenu();
            } else if (choice == 2) {
                //quarantinemanagement(username,passwrd);
                QuarantineManagement.user_menu();
            } else if (choice == 3) {
                //Donation(username,passwrd);
                DonationMenu.menu_user();
            } else if (choice == 4) {
                help.requesthelp();
            } else if (choice == 5) {
                System.out.println("Thank you ");
                System.out.println("The program is being closed");
                loop = false;
            } else {
                System.out.println("Please Enter A Valid Input");
                user_menu();
            }
        }
    }

    public static void main(String[] args) {
        try
        { 
            QuarantineManagement.quarantineUserList = loadFromFile("Qusers.dat");
            UserManagement.UserData = loadFromFile("User.dat");
            DonationMenu.donationList = loadFromFile("Donations.dat");
            
            System.out.print("Enter your userId : ");
            int userId = in.nextInt();

            System.out.print("Enter your password : ");
            String passwd = in.next();
            
            Menu.user = login(userId, passwd);
            if(user.isAdmin()) admin_menu();
            else user_menu();

            saveToFile(QuarantineManagement.quarantineUserList, "Qusers.dat");   
            saveToFile(UserManagement.UserData, "User.dat");
            saveToFile(DonationMenu.donationList, "Donations.dat");
        }
        catch(UserNotFoundException unfe)
        {
            System.out.println(unfe);
        }
        catch(IOException ioe)
        {
            System.out.println(ioe);
        }
        catch(ClassNotFoundException cnfe)
        {
            System.out.println(cnfe);
        }
    }
    public static  <T> T loadFromFile(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
        Object obj =  in.readObject();
        in.close();
        return (T) obj;    
    }

    public static <T> void saveToFile(T list, String filename) throws IOException, ClassNotFoundException {
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(list);
        oos.close();
        fos.close();
    }

}

class help {
    static Scanner sc =new Scanner(System.in);
    static boolean requesthelp() {
        System.out.println("\n\n\n-------------------------------------");
        System.out.println("Requesting Help     @User");
        System.out.println("-------------------------------------\n");
        
        System.out.print("Enter your details \nEnter your name: ");
        String name = null;
        try {
            name = sc.next();
        } catch (InputMismatchException ie) {
            System.out.println("You Have Entered Invalid Details!!");
            requesthelp();
        }
        System.out.print("Enter The Bugs or uncomfortabilities you are feeling in the program :");
        String Message = sc.next();
        Message+=sc.nextLine();
        Date date = Calendar.getInstance().getTime();

        try {
            File f = new File("help.txt");
            FileWriter fw = new FileWriter(f, true);
            if (!f.exists()) {
                f.createNewFile();
            }
            fw.write("Name:\t"+name + "\t"+"\t"+"Message:\t" + Message +"\t"+ "\t"+"Date:\t" +date+"\n");
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    static void mng_hlp_requests() {
        System.out.println("\n\n\n-------------------------------------");
        System.out.println("Help Requests Management     @Admin");
        System.out.println("-------------------------------------\n");
        System.out.println("Welcome to the Help Requests Management Software :\nwhat would you like to do ?");
        System.out.println("1.Delete All Help Requests");
        System.out.println("2.Get Help Request Data");
        System.out.print("Enter your choice : ");
        int choice = 0;
        try {
            choice = sc.nextInt();
            System.out.println();
        } catch (InputMismatchException ime) {
            System.out.println("Enter Correct Datatype in the coloumn");
            mng_hlp_requests();
        }
        if(choice==1){
            del_help_req();
        }else if(choice==2){
            read_help_req();
        }
    }
    static boolean del_help_req(){
        try{
            FileWriter file=new FileWriter("help.txt",false);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;

    }
    static boolean read_help_req(){
        try{
            File f = new File("help.txt");
            if(!f.exists()){
                f.createNewFile();
            }
            FileReader fr =new FileReader(f);
            char[] chars = new char[(int) f.length()];
            fr.read(chars);
 
            String fileContent = new String(chars);
            System.out.println(fileContent);

            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}

class UserNotFoundException extends Exception
{
    UserNotFoundException(String str)
    {
        super(str);
    }
}