package by.sergey.belyakov;

import java.util.ArrayList;
import java.util.List;

public class RingBuffer {

	private final Object[] elements;
	private final int capacity; // вместимость буфера
	private int writePos = 0;    // куда пишем следующий элемент
	private int count = 0;      // сколько элементов сейчас в буфере

	public RingBuffer(int capacity) {
		if (capacity <= 0) throw new IllegalArgumentException("Capacity > 0");
		this.capacity = capacity;
		this.elements = new Object[capacity];
	}


	public synchronized boolean put(Object element) {
		if (element == null) throw new NullPointerException("Element cannot be null");

		elements[writePos] = element;
		writePos = (writePos + 1) % capacity;

		if (count < capacity) {
			count++;
		}

		System.out.printf("[%s] PUT: %s (добавляем данные в буффер, буффер заполнен на = %d из %d)%n",
				Thread.currentThread().getName(), element, count, capacity);

		return true;
	}

	public synchronized Object get(int index) {
		if (index < 0 || index >= count) {
			return null;
		}
		int pos = (writePos - count + index + capacity) % capacity;

		Object value = elements[pos];

		return elements[pos];
	}

	public synchronized int size() {
		return count;
	}

	public int capacity() {
		return capacity;
	}

	public synchronized void reset() {
		writePos = 0;
		count = 0;
	}

	public synchronized List<Object> getAllAsList() {
		List<Object> result = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			result.add(get(i));
		}
		return result;
	}
}
