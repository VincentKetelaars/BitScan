package objects;

public interface IFileWriter {
	
	public void open();
	
	public void close();
	
	public void update(TicketsFile tf);
	
	public void forceUpdate();
	
	public boolean isDone();
}
