package com.tank.map;

import com.tank.game.Bullet;
//import com.tank.util.Bullet;
import com.tank.util.Constant;
import com.tank.util.MyUtil;

import java.awt.*;
import java.util.List;

/**
 * 地图元素块
 */
public class MapTile {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_HOUSE = 1;
    public static final int TYPE_COVER = 2;
    public static final int TYPE_HARD = 3;
    public static int tileW = 30;
    public static int radius = tileW >> 1;
    private int type = TYPE_NORMAL;

    private static Image[] tileImg;


    static {
        tileImg = new Image[4];
        tileImg[TYPE_NORMAL] = MyUtil.createImage("res/tile.gif");
        tileImg[TYPE_HOUSE] = MyUtil.createImage("res/star.gif");
        tileImg[TYPE_COVER] = MyUtil.createImage("res/grass.png");
        tileImg[TYPE_HARD] = MyUtil.createImage("res/steels.gif");


        if (tileW <= 0) {
            tileW = tileImg[TYPE_NORMAL].getWidth(null);
        }
    }

    //图片资源的左上角
    private int x, y;
    private boolean visible = true;

    public MapTile() {
    }

    public MapTile(int x, int y) {
        this.x = x;
        this.y = y;
        if (tileW <= 0) {
            tileW = tileImg[TYPE_NORMAL].getWidth(null);
        }
    }

    public void draw(Graphics g) {
        if (!visible)
            return;
        if (tileW <= 0) {
            tileW = tileImg[TYPE_NORMAL].getWidth(null);
        }
        //不同类型传不同的
        g.drawImage(tileImg[type], x, y, null);

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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 地图块和子弹是否有碰撞
     *
     * @param bullets
     * @return
     */
    public boolean isCollideBullet(List<Bullet> bullets) {
        if (!visible || type == TYPE_COVER)
            return false;
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            int bulletX = bullet.getX();
            int bulletY = bullet.getY();
            boolean collide = MyUtil.isCollide(x + radius, y + radius, radius, bulletX, bulletY);
            if (collide){
                //子弹销毁
                bullet.setVisible(false);
//                Bullet.theReturn(bullet);
                return true;
            }
        }
        return false;
    }

    //判断当前地图块是否是老巢
    public boolean isHouse(){
        return type == TYPE_HOUSE;
    }


}


