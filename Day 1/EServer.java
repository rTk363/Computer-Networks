import java.io.*;
import java.net.*;

public class EServer
{
 	public static void main(String[] args) throws IOException 
 	{

  		ServerSocket serverSocket = new ServerSocket(3000);

  		while(true)
  		{
   			Socket client = serverSocket.accept();

   			InputStream in = client.getInputStream();

   			DataInputStream dis = new DataInputStream(in);

   			System.out.println(dis.readUTF());

   			client.close();

  		}

 	}
}
