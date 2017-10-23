package tij.wzt.ch18_io;

/**
 * 后台线程一直在运行，知道前台mian阻塞结束
 * @author wzt
 *
 */
public class ResponsiveUI extends Thread{
	private static volatile double d = 1;
	public ResponsiveUI() {
		setDaemon(true);
		start();
	}
	
	public void run() {
		while(d > 0) {
			d += (Math.PI + Math.E) / d;
		}
	}
	public static void main(String[] args) throws Exception {
		new ResponsiveUI();
		System.in.read();
		System.out.println(d);
	}

}
