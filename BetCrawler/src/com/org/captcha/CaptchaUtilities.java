package com.org.captcha;

import java.awt.image.BufferedImage;


public class CaptchaUtilities {
	private CaptCha AIbet;
	private int[][] AIbetCode = new int[][] {
			new int[] { 124, 198, 387, 387, 387, 387, 387, 387, 387, 198, 124 },
			new int[] { 24, 120, 24, 24, 24, 24, 24, 24, 24, 24, 126 },
			new int[] { 252, 390, 387, 3, 3, 6, 12, 56, 96, 192, 511 },
			new int[] { 252, 390, 387, 3, 6, 60, 6, 2, 259, 390, 252 },
			new int[] { 6, 14, 22, 38, 70, 134, 262, 511, 6, 6, 6 },
			new int[] { 255, 192, 192, 192, 252, 6, 3, 3, 386, 390, 252 },
			new int[] { 62, 96, 192, 384, 508, 390, 387, 387, 387, 198, 124 },
			new int[] { 511, 6, 6, 12, 8, 24, 24, 48, 48, 96, 96 },
			new int[] { 124, 198, 387, 386, 198, 124, 198, 386, 131, 198, 124 },
			new int[] { 124, 198, 387, 387, 387, 195, 127, 3, 6, 12, 248 } };
	private CaptCha AIbet2;
	private int[][] AIbetCode2 = new int[][] {
			new int[] { 124, 198, 387, 387, 387, 387, 387, 387, 387, 198, 124 },
			new int[] { 24, 120, 24, 24, 24, 24, 24, 24, 24, 24, 126 },
			new int[] { 252, 390, 387, 3, 3, 6, 12, 56, 96, 192, 511 },
			new int[] { 252, 390, 387, 3, 6, 60, 6, 2, 259, 390, 252 },
			new int[] { 6, 14, 22, 38, 70, 134, 262, 511, 6, 6, 6 },
			new int[] { 255, 192, 192, 192, 252, 6, 3, 3, 386, 390, 252 },
			new int[] { 62, 96, 192, 384, 508, 390, 387, 387, 387, 198, 124 },
			new int[] { 511, 6, 6, 12, 8, 24, 24, 48, 48, 96, 96 },
			new int[] { 124, 198, 387, 386, 198, 124, 198, 386, 131, 198, 124 },
			new int[] { 124, 198, 387, 387, 387, 195, 127, 3, 6, 12, 248 } };
	private CaptCha ASbobet;
	private int[][] ASbobetCode = new int[][] {
			new int[] { 248, 508, 990, 1798, 1540, 3590, 3590, 7174, 7174,
					7182, 7182, 7182, 7196, 7196, 7224, 3704, 4080, 1984 },
			new int[] { 24, 56, 120, 504, 2032, 2032, 1136, 112, 96, 96, 224,
					224, 224, 192, 448, 448, 448, 448 },
			new int[] { 1016, 2044, 4030, 7175, 7175, 6151, 7, 14, 30, 60, 112,
					480, 960, 1920, 3840, 4092, 8188, 8188 },
			new int[] { 120, 510, 991, 903, 1799, 7, 6, 94, 124, 124, 30, 14,
					1038, 7182, 7182, 3868, 4092, 1008 },
			new int[] { 6, 14, 30, 62, 126, 236, 476, 412, 1948, 1816, 3640,
					8190, 8190, 8190, 48, 112, 112, 112 },
			new int[] { 511, 511, 1023, 896, 768, 1792, 2040, 2044, 3870, 1550,
					6, 14, 14, 3086, 7196, 7228, 4088, 992 },
			new int[] { 120, 508, 1022, 1926, 1798, 3840, 4080, 4088, 7964,
					7694, 7182, 7182, 7182, 7180, 7196, 3640, 2040, 992 },
			new int[] { 8190, 8190, 8188, 28, 120, 112, 224, 448, 448, 896,
					896, 1792, 1792, 3584, 3584, 3584, 7168, 3072 },
			new int[] { 240, 1016, 2014, 1806, 1542, 1550, 1934, 1020, 2040,
					4092, 7694, 7174, 7174, 6158, 7182, 7708, 4092, 2032 },
			new int[] { 240, 1020, 1980, 1806, 3598, 3086, 3086, 3086, 3614,
					3646, 2046, 1020, 28, 6168, 7224, 7280, 4080, 1984 } };
	private CaptCha Ibet;
	private int[][] IbetCode = new int[][] {
			new int[] { 62, 99, 99, 99, 99, 99, 99, 99, 62 },
			new int[] { 12, 60, 12, 12, 12, 12, 12, 12, 63 },
			new int[] { 62, 67, 67, 3, 6, 12, 24, 48, 127 },
			new int[] { 62, 67, 3, 3, 30, 3, 3, 67, 62 },
			new int[] { 6, 14, 22, 38, 70, 127, 6, 6, 6 },
			new int[] { 63, 48, 48, 62, 3, 3, 3, 67, 62 },
			new int[] { 30, 48, 96, 126, 99, 99, 99, 99, 62 },
			new int[] { 127, 3, 6, 6, 12, 12, 24, 24, 24 },
			new int[] { 62, 99, 99, 99, 62, 99, 99, 99, 62 },
			new int[] { 62, 99, 99, 99, 99, 63, 3, 6, 60 } };
	private CaptCha Ibet2;
	private int[][] Ibet2Code = new int[][] {
			new int[] { 62, 99, 99, 99, 99, 99, 99, 99, 62 },
			new int[] { 12, 60, 12, 12, 12, 12, 12, 12, 63 },
			new int[] { 62, 67, 67, 3, 6, 12, 24, 48, 127 },
			new int[] { 62, 67, 3, 3, 30, 3, 3, 67, 62 },
			new int[] { 6, 14, 22, 38, 70, 127, 6, 6, 6 },
			new int[] { 63, 48, 48, 62, 3, 3, 3, 67, 62 },
			new int[] { 30, 48, 96, 126, 99, 99, 99, 99, 62 },
			new int[] { 127, 3, 6, 6, 12, 12, 24, 24, 24 },
			new int[] { 62, 99, 99, 99, 62, 99, 99, 99, 62 },
			new int[] { 62, 99, 99, 99, 99, 63, 3, 6, 60 } };
	private CaptCha Sb168;
	private int[][] Sb168Code = new int[][] {
			new int[] { 0, 0, 0, 60, 102, 231, 199, 199, 198, 204, 48 },
			new int[] { 0, 0, 24, 248, 48, 48, 112, 112, 240, 0, 0 },
			new int[] { 0, 0, 62, 102, 103, 14, 60, 254, 254, 0, 0 },
			new int[] { 0, 0, 62, 103, 119, 6, 62, 14, 15, 206, 220 },
			new int[] { 0, 0, 7, 15, 30, 54, 102, 206, 255, 14, 12 },
			new int[] { 0, 0, 63, 63, 32, 64, 126, 14, 14, 206, 220 },
			new int[] { 0, 15, 56, 96, 236, 254, 227, 199, 230, 236, 48 },
			new int[] { 0, 0, 127, 255, 196, 140, 8, 16, 32, 96, 192 },
			new int[] { 0, 62, 103, 103, 118, 124, 94, 198, 198, 204, 112 },
			new int[] { 0, 0, 60, 102, 231, 231, 231, 126, 14, 28, 56 } };
	private CaptCha Sbobet;
	private int[][] SboCode = new int[][] {
			new int[] { 30, 115, 225, 193, 385, 385, 385, 387, 387, 899, 391,
					390, 460, 248 },
			new int[] { 4, 12, 124, 236, 200, 24, 24, 24, 24, 48, 48, 48, 48,
					48 },
			new int[] { 62, 231, 195, 1, 3, 7, 12, 24, 48, 96, 192, 384, 510,
					1022 },
			new int[] { 60, 103, 195, 195, 3, 7, 30, 30, 7, 3, 3, 390, 462, 124 },
			new int[] { 3, 7, 15, 30, 54, 102, 198, 390, 782, 1023, 12, 12, 24,
					24 },
			new int[] { 127, 127, 192, 192, 252, 510, 455, 7, 3, 3, 774, 774,
					476, 248 },
			new int[] { 62, 119, 224, 192, 408, 510, 454, 903, 899, 771, 903,
					390, 478, 248 },
			new int[] { 511, 511, 7, 6, 12, 24, 24, 48, 48, 96, 96, 192, 192,
					192 },
			new int[] { 126, 231, 195, 195, 195, 254, 252, 454, 902, 775, 774,
					774, 478, 504 },
			new int[] { 124, 198, 387, 387, 387, 387, 391, 463, 254, 38, 270,
					780, 984, 496 } };
	private CaptCha Sb188;
	private int[][] Sb188Code = new int[][] {
			new int[] { 0, 0, 0, 60, 102, 231, 199, 199, 198, 204, 48 },
			new int[] { 0, 0, 24, 248, 48, 48, 112, 112, 240, 0, 0 },
			new int[] { 0, 0, 62, 102, 103, 14, 60, 254, 254, 0, 0 },
			new int[] { 0, 0, 62, 103, 119, 6, 62, 14, 15, 206, 220 },
			new int[] { 0, 0, 7, 15, 30, 54, 102, 206, 255, 14, 12 },
			new int[] { 0, 0, 63, 63, 32, 64, 126, 14, 14, 206, 220 },
			new int[] { 0, 15, 56, 96, 236, 254, 227, 199, 230, 236, 48 },
			new int[] { 0, 0, 127, 255, 196, 140, 8, 16, 32, 96, 192 },
			new int[] { 0, 62, 103, 103, 118, 124, 94, 198, 198, 204, 112 },
			new int[] { 0, 0, 60, 102, 231, 231, 231, 126, 14, 28, 56 } };
	private CaptCha ThreeInOne;
	private int[][] ThreeInOneCode = new int[][] {
			new int[] { 28, 62, 99, 99, 99, 99, 99, 99, 99, 62, 28 },
			new int[] { 6, 14, 30, 54, 54, 6, 6, 6, 6, 6, 6 },
			new int[] { 28, 62, 99, 99, 6, 12, 24, 48, 48, 127, 127 },
			new int[] { 28, 62, 102, 6, 12, 12, 6, 99, 99, 62, 28 },
			new int[] { 6, 6, 14, 30, 54, 102, 198, 255, 255, 6, 6 },
			new int[] { 31, 63, 48, 60, 126, 103, 3, 99, 102, 62, 28 },
			new int[] { 28, 62, 102, 96, 124, 126, 99, 99, 99, 62, 28 },
			new int[] { 255, 255, 3, 6, 6, 12, 12, 24, 24, 48, 48 },
			new int[] { 28, 62, 99, 99, 62, 62, 99, 99, 99, 62, 28 },
			new int[] { 24, 60, 103, 99, 103, 63, 27, 3, 103, 62, 28 } };
	private CaptCha IbetTau;
	private int[][] IbetTauCode = new int[][] {
			new int[] { 62, 99, 99, 99, 99, 99, 99, 99, 62 },
			new int[] { 12, 60, 12, 12, 12, 12, 12, 12, 63 },
			new int[] { 62, 67, 67, 3, 6, 12, 24, 48, 127 },
			new int[] { 62, 67, 3, 3, 30, 3, 3, 67, 62 },
			new int[] { 6, 14, 22, 38, 70, 127, 6, 6, 6 },
			new int[] { 63, 48, 48, 62, 3, 3, 3, 67, 62 },
			new int[] { 30, 48, 96, 126, 99, 99, 99, 99, 62 },
			new int[] { 127, 3, 6, 6, 12, 12, 24, 24, 24 },
			new int[] { 62, 99, 99, 99, 62, 99, 99, 99, 62 },
			new int[] { 62, 99, 99, 99, 99, 63, 3, 6, 60 } };

