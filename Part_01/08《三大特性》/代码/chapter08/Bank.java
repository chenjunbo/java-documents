package com.qf.encap_2;

import java.util.Scanner;

/**
 * 银行类
 * @author wgy
 *
 */
public class Bank {
	
	private User[] users=new User[5];
	private int size;
	
	public Bank() {
		initial();
	}
	//初始化方法
	public void initial() {
		User user1=new User();
		user1.setCardNo("6220088066001122");
		user1.setIdentity("1111222");
		user1.setUsername("曹操");
		user1.setPassword("123456");
		user1.setPhone("110");
		user1.setBalance(10000);
		
		User user2=new User("6220088066001133", "112312242", "吕布", "123456", "112", 20000);
		
		users[0]=user1;
		users[1]=user2;
		this.size=2;
	}
	
	
	//登录
	public void login() {
		Scanner input=new Scanner(System.in);
		System.out.println("请输入账号");
		String cardNo=input.next();
		System.out.println("请输入密码");
		String password=input.next();
		User u=null;
		for(int i=0;i<size;i++) {
			if(users[i].getCardNo().equals(cardNo)&&users[i].getPassword().equals(password)) {
				u=users[i];
				break;
			}
		}
		if(u!=null) {
			//显示菜单
			this.showMenu(u);
		}else {
			System.out.println("卡号或密码错误");
		}
	}
	
	//菜单
	public void showMenu(User u) {
		Scanner input=new Scanner(System.in);
		System.out.println("-------欢迎进入xxx银行系统-------");
		do {
			System.out.println("------1.存款  2.取款  3.转账  4.查询余额  5.修改密码 0.退出----");
			int choice = input.nextInt();
			switch (choice) {
			case 1:
				this.saveMoney(u);
				break;
			case 2:
				this.subMoney(u);
				break;
			case 3:
				this.transMoney(u);
				break;
			case 4:
				this.queryBanlance(u);
				break;
			case 5:
				
				break;
			case 0:
				return;
			default:
				break;
			}
		} while (true);
	}
	//存钱
	private void saveMoney(User u) {
		Scanner input=new Scanner(System.in);
		System.out.println("请输入存钱金额");
		double m=input.nextDouble();
		if(m>0) {
			u.setBalance(u.getBalance()+m);
			System.out.println("存钱成功,余额是:"+u.getBalance());
		}else {
			System.out.println("存钱失败");
		}
	}
	//取钱
	private void subMoney(User u) {
		Scanner input=new Scanner(System.in);
		System.out.println("请输入取钱金额");
		double m=input.nextDouble();
		if(m>0) {
			if(u.getBalance()>=m) {
				u.setBalance(u.getBalance()-m);
				System.out.println("取款成功,余额是:"+u.getBalance());
			}else {
				System.out.println("余额不足");
			}
		}else {
			System.out.println("取钱失败");
		}
	}
	//转载
	public void transMoney(User u) {
		Scanner input=new Scanner(System.in);
		System.out.println("请输入对方账户");
		String cardNo=input.next();
		System.out.println("请输入转账金额");
		double m=input.nextDouble();
		
		//查找对方账户
		User toUser=null;
		for(int i=0;i<size;i++) {
			if(users[i].getCardNo().equals(cardNo)){
				toUser=users[i];
				break;
			}
		}
		if(toUser!=null) {
			if(m>0) {
				if(u.getBalance()>=m) {
					//修改金额
					u.setBalance(u.getBalance()-m);
					toUser.setBalance(toUser.getBalance()+m);
					System.out.println("转账成功");
				}else {
					System.out.println("余额不足");
				}
			}else {
				System.out.println("转账金额有误");
			}
		}else {
			System.out.println("对方账户不存在");
		}
	}
	//查询余额
	public void queryBanlance(User u) {
		System.out.println("当前账户余额:"+u.getBalance());
	}
	
	
}
