package com.org.captcha;

public class Digit {
	private int height;
	public int[][] matrix;
	private int width;

	public Digit(int h, int w) {
		this.height = h;
		this.width = w;
		this.matrix = new int[this.height][];
		for (int i = 0; i < this.height; i++) {
			this.matrix[i] = new int[this.width];
		}
	}

	public Digit(int h, int w, int[] code) {
		this.height = h;
		this.width = w;
		this.matrix = new int[this.height][];
		for (int i = 0; i < this.height; i++) {
			this.matrix[i] = new int[this.width];
			for (int j = this.width - 1; j >= 0; j--) {
				this.matrix[i][j] = code[i] % 2;
				code[i] /= 2;
			}
		}
	}

	public Digit(int h, int w, int[][] m) {
		this.height = h;
		this.width = w;
		this.matrix = new int[this.height][];
		for (int i = 0; i < this.width; i++) {
			this.matrix[i] = new int[this.width];
		}
		this.matrix = m;
	}

	public int Distance(int[][] other) {
		int length = other.length;
		int num2 = other[0].length;
		int num3 = 100;
		for (int i = 1 - length; i < this.height; i++) {
			for (int j = 1 - num2; j < this.width; j++) {
				int num6 = 0;
				int num7 = Math.max(0, i);
				int num8 = Math.min(this.height, i + length);
				int num9 = Math.max(0, j);
				int num10 = Math.min(this.width, j + num2);
				for (int k = num7; k < num8; k++) {
					for (int m = num9; m < num10; m++) {
						if ((this.matrix[k][m] == 1)
								&& (other[k - i][m - j] == 1)) {
							num6++;
						}
					}
				}
				num6 = (this.PixCount(this.matrix) + this.PixCount(other))
						- (num6 * 2);
				if (num3 > num6) {
					num3 = num6;
				}
			}
		}
		return num3;
	}

	public int Distance2(int[][] other) {
		int length = other.length;
		int num2 = other[0].length;
		int num3 = 0x3e8;
		for (int i = 1 - length; i < this.height; i++) {
			for (int j = 1 - num2; j < this.width; j++) {
				int num6 = 0;
				int num7 = Math.max(0, i);
				int num8 = Math.min(this.height, i + length);
				int num9 = Math.max(0, j);
				int num10 = Math.min(this.width, j + num2);
				for (int k = num7; k < num8; k++) {
					int num12 = 0;
					int num13 = 0;
					for (int m = num9; m < num10; m++) {
						if (this.matrix[k][m] != other[k - i][m - j]) {
							num12++;
						} else {
							num13++;
						}
					}
					num6 += (num12 * num12) - (num13 * num13);
				}
				if (num3 > num6) {
					num3 = num6;
				}
			}
		}
		return num3;
	}

	public boolean IsSame(int[][] other) {
		int num = 0;
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				num += Math.abs((int) (this.matrix[i][j] - other[i][j]));
			}
		}
		return (num < 3);
	}

	public boolean IsSame(Digit other) {
		if ((other.getWidth() != this.width) || (other.getHeight() != this.height)) {
			return false;
		}
		int[][] matrix = other.getMatrix();
		return this.IsSame(matrix);
	}

	public int PixCount(int[][] m) {
		int num = 0;
		int length = m.length;
		int num3 = m[0].length;
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < num3; j++) {
				num += m[i][j];
			}
		}
		return num;
	}

	public int PixCount2(int[][] m) {
		int length = m.length;
		int num3 = m[0].length;
		for (int i = 0; i < length; i++) {
			int num5 = 0;
			for (int j = 0; j < num3; j++) {
				num5 += m[i][j];
			}
			num5 *= num5;
		}
		return 0;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
