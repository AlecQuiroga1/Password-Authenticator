/** 
 * @author Alec
 *
 *
 * This program reads in the file of users.
 * Then allows the person to add or remove the users.
 * Also Sign in to check if there username and password is already in the Arraylist.
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;

public class Authenticator {
	
	// creating the array list using object user
	static ArrayList <User> credentials = new ArrayList <User>();
	 
	// creating a global instance of scanner
	public static Scanner keyboard = new Scanner(System.in);
	
	/**
	 * Runs the program and gives the navigation towards what wants to be done.
	 * 
	 * @param args to accept the file path.
	 * @throws IOException calls loadUsers which puts the users into an Arraylist
	 */
	
	public static void main(String[] args) throws IOException {
		String input = "0";
		
		String path = args[0];
		loadUsers(path);
		
		// Loops until the user wants to exit.
		do {
		do {
		System.out.println("Welcome Type What number you would like to do.");
		System.out.println("1. Sign in");
		System.out.println("2. Add a User");
		System.out.println("3. Remove a User");
		System.out.println("4. Exit the program");
		input = keyboard.nextLine();
		
		}while (!input.equals("1") && !input.equals("2") && !input.equals("3") && !input.equals("4"));
		
		//Puts the input in where ever the user chose
		switch (input) 
		{
		
		case "1":
			signIn();
			break;
		case "2":
			addUsers();
			break;
		case "3":
			removeUser();
			break;
		case "4":
			System.out.println("Goodbye");
			System.out.println(credentials.get(2));
			writeToFile();
			break;
			
		}
		} while (!input.equals("4"));
		
		}
	
	/**
	 * Takes in the Username and password.
	 * Calls isUserValid to check the validity of the password.
	 * 
	 * @return The amount of users in credentials
	 */
	public static int addUsers() {
		String username = "";
		String password = "";
		String passwordHash;
		String passwordSalt;
		String salt = generateSalt();
		
		System.out.println("Input the Username:");
		username = keyboard.nextLine();
		
		// Loops Until the password is valid.
		do {
			System.out.println("Input the Password:");
			System.out.println("It must be between 8 and 16 letters.");
			System.out.println("It must contain 1 number.");
			
			password = keyboard.nextLine();
			
			}while (isUserValid(password) == false);
		
		passwordSalt = password + salt;
		passwordHash = getAscii(passwordSalt);//converts the password to Ascii
		
		// Creates a new user object and adds it to credentials
		User users = new User(username, salt, passwordHash);
		credentials.add(users);  
		return credentials.size();
		
	}
	
	/**
	 * Loads in the string containing username, passwordHash, salt.
	 * Tokenizes then places them into the array list of Users.
	 * 
	 * @param path to csv containing Users.
	 * @return The amount of users perviously added.
	 * @throws FileNotFoundException
	 */
	
	public static int loadUsers(String path) throws FileNotFoundException {
		String input;
		String username;
		String passwordHash;
		String salt;
		User currentUser;
		String [] tokens = new String[3];
		File file = new File(path);
		Scanner reader = new Scanner(file);
	
		
		while (reader.hasNextLine()) {
			input = reader.nextLine();
			tokens = input.split(",");
			username = tokens[0];
			passwordHash = tokens[1];
			salt = tokens[2];
			currentUser = new User(username, passwordHash, salt);
			credentials.add(currentUser);
		}
		reader.close();
		return credentials.size();
	}
	
	/**
	 * Removes users from arraylist credentials.
	 * 
	 * Searches the array list based off the username.
	 * Checks to see if they can remove it based off knowing the password.
	 * Checks based off password hash.
	 * 
	 */
	
	public static void removeUser() {
		String usernameSearch;
		String passwordSearch;// The password for the user they are trying to get rid of
		String passwordSalt;// Includes the searched password and the salt
		String passwordTest;// Stores the password hash for comparing
		User currentUser;
		
		System.out.println("Please input username of who you want to remove:");
		usernameSearch = keyboard.nextLine();
		
		// Loops to check it the username matches any  username in Credentials
		for(int i = 0; i < credentials.size(); i++) {
			currentUser = credentials.get(i);
			if(usernameSearch.equals(currentUser.username)) {
				System.out.println("Input the password");
				
				//Gives the person three chances to input the password
				for(int j = 3; j > 0; j--) {
					System.out.println("Input the password:");
					System.out.println("You have"+ j + "chances left.");
					passwordSearch = keyboard.nextLine();
					passwordSalt = passwordSearch + currentUser.getSalt();
					passwordTest = getAscii(passwordSalt);
					
					//Test that compares the passwords
					if(passwordTest.equals(currentUser.passwordHash)) {
						credentials.remove(i);// Removes the user if they get the password correct
						System.out.println("The User was removed.");
					}
				}
					
			}
		}
		
	}
	
	/**
	 * Checks if the password input is valid.
	 * It must be between 8 and 16 characters.
	 * Cannot contain the word password.
	 * Must contain a digit.
	 * 
	 * @param password
	 * @return
	 */
	public static boolean isUserValid(String password) {
		boolean valid = true;
		boolean containsDigit = false;
		
		// Checks if the password contains the word password.
		if (password.toLowerCase().contains("password")) {
			valid = false;
			
		// Checks the length of the password 
		}else if(password.length() < 8 || password.length() > 16) {
			valid = false;
		}
		
		// Makes sure that the password contains a number
		if (valid == true) {
			for (int i = 0; i < password.length(); i++) {
				if (Character.isDigit(password.charAt(i))) {
					containsDigit = true;
				}
			}
		}
		valid = containsDigit;
		
		return valid;
		
	}
	
	/**
	 * The user signs in by checking for the username.
	 * Then Compares the password Asciis.
	 * 
	 */
	public static void signIn() {
		
		String usernameSearch;
		String passwordSearch;
		String passwordSalt;
		String passwordTest;
		User currentUser;
		
		System.out.println("Input your Username");
		usernameSearch = keyboard.nextLine();
		
		//checks to see if the username is in credentials
		for (int i = 0; i < credentials.size(); i++) {
			currentUser = credentials.get(i);
			if (usernameSearch.equals(currentUser.getUsername())) {
				System.out.println("Input your Password");
				
				//Gives the user three tries on the password
				for(int j = 3; j > 0; j--) {
					passwordSearch = keyboard.nextLine();
					passwordSalt = passwordSearch + currentUser.getSalt();
					passwordTest = getAscii(passwordSalt);
					if(passwordTest.equals(currentUser.passwordHash)) {//Compares the two hashes
						System.out.println("Thank you for signing in!");
						break;
				}else {
					System.out.println("Incorrect password you get " + j +" more tries.");
				}
				
				}
			}
		}
		
	}
	
	/**
	 * It creates a salt by choosing a random character from the string.
	 * Then concatenates all the random characters to create the salt.
	 * @return salt that is added to the password.
	 */
	public static String generateSalt() {
		String salt = "";
		int saltLength = 4;
		Random rand = new Random();
		String special = "@#$%&!";// string of special characters
		
		for (int i = 0; i < saltLength; i++) {
			int randomCharacter = rand.nextInt(6);
			
			salt = salt + special.charAt(randomCharacter);
		}
		return salt;
	}
	
	/**
	 * Converts the string input to Ascii and iterates it by one.
	 * 
	 * @param password string containing an approved password
	 * @return The now hashed password
	 */
	
	public static String getAscii(String password) {
		String passwordHash;
		char hashToChar;
		int ascii;
        
        // Create a int array for the Ascii
        int[] asciiHash = new int[password.length()];
        
        // Converts password to char array
        char[] stringAsChars = password.toCharArray();
        char[] hashAsChars = new char[password.length()];
        
        
        //Converts the char array into an int array
        for(int charAt = 0; charAt < stringAsChars.length; charAt++)
		{
        	ascii = stringAsChars[charAt];
        	
        	asciiHash[charAt] = ascii + 1;
		}
        
        //Converts it back into an char array but iterated 1 integer
        for (int i = 0; i < asciiHash.length; i++) 
        {
        	hashToChar = (char) asciiHash[i];
        	
        	hashAsChars[i] = hashToChar;
        }
        
        //Converts the char array into a string
        passwordHash = new String(hashAsChars);
        return passwordHash;
        
	}
	/**
	 * Writes the users to a file.
	 * 
	 * @throws IOException
	 */
	public static void writeToFile() throws IOException {
		
		File file = new File("User.csv");
		FileWriter fw = new FileWriter(file, true);
		PrintWriter outputFile = new PrintWriter(fw);
		User currentUser;

		// Loops writing the users back to file
		for(int i = 0; i< credentials.size(); i++) {
			currentUser = credentials.get(i);
			outputFile.println(currentUser.username + ","+ currentUser.passwordHash + ","
					+ currentUser.salt);

		}
		
		// Closing the file and Scanner
		outputFile.close();
		keyboard.close();
	}
}
