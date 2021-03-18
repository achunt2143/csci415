
/*****
 **
 **  USCA ACSC492F
 **
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class EmailClient {

	Socket clientSocket;
	Scanner In;
	DataOutputStream Out;

	// A email sender
	public EmailClient(String Host, int Port) {

		try {

			// Establish a TCP connection with the mail server.
			clientSocket = new Socket(Host, Port);

			// Create a Scanner to read a line at a time.
			In = new Scanner(clientSocket.getInputStream());

			// Get a reference to the socket's output stream.
			Out = new DataOutputStream(clientSocket.getOutputStream());

			// Read greeting from the server.
			String response = In.nextLine();
			System.out.println(response);
			if (!response.startsWith("220")) {
				throw new Exception("220 reply not received from server. ");
			}

			// Send HELO command and get server response.
			String message = "HELO usca.edu\r\n";
			System.out.print(message);
			Out.writeBytes(message);
			response = In.nextLine();
			System.out.println(response);
			if (!response.startsWith("250")) {
				throw new Exception("250 reply not received from server. Handshaking failed.");
			}

			// Send MAIL FROM command.
			message = "MAIL FROM: cindy@usca.edu\r\n";
			System.out.print(message);
			Out.writeBytes(message);
			response = In.nextLine();
			if (!response.startsWith("250")) {
				throw new Exception("250 reply not received from server. MAIL FROM not accepted");
			}

			// Send RCPT TO command.
			message = "RCPT TO: ach6@usca.edu\r\n";
			System.out.print(message);
			Out.writeBytes(message);
			response = In.nextLine();
			if (!response.startsWith("250")) {
				throw new Exception("250 reply not received from server. RCPT TO not accepted");
			}

			// Send DATA command.
			message = "DATA\r\n";
			System.out.print(message);
			Out.writeBytes(message);
			response = In.nextLine();
			if (!response.startsWith("354")) {
				throw new Exception("354 reply not received from server. DATA not accepted");
			}

			// Send message data.
			message = "From: cindy@usca.edu\nTo: ach6@usca.edu\nSubject: Hello\n\nTHIS IS DATA IN THE MAIL\r\n.\r\n";
			System.out.print(message);
			Out.writeBytes(message);
			response = In.nextLine();
			if (!response.startsWith("250")) {
				throw new Exception("250 reply not received from server. QUEUED DATA not accepted");
			}

			// Send QUIT command.
			message = "QUIT\r\n";
			System.out.print(message);
			Out.writeBytes(message);
			response = In.nextLine();
			if (!response.startsWith("221")) {
				throw new Exception("221 reply not received from server. QUIT not accepted");
			}

			// close socket and all streams
			if (clientSocket != null)
				clientSocket.close();
			if (In != null)
				In.close();
			if (Out != null)
				Out.close();

		} catch (Exception e) {
			System.err.print(e.getMessage());
		}

	}

	// Send message and get response from mail server
	public String send(String message) throws IOException {
		System.out.print(message);
		Out.writeBytes(message);
		String response = In.nextLine();
		System.out.println(response);
		return response;
	}

	public static void main(String argv[]) throws Exception {
		EmailClient p = new EmailClient("129.252.199.151", 25);
	}
}
