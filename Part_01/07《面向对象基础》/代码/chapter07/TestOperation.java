package com.qf.chap07_1;

public class TestOperation {
	public static void main(String[] args) {
		//创建Operation
		Operation ope=new Operation();
		//调用方法
		ope.show();
		ope.show(100);
		ope.show("小张");
		
		ope.show(200, "hello");
	}
}
