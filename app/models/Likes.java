package models;

import play.db.ebean.*;
import javax.persistence.*;
import com.avaje.ebean.*;
import java.lang.String;

import play.data.validation.Constraints.*;

@Entity
public class Likes extends Model {

	@Id
	public long id;
	public int likes = 0;
	public int dislikes = 0;
	
	public void Likes() {
		System.out.println("CALLED LIKES CONSTRUCTOR");
	}
	
	public void addLike() {
		likes++;
	}
	
	public void addDislike() {
		dislikes++;
	}
	
}