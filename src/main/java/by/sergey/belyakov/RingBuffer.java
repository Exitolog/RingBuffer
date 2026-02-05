package by.sergey.belyakov;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RingBuffer {

	private final Object[] elements;
	private final int capacity; // вместимость буфера
	private int writePos = 0;    // куда пишем следующий элемент
	private int count = 0;      // сколько элементов сейчас в буфере

	//Инструменты для синхронизации
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition notFull = lock.newCondition();
	private final Condition notEmpty = lock.newCondition();

	public RingBuffer(int capacity) {
		if (capacity <= 0) throw new IllegalArgumentException("Capacity > 0");
		this.capacity = capacity;
		this.elements = new Object[capacity];
	}


	public void put(Object element) {

		if (element == null) throw new NullPointerException("Element cannot be null");

		lock.lock();

		try {

			while (count == capacity) {
				notFull.await();
			}

			elements[writePos] = element;
			writePos = (writePos + 1) % capacity;

			if (count < capacity) {
				count++;
			}

			System.out.printf("[%s] PUT: %s (добавляем данные в буффер, буффер заполнен на = %d из %d)%n", Thread.currentThread().getName(), element, count, capacity);

			notEmpty.signal();
		} catch (InterruptedException e) {
			throw new RuntimeException("Ошибка в потоке производителя", e);
		} finally {
			lock.unlock();
		}
	}

	public Object take()  {
		lock.lock();
		try {

			while (count == 0) {
				notEmpty.await();
			}

			int readPos = (writePos - count + capacity) % capacity;
			Object value = elements[readPos];
			elements[readPos] = null;
			count--;

			System.out.printf("[%s] TAKE: %s (буфер заполнен на %d из %d)%n",
					Thread.currentThread().getName(), value, count, capacity);

			notFull.signalAll();

			return value;

		} catch (InterruptedException e) {
			throw new RuntimeException("Ошибка в потоке потребителя", e);
		} finally {
			lock.unlock();
		}
	}

	public Object get(int index) {

		lock.lock();

		try {
			if (index < 0 || index >= count) {
				return null;
			}
			int pos = (writePos - count + index + capacity) % capacity;

			Object value = elements[pos];

			return elements[pos];
		}  finally {
			lock.unlock();
		}
	}

	public int size() {
		lock.lock();
		try {
			return count;
		} finally {
			lock.unlock();
		}
	}

	public int capacity() {
		return capacity;
	}

	public void reset() {
		lock.lock();
		try {
			writePos = 0;
			count = 0;
			for (int i = 0; i < elements.length; i++) {
				elements[i] = null;
			}
		} finally {
			lock.unlock();
		}
	}

	public  List<Object> getAllAsList() {
		lock.lock();
		try {
			List<Object> result = new ArrayList<>(count);
			for (int i = 0; i < count; i++) {
				result.add(get(i));
			}
			return result;
		} finally {
			lock.unlock();
		}
		}
}
