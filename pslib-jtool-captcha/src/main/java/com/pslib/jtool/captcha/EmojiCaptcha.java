package com.pslib.jtool.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.pslib.jtool.captcha.util.CaptchaConstants;
import com.pslib.jtool.captcha.util.CaptchaFileUtil;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Coordinate;
/*
 * 生成4个emoji，点击正确顺序验证
 */
public class EmojiCaptcha {
	public static int rand(int min, int max) {
		return (int) (min + Math.random() * (max - min + 1));
	}

	public static double rand(double min, double max) {
		return (double) (min + Math.random() * (max - min + 1));
	}
	public void entry(OutputStream out, EmojiCaptchaItem[] emojis) {
		BufferedImage _image = createBufferedImage(CaptchaConstants.bgList.get(rand(0, CaptchaConstants.bgList.size() - 1)));
		int width = _image.getWidth();
		int height = _image.getHeight();
		
		try {
			Builder<BufferedImage> thumb = Thumbnails.of(_image).imageType(ThumbnailParameter.DEFAULT_IMAGE_TYPE).scale(1).outputFormat("png");
			BufferedImage emoji;
			for(int i = 0; i < emojis.length; i++) {
				emoji = CaptchaConstants.getEmoji(emojis[i].emojiName);
				thumb.watermark(new Coordinate(emojis[i].x, emojis[i].y), 
						Thumbnails.of(emoji).size(emojis[i].width, emojis[i].height)
						.rotate(emojis[i].rotate).imageType(ThumbnailParameter.DEFAULT_IMAGE_TYPE)
						.asBufferedImage(), 0.5f);
				
			}
			BufferedImage tipImage = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
			Graphics2D _g = (Graphics2D) tipImage.getGraphics();
			_g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			//干扰字
			_writeNoise(_g, width, height);
			
			//提示框
			_g.setColor(new Color(rand(180, 250), rand(180, 250), rand(180, 250)));
			_g.fillRoundRect(5, 90, width-10, height-95, 8, 8);
			
			Color cnColor = translucent(new Color(rand(0, 90), rand(0, 90), rand(0, 90)), 95);
			_g.setColor(cnColor);
			_g.setFont(CaptchaFileUtil.loadFont(CaptchaConstants.cnFont, 18));
			_g.drawString("↑请依次点击↑", 10, 113);
			_g.dispose();
			
			thumb.watermark(new Coordinate(0, 0), 
					Thumbnails.of(tipImage).size(width, height)
					.imageType(ThumbnailParameter.DEFAULT_IMAGE_TYPE)
					.asBufferedImage(), 0.92f);
			int x = 135;
			int new_width = 32;
			int new_height = 32;
			for(int i = 0; i < emojis.length; i++) {
				emoji = CaptchaConstants.getEmoji(emojis[i].emojiName);
				thumb.watermark(new Coordinate(x, 91), 
						Thumbnails.of(emoji).size(new_width, new_height)
						.imageType(ThumbnailParameter.DEFAULT_IMAGE_TYPE)
						.asBufferedImage(), 0.8f);
				x += new_width+5;
			}
			
			thumb.toOutputStream(out);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取emoji列表
	 * @return
	 */
	public EmojiCaptchaItem[] getEmojis() {
		EmojiCaptchaItem[] emojis = new EmojiCaptchaItem[4];
		int x = 5;
		List<Integer> xList = new ArrayList<Integer>();
		BufferedImage emoji;
		for(int i = 0; i < emojis.length;i++) {
			emojis[i] = new EmojiCaptchaItem();
			emojis[i].emojiName = CaptchaConstants.emojiList.get(rand(0, CaptchaConstants.emojiList.size()-1));
			emojis[i].index = i;
			emoji = CaptchaConstants.getEmoji(emojis[i].emojiName);
			emojis[i].width = (int)(emoji.getWidth()*0.9);
			emojis[i].height = (int)(emoji.getHeight()*0.9);
			
			emojis[i].y = rand(-25, 16);
			emojis[i].rotate = rand(-90,90);
			xList.add(x);
			
			x += emojis[i].width + 3;
		}
		int index = 0;
		for(int i = 0; i < emojis.length;i++) {
			index = rand(0, xList.size()-1);
			emojis[i].x = xList.get(index);
			xList.remove(index);
		}
		return emojis;
	}
	/**
	 * 检测点击是否正确
	 * @return
	 */
	public boolean checkClick(EmojiCaptchaItem[] emojis, int[][] clicks) {
		if(clicks.length != emojis.length) {
			return false;
		}
		for(int i = 0; i < emojis.length;i++) {
			EmojiCaptchaItem row = emojis[i];
			int[] click = clicks[i];
			if(click[0] < row.x  || click[1] < row.y || click[0] > (row.x+row.width) || click[1] > (row.y+row.height)) {
				return false;
			}
			
		}
		return true;
	}

	public void _writeNoise(Graphics2D g, int width, int height) {
		String codeSet = "012345678abcdefhijkmnpqrstuvwxyz";
		Color noiseColor;
		int i, j, codeIndex;
		int fontSize = 40;
		for (i = 0; i < 10; i++) {
			for (j = 0; j < 5; j++) {
				noiseColor = translucent(new Color(rand(0, 210), rand(0, 210), rand(0, 210)), 80);
				codeIndex = rand(0, codeSet.length() - 1);
				int _fontsize2 = (int) rand(fontSize * 0.7, fontSize * 0.9);
				g.setFont(new Font("Times New Roman", Font.PLAIN, _fontsize2));
				g.setColor(noiseColor);
				g.drawString(codeSet.substring(codeIndex, codeIndex + 1), rand(-10, width), rand(-10, height));
			}
		}
	}

	/**
	 * 颜色透明
	 * 
	 * @param c
	 * @param newAlpha
	 * @return
	 */
	public static Color translucent(Color c, int newAlpha) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		if (newAlpha < 0) {
			newAlpha = 0;
		}
		if (newAlpha > 255) {
			newAlpha = 255;
		}
		return new Color(r, g, b, newAlpha);
	}

	/**
	 * 复制BufferedImage
	 * 
	 * @param bi
	 * @return
	 */
	public BufferedImage createBufferedImage(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
}
