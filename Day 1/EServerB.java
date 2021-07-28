import java.io.*;
import java.net.*;

public class EServerB
{
 	public static void main(String[] args) throws IOException 
 	{

  		ServerSocket serverSocket = new ServerSocket(3000);

  		while(true)
  		{
   			Socket client = serverSocket.accept();
			boolean quit= false;
   			InputStream in = client.getInputStream();
			
   			DataInputStream dis = new DataInputStream(in);
			String msg;

			System.out.println("Server is online...");
			while(!quit)
            {
                msg=dis.readUTF();
                System.out.println(msg);
                if(msg.contains("BYE")){
                    quit=true;
                    System.out.println("Quiting...");
                }
            }

   			client.close();

  		}

 	}
}