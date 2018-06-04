package com.example.sell2.vcode;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * <p>Gif验证码类</p>
 *
 * @author: wuhongjun
 * @version:1.0
 */
public class GifCaptcha extends Captcha
{
    public GifCaptcha()
    {
    }

    public GifCaptcha(int width,int height){
        this.width = width;
        this.height = height;
    }

    public GifCaptcha(int width,int height,int len){
        this(width,height);
        this.len = len;
    }

    public GifCaptcha(int width,int height,int len,Font font)
    {
        this(width,height,len);
        this.font = font;
    }

    @Override
    public void out(OutputStream os)
    {
        try
        {
            GifEncoder gifEncoder = new GifEncoder();   // gif编码类，这个利用了洋人写的编码类，所有类都在附件中
            //生成字符
            gifEncoder.start(os);
            gifEncoder.setQuality(180);
            gifEncoder.setDelay(100);
            gifEncoder.setRepeat(0);
            BufferedImage frame;
            char[] rands =alphas();
            Color fontcolor[]=new Color[len];
            for(int i=0;i<len;i++)
            {
                fontcolor[i]=new Color(20 + num(110), 20 + num(110), 20 + num(110));
            }
            for(int i=0;i<len;i++)
            {
                frame=graphicsImage(fontcolor, rands, i);
                gifEncoder.addFrame(frame);
                frame.flush();
            }
            gifEncoder.finish();
        }finally
        {
        	try {
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

    }

    /**
     * 画随机码图
     * @param fontcolor 随机字体颜色
     * @param strs 字符数组
     * @param flag 透明度使用
     * @return BufferedImage
     */
    private BufferedImage graphicsImage(Color[] fontcolor,char[] strs,int flag)
    {
        Random rand=new Random();
        Double rd = rand.nextDouble();
        Boolean rb = rand.nextBoolean();
        BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d=image.createGraphics();
//        Graphics2D g2d = (Graphics2D)image.getGraphics();
        //利用指定颜色填充背景
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        AlphaComposite ac3;
        int h  = height - ((height - font.getSize()) >>1) ;
        int w = width/len;

        for(int i=0;i<len;i++)
        {

            ac3 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha(flag, i));
            g2d.setComposite(ac3);
            g2d.setFont(getRandomFont(height));
            g2d.setColor(fontcolor[i]);
            g2d.drawOval(num(width), num(height), 5+num(10), 5+num(10));
//            g2d.drawString(""+strs[i], font.getSize()+20*i-15, h-4);
            g2d.drawChars(strs, i, 1, ((width - 10) / len) * i + 5, height / 2 + (height - 4) / 2 - 10);

        }
        //(width-(len-i)*w)+(w-font.getSize())-3
        g2d.dispose();
        return image;
    }

    /**
     * 获取透明度,从0到1,自动计算步长
     * @return float 透明度
     */
    public float getAlpha(int i,int j)
    {
        return 1;
//        if (i==1&&j==1){
//            return 0;
//        }
//        int num = i+j;
//
//        float r = (float)1/len,s = (len+1) * r;
//        return num > len ? (num *r - s) : num * r;
    }
    public static String[] fontName = {
                    "Algerian", "Arial", "Arial Black", "Agency FB", "Calibri", "Cambria", "Gadugi", "Georgia", "Consolas", "Comic Sans MS", "Courier New",
                    "Gill sans", "Time News Roman", "Tahoma", "Quantzite", "Verdana"
            };
    public static int[] fontStyle = {
                    Font.BOLD, Font.ITALIC, Font.ROMAN_BASELINE, Font.PLAIN, Font.BOLD + Font.ITALIC
            };
    public static int getRandomFontSize(int h)
    {
        int min = h - 8;
        // int max = 46;
        Random random = new Random();
        return random.nextInt(11) + min;
    }
    public static Font getRandomFont(int h){
        Random random = new Random();
        String name = fontName[random.nextInt(fontName.length)];
        int style = fontStyle[random.nextInt(fontStyle.length)];
        int size=getRandomFontSize(h);
        int flag=random.nextInt(10);
        if (flag>4){
            return new Font(name,style,size);
        }else {
            return FontD.ImgFontByte.getFont(size,style);
        }
    }
    public static float getRandomDrawPoint()
    {
        Random rand=new Random();
        float min = 0.05f;
        float max = 0.1f;
        return min + ((max - min) * rand.nextFloat());
    }

}
