package stringaccumulator;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class StringAccumulatorTest {

	private static StringAccumulator accumulator = null;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		accumulator = new StringAccumulator();
	}

	@Test
	public void testNormalCases() {
		int result = 0;
		result = accumulator.add("");
		Assert.assertEquals(0, result);
		result = accumulator.add("1");
		Assert.assertEquals(1, result);
		result = accumulator.add("1,2");
		Assert.assertEquals(3, result);
		result = accumulator.add("1, 2");
		Assert.assertEquals(3, result);
		result = accumulator.add(" 1, 2 ");
		Assert.assertEquals(3, result);
		result = accumulator.add("1,2,3");
		Assert.assertEquals(6, result);
		result = accumulator.add("+1,+2,+3");
		Assert.assertEquals(6, result);
		result = accumulator.add("1\n2\n3");
		Assert.assertEquals(6, result);
		result = accumulator.add("1,2,3,4,5");
		Assert.assertEquals(15, result);
		result = accumulator.add("1\n2,3");
		Assert.assertEquals(6, result);
		result = accumulator.add("1\n2\n3");
		Assert.assertEquals(6, result);
		result = accumulator.add("1,2\n3");
		Assert.assertEquals(6, result);
		result = accumulator.add("1000,2");
		Assert.assertEquals(2, result);
		result = accumulator.add("10000,2");
		Assert.assertEquals(2, result);
		result = accumulator.add("100000,2");
		Assert.assertEquals(2, result);
		result = accumulator.add("999,2");
		Assert.assertEquals(1001, result);
		try {
			accumulator.add("1,\n");
			Assert.fail("Exception is not thrown using invalid argument");
		} catch (IllegalArgumentException e) {
		}
		
		try {
			accumulator.add("a,1");
			Assert.fail("Exception is not thrown using invalid argument");
		} catch (IllegalArgumentException e) {
		}
	}
	
	@Test
	public void testDelimiters() {
		int result = 0;	
		// testing using special character as delimiter
		String [] sepecialCharacters = {"<" , "(", "[", "{", "\\", "^", "=", "$", "!", "}", "?", "*", ".", ">"};
		for (String sepecialCharacter : sepecialCharacters) {
			result = accumulator.add(String.format("//%s\n1%s2", sepecialCharacter, sepecialCharacter));
			Assert.assertEquals(String.format("Error in using %s as delimiter.", sepecialCharacter), 3, result);	
		}
		result = accumulator.add("//,\n");
		Assert.assertEquals(0, result);
		result = accumulator.add("//,\n1");
		Assert.assertEquals(1, result);
		result = accumulator.add("//,\n1,2");
		Assert.assertEquals(3, result);
		result = accumulator.add("//%\n1%2");
		Assert.assertEquals(3, result);
		result = accumulator.add("//;\n1;2;3");
		Assert.assertEquals(6, result);
		result = accumulator.add("//%|,\n1%2,3");
		Assert.assertEquals(6, result);		
		result = accumulator.add("//%|[\n1%2[3");
		Assert.assertEquals(6, result);
		result = accumulator.add("//%|[\n1%2\n3");
		Assert.assertEquals(6, result);
		result = accumulator.add("//%|[|]\n1%2[3]4");
		Assert.assertEquals(10, result);
		result = accumulator.add("//%|***|[[\n1[[2***3");
		Assert.assertEquals(6, result);
		result = accumulator.add("//%|***|**\n1**2***3");
		Assert.assertEquals(6, result);
		try {
			accumulator.add("//,1,2");
			Assert.fail("Exception is not thrown using argument without \n in delimiter definition");
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Missing \n in the delimiter definition //,1,2", e.getMessage());
		}
	}
	
	@Test
	public void testNegativeNumbers() {
		try {
			accumulator.add("-1,-2");
			Assert.fail("Exception is not thrown with negative number");
		} catch (NegativeNumberException e) {
			Assert.assertEquals("Negative numbers -1, -2 are not supported.", e.getMessage());
		}
		try {
			accumulator.add("//,\n-1,-2");
			Assert.fail("Exception is not thrown with negative number");
		} catch (NegativeNumberException e) {
			Assert.assertEquals("Negative numbers -1, -2 are not supported.", e.getMessage());
		}
		try {
			accumulator.add("//,\n-1,2\n-3");
			Assert.fail("Exception is not thrown with negative number");
		} catch (NegativeNumberException e) {
			Assert.assertEquals("Negative numbers -1, -3 are not supported.", e.getMessage());
		}
	}
}
