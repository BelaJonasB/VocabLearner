package application;


public class Variables {
	private static String mail, server;
	private static Vocab[]  usersVocab;

	public static String getServer() {
		return server;
	}
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



}
