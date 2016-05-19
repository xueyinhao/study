package org.hao.weixin;

import java.util.Timer;
import java.util.TimerTask;

public class TestTimer {
	
	public static void main(String[] args) {
		new TestTimer(4);
	}
	
	public TestTimer(int second) {
		Timer timer = new Timer();
		timer.schedule(new MyTimeTask(), 0, second*1000);
	}
	
	private class MyTimeTask extends TimerTask {
		@Override
		public void run() {
			System.out.println("timetask is run!");
		}
	}
}
