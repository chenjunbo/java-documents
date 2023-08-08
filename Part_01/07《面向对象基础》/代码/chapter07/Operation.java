package com.qf.chap07_1;
/**
 * 方法重载
 * (1)方法名相同
 * (2)参数列表不同(类型、个数、顺序)
 * (3)和返回值、修饰符无关
 * @author wgy
 *
 */
public class Operation {
	
	public void show() {
		System.out.println("无参方法");
	}
	
//	public void show(int num) {
//		System.out.println("一个int类型参数:"+num);
//	}
	
	public void show(double num) {
		System.out.println("一个double类型参数:"+num);
	}
	
	
	public void show(String name) {
		System.out.println("一个String类型参数:"+name);
	}
	
	public void show(int num,String name) {
		System.out.println("两个参数 int String"+num+"---"+name);
	}
	
	public void show(String name,int num) {
		System.out.println("两个参数 String int"+name+"---"+num);
	}
	
}
