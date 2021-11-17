package com.tank.util;


import javax.tools.Tool;
import java.awt.*;

/**
 * 工具类
 */
public class MyUtil {
    private  MyUtil(){}

    /**
     * 得到指定区间的随机数
     * @param min   区间最小值
     * @param max   区间最大值
     * @return
     */
    public static  final int getRandomNumber(int min,int max){
        return (int)(Math.random()*(max-min)+min);
    }

    /**
     * 得到随机的颜色
     * @return
     */
    public static final Color getRandomColor(){
        int red = getRandomNumber(0,256);
        int blue = getRandomNumber(0,256);
        int green = getRandomNumber(0,256);
        return new Color(red,green,blue);
    }


    /**
     * 判断某一点是否在某一个正方形内部
     * @param rectX 正方形中心点的x坐标
     * @param rectY y坐标
     * @param radius 边长的一半
     * @param pointX 点的x坐标
     * @param pointY 点的y坐标
     * @return 如果在正方形内部，返回true，否则false
     */
    public static final boolean isCollide(int rectX,int rectY,int radius,int pointX,int pointY){
        //正方形中心点x与y轴的距离
        int disX = Math.abs(rectX - pointX);
        int disY = Math.abs(rectY - pointY);
        if (disX < radius && disY < radius)
            return true;

        return false;
    }


    /**
     * 根据图片资源路径，加载图片
     * @param path
     * @return
     */
    public static final Image createImage(String path){
        return Toolkit.getDefaultToolkit().createImage(path);
    }


    private static final String[] NAME = {
            "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"
    };

    private static final String[] NAME2 = {
            "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"
    };

    //随机名字
    public static final String getRandomName(){
        return NAME2[getRandomNumber(0,NAME2.length)]+"_"+NAME[getRandomNumber(0,NAME.length)];
    }



}
