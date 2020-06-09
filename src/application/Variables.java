package application;

public class Variables {
	private static String mailShow;

	public static String getMailShow() {
		return mailShow;
	}

	public static void setMailShow(Object m) {
		mailShow = m.toString();
	}
}
