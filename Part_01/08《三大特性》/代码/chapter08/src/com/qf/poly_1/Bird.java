package com.qf.poly_1;

public class Bird extends Animal{
	//毛色
	String furColor;
	
	//飞
	public void fly() {
		System.out.println("鸟儿开始飞了...");
	}
	
	@Override
	public void eat() {
		System.out.println("鸟儿开始吃虫子....");
	}
}
