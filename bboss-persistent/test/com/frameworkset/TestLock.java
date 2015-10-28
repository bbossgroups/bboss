package com.frameworkset;


public class TestLock {
	static Deadlocker syncTest = new Deadlocker();

	public static void main(String[] args) {
		ThreadLock t = new ThreadLock(1);
		t.start();
		ThreadLock t1 = new ThreadLock(2);
		t1.start();

	}

	static class ThreadLock extends Thread {
		int num;

		public ThreadLock(int i) {
			this.num = i;
		}

		public void run() {
			if (num == 1) {
				while (true) {
					syncTest.method1(1);
				}
			} else {
				while (true) {
					syncTest.method2(1);
				}
			}
		}
	}

	static class Deadlocker {
		int field_1;

		private Object lock_1 = new int[1];

		int field_2;

		private Object lock_2 = new int[1];

		public void method1(int value) {
			synchronized (lock_1) {
				synchronized (lock_2) {
					field_1 = 0;
					field_2 = 0;
					System.out.println("call method1");
				}
			}
		}

		public void method2(int value) {
			synchronized (lock_2) {
				synchronized (lock_1) {
					field_1 = 0;
					field_2 = 0;
					System.out.println("call method2");
				}
			}
		}
	}

}
