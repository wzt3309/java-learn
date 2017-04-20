package jxl.wzt.u2nio;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

/**
 * 文件修改通知，消息机制非轮询
 * @author wzt
 *
 */
public class FileMonitor {

	public static void main(String[] args) {
		boolean shutdown=false;
		try {
			WatchService watcher=FileSystems.getDefault().newWatchService();
			Path path=FileSystems.getDefault().getPath(".");
			WatchKey key=path.register(watcher,StandardWatchEventKinds.ENTRY_MODIFY);
			while(!shutdown){
				key=watcher.take();
				for(WatchEvent<?> event:key.pollEvents()){
					if(event.kind()==StandardWatchEventKinds.ENTRY_MODIFY){
						System.out.println("data1 is modified");
					}
				}
				key.reset();
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
