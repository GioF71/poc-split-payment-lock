package eu.giof.poc.util.impl;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;

public class WrapperComparatorProvider {

	private WrapperComparatorProvider() {
	}

	private static <T, S> S orWhat(T t, Function<T, S> getter, S what) {
		return Optional.ofNullable(t).map(getter).orElse(what);
	}

	private static <T, S extends Comparable<S>> 
	int doCompare(
			T o1, 
			T o2, 
			Function<T, S> getter, S orWhat) {
		S left = orWhat(o1, getter, orWhat);
		S right = orWhat(o2, getter, orWhat);
		return left.compareTo(right);
	}

	public static <T, S extends Comparable<S>> 
	Comparator<T> comparator(Function<T, S> getter, S whenNull) {
		return (o1, o2) -> doCompare(o1, o2, getter, whenNull);
	}

	public static <T> 
	Comparator<T> comparator(Function<T, String> getter) {
		return comparator(getter, "");
	}
}
