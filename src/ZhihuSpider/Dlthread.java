package ZhihuSpider;

import java.io.IOException;

	class Dlthread extends Thread{
		private Callback callback,mythread;
		String url;
		WindowUI ui;
		Dlthread(String url,WindowUI ui ){
			this.url=url;
			this.ui=ui;
			callback=ui;
		};
		public void setCallback(Callback callback){
			mythread=callback;
		}
		public void call(){
			callback.allFinished();
			mythread.threadfinished();
		}
		public void run(){
			try{
    			new Spider(url,ui);
    			call();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
