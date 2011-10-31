package com.org.captcha;

import java.util.List;

public class Sample {
	private List<Digit> digits;
	private static final int DIRT = 7;

	public Sample(List<Digit> digits) {
		this.digits = digits;
	}

	private String GetDigit(int[][] d) {
		int num = 100;
		int num2 = 0;
		for (int i = 0; i < this.digits.toArray().length; i++) {
			int num4 = this.digits.get(i).Distance2(d);
			if (num4 < num) {
				num2 = i;
				num = num4;
			}
		}
		String res = new String("" + num2);
		return res;
	}

	public String GetNumber(int[][] matrix) {
		int num6;
		String str = "";
		if (matrix == null) {
			return "";
		}
		int length = matrix.length;
		if (matrix[0] == null) {
			return "";
		}
		int num2 = matrix[0].length;
		for (int i = 0; i < num2; i = num6) {
			int num5;
			int num4 = 0;
			do {
				num5 = 0;
				for (int j = 0; j < length; j++) {
					num5 += matrix[j][i];
				}
				if (num5 == 0) {
					i++;
				}
			} while ((i < num2) && (num5 == 0));
			num6 = i;
			if (i == num2) {
				return str;
			}
			do {
				num5 = 0;
				for (int k = 0; k < length; k++) {
					num5 += matrix[k][num6];
					num4 += matrix[k][num6];
				}
				num6++;
			} while (((num6 < num2) && ((num6 - i) < this.digits.get(0)
					.getWidth())) && (num5 > 0));
			if (num4 > 7) {
				str = str + this.GetDigit(this.Sub(matrix, i, num6));
			}
		}

		return str;
	}

	private int[][] Sub(int[][] m, int start, int end) {
		int[][] numArray = new int[m.length][];
		for (int i = 0; i < m.length; i++) {
			numArray[i] = new int[end - start];
			for (int j = start; j < end; j++) {
				numArray[i][j - start] = m[i][j];
			}
		}
		return numArray;
	}

	public List<Digit> getDigits() {
		return digits;
	}

	public void setDigits(List<Digit> digits) {
		this.digits = digits;
	}

}
