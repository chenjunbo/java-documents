// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Teacher.java

package com.qf.chap07_1;

import java.io.PrintStream;

public class Teacher
{

	String name;
	int age;

	public Teacher()
	{
		name = "уехЩ";
		age = 20;
		System.out.println(name);
		System.out.println(age);
	}

	public void say()
	{
		System.out.println((new StringBuilder(String.valueOf(name))).append("-----").append(age).toString());
	}
}
