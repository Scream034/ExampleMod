package com.pmod.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.pmod.Main;

/**
 * Class for generating random values based on probabilities.
 */
public final class RandomValueFromList<T> extends ArrayList<com.pmod.list.RandomValueFromList.Pair<Float, T>> {
	private static transient final Random random = new Random();

	/**
	 * @param data - even number of elements: value, probability, value,
	 *             probability...
	 */
	public RandomValueFromList(final List<?> data) {
		if (data.size() % 2 != 0) {
			Main.Log.warn(
					"Incorrect number of elements in RandomValue (list should have even number of elements: value, probability, value, probability...).");
			return;
		}

		float totalProbability = 0;
		for (int i = 0; i < data.size(); i += 2) {
			try {
				@SuppressWarnings("unchecked")
				T value = (T) data.get(i);
				Object probObj = data.get(i + 1);
				if (probObj instanceof Number) {
					float probability = ((Number) probObj).floatValue();
					if (probability >= 0) {
						add(new Pair<>(probability, value));
						totalProbability += probability;
					} else {
						Main.Log.warn("Negative probability {} for value: {}", probability, value);
					}
				} else {
					Main.Log.warn("Probability should be a number. Got: {}", probObj.getClass().getName());
				}
			} catch (ClassCastException e) {
				Main.Log.warn("Incorrect data type in RandomValue.");
			} catch (IndexOutOfBoundsException e) {
				Main.Log.warn("Error reading data from RandomValue.");
			}
		}

		// Normalize probabilities by total probability
		if (totalProbability > 0) {
			for (Pair<Float, T> pair : this) {
				pair.setKey(pair.getKey() / totalProbability);
			}
		} else {
			Main.Log.warn("Sum of probabilities is 0. RandomValue will return null.");
			clear();
		}
	}

	/**
	 * @param valuesWithProbabilities - even number of elements: value, probability,
	 *                                value, probability...
	 */
	public RandomValueFromList(Object... valuesWithProbabilities) {
		this(Arrays.asList(valuesWithProbabilities));
	}

	/**
	 * Returns a random value based on the probabilities.
	 * 
	 * @return random value or null if the list is empty
	 */
	public T value() {
		if (isEmpty()) {
			return null;
		}

		float randomNumber = random.nextFloat();
		float cumulativeProbability = 0;

		for (Pair<Float, T> pair : this) {
			cumulativeProbability += pair.getKey();
			if (randomNumber < cumulativeProbability) {
				return pair.getValue();
			}
		}
		
		return get(size() - 1).getValue();
	}

	public final static class Pair<K, V> {
		private K key;
		private final V value;

		public Pair(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return key;
		}

		public void setKey(K key) {
			this.key = key;
		}

		public V getValue() {
			return value;
		}
	}
}