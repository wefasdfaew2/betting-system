package com.org.captcha;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class CaptCha {
	private int bright;
	private int dh;
	private Sample digits;
	private int dw;
	private int xmax;
	private int xmin;
	private int ymax;
	private int ymin;

	public CaptCha(int[] param, int[][] code) {
		this.xmin = param[0];
		this.xmax = param[1];
		this.ymin = param[2];
		this.ymax = param[3];
		this.dh = param[4];
		this.dw = param[5];
		this.bright = param[6];
		List<Digit> digits = new ArrayList<Digit>();
		for (int i = 0; i < code.length; i++) {
			Digit item = new Digit(this.dh, this.dw, code[i]);
			digits.add(item);
		}
		this.digits = new Sample(digits);
	}

	public int[][] Binarize(BufferedImage source, int bright) {
		// Bitmap bitmap = new Bitmap(source);
		int width = source.getWidth();
		int height = source.getHeight(null);

		int[][] numArray = new int[this.ymax - this.ymin][];
		boolean flag = false;
		if (bright < 0) {
			flag = true;
			this.bright = -bright;
		}

		if ((this.ymax <= height) && (this.xmax <= width)) {
			for (int i = this.ymin; i < this.ymax; i++) {
				numArray[i - this.ymin] = new int[this.xmax - this.xmin];
				for (int j = this.xmin; j < this.xmax; j++) {
					Color pixel = new Color(source.getRGB(j, i));

					int num5 = (int) (((pixel.getRed() * 0.3) + (pixel
							.getGreen() * 0.59)) + (pixel.getBlue() * 0.11));
					if (num5 > bright) {
						num5 = 1;
					} else {
						num5 = 0;
					}
					if (flag) {
						num5 = 1 - num5;
					}
					numArray[i - this.ymin][j - this.xmin] = num5;
				}
			}
		}

		return numArray;
	}

	public String GetNumber(String imgPath) {
		BufferedImage source;
		try {
			source = ImageIO.read(new File(imgPath));
			int[][] matrix = this.Binarize(source, this.bright);
			return this.digits.GetNumber(matrix);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Image not found");
			return "";
		}
	}

	public String GetNumber(BufferedImage source) {
		int[][] matrix = this.Binarize(source, this.bright);
		return this.digits.GetNumber(matrix);
	}

}
