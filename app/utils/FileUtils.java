package utils;

import java.io.File;
import java.util.Date;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

public class FileUtils {
	public static void compress(String srcPathName,String pathName){
		File zipFile = new File(pathName);
		File srcdir = new File(srcPathName);
		if(!srcdir.exists())
			throw new RuntimeException(srcPathName+"不存在!");

		Project prj = new Project();
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(zipFile);
		FileSet fileSet = new FileSet();
		fileSet.setProject(prj);
		fileSet.setDir(srcdir);
		zip.addFileset(fileSet);

		zip.execute();
	}

	public static String getModifiedTime(String filePath){
		File file = new File(filePath);
		long modifiedTime = file.lastModified();
		Date d = new Date(modifiedTime);
		return (new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm")).format(d);
	}

	public static String getCrtDate(){
		Date crtTime = new Date();
		return (new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm")).format(crtTime);
	}
}
