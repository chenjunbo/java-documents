package com.qf.chap17_2;

public class Mouse implements Usb{

	@Override
	public void service() {
		System.out.println("鼠标开始工作了.....");
	}

}