	public CaptchaUtilities() {
		this.Ibet = new CaptCha(new int[] { 25, 55, 21, 30, 9, 7, 200 },
				this.IbetCode);
		this.IbetTau = new CaptCha(new int[] { 3, 35, 5, 15, 9, 7, 200 },
				this.IbetTauCode);
		this.Ibet2 = new CaptCha(new int[] { 3, 36, 6, 15, 9, 7, 200 },
				this.Ibet2Code);
		this.Sbobet = new CaptCha(new int[] { 9, 54, 7, 21, 14, 10, 170 },
				this.SboCode);
		this.AIbet = new CaptCha(new int[] { 12, 78, 18, 30, 11, 9, -150 },
				this.AIbetCode);
		this.AIbet2 = new CaptCha(new int[] { 1, 56, 1, 18, 11, 9, 103 },
				this.AIbetCode2);
		// this.AIbet2 = new CaptCha(new int[] { 6, 58, 5, 17, 11, 9, 103 },
		// this.AIbetCode);
		this.ASbobet = new CaptCha(new int[] { 7, 63, 1, 19, 18, 13, 130 },
				this.ASbobetCode);
		this.Sb168 = new CaptCha(new int[] { 11, 50, 20, 30, 11, 8, -200 },
				this.Sb168Code);
		this.Sb188 = new CaptCha(new int[] { 11, 50, 20, 30, 11, 8, -200 },
				this.Sb188Code);
		this.ThreeInOne = new CaptCha(new int[] { 7, 52, 7, 16, 11, 8, 200 },
				this.ThreeInOneCode);
	}

