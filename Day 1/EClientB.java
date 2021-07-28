import java.io.*;
import java.net.*;
import java.util.*;

public class EClientB
{
 	public static void main(String[] args) throws IOException, InterruptedException
 	{
  		Socket client = new Socket("localhost",3000);
  		BufferedReader buff = new BufferedReader(new InputStreamReader (System.in));
  		OutputStream out = client.getOutputStream();
  		DataOutputStream dos = new DataOutputStream(out);
        boolean quit=false;

        while(!quit)
        {
            System.out.print("> ");
            String message = buff.readLine();
            dos.writeUTF("Client Say :: " + message);
  		    dos.flush();

            if(message.equals("BYE")){
                quit=true;
                Thread.sleep(500);
            }
        }

  		client.close();
 	}
}