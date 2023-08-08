package com.qf.chapter12_2;

public class MyInterfaceImpl implements MyInterface<String> {

	@Override
	public String server(String t) {
		// TODO Auto-generated method stub
		System.out.println(t);
		return t;
	}

}
