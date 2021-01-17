package com.pslib.jtool.captcha;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.pslib.jtool.captcha.util.CaptchaConstants;
import com.pslib.jtool.captcha.util.CaptchaFileUtil;
import com.pslib.jtool.captcha.util.CaptchaStringUtil;

public class ImageCaptcha {
	Graphics2D _g = null; // 验证码图片实例
	
	
	//public boolean useImgBg = false; // 使用背景图片
	/**
	 * 是否画混淆曲线
	 */
	public boolean useCurve = true;
	/**
	 * 是否添加杂点
	 */
	public boolean useNoise = true;
	/**
	 * 是否字体大小随机
	 */
	public boolean useRandFontSize = true;
	
	/**
	 * 验证码字体大小(px)
	 */
	public int fontSize = 25;
	/**
	 * 验证码图片宽度
	 */
	public int width = 0;
	/**
	 * 验证码图片高度
	 */
	public int height = 0;
	
	public int fontMarginLeft = 20;
	
	/**
	 * 线条宽度
	 */
	public int dotStroke = 1;
	
	/**
	 * 验证码位数
	 */
	public int codeLength = 5;
	/**
	 * 验证码类型 word/word_lcase/word_ucase/en/en_lcase/en_ucase/num/cn  /custom
	 */
	public String codeType = "word";
	/**
	 * 自定义验证码字符，验证码类型为custom时启用
	 */
	public String[] customCodeStrings = null;
	
	/**
	 * 验证码字体颜色
	 */
	public Color fontColor = null;
	/**
	 * 背景颜色
	 */
	public Color bgColor = new Color(243, 251, 254);


	public ImageCaptcha(){
		
	}

	public static void main(String[] args) {

	}

	/**
	 * 获取验证码字符
	 * @return
	 */
	public String getCodeStr() {
		return CaptchaStringUtil.join(getCodes());
	}
	/**
	 * 获取验证码字符数组
	 * @return
	 */
	public String[] getCodes() {
		String[] codeStrings = null;
		if(codeType.equals("custom")){
			codeStrings = customCodeStrings != null ? customCodeStrings : CaptchaConstants.codeStrings.get("word");
		}else if(CaptchaConstants.codeStrings.containsKey(codeType)){
			codeStrings = CaptchaConstants.codeStrings.get(codeType);
		}else{
			codeStrings = CaptchaConstants.codeStrings.get("word");
		}
		String[] codes = new String[codeLength];
		for (int i = 0; i < codeLength; i++) {
			codes[i] = codeStrings[rand(0, codeStrings.length - 1)];
		}
		return codes;
	}

	public Font getFont() {
		return CaptchaConstants.fontList.get(rand(0, CaptchaConstants.fontList.size() - 1));
	}

	/**
	 * 输出验证码并把验证码的值保存的session中 验证码保存到session的格式为： array('verify_code' =>
	 * '验证码值', 'verify_time' => '验证码创建时间');
	 * 
	 * @access public
	 * @param string
	 *                $id 要生成验证码的标识
	 * @return void
	 */
	public void entry(OutputStream out, String[] codes) {
		width = (int) (width == 0 ? (double) fontMarginLeft + codeLength * (double) fontSize * 1.5 : width);
		height = (int) (height == 0 ? (double) fontSize * 2.4 : height);
		BufferedImage _image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		_g = _image.createGraphics();
		_g.setStroke(new BasicStroke(dotStroke)); 

		// 设定背景色
		_g.setColor(bgColor);
		_g.fillRect(0, 0, width, height);
		
		fontColor = new Color(rand(1, 150), rand(1, 150), rand(1, 150));
		
		// if(this.useImgBg) {
		// this._background();
		// }
		if (useNoise) {// 绘杂点
			_writeNoise();
		}
		if (useCurve) {// 绘干扰线
			_writeCurve();
		}
		int codeNX = fontMarginLeft, codeNY = 0;
		int fontsize2;
		Font font = getFont();
		//System.out.println(font.getFontName());
		
		_g.setColor(fontColor);
		/* 消除java.awt.Font字体的锯齿 */
		_g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		double radianPercent;
		for (int i = 0; i < codes.length; i++) {
			fontsize2 = useRandFontSize ? (int) rand(fontSize * 1, fontSize * 1.3) : fontSize;
			
			
			_g.setFont(CaptchaFileUtil.loadFont(font, fontsize2));
			codeNY = fontsize2;
			
			radianPercent = Math.PI * (rand(-40, 40) / 180D);
			_g.rotate(radianPercent, codeNX + 9, codeNY);
			_g.drawString(codes[i], codeNX, codeNY);
			_g.rotate(-radianPercent, codeNX + 9, codeNY);
			
			codeNX += fontsize2;
		}

		// 图象生效
		_g.dispose();


		// 输出图象到页面
		try {
			ImageIO.write(_image, "PNG", out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void _writeCurve() {
		int px = 0, py = 0, i;

		// 曲线前部分
		int A = rand(1, height / 2); // 振幅
		int b = rand(-height / 4, height / 4); // Y轴方向偏移量
		int f = rand(-height / 4, height / 4); // X轴方向偏移量
		int T = rand(height, width * 2); // 周期
		double w = (2 * Math.PI) / T;

		int px1 = 0; // 曲线横坐标起始位置
		int px2 = (int) rand(width / 2, width * 0.8); // 曲线横坐标结束位置

		for (px = px1; px <= px2; px = px + 1) {
			if (w != 0) {
				py = (int) (A * Math.sin(w * px + f) + b + height / 2);
				i = (int) (fontSize / 5);
				while (i > 0) {
					_g.setColor(fontColor);
					_g.drawLine(px + i, py + i, px + i + 1, py + i + 1);
					i--;
				}
			}
		}

		// 曲线后部分
		A = rand(1, height / 2); // 振幅
		f = rand(-height / 4, height / 4); // X轴方向偏移量
		T = rand(height, width * 2); // 周期
		w = (2 * Math.PI) / T;
		b = (int) (py - A * Math.sin(w * px + f) - height / 2);
		px1 = px2;
		px2 = width;

		for (px = px1; px <= px2; px = px + 1) {
			if (w != 0) {
				py = (int) (A * Math.sin(w * px + f) + b + height / 2); 
				i =  (int) (fontSize / 5);
				while (i > 0) {
					_g.drawLine(px + i, py + i, px + i + 1, py + i + 1);
					i--;
				}
			}
		}
	}

	public void _writeNoise() {
		String codeSet = "012345678abcdefhijkmnpqrstuvwxyz";
		Color noiseColor;
		int i, j, codeIndex;
		int _fontsize2 = (int) (fontSize*0.8);
		for (i = 0; i < 10; i++) {
			noiseColor = new Color(rand(150, 225), rand(150, 225), rand(150, 225));
			for (j = 0; j < 5; j++) {
				codeIndex = rand(0, codeSet.length() - 1);
				_g.setFont(new Font("Times New Roman", Font.PLAIN, _fontsize2));
				_g.setColor(noiseColor);
				_g.drawString(codeSet.substring(codeIndex, codeIndex + 1), rand(-10, width), rand(-10, height));
			}
		}
	}



	public static int rand(int min, int max) {
		return (int) (min + Math.random() * (max - min + 1));
	}

	public static double rand(double min, double max) {
		return (double) (min + Math.random() * (max - min + 1));
	}
}
