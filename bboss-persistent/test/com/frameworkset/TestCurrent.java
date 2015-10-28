package com.frameworkset;

import javax.transaction.RollbackException;

//import EDU.oswego.cs.dl.util.concurrent.FJTask;
//import EDU.oswego.cs.dl.util.concurrent.FJTaskRunnerGroup;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.orm.transaction.TransactionManager;

/**
 * java并发编程之并行分解合并
 * <p>Title: TestCurrent</p>
 *
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2009-6-1 下午08:58:51
 * @author biaoping.yin
 * @version 1.0
 */
public class TestCurrent {

	public static void main(String[] args) throws InterruptedException {
//		FibVL f = new FibVL(40, null);
//
//		System.out.println(f.number);
//		int groupSize = 20; // 2 worker threads
//		// compute fib(35)
//		FJTaskRunnerGroup group = new FJTaskRunnerGroup(groupSize);
//
//		group.invoke(f);
//		int result = f.number;
//		System.out.println("Answer: " + result);
		
//		for(int j = 0; j < 300; j ++)
//		{
//			TestCurrent i = new TestCurrent();
//			i.test(false);
//		}
		
		for(int j = 0; j < 300; j ++)
		{
			TestCurrent i = new TestCurrent();
			i.test(true);
		}

	}
	
	public void test(boolean usetx)
	{
		if(!usetx)
		{
			Task t = new Task();
			t.start();
		}
		else
		{
			TXTask t = new TXTask();
			t.start();
		}
			
	}
	static class Task extends Thread
	{

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			DBUtil db = new DBUtil();
		
			try {

				db.executeInsert("insert into TEST_CURRENT(id,name) values(10,'test')");
				
				db.executeInsert("insert into TEST_CURRENT(id,name) values(10,'test')");
				
				db.executeInsert("insert into TEST_CURRENT(id,name) values(10,'test')");
				
				db.executeInsert("insert into TEST_CURRENT(id,name) values(10,'test')");

			} catch (Exception e) {

				e.printStackTrace();
			}
			
		}
		
	}
	
	static class TXTask extends Thread
	{

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			DBUtil db = new DBUtil();
			TransactionManager tm = new TransactionManager();
			try {
				tm.begin(tm.RW_TRANSACTION);
				db.executeInsert("insert into TEST_CURRENT(id,name) values(10,'test')");
				
				db.executeInsert("insert into TEST_CURRENT(id,name) values(10,'test')");
				
				db.executeInsert("insert into TEST_CURRENT(id,name) values(10,'test')");
				
				db.executeInsert("insert into TEST_CURRENT(id,name) values(10,'test')");
				tm.commit();
			} catch (Exception e) {
				try {
					tm.rollback();
				} catch (RollbackException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			
		}
		
	}

}

