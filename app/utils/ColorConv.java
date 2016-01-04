package utils;

import java.awt.Color;

public class ColorConv extends Color{
	public ColorConv(int r,int g,int b){
		super(r,g,b);
	}

	public String getHex(){
		return toHex(getRed(),getGreen(),getBlue());
	}

	public static String toHex(int r,int g,int b){
		return "#"+toBrowserHexValue(r)+toBrowserHexValue(g)+toBrowserHexValue(b);
	}

	private static String toBrowserHexValue(int number){
		StringBuilder builder = new StringBuilder(Integer.toHexString(number&0xff));
		while(builder.length()<2){
			builder.append("0");
		}
		return builder.toString().toUpperCase();
	}
}
