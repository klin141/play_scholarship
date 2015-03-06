package models;

import play.db.ebean.*;
import javax.persistence.*;
import com.avaje.ebean.*;
import java.lang.String;
import java.util.List;
import java.util.ArrayList;

import play.data.validation.Constraints.*;

@Entity
public class Scholarship extends Model {

	@Id 
	public Long id;
	@Required
	public String school;
	@Required
	public String description;
	@Required
	public int amount;
	
	public int likes = 0;
	public int dislikes = 0;
	
	@ManyToMany(cascade = CascadeType.REMOVE)
	public List<User> users = new ArrayList<User>();
	
	public static Finder<Long, Scholarship> find = new Finder<Long, Scholarship>(Long.class, Scholarship.class);

	public Scholarship(String school, String description, int amount) {
		this.school = school;
		this.description = description;
		this.amount = amount;
	}
	
	public static Scholarship create(String school, String description, String amount) {
		Scholarship scholarship = new Scholarship(school, description, Integer.parseInt(amount));
		scholarship.save();
		return scholarship;
	}
	
	public static void edit(Long id, String school, String description, String amount) {
		Scholarship scholarship = find.ref(id);
		scholarship.school = school;
		scholarship.description = description;
		scholarship.amount = Integer.parseInt(amount);
		scholarship.save();
	}

}