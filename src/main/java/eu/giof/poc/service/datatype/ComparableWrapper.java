package eu.giof.poc.service.datatype;

public interface ComparableWrapper<T extends Comparable<T>, X extends ComparableWrapper<T, X>> 
		extends Wrapper<T>, 
		Comparable<X> {
	T whenNull();
}
