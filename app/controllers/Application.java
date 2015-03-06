package controllers;

import play.*;
import play.mvc.*;
import models.*;
import play.data.*;
import views.html.*;
import  play.mvc.Http.Session;
import  play.mvc.Http;
import static play.data.Form.*;
import java.util.List;
import java.util.ArrayList;

public class Application extends Controller {

	private static List<Scholarship> scholarships;
	private static List<Scholarship> userScholarships;

	private static Form<Scholarship> scholarshipForm = Form.form(Scholarship.class);
	private static Form<User> userForm = Form.form(User.class);

	static Session session = Http.Context.current().session();

    public static Result index() {
		scholarships = Scholarship.find.all();
        return ok(index.render(scholarships));
    }

	public static Result logout() {
		return index();
	}

	public static Result login() {
		return ok(login.render(Form.form(Login.class), ""));
	}

	public static Result users() {
		return ok(users.render(User.find.findList()));
	}

	public static Result signup() {
		return ok(signup.render(userForm, ""));
	}

	public static Result profile() {
		createUserScholarshipsList();
		createScholarshipsList();
		return ok(profile.render(userScholarships, scholarships));
	}

	public static Result newUser() {
		Form<User> filledForm = userForm.bindFromRequest();
		if(filledForm.hasErrors()) {
			return badRequest(signup.render(userForm, "Form had errors."));
		} else {
			if(!filledForm.data().get("email").contains("@")) {
				return badRequest(signup.render(userForm, "Given credentials had errors."));
			}
			User user = User.create(filledForm.data().get("name"), filledForm.data().get("email"), filledForm.data().get("password"));
			if(user == null) {
				return badRequest(signup.render(userForm, "That email is already in use"));
			}
			return ok(index.render(scholarships));
		}
	}

	public static Result newScholarship() {
		Form<Scholarship> filledForm = scholarshipForm.bindFromRequest();
		if(filledForm.hasErrors()) {
			return badRequest(profile.render(userScholarships, scholarships));
		} else {
			Scholarship newScholarship = Scholarship.create(filledForm.data().get("school"), filledForm.data().get("description"), filledForm.data().get("amount"));
			scholarships.add(newScholarship);
			createUserScholarshipsList();
			createScholarshipsList();
			return ok(profile.render(userScholarships, scholarships));
		}
	}

	public static Result editScholarship(Long id) {
		Form<Scholarship> filledForm = scholarshipForm.bindFromRequest();
		if(filledForm.hasErrors()) {
			return badRequest(profile.render(userScholarships, scholarships));
		} else {
			Scholarship.edit(id, filledForm.data().get("school"), filledForm.data().get("description"), filledForm.data().get("amount"));
			scholarships = Scholarship.find.all();
			createUserScholarshipsList();
			createScholarshipsList();
			return ok(profile.render(userScholarships, scholarships));
		}
	}

	public static Result editPageScholarship(Long id) {
		Scholarship scholarship = Scholarship.find.ref(id);
		return ok(scholarshipEdit.render(scholarship));
	}

	public static Result deleteScholarship(Long id) {
		Scholarship scholarship = Scholarship.find.ref(id);
		for(int i = 0; i < scholarship.users.size(); i++) {
			if(scholarship.users.get(i).email.equals(session.get("email"))) {
				scholarship.users.remove(i);
				scholarship.save();
				break;
			}
		}
		scholarships = Scholarship.find.all();
		createUserScholarshipsList();
		createScholarshipsList();
		return ok(profile.render(userScholarships, scholarships));
	}

	public static Result addToScholarships(Long id) {
		Scholarship scholarship = Scholarship.find.ref(id);
		User user = User.find.where().eq("email", session.get("email")).findUnique();
		scholarship.users.add(user);
		scholarship.save();
		createUserScholarshipsList();
		createScholarshipsList();
		return ok(profile.render(userScholarships, scholarships));
	}

	public static Result addLike(Long id) {
		Scholarship scholarship = Scholarship.find.ref(id);
		scholarship.likes++;
		scholarship.save();
		createUserScholarshipsList();
		createScholarshipsList();
		return ok(profile.render(userScholarships, scholarships));
	}

	public static Result addDislike(Long id) {
		Scholarship scholarship = Scholarship.find.ref(id);
		scholarship.dislikes++;
		scholarship.save();
		createUserScholarshipsList();
		createScholarshipsList();
		return ok(profile.render(userScholarships, scholarships));
	}

	public static void createUserScholarshipsList() {
		userScholarships = new ArrayList<Scholarship>();
		scholarships = Scholarship.find.all();
		for(int i = 0; i < scholarships.size(); i++) {
			for(int z = 0; z < scholarships.get(i).users.size(); z++) {
				if(scholarships.get(i).users.get(z).email.equals(session.get("email"))) {
					userScholarships.add(scholarships.get(i));
					break;
				}
			}
		}
	}

	public static void createScholarshipsList() {
		List<Scholarship> tempScholarships = Scholarship.find.findList();
		scholarships.clear();
		for(int i = 0; i < tempScholarships.size(); i++) {
			if(tempScholarships.get(i).users.size() <= 0) {
				scholarships.add(tempScholarships.get(i));
				continue;
			}
			for(int z = 0; z < tempScholarships.get(i).users.size(); z++) {
				if(!tempScholarships.get(i).users.get(z).email.equals(session.get("email"))) {
					scholarships.add(tempScholarships.get(i));
					break;
				}
			}
		}
	}

	public static Result authenticate() {
		Form<Login> loginForm = Form.form(Login.class);
		Form<Login> filledForm = null;
		try {
			filledForm = loginForm.bindFromRequest();
		} catch(Exception e) {
			return badRequest(login.render(loginForm, "Invalid email or password"));
		}
		if(filledForm.hasErrors()) {
			return badRequest(login.render(loginForm, "Form had errors."));
		} else {
			if(User.authenticate(filledForm.data().get("email"), filledForm.data().get("password")) == null) {
				return badRequest(login.render(loginForm, "Invalid email or password."));
			}
			session.clear();
			User user = User.find.ref(filledForm.get().email);
			session.put("email", user.email);
			session.put("name",user.name);
			session.put("password", user.password);
			return redirect(routes.Application.profile());
		}
	}

	public static class Login {
		public String email;
		public String password;

		public String validate() {
			if(User.authenticate(email, password) == null) {
				return "Invalid email or password";
			}
			System.err.println("Authenticated");
			return null;
		}
	}

}
