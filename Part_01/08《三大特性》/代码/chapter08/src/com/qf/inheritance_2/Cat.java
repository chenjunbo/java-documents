package com.qf.inheritance_2;

import com.qf.inheritance_1.Animal;

public class Cat extends Animal{
	
	String hobby;
	
	public void playBall() {
		System.out.println(this.hobby);
	}
}
