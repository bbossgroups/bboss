package com.sany.bigdata.imp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.frameworkset.common.poolman.SQLExecutor;

public class Solver {
	int N;
	float[][] data;
	CyclicBarrier barrier;
	
	

	

	class Worker implements Runnable {
		int myRow;
		boolean done;
		int count = 0;
		Worker(int row) {
			myRow = row;
		}
		boolean done() {
			return done;
		}
		void processRow(int row) {
			System.out.print("row:"+row);
			count ++;
			if(count == 3)
				done = true;
		}
		public void run() {
			while (!done()) {
				processRow(myRow);

				try {
					barrier.await();
				} catch (InterruptedException ex) {
					return;
				} catch (BrokenBarrierException ex) {
					return;
				}
			}
		}

		

		
	}
	private void mergeRows() {
		System.out.println("mergeRows");
	}
	public void solver(float[][] matrix) {
		data = matrix;
		N = matrix.length;
		Runnable barrierAction = new Runnable() {
			public void run() {
				mergeRows();
			}
		};
		barrier = new CyclicBarrier(N, barrierAction);

		List<Thread> threads = new ArrayList<Thread>(N);
		for (int i = 0; i < N; i++) {
			Thread thread = new Thread(new Worker(i));
			threads.add(thread);
			thread.start();
		}

		// wait until done
		for (Thread thread : threads)
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public static void main(String[] args) throws SQLException
	{
////		Solver s = new Solver();
////		s.solver(new float[][]{{1,2,},{3,4}});
//		System.out.println(10000001%3);
//		System.out.println(10000001/3);
//		
//		System.out.println(3333333*3+2);
//		for(int i = 0; i < 1000;i ++)
//		{
//			if(i%2 == 0)
//				SQLExecutor.insertWithDBName("test","INSERT INTO TESTBIGDATA (TID, NAME, SEX) VALUES ( ?, ?,? )", i,"name"+1,"男");
//			else
//				SQLExecutor.insertWithDBName("test","INSERT INTO TESTBIGDATA (TID, NAME, SEX) VALUES ( ?, ?,? )", i,"name"+1,"女");
//		}
		String t = "size:aaaa";
		System.out.println(t.substring(5));
	}
}
