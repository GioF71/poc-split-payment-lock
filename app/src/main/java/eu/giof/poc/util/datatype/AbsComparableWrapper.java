package eu.giof.poc.util.datatype;

import java.util.Optional;

import com.google.common.collect.ComparisonChain;

public abstract class AbsComparableWrapper<T extends Comparable<T>, X extends AbsComparableWrapper<T, X>> 
	implements 
		ComparableWrapper<T, X>, 
		Wrapper<T> {
	
	private T value;

	protected AbsComparableWrapper(T value) {
		this.value = value;
	}

	@Override
	public T get() {
		return value;
	}

	@Override
	public T whenNull() {
		throw new IllegalArgumentException(String.format("%s should not be built using a null value", getClass().getSimpleName()));
	}

	@Override
	public int compareTo(X o) {
		return ComparisonChain.start()
			.compare(Optional.ofNullable(get()).orElseGet(() -> whenNull()), Optional.ofNullable(o.get()).orElseGet(() -> o.whenNull()))
			.result();
	}
}
