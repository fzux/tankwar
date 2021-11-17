package com.tank.tank;

import com.tank.game.GameFrame;
import com.tank.game.LevelInof;
import com.tank.util.Constant;
import com.tank.util.EnemyTanksPool;
import com.tank.util.MyUtil;

import java.awt.*;
import java.util.Random;

/**
 * 敌人坦克类
 */
public class EnemyTank extends Tank{
    private static final int TYPE_GREEN = 0;
    private static final int TYPE_YELLOW = 1;
    private static final int TYPE_WHITE = 2;
    private int type = TYPE_GREEN;

    private static Image[] greenImg;
    private static Image[] yellowImg;
    private static Image[] whiteImg;

    //用于记录5秒开始的时间
    private long aiTime;






    static{
        whiteImg = new Image[4];
        whiteImg[0] = MyUtil.createImage("res/uu.gif");
        whiteImg[1] = MyUtil.createImage("res/ud.gif");
        whiteImg[2] = MyUtil.createImage("res/ul.gif");
        whiteImg[3] = MyUtil.createImage("res/ur.gif");
        yellowImg = new Image[4];
        yellowImg[0] = MyUtil.createImage("res/3U.gif");
        yellowImg[1] = MyUtil.createImage("res/3D.gif");
        yellowImg[2] = MyUtil.createImage("res/3L.gif");
        yellowImg[3] = MyUtil.createImage("res/3R.gif");
        greenImg = new Image[4];
        greenImg[0] = MyUtil.createImage("res/p2U.gif");
        greenImg[1] = MyUtil.createImage("res/p2D.gif");
        greenImg[2] = MyUtil.createImage("res/p2L.gif");
        greenImg[3] = MyUtil.createImage("res/p2R.gif");
    }

    public EnemyTank(int x, int y, int dir) {

        super(x, y, dir);
        //敌人一旦创建就开始计时
        aiTime = System.currentTimeMillis();
        type = MyUtil.getRandomNumber(0,2);
    }

    public EnemyTank(){
        type = MyUtil.getRandomNumber(0,2);
        aiTime = System.currentTimeMillis();
    }



    //创建敌人的坦克
    public static Tank createEnemy(){
        int x = MyUtil.getRandomNumber(0,2) == 0 ? RADIUS : Constant.FRAME_WIDTH-RADIUS;
        int y = GameFrame.titleBarH + RADIUS;
        int dir = DIR_DOWN;
        EnemyTank enemy = (EnemyTank)EnemyTanksPool.get();
        enemy.setX(x);
        enemy.setY(y);
        enemy.setDir(dir);
        enemy.setEnemy(true);
        enemy.setState(STATE_MOVE);
        enemy.setHp(Tank.DEFAULT_HP);
        enemy.setSpeed(3+LevelInof.getInstance().getLevel());
        enemy.setType(MyUtil.getRandomNumber(0,3));
        return enemy;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void drawImgTank(Graphics g) {
        int typetank;

        ai();

        g.drawImage(type == TYPE_GREEN ? greenImg[getDir()] : (type==TYPE_YELLOW?yellowImg[getDir()]:whiteImg[getDir()]),
                getX()-RADIUS,getY()-RADIUS,null );



//        g.drawImage(type == TYPE_GREEN ? greenImg[getDir()] : yellowImg[getDir()],
//                getX()-RADIUS,getY()-RADIUS,null );
    }

    /**
     * 敌人的ai
     */
    private void ai(){
        if (System.currentTimeMillis() - aiTime > Constant.ENEMY_AI_INTERVAL){
            //每隔五秒随机一个状态
            setDir(MyUtil.getRandomNumber(DIR_UP,DIR_RIGHT+1));
            setState(MyUtil.getRandomNumber(0,2) == 0 ? STATE_STAND : STATE_MOVE);
            aiTime = System.currentTimeMillis();

        }
        //小概率开火
        if (Math.random() < Constant.ENEMY_FIRE_PERCENT){
            fire();
        }


    }


}
