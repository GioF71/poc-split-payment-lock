package eu.giof.poc.util.datatype;

public abstract class AbsStringComparableWrapper<X extends AbsStringComparableWrapper<X>>  
	extends AbsComparableWrapper<String, X> {

	protected AbsStringComparableWrapper(String value) {
		super(value);
	}

	@Override
	public String whenNull() {
		return "";
	}
}
