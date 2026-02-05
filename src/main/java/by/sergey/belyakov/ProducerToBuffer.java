package by.sergey.belyakov;

public class ProducerToBuffer implements Runnable {

	private RingBuffer buffer;
	private final String prefix;

	public ProducerToBuffer(RingBuffer buffer, String prefix) {
		this.buffer = buffer;
		this.prefix = prefix;
	}

	@Override
	public void run() {
		int counter = 0;
		while (!Thread.interrupted()) {
			buffer.put(prefix + counter);
			counter++;
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
