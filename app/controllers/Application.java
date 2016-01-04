package controllers;

import play.*;
import play.data.validation.*;
import play.mvc.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import utils.*;

import org.apache.commons.io.FileUtils;

import models.*;

public class Application extends Controller {

    /*注册页面*/
    public static void register(String userid,String password,String name){
        render();
    }

    /*登录页面*/
    public static void login(){
        render();
    }

    /*注册页面*/
    public static void addUser(String userid,String password,String name){
        if(User.findById(userid)!=null){
            validation.addError("errorMsg", "用户名已存在!");
            renderTemplate("Application/register.html");
        }
        else{
            User newUser = new User(userid,password,name);
            newUser.save();
            validation.addError("errorMsg", "注册成功!");
            renderTemplate("Application/register.html");
        }
    }

    /*主页面*/
    public static void index(String username,String password) {
        User user = User.connect(username,password);
        if(user!=null){
    	   List<Presentation> slideList = Presentation.getAllSlideList();
    	   session.put("crtUser", user.userid);
            render(user,slideList);
        }
         else{
    	   validation.addError("errorMsg", "用户名或密码错误!");
            renderTemplate("Application/login.html");
        }
    }

    /*游客登录*/
    public static void visitor(){
        index("visitor","visitor");
    }

    public static void uploadFile(File pptFile){
        String userId = session.get("crtUser");
        int presId = Presentation.getNumByUserId(userId);
        String folder = userId+"/"+presId;
    	if(pptFile != null){
    		String targetPath = "upload/"+pptFile.getName();
    		File targetFile = new File(targetPath);
    		if(targetFile.exists())
    			targetFile.delete();
        	try {
				FileUtils.moveFile(pptFile,targetFile);
				Presentation newSlide = new Presentation(User.findById(userId),pptFile.getName(),"assets/"+folder+"/presentation.json");
				newSlide.save();
				Poi.analysis(targetPath,folder,newSlide.id);
                renderJSON(newSlide);
            }
        	catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	else{
    		System.out.println("file is null!");
    	}
    }

    public static void downloadFile(String assetsSrc){
        utils.FileUtils.compress(assetsSrc.substring(0,assetsSrc.lastIndexOf("/")),"upload/demo.osl");
    }
}
