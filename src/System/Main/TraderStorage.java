package System.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class TraderStorage {
	private final static String FILEPATH = "systemLoginTraders.dat";
	private static ArrayList<TraderDetails> allUsers;
	private File usersFile = new File(FILEPATH);
	private PrintStream systemPw = new PrintStream(new AppendFileStream(FILEPATH));

	public TraderStorage() throws IOException {
		if (usersFile.createNewFile())
			usersFile = new File(FILEPATH);
		allUsers = getUsersFromFile(FILEPATH);
	}

	public boolean addUser(TraderDetails me) throws FileNotFoundException {
		if (!allUsers.contains(me)) {
			allUsers.add(me);
			saveUserToFile(me, systemPw);
			return true;
		}
		return false;
	}

	public boolean checkUser(String username, String password) {
		TraderDetails temp = new TraderDetails(username, password);
		Iterator<TraderDetails> iter = allUsers.iterator();
		while (iter.hasNext()) {
			temp = iter.next();
			if (temp.getUsername().equals(username)) {
				if (temp.getPassword().equals(password))
					return true;
				else
					return false;
			}
		}
		return false;
	}

	public static void saveUserToFile(TraderDetails me, PrintStream pw) throws FileNotFoundException {
		pw.append(me.getUsername() + " " + me.getPassword() + " " + me.getId() + " ");
	}

	public ArrayList<TraderDetails> getUsersFromFile(String fileName) throws FileNotFoundException {
		File f = new File(fileName);
		Scanner s = new Scanner(f);
		ArrayList<TraderDetails> allUsers = new ArrayList<TraderDetails>();
		while (s.hasNext()) {
			allUsers.add(new TraderDetails(s.next(), s.next(), s.nextInt()));
		}
		s.close();
		return allUsers;
	}
}

class AppendFileStream extends OutputStream {
	RandomAccessFile fd;

	public AppendFileStream(String file) {
		try {
			fd = new RandomAccessFile(file, "rw");
			fd.seek(fd.length());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() throws IOException {
		fd.close();
	}

	public void write(byte[] b) throws IOException {
		fd.write(b);
	}

	public void write(byte[] b, int off, int len) throws IOException {
		fd.write(b, off, len);
	}

	public void write(int b) throws IOException {
		fd.write(b);
	}
}
