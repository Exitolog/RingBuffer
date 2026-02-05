package by.sergey.belyakov;


/**
 * Hello world!
 */
public class App {

	public static void main(String[] args) throws InterruptedException {
		RingBuffer buffer = new RingBuffer(7);

		// Потоки‑производители
		Thread producer1 = new Thread(new ProducerToBuffer(buffer, "P1-"), "Producer-1");
		Thread producer2 = new Thread(new ProducerToBuffer(buffer, "P2-"), "Producer-2");
		Thread producer3 = new Thread(new ProducerToBuffer(buffer, "P3-"), "Producer-3");

		// Потоки‑потребители
		Thread consumer1 = new Thread(new ConsumerWithBuffer(buffer), "Consumer-1");
		Thread consumer2 = new Thread(new ConsumerWithBuffer(buffer), "Consumer-2");

		// Запускаем все потоки
		producer1.start();
		producer2.start();
		producer3.start();
		consumer1.start();
		consumer2.start();

		// Ждём 2 секунды
		Thread.sleep(2000);

		// Прерываем потоки
		producer1.interrupt();
		producer2.interrupt();
		producer3.interrupt();
		consumer1.interrupt();
		consumer2.interrupt();

		System.out.println("Работа завершена!");
	}

}
