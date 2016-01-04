package models;

import java.util.*;

import javax.persistence.*;


import play.db.DB;
import play.db.jpa.*;
import play.data.validation.*;

@Entity
@Table(name="presentation")
public class Presentation extends Model{

	@Required
	public String presName;

	@Required
	public String uploadAt;

	@Required
	public String presPath;

	@ManyToOne
	@Required
	public User user;

	public Presentation(User user,String presName,String presPath){
		this.user = user;
		this.presName = presName;
		this.uploadAt = utils.FileUtils.getCrtDate();
		this.presPath = presPath;
	}

	public static List<Presentation> getSlideListById(String userid){
		return Presentation.find("byUser", User.findById(userid)).fetch();
	}

	public static List<Presentation> getAllSlideList(){
		return Presentation.findAll();
	}

	public static int getNumByUserId(String userId){
		return (int) (Presentation.count()+1);
	}
}
