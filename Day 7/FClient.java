import java.net.*;
import java.io.*;
import java.util.*;

public class FClient {

	public static void main(String[] args) {

		DatagramSocket cs = null;
		FileOutputStream fos = null;

		boolean failed = false;
		try {

			cs = new DatagramSocket();
			cs.setSoTimeout(3000);

			byte[] rd, sd;
			String reply;
			DatagramPacket sp, rp;
			int count = 0;
			boolean end = false;

			// write received data into demoText1
			fos = new FileOutputStream(args[2].trim().split("\\.")[0] + "1." + args[2].trim().split("\\.")[1]);

			// handshake
			System.out.println("Requesting for file: " + args[2]);
			byte[] hs = new String("REQUEST " + args[2].trim() + " \r\n").getBytes();
			sp = new DatagramPacket(hs, hs.length, InetAddress.getByName(args[0]), Integer.parseInt(args[1]));
			cs.send(sp);

			while (!end) {
				count = count > 9 ? 0 : count;
				String ack = "ACK " + count + " \r\n";
				Random random = new Random();
				int chance = random.nextInt(4);

				try {
					// get next consignment
					rd = new byte[525];
					rp = new DatagramPacket(rd, rd.length);
					cs.receive(rp);

					// concat consignment
					reply = new String(rp.getData());
					String[] reply_array = new String[4];

					reply_array[0] = reply.substring(0, 3);
					reply_array[1] = reply.substring(4, 5);
					reply_array[2] = reply.substring(7, 519);
					reply_array[3] = reply.substring(519, 522);

					System.out.println("Consignment " + reply_array[1] + " recieved!\n");
					if (failed) {
						System.out.print(" -deleting duplicate file\n");
						failed = false;
						continue;
					}
					byte[] data = Arrays.copyOfRange(rp.getData(), 7, 519);
					fos.write(data);

					if ((chance != 1)) {
						// send ACK
						sd = ack.getBytes();
						sp = new DatagramPacket(sd, sd.length, InetAddress.getByName(args[0]),
								Integer.parseInt(args[1]));
						cs.send(sp);
						count++;

					} else {
						System.out.println("Failed to send acknowledgement " + ack);
						failed = true;
					}

					if (reply_array[3].trim().equals("END")) // if last consignment
						end = true;

				} catch (SocketTimeoutException ex) {
					System.out.println("Forgot Consignment: " + count + "\n");
				}
			}

		} catch (IOException ex) {
			System.out.println(ex.getMessage());

		} catch (Exception e) {
			System.out.println(e);
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