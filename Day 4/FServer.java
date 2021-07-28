import java.net.*;
import java.io.*;
import java.util.*;

public class FServer {

	public static void main(String[] args) {

		DatagramSocket ss = null;
		FileInputStream fis = null;
		DatagramPacket rp, sp;
		byte[] rd, sd;

		InetAddress ip;
		int port;

		try {
			ss = new DatagramSocket(Integer.parseInt(args[0]));
			System.out.println("Server is up....");

			// read file into buffer
			fis = new FileInputStream("demoText.html");

			int consignment = 0;
			String strConsignment;
			int result = 0; // number of bytes read

			// handshake
			rd = new byte[100];
			rp = new DatagramPacket(rd, rd.length);
			ss.receive(rp);
			String[] hs = new String(rp.getData()).split(" ");
			System.out.println("Recieved request for file: " + hs[1] + hs[2]);
			ip = rp.getAddress();
			port = rp.getPort();
			System.out.println("Client IP Address = " + ip);
			System.out.println("Client port = " + port + "\n");

			while (true && result != -1) {
				ss.setSoTimeout(3000);
				rd = new byte[100];
				sd = new byte[512];

				rp = new DatagramPacket(rd, rd.length);

				strConsignment = consignment++ + "";

				// Random random = new Random();
				// int chance = random.nextInt(100);

				// if (((chance % 2) == 0)) {
				// prepare data
				result = fis.read(sd);
				byte[] arr1 = new String("RDT ").getBytes();
				byte[] arr2 = new String(strConsignment.trim() + " ").getBytes();
				byte[] arr3 = new String(" END").getBytes();
				byte[] arr4 = new String(" \r\n").getBytes();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				baos.write(arr1);
				baos.write(arr2);
				baos.write(sd);

				if (fis.available() == 0) {
					baos.write(arr3);
					consignment = -1;
					result = -1;
				}

				baos.write(arr4);

				byte[] final_sd = baos.toByteArray();
				sp = new DatagramPacket(final_sd, final_sd.length, ip, port);

				ss.send(sp);
				System.out.println("Sent consignment :" + consignment);

				// } else {
				// System.out.println("\nFailed to send Consignment #" + consignment);
				// }
				try {
					ss.receive(rp);

					System.out.println("Client ACK = " + new String(rp.getData()).split(" ")[1]);

					rp = null;
					sp = null;

				} catch (SocketTimeoutException ex) {
					System.out.println("Forgot acknowledgement : " + consignment);
					ss.send(sp);
					System.out.println("Sent consignment :" + consignment + " again!\n");
				}
			}

		} catch (IOException ex) {
			System.out.println(ex.getMessage());

		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}

	}
}
