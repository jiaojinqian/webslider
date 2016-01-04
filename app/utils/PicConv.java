package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.wmf.tosvg.WMFTranscoder;

public class PicConv {

	public static boolean transcode(String wmfFile){
		WMFTranscoder transcoder = new WMFTranscoder();
		TranscodingHints hints = new TranscodingHints();
		try{
			TranscoderInput wmf = new TranscoderInput(new FileInputStream("res/"+wmfFile+".wmf"));
			FileOutputStream fos = new FileOutputStream("testSVG.svg");
			TranscoderOutput svg = new TranscoderOutput(new OutputStreamWriter(fos,"UTF-8"));
			transcoder.transcode(wmf, svg);
			return PicConv.toJPG(wmfFile);
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean toJPG(String svgPath){
		JPEGTranscoder t = new JPEGTranscoder();
		t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(.8));
		try{
			TranscoderInput input = new TranscoderInput(new FileInputStream("testSVG.svg"));
			 // Create the transcoder output.
	        OutputStream ostream = new FileOutputStream("res/"+svgPath+".jpg");
	        TranscoderOutput output = new TranscoderOutput(ostream);

	        // Save the image.
	        t.transcode(input, output);

	        // Flush and close the stream.
	        ostream.flush();
	        ostream.close();
	        File svgFile = new File("testSVG.svg");
	        svgFile.delete();
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
}