	public String GetNumber(String imgPath, Site s) {
		switch (s) {
		case IBET:
			return this.Ibet.GetNumber(imgPath);

		case SBOBET:
			return this.Sbobet.GetNumber(imgPath);

		case AIBET:
			return this.AIbet.GetNumber(imgPath);

		case AIBET2:
			return this.AIbet2.GetNumber(imgPath);

		case ASBOBET:
			return this.ASbobet.GetNumber(imgPath);

		case SB168:
			return this.Sb168.GetNumber(imgPath);

		case IBET2:
			return this.Ibet2.GetNumber(imgPath);

		case SB188:
			return this.Sb188.GetNumber(imgPath);

		case THREEINONE:
			return this.ThreeInOne.GetNumber(imgPath);

		case IBETTAU:
			return this.IbetTau.GetNumber(imgPath);

		}
		return "";
	}

	public String GetNumber(BufferedImage img, Site s) {

		switch (s) {
		case IBET:
			return Ibet.GetNumber(img);
		case SBOBET:
			return Sbobet.GetNumber(img);
		case AIBET:
			return AIbet.GetNumber(img);
		case AIBET2:
			return AIbet2.GetNumber(img);
		case ASBOBET:
			return ASbobet.GetNumber(img);
		case SB168:
			return Sb168.GetNumber(img);
		case IBET2:
			return Ibet2.GetNumber(img);
		case SB188:
			return Sb188.GetNumber(img);
		case THREEINONE:
			return ThreeInOne.GetNumber(img);
		case IBETTAU:
			return IbetTau.GetNumber(img);
		}
		return "";
	}

}
