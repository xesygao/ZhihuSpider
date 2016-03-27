package ZhihuSpider;

public class Mythread implements Runnable,Callback{
	int numofurl,ithreadnum,count=0,finishcount=0,threadnum;
	String url;
	WindowUI ui;
	Mythread(WindowUI ui,int numofurl,int ithreadnum){
		this.ui=ui;
		this.ithreadnum=ithreadnum;
		threadnum=ithreadnum;
		this.numofurl=numofurl;
	}
	public void run(){
		for(int i=0;i<numofurl;i++){
			if(numofurl-i<ithreadnum)
				ithreadnum=numofurl-i;
			url=ui.list.getElementAt(i);
			Dlthread dl=new Dlthread(url,ui);
			dl.setCallback(this);
			dl.start();
			synchronized(this){
				if((i+1)%threadnum==0){
					try{
						wait();
					}catch(InterruptedException e){}
				}
			}
		}
	}

	public synchronized void threadfinished(){
		count++;
		if(count==ithreadnum){
			System.out.println(count);
			count=0;

			notify();
		}
	}
	public void allFinished(){}
}
