package ui;

import client.HttpServiceClient;

public class LabaRegister {

	private HttpServiceClient http;
	public LogingIn logIn;

	public LabaRegister(HttpServiceClient http) {
		this.http=http;
		logingIn();
	}

	// ���������
	private void logingIn() {
//		UsersRegister reg = new UsersRegister();
		logIn = new LogingIn(this,http);
//		User u1 = new User("user", "1234");
//		reg.addUser(u1);
		logIn.logIn();

	}

	// ��������� ���� ��������
	public void openMenu() {
		MenuProgram menu = new MenuProgram(http);

	}

}
