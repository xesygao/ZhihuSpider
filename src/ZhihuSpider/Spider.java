package ZhihuSpider;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Spider {
	Spider(String url,WindowUI ui) throws IOException{
	getURL(url,ui);
}
	
	void getURL(String pageUrl,WindowUI ui) throws IOException{
		String title="";
		LinkedList<String> urlList=new LinkedList<String>();
		LinkedList<String> nameList=new LinkedList<String>();
		//String[] patUrl=new String[100];
		URL page=new URL(pageUrl);
		StringBuffer contentBuffer = new StringBuffer();  
		HttpURLConnection pageconn=(HttpURLConnection)page.openConnection();
        //设置超时间为3秒  
		pageconn.setConnectTimeout(3*1000);  
		//防止屏蔽程序抓取而返回403错误  
		pageconn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
		InputStream ins=pageconn.getInputStream();
		InputStreamReader isr=new InputStreamReader(ins,"utf-8");
		BufferedReader bufr=new BufferedReader(isr);
		String str=null;
		while((str=bufr.readLine())!=null){
			contentBuffer.append(str);
		}
		ins.close();
		Pattern pat=Pattern.compile("actualsrc=\"(.*?)\">");
		Matcher mat=pat.matcher(contentBuffer);
		while(mat.find()){
			urlList.add(mat.group(1));
		}
		urlList.add(",");
		Pattern titlePat=Pattern.compile(".*?<title>(.*?) - 知乎</title>");
		Matcher titleMat=titlePat.matcher(contentBuffer);
		if(titleMat.find()){
			title=titleMat.group(1);
		}
		
		Pattern namePat=Pattern.compile("https://.*?/(.*?),");
		Matcher fileMat=namePat.matcher(urlList.toString());
		while(fileMat.find()){
			nameList.add(fileMat.group(1));
		}
		ui.info.append("正在抓取帖子"+pageUrl+"\n标题:"+title+"\n");
		for(int i=0;i<urlList.size()-1;i++){
			dlPic(urlList.get(i),nameList.get(i),title);
			ui.info.append("图片"+urlList.get(i).toString()+"抓取完成\n");
		}
		ui.info.append("帖子---"+title+"抓取完成\n");
		
	}
	public static void dlPic(String s_url,String name,String path) throws IOException{
		URL url=new URL(s_url);
		HttpURLConnection conn=(HttpURLConnection)url.openConnection();
        //设置超时间为3秒  
		conn.setConnectTimeout(3*1000);  
		//防止屏蔽程序抓取而返回403错误  
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
		InputStream ins=conn.getInputStream();
		byte[] getData=readins(ins);
		File saveDir=new File("Picture/"+path);
		if(!saveDir.exists()){
			saveDir.mkdirs();
		}
		File file=new File("Picture/"+path+"/"+name);
		FileOutputStream fos=new FileOutputStream(file);
		fos.write(getData);
		if(fos!=null){
			fos.close();
		}
		if(ins!=null){
			ins.close();
		}
		
	}
	
	private static byte[] readins(InputStream ins) throws IOException{
		byte[] buf=new byte[1024];
		int len=0;
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		while((len=ins.read(buf))!=-1){
			out.write(buf,0,len);
		}
		out.close();
		return out.toByteArray();
	}
	


}
