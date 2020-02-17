package eu.giof.poc.service.datatype;

import org.junit.Assert;
import org.junit.Test;

public class ComparableWrapperTest {
	
	static class MyIntegerWrapper extends AbsComparableWrapper<Integer, MyIntegerWrapper> {

		static MyIntegerWrapper valueOf(Integer value) {
			return new MyIntegerWrapper(value);
		}
		
		protected MyIntegerWrapper(Integer value) {
			super(value);
		}
	}
	
	static class MyStringWrapper extends AbsStringComparableWrapper<MyStringWrapper> {

		static MyStringWrapper valueOf(String value) {
			return new MyStringWrapper(value);
		}
		
		private MyStringWrapper(String value) {
			super(value);
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void doesNotSupportNull() {
		MyIntegerWrapper left = MyIntegerWrapper.valueOf(1);
		MyIntegerWrapper right = MyIntegerWrapper.valueOf(null);
		left.compareTo(right);
	}
	
	@Test
	public void intEq() {
		MyIntegerWrapper left = MyIntegerWrapper.valueOf(2);
		MyIntegerWrapper right = MyIntegerWrapper.valueOf(2);
		int cmpNullWithValue = left.compareTo(right);
		Assert.assertTrue(cmpNullWithValue == 0);
	}

	@Test
	public void intLt() {
		MyIntegerWrapper left = MyIntegerWrapper.valueOf(5);
		MyIntegerWrapper right = MyIntegerWrapper.valueOf(6);
		int cmpNullWithValue = left.compareTo(right);
		Assert.assertTrue(cmpNullWithValue < 0);
	}

	@Test
	public void intGt() {
		MyIntegerWrapper left = MyIntegerWrapper.valueOf(5);
		MyIntegerWrapper right = MyIntegerWrapper.valueOf(4);
		int cmpNullWithValue = left.compareTo(right);
		Assert.assertTrue(cmpNullWithValue > 0);
	}

	@Test
	public void strEq() {
		MyStringWrapper left = MyStringWrapper.valueOf("abc");
		MyStringWrapper right = MyStringWrapper.valueOf("abc");
		int cmpNullWithValue = left.compareTo(right);
		Assert.assertTrue(cmpNullWithValue == 0);
	}

	@Test
	public void strLt() {
		MyStringWrapper left = MyStringWrapper.valueOf("ab");
		MyStringWrapper right = MyStringWrapper.valueOf("abc");
		int cmpNullWithValue = left.compareTo(right);
		Assert.assertTrue(cmpNullWithValue < 0);
	}

	@Test
	public void strGt() {
		MyStringWrapper left = MyStringWrapper.valueOf("abcd");
		MyStringWrapper right = MyStringWrapper.valueOf("abc");
		int cmpNullWithValue = left.compareTo(right);
		Assert.assertTrue(cmpNullWithValue > 0);
	}

	@Test
	public void strLeftNull() {
		MyStringWrapper left = MyStringWrapper.valueOf(null);
		MyStringWrapper right = MyStringWrapper.valueOf("abc");
		int cmpNullWithValue = left.compareTo(right);
		Assert.assertTrue(cmpNullWithValue < 0);
	}

	@Test
	public void strRightNull() {
		MyStringWrapper left = MyStringWrapper.valueOf("abc");
		MyStringWrapper right = MyStringWrapper.valueOf(null);
		int cmpNullWithValue = left.compareTo(right);
		Assert.assertTrue(cmpNullWithValue > 0);
	}
}
