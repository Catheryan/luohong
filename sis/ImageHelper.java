package com.luohong.sis;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

/**
 * ͼƬ�����࣬��Ҫ���ͼƬˮӡ����
 * 
 * @author  025079
 * @version  [�汾��, 2011-11-28]
 * @see  [�����/����]
 * @since  [��Ʒ/ģ��汾]
 */
public class ImageHelper {
	// ��Ŀ��Ŀ¼·��
	/*public static final String path = System.getProperty("user.dir");*/
	public static final String path = "file:///android_asset/processedimages/";
	public static String[] initdata(Context context)
	{
	//other codes...
	      String[] list_image = null;
	     try {
	     //�õ�assets/processedimages/Ŀ¼�µ������ļ����ļ������Ա����򿪲���ʱʹ��
	                list_image = context.getAssets().list("processedimages");//attention this line
	          } catch (IOException e1)
	            {
	                e1.printStackTrace();
	            }
	      //other codes.....
		return list_image;
	}
	/**
	 * ��������ͼ <br/>
	 * ����:ImageIO.write(BufferedImage, imgType[jpg/png/...], File);
	 * 
	 * @param source
	 *            ԭͼƬ
	 * @param width
	 *            ����ͼ��
	 * @param height
	 *            ����ͼ��
	 * @param b
	 *            �Ƿ�ȱ�����
	 * */
	/*public static BufferedImage thumb(BufferedImage source, int width,
			int height, boolean b) {
		// targetW��targetH�ֱ��ʾĿ�곤�Ϳ�
		int type = source.getType();
		BufferedImage target = null;
		double sx = (double) width / source.getWidth();
		double sy = (double) height / source.getHeight();

		if (b) {
			if (sx > sy) {
				sx = sy;
				width = (int) (sx * source.getWidth());
			} else {
				sy = sx;
				height = (int) (sy * source.getHeight());
			}
		}

		if (type == BufferedImage.TYPE_CUSTOM) { // handmade
			ColorModel cm = source.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(width,
					height);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else
			target = new BufferedImage(width, height, type);
		Graphics2D g = target.createGraphics();
		// smoother than exlax:
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
		g.dispose();
		return target;
	}*/
	
	
	
	
	
	
	
	// ��������������ͼ��
    /**
     * Creates a centered bitmap of the desired size. Recycles the input.
     * 
     * @param source
     */
    public static Bitmap extractMiniThumb(Bitmap source, int width, int height) {
        return extractMiniThumb(source, width, height, true);
    }

    public static Bitmap extractMiniThumb(Bitmap source, int width, int height,
            boolean recycle) {
        if (source == null) {
            return null;
        }

        float scale;
        if (source.getWidth() < source.getHeight()) {
            scale = width / (float) source.getWidth();
        } else {
            scale = height / (float) source.getHeight();
        }
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        Bitmap miniThumbnail = transform(matrix, source, width, height, false);

        if (recycle && miniThumbnail != source) {
            source.recycle();
        }
        return miniThumbnail;
    }

    public static Bitmap transform(Matrix scaler, Bitmap source,
            int targetWidth, int targetHeight, boolean scaleUp) {
        int deltaX = source.getWidth() - targetWidth;
        int deltaY = source.getHeight() - targetHeight;
        if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
            /*
             * In this case the bitmap is smaller, at least in one dimension,
             * than the target. Transform it by placing as much of the image as
             * possible into the target and leaving the top/bottom or left/right
             * (or both) black.
             */
            Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight,
                    Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b2);

