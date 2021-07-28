import java.net.*;
import java.io.*;
import java.util.*;

public class FClient {

	public static void main(String[] args) {

		DatagramSocket cs = null;
		FileOutputStream fos = null;

		try {

			cs = new DatagramSocket();
			cs.setSoTimeout(3000);

			byte[] rd, sd;
			String reply;
			DatagramPacket sp, rp;
			int count = 0;
			boolean end = false;

			// write received data into demoText1.html
			fos = new FileOutputStream("demoText1.html");

			while (!end) {
				count = count > 9 ? 0 : count;
				String ack = "" + count;

				// send ACK
				sd = ack.getBytes();
				sp = new DatagramPacket(sd, sd.length, InetAddress.getByName(args[0]), Integer.parseInt(args[1]));
				cs.send(sp);

				try {
					// get next consignment
					rd = new byte[600];
					rp = new DatagramPacket(rd, rd.length);
					cs.receive(rp);

					// concat consignment
					reply = new String(rp.getData());
					String[] reply_array = new String[4];

					int i = 0;
					reply_array[0] = reply.substring(0, i += 3);
					reply_array[1] = reply.substring(4, 5);
					reply_array[2] = reply.substring(6, 518);
					reply_array[3] = reply.substring(519, 522);

					System.out.println(reply_array[0] + "\n");
					System.out.println("Consignment " + reply_array[1] + "\n");
					System.out.println(reply_array[2] + "\n");
					System.out.println(reply_array[3] + "\n");

					fos.write(reply_array[2].getBytes());

					if (reply_array[3].trim().equals("END")) // if last consignment
						end = true;

					count++;
				} catch (SocketTimeoutException ex) {
					System.out.println("Timed out! ");
				}
			}

		} catch (IOException ex) {
			System.out.println(ex.getMessage());

		} finally {

			try {
				if (fos != null)
					fos.close();
				if (cs != null)
					cs.close();
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
}