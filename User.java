/**
 * @author Alec Quiroga
 *
 * This has the setters and getters for each of the saved variables.
 */
public class User {
	
	/**
	 * Variables 
	 */
	
	String userId;
	String username;
	String salt;
	String passwordHash;
	
	/**
	 * Creates instance of user
	 * @param username
	 * @param salt
	 * @param password
	 */
	
	public User (String username, String salt, String password) {
		
		
		setPasswordHash(password);
		setSalt(salt);
		setUsername(username);
		
		
		
	}
	
	// Sets the input passwordHash to be save to the global variable passwordHash.
	public void setPasswordHash(String passwordHash){
		
		this.passwordHash = passwordHash;
	}
	
	// Returns the password hash to be used in 
	public String getPasswordHash() {
		
		return passwordHash;
	}
	
	// Sets the salt that was created in Authenticator
	public void setSalt(String salt) {
		
		this.salt = salt;
	}
	
	// Returns the salt previously set.
	public String getSalt() {
		
		return salt;
	}
	
	// Sets the username input in Authenticator
	public void setUsername(String username) {
		
		this.username = username;
	}
	
	// Returns the username input by the user.
	public String getUsername() {
		
		return username;
	}
	

		
}
	
