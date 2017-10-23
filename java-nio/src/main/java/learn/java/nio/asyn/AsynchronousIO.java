package learn.java.nio.asyn;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsynchronousIO {

	public static void main(String[] args) {
		Path file=Paths.get("/home/wzt/桌面/Think In Java v4.pdf");
		AsynchronousFileChannel channel;
		ByteBuffer bf=ByteBuffer.allocate(100_000);
		
		//将来式
		try{
			channel=AsynchronousFileChannel.open(file);			
			Future<Integer> result=channel.read(bf, 0);	//未阻塞	
			
			
			System.out.println("do else");	//read返回之前可以做其他事情					
			Integer bytesRead=result.get();	//如果read还未返回，则在此处等待
			System.out.println("Bytes read ["+bytesRead+"]");
			
		}catch(IOException | ExecutionException | InterruptedException e){
			e.printStackTrace();
		}
		
		//回调式
		try{
			channel=AsynchronousFileChannel.open(file);
			channel.read(bf, 0, bf, new CompletionHandler<Integer,ByteBuffer>(){
				@Override
				public void completed(Integer result,ByteBuffer attachment){
					System.out.println("Bytes read ["+result+"]");
					System.exit(0);
				}
				@Override
				public void failed(Throwable e,ByteBuffer attachment){
					e.printStackTrace();
				}
			});
			//while(true);//不然主线程退出时，异步io还未完成，则不会输出completed内的内容
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
