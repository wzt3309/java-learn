package learn.java.tij.ch21;

public abstract class IntGenerator {
	private volatile boolean cancel = false;
	public abstract int next();
	public boolean isCanceled() { return cancel;}
	public void canceled() {this.cancel = true;}

}
