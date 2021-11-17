package com.tank.tank;

import com.tank.util.MyUtil;

import java.awt.*;

/**
 * 玩家坦克类
 */
public class MyTank extends Tank{

    //坦克图片
    private static Image[] tankImg;



    static{
        tankImg = new Image[4];
        tankImg[0] = MyUtil.createImage("res/eu.gif");
        tankImg[1] = MyUtil.createImage("res/ed.gif");
        tankImg[2] = MyUtil.createImage("res/el.gif");
        tankImg[3] = MyUtil.createImage("res/er.gif");
    }

    public MyTank(int x, int y, int dir) {
        super(x, y, dir);
    }


    @Override
    public void drawImgTank(Graphics g) {

        g.drawImage(tankImg[getDir()], getX() - RADIUS, getY() - RADIUS, null);
    }



}
