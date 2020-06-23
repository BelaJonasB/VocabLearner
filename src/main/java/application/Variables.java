package application;


public class Variables {
	private static String mail;
	private static Vocab[]  usersVocab;


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
