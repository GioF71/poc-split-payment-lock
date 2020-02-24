package eu.giof.poc.util.datatype;

public interface ComparableWrapper<T extends Comparable<T>, X extends ComparableWrapper<T, X>> 
		extends Wrapper<T>, 
		Comparable<X> {
	T whenNull();
}