            int deltaXHalf = Math.max(0, deltaX / 2);
            int deltaYHalf = Math.max(0, deltaY / 2);
            Rect src = new Rect(deltaXHalf, deltaYHalf, deltaXHalf
                    + Math.min(targetWidth, source.getWidth()), deltaYHalf
                    + Math.min(targetHeight, source.getHeight()));
            int dstX = (targetWidth - src.width()) / 2;
            int dstY = (targetHeight - src.height()) / 2;
            Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight
                    - dstY);
            c.drawBitmap(source, src, dst, null);
            return b2;
        }
        float bitmapWidthF = source.getWidth();
        float bitmapHeightF = source.getHeight();

        float bitmapAspect = bitmapWidthF / bitmapHeightF;
        float viewAspect = (float) targetWidth / targetHeight;

        if (bitmapAspect > viewAspect) {
            float scale = targetHeight / bitmapHeightF;
            if (scale < .9F || scale > 1F) {
                scaler.setScale(scale, scale);
            } else {
                scaler = null;
            }
        } else {
            float scale = targetWidth / bitmapWidthF;
            if (scale < .9F || scale > 1F) {
                scaler.setScale(scale, scale);
            } else {
                scaler = null;
            }
        }

        Bitmap b1;
        if (scaler != null) {
            // this is used for minithumb and crop, so we want to filter here.
            b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                    source.getHeight(), scaler, true);
        } else {
            b1 = source;
        }

        int dx1 = Math.max(0, b1.getWidth() - targetWidth);
        int dy1 = Math.max(0, b1.getHeight() - targetHeight);

        Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth,
                targetHeight);

        if (b1 != source) {
            b1.recycle();
        }

        return b2;
    }

    
    
    
	/**
	 * ͼƬˮӡ
	 * 
	 * @param imgPath
	 *            ������ͼƬ
	 * @param markPath
	 *            ˮӡͼƬ
	 * @param x
	 *            ˮӡλ��ͼƬ���Ͻǵ� x ����ֵ
	 * @param y
	 *            ˮӡλ��ͼƬ���Ͻǵ� y ����ֵ
	 * @param alpha
	 *            ˮӡ͸���� 0.1f ~ 1.0f
	 * */
	/*public static void waterMark(String imgPath, String markPath, int x, int y,
			float alpha) {
		try {
			// ���ش�����ͼƬ�ļ�
			Image img = ImageIO.read(new File(imgPath));

			BufferedImage image = new BufferedImage(img.getWidth(null),
					img.getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(img, 0, 0, null);

			// ����ˮӡͼƬ�ļ�
			Image src_biao = ImageIO.read(new File(markPath));
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					alpha));
			g.drawImage(src_biao, x, y, null);
			g.dispose();

			// ���洦�����ļ�
			FileOutputStream out = new FileOutputStream(imgPath);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * ����ˮӡ
	 * 
	 * @param imgPath
	 *            ������ͼƬ
	 * @param text
	 *            ˮӡ����
	 * @param font
	 *            ˮӡ������Ϣ
	 * @param color
	 *            ˮӡ������ɫ
	 * @param x
	 *            ˮӡλ��ͼƬ���Ͻǵ� x ����ֵ
	 * @param y
	 *            ˮӡλ��ͼƬ���Ͻǵ� y ����ֵ
	 * @param alpha
	 *            ˮӡ͸���� 0.1f ~ 1.0f
	 */

	/*public static void textMark(String imgPath, String text, Font font,
			Color color, int x, int y, float alpha) {
		try {
			Font Dfont = (font == null) ? new Font("����", 20, 13) : font;

			Image img = ImageIO.read(new File(imgPath));

			BufferedImage image = new BufferedImage(img.getWidth(null),
					img.getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();

			g.drawImage(img, 0, 0, null);
			g.setColor(color);
			g.setFont(Dfont);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					alpha));
			g.drawString(text, x, y);
			g.dispose();
			FileOutputStream out = new FileOutputStream(imgPath);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			out.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}*/
	
	/**
	 * ��ȡJPEGͼƬ
	 * @param filename �ļ���
	 * @return BufferedImage ͼƬ����
	 */
	/*public static BufferedImage readJPEGImage(String filename)
	{
		try {
			InputStream imageIn = new FileInputStream(new File(filename));
			// �õ�����ı����������ļ�������jpg��ʽ����
			JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(imageIn);
			// �õ�������ͼƬ����
			BufferedImage sourceImage = decoder.decodeAsBufferedImage();
			
			return sourceImage;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ImageFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}*/
	
	
	public static Bitmap getBitmap(String resName) { 
        try {
        	 return BitmapFactory.decodeFile(resName);
        } catch (Exception e) {
           return null;
        }
    }
	
	/**
	 * ��ȡJPEGͼƬ
	 * @param filename �ļ���
	 * @return BufferedImage ͼƬ����
	 */
	/*public static BufferedImage readPNGImage(String filename)
	{
		try {
			File inputFile = new File(filename);  
	        BufferedImage sourceImage = ImageIO.read(inputFile);
			FileInputStream fIn = new FileInputStream(filename);
	        
			//��Ҫ�滻��ߵ����䣺
			 JPEGImageDecoder jpeg_decode = JPEGCodec.createJPEGDecoder(fIn);
			 BufferedImage sourceImage = jpeg_decode.decodeAsBufferedImage();
			return sourceImage;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ImageFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}*/
	
	/**
	 * �Ҷ�ֵ����
	 * @param pixels ����
	 * @return int �Ҷ�ֵ
	 */
	public static int rgbToGray(int pixels) {
		// int _alpha = (pixels >> 24) & 0xFF;
		int _red = (pixels >> 16) & 0xFF;
		int _green = (pixels >> 8) & 0xFF;
		int _blue = (pixels) & 0xFF;
		return (int) (0.3 * _red + 0.59 * _green + 0.11 * _blue);
	}
	
	/**
	 * ���������ƽ��ֵ
	 * @param pixels ����
	 * @return int ƽ��ֵ
	 */
	public static int average(int[] pixels) {
		float m = 0;
		for (int i = 0; i < pixels.length; ++i) {
			m += pixels[i];
		}
		m = m / pixels.length;
		return (int) m;
	}
}
