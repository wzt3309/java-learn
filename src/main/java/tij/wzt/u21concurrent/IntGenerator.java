package tij.wzt.u21concurrent;

public abstract class IntGenerator {
	private volatile boolean cancel = false;
	public abstract int next();
	public boolean isCanceled() { return cancel;}
	public void canceled() {this.cancel = true;}

}
