package by.sergey.belyakov;

import java.util.List;

public class ConsumerWithBuffer implements Runnable {

	private final RingBuffer buffer;

	public ConsumerWithBuffer(RingBuffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			List<Object> all = buffer.getAllAsList();
			System.out.printf("[%s] ALL (%d): %s%n",
					Thread.currentThread().getName(),
					all.size(),
					(all));
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

}
