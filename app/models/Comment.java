package models;

import play.db.ebean.*;
import javax.persistence.*;
import com.avaje.ebean.*;
import java.lang.String;

import play.data.validation.Constraints.*;

@Entity
public class Comment extends Model {

	public String comment;
	
	public Comment(String comment) {
		this.comment = comment;
	}

}