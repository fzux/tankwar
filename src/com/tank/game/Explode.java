package com.tank.game;


import com.tank.tank.Tank;
import com.tank.util.MyUtil;

import java.awt.*;
import java.util.Random;

/**
 * 控制爆炸效果的类
 */
public class Explode {
    //爆炸4桢
    public static final int EXPLODE_FRAME_COUNT = 12;
    //导入资源
    private static Image[] img;
    //爆炸效果的宽和高
    private static int explodeWidth;
    private static int explodeHeight;

    static {
        img = new Image[EXPLODE_FRAME_COUNT/3];
        for (int i = 0; i < img.length; i++) {
            img[i] = MyUtil.createImage("res/blast"+i+".gif");
        }
        int explodeWidth = img[0].getWidth(null)/2;
        int explodeHeight = img[0].getHeight(null);

    }

    //爆炸效果的属性
    private int x,y;

    //当前播放的桢下标

    private int index;

    private boolean visible = true;

    public Explode() {
        index = 0;
    }

    public Explode(int x, int y) {
        this.x = x;
        this.y = y;
        index = 0;
    }

    public void draw(Graphics g){

        if (explodeHeight <= 0){
            explodeHeight = img[0].getHeight(null);
            explodeWidth = img[0].getWidth(null)>>1;
        }


        if (!visible)return;

        g.drawImage(img[index/3],x-explodeWidth,y-explodeHeight+15,null);
        index ++;
        //播放完最后一帧，设置于不可见
        if (index >= EXPLODE_FRAME_COUNT)
            visible = false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }


    @Override
    public String toString() {
        return "Explode{" +
                "x=" + x +
                ", y=" + y +
                ", index=" + index +
                ", visible=" + visible +
                '}';
    }
}
