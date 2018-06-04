package com.example.sell2.vcode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;

import static com.example.sell2.vcode.GifCaptcha.fontName;
import static com.example.sell2.vcode.GifCaptcha.fontStyle;

public class NewC extends Randoms {
    private static Random random = new Random();
    private String chars = null;
    protected char[] alphas() {
        char[] cs = new char[4];
        for (int i = 0; i < 4; i++) {
            cs[i] = alpha();
        }
        chars = new String(cs);
        return cs;
    }
    public String text() {
        alphas();
        return chars;
    }

    private static float getAlpha(int i, int j, int verifySize)
    {
        int num = i + j;
        float r = (float) 1 / verifySize, s = (verifySize + 1) * r;
        return num > verifySize ? (num * r - s) : num * r;
    }
    private static Color[] colorRange =
            {
                    Color.WHITE, Color.CYAN, Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.YELLOW, Color.GREEN, Color.BLUE,
                    Color.DARK_GRAY, Color.BLACK, Color.RED
            };
    public static String outputImage(int w, int h, OutputStream os, String code, String type) throws IOException
    {
        int verifySize = code.length();
        Random rand = new Random();
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color[] colors = new Color[5];
        Color[] colorSpaces = colorRange;
        float[] fractions = new float[colors.length];
        for (int i = 0; i < colors.length; i++)
        {
            colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)];
            fractions[i] = rand.nextFloat();
        }
        Arrays.sort(fractions);

        g2.setColor(Color.GRAY);// 设置边框色
        g2.fillRect(0, 0, w, h);

        Color c = getRandColor(200, 250);
        g2.setColor(c);// 设置背景色
        g2.fillRect(0, 2, w, h - 4);

        char[] charts = code.toCharArray();
        for (int i = 0; i < charts.length; i++)
        {
            g2.setColor(c);// 设置背景色
            g2.setFont(getRandomFont(h, type));
            g2.fillRect(0, 2, w, h - 4);
        }

        // 1.绘制干扰线
        Random random = new Random();
        g2.setColor(getRandColor(160, 200));// 设置线条的颜色
        int lineNumbers = 20;
        if (type.equals("login") || type.contains("mix") || type.contains("3D"))
        {
            lineNumbers = 20;
        }
        else if (type.equals("coupons"))
        {
            lineNumbers = getRandomDrawLine();
        }
        else
        {
            lineNumbers = getRandomDrawLine();
        }
        for (int i = 0; i < lineNumbers; i++)
        {
            int x = random.nextInt(w - 1);
            int y = random.nextInt(h - 1);
            int xl = random.nextInt(6) + 1;
            int yl = random.nextInt(12) + 1;
            g2.drawLine(x, y, x + xl + 40, y + yl + 20);
        }

        // 2.添加噪点
        float yawpRate = 0.05f;
        if (type.equals("login") || type.contains("mix") || type.contains("3D"))
        {
            yawpRate = 0.05f; // 噪声率
        }
        else if (type.equals("coupons"))
        {
            yawpRate = getRandomDrawPoint(); // 噪声率
        }
        else
        {
            yawpRate = getRandomDrawPoint(); // 噪声率
        }
        int area = (int) (yawpRate * w * h);
        for (int i = 0; i < area; i++)
        {
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            int rgb = getRandomIntColor();
            image.setRGB(x, y, rgb);
        }

        // 3.使图片扭曲
       //shear(g2, w, h, c);

        char[] chars = code.toCharArray();
        Double rd = rand.nextDouble();
        Boolean rb = rand.nextBoolean();


         if (type.contains("GIF") || type.contains("mixGIF"))
        {
            GifEncoder gifEncoder = new GifEncoder();

            gifEncoder.start(os);
            gifEncoder.setQuality(180);
            gifEncoder.setDelay(150);
            gifEncoder.setRepeat(0);
            Font rotatedFont =getRandomFont(h, type);
            AlphaComposite ac3;
            for (int i = 0; i < verifySize; i++)
            {
                g2.setColor(getRandColor(100, 160));
                g2.setFont(getRandomFont(h, type));
                for (int j = 0; j < verifySize; j++)
                {
                    AffineTransform affine = new AffineTransform();
                    affine.setToRotation(Math.PI / 4 * rd * (rb ? 1 : -1), (w / verifySize) * i + (h - 4) / 2, h / 2);
                    g2.setFont(rotatedFont);
                    g2.setTransform(affine);

                    g2.drawChars(chars, i, 1, ((w - 20) / verifySize) * i + 5, h / 2 + (h - 4) / 2 - 10);
                    ac3 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha(j, i, verifySize));
                    g2.setComposite(ac3);

                    g2.drawOval(random.nextInt(w), random.nextInt(h), 5 + random.nextInt(10), 5 + random.nextInt(10));
                    gifEncoder.addFrame(image);
                    image.flush();
                }
            }
            gifEncoder.finish();
            g2.dispose();
            return null;
        }
        else if (type.contains("ro")){
             GifEncoder gifEncoder = new GifEncoder();
             gifEncoder.start(os);
             gifEncoder.setQuality(180);
             gifEncoder.setDelay(600);
             gifEncoder.setRepeat(0);
             StringBuilder result=new StringBuilder();
             Font font = getRandomFont(h,type);
            int a=0;
            Font tip=new Font("宋体",Font.BOLD,20);
            g2.setFont(tip);
            g2.setColor(getRandColor(100,160));
            g2.drawString("输入正常字符",5,h/2+10);
            gifEncoder.addFrame(image);
            gifEncoder.addFrame(image);

            image.flush();
             BufferedImage image1 = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB);
            Graphics2D mg2=image1.createGraphics();
            getimage(type,w,h,mg2,image1);
            for (int i = 0; i <verifySize ; i++) {
                mg2.setColor(getRandColor(100, 160));
                 int frag=random.nextInt(4);
                 if (frag>2){
                     AffineTransform affineTransform = new AffineTransform();
                     affineTransform.rotate(Math.toRadians(90), 0, 0);
                     Font rotatedFont = font.deriveFont(affineTransform);
                     mg2.setFont(rotatedFont);
                     mg2.drawChars(chars, i, 1, ((w - 20) / verifySize) * i + 25, h / 3);
                     a=13;
                 }else {
                     mg2.setFont(getRandomFont(h,type));
                     mg2.drawChars(chars, i, 1, ((w - 10) / verifySize) * i + 5+a, h / 2 + (h - 4) / 2 - 10);
                     result.append(chars[i]);
                     a=0;
                 }
             }
            gifEncoder.addFrame(image1);
            gifEncoder.addFrame(image1);
             gifEncoder.addFrame(image1);
            mg2.dispose();
            gifEncoder.finish();
            return result.toString();
        }
        else
        {
            for (int i = 0; i < verifySize; i++)
            {
                g2.setColor(getRandColor(100, 160));
                g2.setFont(getRandomFont(h, type));

                AffineTransform affine = new AffineTransform();
                affine.setToRotation(Math.PI / 4 * rd * (rb ? 1 : -1), (w / verifySize) * i + (h - 4) / 2, h / 2);
                g2.setTransform(affine);
                g2.drawOval(random.nextInt(w), random.nextInt(h), 5 + random.nextInt(10), 5 + random.nextInt(10));
                g2.drawChars(chars, i, 1, ((w - 10) / verifySize) * i + 5, h / 2 + (h - 4) / 2 - 10);
            }
            g2.dispose();
            ImageIO.write(image, "jpg", os);
        }

        return null;
    }



    /**
     * 获取随机颜色
     *
     * @param fc
     * @param bc
     * @return
     */
    private static Color getRandColor(int fc, int bc)
    {
        if (fc > 255)
        {
            fc = 255;
        }
        if (bc > 255)
        {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    private static int getRandomIntColor()
    {
        int[] rgb = getRandomRgb();
        int color = 0;
        for (int c : rgb)
        {
            color = color << 8;
            color = color | c;
        }
        return color;
    }

    private static int[] getRandomRgb()
    {
        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++)
        {
            rgb[i] = random.nextInt(255);
        }
        return rgb;
    }

    /**
     * 随机字体、随机风格、随机大小
     *
     * @param h
     *            验证码图片高
     * @return
     */
    private static Font getRandomFont(int h, String type)
    {
        // 字体
        String name = fontName[random.nextInt(fontName.length)];
        // 字体样式
        int style = fontStyle[random.nextInt(fontStyle.length)];
        // 字体大小
        int size = getRandomFontSize(h);

        if (type.equals("login"))
        {
            return new Font(name, style, size);
        }
        else if (type.equals("coupons"))
        {
            return new Font(name, style, size);
        }
        else if (type.contains("3D"))
        {
            return new FontD.ImgFontByte().getFont(size, style);
        }
        else if (type.contains("mix"))
        {
            int flag = random.nextInt(10);
            if (flag > 4)
            {
                return new Font(name, style, size);
            }
            else
            {
                return new FontD.ImgFontByte().getFont(size, style);
            }
        }
        else
        {
            return new Font(name, style, size);
        }
    }

    /**
     * 干扰线按范围获取随机数
     *
     * @return
     */
    private static int getRandomDrawLine()
    {
        int min = 20;
        int max = 155;
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

    /**
     * 噪点数率按范围获取随机数
     *
     * @return
     */
    private static float getRandomDrawPoint()
    {
        float min = 0.05f;
        float max = 0.1f;
        return min + ((max - min) * random.nextFloat());
    }

    /**
     * 获取字体大小按范围随机
     *
     * @param h
     *            验证码图片高
     * @return
     */
    private static int getRandomFontSize(int h)
    {
        int min = h - 8;
        // int max = 46;
        Random random = new Random();
        return random.nextInt(11) + min;
    }
    private static void shear(Graphics g, int w1, int h1, Color color)
    {
        shearX(g, w1, h1, color);
        shearY(g, w1, h1, color);
    }

    /**
     * x轴扭曲
     *
     * @param g
     *            绘制图形的java工具类
     * @param w1
     *            验证码图片宽
     * @param h1
     *            验证码图片高
     * @param color
     *            颜色
     */
    private static void shearX(Graphics g, int w1, int h1, Color color)
    {
        int period = random.nextInt(2);

        boolean borderGap = true;
        int frames = 1;
        int phase = random.nextInt(2);

        for (int i = 0; i < h1; i++)
        {
            double d = (double) (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
            g.copyArea(0, i, w1, 1, (int) d, 0);
            if (borderGap)
            {
                g.setColor(color);
                g.drawLine((int) d, i, 0, i);
                g.drawLine((int) d + w1, i, w1, i);
            }
        }
    }

    /**
     * y轴扭曲
     *
     * @param g
     *            绘制图形的java工具类
     * @param w1
     *            验证码图片宽
     * @param h1
     *            验证码图片高
     * @param color
     *            颜色
     */
    private static void shearY(Graphics g, int w1, int h1, Color color)
    {
        int period = random.nextInt(40) + 10; // 50;

        boolean borderGap = true;
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < w1; i++)
        {
            double d = (double) (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            if (borderGap)
            {
                g.setColor(color);
                g.drawLine(i, (int) d, i, 0);
                g.drawLine(i, (int) d + h1, i, h1);
            }
        }
    }
    public static void getimage(String type,int w,int h,Graphics2D g2,BufferedImage image){
        Random rand=new Random();
//        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
//        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color[] colors = new Color[5];
        Color[] colorSpaces = colorRange;
        float[] fractions = new float[colors.length];
        for (int i = 0; i < colors.length; i++)
        {
            colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)];
            fractions[i] = rand.nextFloat();
        }
        Arrays.sort(fractions);

        g2.setColor(Color.GRAY);// 设置边框色
        g2.fillRect(0, 0, w, h);

        Color c = getRandColor(200, 250);
        g2.setColor(c);// 设置背景色
        g2.fillRect(0, 2, w, h - 4);


        for (int i = 0; i < 4; i++)
        {
            g2.setColor(c);// 设置背景色
            g2.setFont(getRandomFont(h, type));
            g2.fillRect(0, 2, w, h - 4);
        }

        // 1.绘制干扰线
        Random random = new Random();
        g2.setColor(getRandColor(160, 200));// 设置线条的颜色
        int lineNumbers = 20;
        if (type.equals("login") || type.contains("mix") || type.contains("3D"))
        {
            lineNumbers = 20;
        }
        else if (type.equals("coupons"))
        {
            lineNumbers = getRandomDrawLine();
        }
        else
        {
            lineNumbers = getRandomDrawLine();
        }
        for (int i = 0; i < lineNumbers; i++)
        {
            int x = random.nextInt(w - 1);
            int y = random.nextInt(h - 1);
            int xl = random.nextInt(6) + 1;
            int yl = random.nextInt(12) + 1;
            g2.drawLine(x, y, x + xl + 40, y + yl + 20);
        }

        // 2.添加噪点
        float yawpRate = 0.05f;
        if (type.equals("login") || type.contains("mix") || type.contains("3D"))
        {
            yawpRate = 0.05f; // 噪声率
        }
        else if (type.equals("coupons"))
        {
            yawpRate = getRandomDrawPoint(); // 噪声率
        }
        else
        {
            yawpRate = getRandomDrawPoint(); // 噪声率
        }
        int area = (int) (yawpRate * w * h);
        for (int i = 0; i < area; i++)
        {
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            int rgb = getRandomIntColor();
            image.setRGB(x, y, rgb);
        }


    }

}
