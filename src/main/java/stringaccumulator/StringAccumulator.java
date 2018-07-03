package stringaccumulator;

import java.util.ArrayList;
import java.util.List;



public class StringAccumulator implements Accumulator {

	private static String NEW_LINE="\n";
	private static String COMMA = ",";
	private static String DELIMITER_SEPARATOR = "|";
	private static String BLANK = "";
	
	public StringAccumulator() {
		// TODO Auto-generated constructor stub
	}

	public int add(final String numbers) throws NegativeNumberException, IllegalArgumentException {
		int result = 0;		
		if (!(isBlank(numbers))) {
			String inputs = numbers.replaceAll(" ", ""); 
			String delimiterStrings = NEW_LINE + DELIMITER_SEPARATOR + COMMA;
			if (inputs.startsWith("//")) {
				int newLineIndex = numbers.indexOf(NEW_LINE);
				if (newLineIndex > 2) {
					delimiterStrings = getDelimiterString(inputs.substring(2, newLineIndex));
					inputs = inputs.substring(newLineIndex+1);
				} else {
					throw new IllegalArgumentException(String.format("Missing \n in the delimiter definition %s", inputs));
				}
			}
			if (!isBlank(inputs)) {
				try {
					int[] operants = getOperants(inputs, delimiterStrings);
					for (int operant : operants) {
						result += (operant%1000);
					}
				} catch (NegativeNumberException e){
					throw e;
				} catch (IllegalArgumentException e){
					throw new IllegalArgumentException(String.format("%s contains in valid character", inputs), e);
				}
			}
		}
		return result;
	}
	
	private String getDelimiterString(String raw) {
		String delimiterString = NEW_LINE + DELIMITER_SEPARATOR+raw.replaceAll("[\\<\\(\\[\\{\\\\\\^\\=\\$\\!\\]\\}\\)\\?\\*\\.\\>]", "\\\\$0");
		return delimiterString;
	}
	
	private int[] getOperants(String numbers, String delimiterStrings) throws NegativeNumberException, IllegalArgumentException {
		if (numbers.endsWith(NEW_LINE)) {
			throw new IllegalArgumentException("\n is not expected at the end");
		}
		String[] operantStrings = numbers.split(delimiterStrings);
		int[] operants = new int[operantStrings.length];
		String negativeNumbers = "";
		for (int i = 0; i < operantStrings.length; i++) {
			try {
				operants[i] = getIntValue(operantStrings[i]);
			} catch (NegativeNumberException e) {
				if (BLANK.equals(negativeNumbers)) {
					negativeNumbers = operantStrings[i];						
				} else {
					negativeNumbers = negativeNumbers +", " + operantStrings[i];
				}
			}
		}
		if (!BLANK.equals(negativeNumbers)) {
			throw new NegativeNumberException(String.format("Negative numbers %s are not supported.", negativeNumbers));
		}
		return operants;
	}
	private int getIntValue(String operant) throws NegativeNumberException, IllegalArgumentException {
		int value = 0;
		value = Integer.parseInt(operant.trim());
		if (value < 0) {
			throw new NegativeNumberException(String.format("%s negatives not allowed", value));
		}
		return value;
	}

	private boolean isBlank(String value) {
		if (value != null) {
			return BLANK.equals(value.trim());
		} else {
			return true;
		}
	}
}
