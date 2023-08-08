// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Season.java

package com.qf.chap17_4;


public final class Season extends Enum
{

	public static final Season SPRING;
	public static final Season SUMMER;
	public static final Season AUTUMN;
	public static final Season WINTER;
	private static final Season ENUM$VALUES[];

	private Season(String s, int i)
	{
		super(s, i);
	}

	public static Season[] values()
	{
		Season aseason[];
		int i;
		Season aseason1[];
		System.arraycopy(aseason = ENUM$VALUES, 0, aseason1 = new Season[i = aseason.length], 0, i);
		return aseason1;
	}

	public static Season valueOf(String s)
	{
		return (Season)Enum.valueOf(com/qf/chap17_4/Season, s);
	}

	static 
	{
		SPRING = new Season("SPRING", 0);
		SUMMER = new Season("SUMMER", 1);
		AUTUMN = new Season("AUTUMN", 2);
		WINTER = new Season("WINTER", 3);
		ENUM$VALUES = (new Season[] {
			SPRING, SUMMER, AUTUMN, WINTER
		});
	}
}
