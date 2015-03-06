package models;

import play.db.ebean.*;
import javax.persistence.*;
import com.avaje.ebean.*;
import java.lang.String;

import play.data.validation.Constraints.*;

@Entity
public class User extends Model {

	@Required
	public String name;
	@Id
	@Required
	public String email;
	@Required
	public String password;
	
	public static Finder<String, User> find = new Finder<String, User>(String.class, User.class);


	public User(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}
	
	public static User create(String name, String email, String password) {
		if(find.where().eq("email", email).findUnique() == null) {
			User user = new User(name, email, password);
			user.save();
			return user;
		}
		return null;
	}
	
	public static User authenticate(String email, String password) {
		User user =  find.where().eq("email", email).eq("password", password).findUnique();
		System.err.println("found user " + user.email);
		return user;
	}
	
}