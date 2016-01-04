package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
@Table(name="user")
public class User extends Model {

	@Required
	public String userid;
	
	@Required
	public String password;
	
	@Required
	public String nickname;
	
	@Required
	public int identity;

	public User(String userid,String password,String nickname){
		this.userid = userid;
		this.password = password;
		this.nickname = nickname;
		this.identity = 0;
	}

	public static User connect(String userid,String password){
		return find("byUseridAndPassword",userid,password).first();
	}
	
	public static User findById(String userid){
		return find("byUserid",userid).first();
	}
}