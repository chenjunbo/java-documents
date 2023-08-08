// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MyAnnotation.java

package com.qf.chap17_5;

import java.lang.annotation.Annotation;

public interface MyAnnotation
	extends Annotation
{

	public abstract String name();

	public abstract int age();
}
