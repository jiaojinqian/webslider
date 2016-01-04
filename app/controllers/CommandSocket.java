package controllers;

import java.util.ArrayList;
import java.util.List;

import play.Logger;
import play.mvc.*;
import play.mvc.Http.Inbound;
import play.mvc.Http.Outbound;
import play.mvc.Http.WebSocketClose;
import play.mvc.Http.WebSocketEvent;
import play.mvc.Http.WebSocketFrame;

public class CommandSocket extends WebSocketController{
	public static List<Outbound> socketList = new ArrayList();
	public static void interactive(String identity){
		socketList.add(outbound);
		while(inbound.isOpen()){
			WebSocketEvent e = await(inbound.nextEvent());
			if(e instanceof WebSocketFrame){
				WebSocketFrame frame = (WebSocketFrame)e;
				/*socketList.add(frame);*/
				if(!frame.isBinary){
					if(frame.textData.equals("quit")){
						outbound.send("Bye!");
						disconnect();
					}
					else if(frame.textData.equals("command!")){
						for(int i=0;i<socketList.size();i++){
							if(socketList.get(i)!=outbound)
								socketList.get(i).send("成功发送了");
						}
					}
					else if(frame.textData.startsWith("jumpTo:")){
						if(identity.equals("1")){
							for(int i=0;i<socketList.size();i++){
								if(socketList.get(i)!=outbound)
									socketList.get(i).send(frame.textData);
							}
						}
					}
					else if(frame.textData.equals("fullScreen")){
						if(identity.equals("1")){
							for(int i=0;i<socketList.size();i++){
								if(socketList.get(i)!=outbound)
									socketList.get(i).send(frame.textData);
							}
						}	
					}
				}
			}
			if(e instanceof  WebSocketClose){
				Logger.info("Socket closed!");
				socketList.remove(outbound);
			}
		}
	}
}
