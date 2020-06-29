package application;


public class Variables {
	private static String mail, server;
	private static Vocab[]  usersVocab;
	private static double x,y;

	public static String getServer() { return server; }
	public static void setServer(String server) {
		Variables.server = server;
	}

	public static Vocab[] getUsersVocab() {
		return usersVocab;
	}
	public static void setUsersVocab(Vocab[] usersVocab) {
		Variables.usersVocab = usersVocab;
	}

	public static String getMail() {
		return mail;
	}
	public static void setMail(String mail) {
		Variables.mail = mail;
	}

	public static double getX() { return x; }
	public static void setX(double x) { Variables.x = x; }

	public static double getY() { return y; }
	public static void setY(double y) { Variables.y = y; }
}
