package by.sergey.belyakov;

public class DeleterWithBuffer implements Runnable{

	private RingBuffer buffer;

	public DeleterWithBuffer(RingBuffer buffer) {
		this.buffer = buffer;
	}
	@Override
	public void run() {
		while (!Thread.interrupted()) {
			Object value = buffer.take();
			System.out.println("[" + Thread.currentThread().getName() + "] DELETE: " + value);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