//class Fib extends FJTask {
//	static final int sequentialThreshold = 13; // for tuning
//
//	volatile int number; // argument/result
//
//	Fib(int n) {
//		number = n;
//	}
//
//	int getAnswer() {
//		if (!isDone())
//			throw new IllegalStateException("Not yet computed");
//		return number;
//	}
//
//	int seqFib(int n) {
//		if (n <= 1)
//			return n;
//		else
//			return seqFib(n - 1) + seqFib(n - 2);
//	}
//
//	public void run() {
//		int n = number;
//
//		if (n <= sequentialThreshold) // base case
//			number = seqFib(n);
//		else {
//			Fib f1 = new Fib(n - 1); // create subtasks
//			Fib f2 = new Fib(n - 2);
//
//			coInvoke(f1, f2); // fork then join both
//
//			number = f1.number + f2.number; // combine results
//		}
//	}
//
//	public static void main(String[] args) { // sample driver
//		try {
//			int groupSize = 2; // 2 worker threads
//			int num = 35; // compute fib(35)
//			FJTaskRunnerGroup group = new FJTaskRunnerGroup(groupSize);
//			Fib f = new Fib(num);
//			group.invoke(f);
//			int result = f.getAnswer();
//			System.out.println("Answer: " + result);
//		} catch (InterruptedException ex) {
//		} // die
//	}
//}
//
//class FibVL extends FJTask {
//	int sequentialThreshold = 10;
//
//	volatile int number; // as before
//
//	final FibVL next; // embedded linked list of sibling tasks
//
//	FibVL(int n, FibVL list) {
//		number = n;
//		next = list;
//	}
//
//	int seqFib(int n) {
//		if (n <= 1)
//			return n;
//		else
//			return seqFib(n - 1) + seqFib(n - 2);
//	}
//
//	public void run() {
//		int n = number;
//		if (n <= sequentialThreshold)
//			number = seqFib(n);
//		else {
//			FibVL forked = null; // list of subtasks
//
//			forked = new FibVL(n - 1, forked); // prepends to list
//			forked.fork();
//
//			forked = new FibVL(n - 2, forked);
//			forked.fork();
//
//			number = accumulate(forked);
//		}
//	}
//
//	// Traverse list, joining each subtask and adding to result
//	int accumulate(FibVL list) {
//		int sum = 0;
//		for (FibVL f = list; f != null; f = f.next) {
//			f.join();
//			sum += f.number;
//		}
//		return sum;
//	}
//
//}
//
//class FibVCB extends FJTask {
//	// ...
//	int sequentialThreshold = 13;
//
//	volatile int number = 0; // as before
//
//	final FibVCB parent; // is null for outermost call
//
//	int callbacksExpected = 0;
//
//	volatile int callbacksReceived = 0;
//
//	FibVCB(int n, FibVCB p) {
//		number = n;
//		parent = p;
//	}
//
//	// Callback method invoked by subtasks upon completion
//	synchronized void addToResult(int n) {
//		number += n;
//		++callbacksReceived;
//	}
//
//	int seqFib(int n) {
//		if (n <= 1)
//			return n;
//		else
//			return seqFib(n - 1) + seqFib(n - 2);
//	}
//
//	public void run() { // same structure as join-based version
//		int n = number;
//		if (n <= sequentialThreshold)
//			number = seqFib(n);
//		else {
//			// Clear number so subtasks can fill in
//			number = 0;
//			// Establish number of callbacks expected
//			callbacksExpected = 2;
//
//			new FibVCB(n - 1, this).fork();
//			new FibVCB(n - 2, this).fork();
//
//			// Wait for callbacks from children
//			while (callbacksReceived < callbacksExpected)
//				yield();
//		}
//
//		// Call back parent
//		if (parent != null)
//			parent.addToResult(number);
//	}
//}
//
//class NQueens extends FJTask {
//	static int boardSize; // fixed after initialization in main
//
//	// Boards are arrays where each cell represents a row,
//	// and holds the column number of the queen in that row
//
//	static class Result { // holder for ultimate result
//		private int[] board = null; // non-null when solved
//
//		synchronized boolean solved() {
//			return board != null;
//		}
//
//		synchronized void set(int[] b) { // Support use by non-Tasks
//			if (board == null) {
//				board = b;
//				notifyAll();
//			}
//		}
//
//		synchronized int[] await() throws InterruptedException {
//			while (board == null)
//				wait();
//			return board;
//		}
//	}
//
//	static final Result result = new Result();
//
//	public static void main(String[] args) throws InterruptedException {
//		boardSize = 8;
//		FJTaskRunnerGroup tasks = new FJTaskRunnerGroup(2);
//		int[] initialBoard = new int[0]; // start with empty board
//		tasks.execute(new NQueens(initialBoard));
//		int[] board = result.await();
//		for (int i = 0; i < board.length; i++) {
//			System.out.println(board[i]);
//		}
//	}
//
//	final int[] sofar; // initial configuration
//
//	NQueens(int[] board) {
//		this.sofar = board;
//	}
//
//	public void run() {
//		if (!result.solved()) { // skip if already solved
//			int row = sofar.length;
//
//			if (row >= boardSize) // done
//				result.set(sofar);
//
//			else { // try all expansions
//
//				for (int q = 0; q < boardSize; ++q) {
//
//					// Check if queen can be placed in column q of next row
//					boolean attacked = false;
//					for (int i = 0; i < row; ++i) {
//						int p = sofar[i];
//						if (q == p || q == p - (row - i) || q == p + (row - i)) {
//							attacked = true;
//							break;
//						}
//					}
//
//					// If so, fork to explore moves from new configuration
//					if (!attacked) {
//						// build extended board representation
//						int[] next = new int[row + 1];
//						for (int k = 0; k < row; ++k)
//							next[k] = sofar[k];
//						next[row] = q;
//						new NQueens(next).fork();
//					}
//				}
//			}
//		}
//	}
//}
