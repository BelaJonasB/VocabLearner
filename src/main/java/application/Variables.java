package application;


public class Variables {
	private static String mail,pw;
	private static boolean remember;

	public static boolean isRemember() {
		return remember;
	}

	public static void setRemember(boolean remember) {
		Variables.remember = remember;
	}

	public static String getMail() {
		return mail;
	}

	public static void setMail(String mail) {
		Variables.mail = mail;
	}

	public static String getPw() {
		return pw;
	}

	public static void setPw(String pw) {
		Variables.pw = pw;
	}
}
