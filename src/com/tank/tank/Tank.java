package com.tank.tank;

import com.tank.game.Bullet;
import com.tank.game.Explode;
import com.tank.game.GameFrame;
import com.tank.map.MapTile;
import com.tank.util.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.tank.game.GameFrame.isCrossLevel;
import static com.tank.game.GameFrame.setGameState;
import static com.tank.util.Constant.*;

/**
 * 坦克类
 */

public abstract class Tank {



    //四个方向
    public static final int DIR_UP = 0;
    public static final int DIR_DOWN = 1;
    public static final int DIR_LEFT = 2;
    public static final int DIR_RIGHT = 3;
    //半径
    public static final int RADIUS = 15;
    //默认速度,每一帧(30ms)跑6像素
    public static int DEFAULT_SPEED = 6;
    private int x,y;
    private int hp = DEFAULT_HP;

    private String name;

    private int atk;
    public static final int ATK = 31;
    private int speed = DEFAULT_SPEED;
    private int dir;
    //状态
    private int state = STATE_STAND;
    private  Color color;
    //标记是否为敌人
    private boolean isEnemy = false;

    public static final int STATE_STAND = 0;
    public static final int STATE_MOVE = 1;
    public static final int STATE_DIE = 2;
    //坦克的初始生命
    public static final int DEFAULT_HP = 60;
    public static final int DEFAULT_HP1 = 30;
    public static final int DEFAULT_HP2 = 90;
    private int maxHP = DEFAULT_HP;

    private BloodBar bar = new BloodBar();


    //炮弹
    private List<Bullet> bullets = new ArrayList();

    private List<Explode>explodes = new ArrayList<>();


    //创建玩家的坦克
    public Tank(int x, int y, int dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;


        initTank();
    }

    public Tank(){
        initTank();
    }

    private void initTank(){
        this.tankLife = Constant.Life;
        color = MyUtil.getRandomColor();
        atk = ATK;
        name = MyUtil.getRandomName();
//        atk = MyUtil.getRandomNumber(ATK_MIN,ATK_MAX);
        //todo name
    }


    /**
     * 绘制坦克
     * @param g
     */
    public void draw(Graphics g){
        //logic方法每一帧都要更新，draw每一帧都要绘画，放在一块
        logic();

        drawImgTank(g);

        drawBullets(g);

        drawName(g);

        bar.draw(g);

    }


    private void drawName(Graphics g){
        g.setColor(color);
        g.setFont(Constant.SMALL_FONT);
        g.drawString(name,x - RADIUS, y - 30);
    }


    /**
     * 使用图片的方式绘制坦克
     * @param g
     */

    public abstract void drawImgTank(Graphics g);


    /**
     * 使用系统的方式去绘制坦克
     * @param g
     */
    public void drawTank(Graphics g){
        g.setColor(color);
        int endX = x;
        int endY = y;
        switch (dir){
            case DIR_UP:
                endY = y - RADIUS * 2;
                g.drawLine(x-1,y,endX-1,endY);
                g.drawLine(x+1,y,endX+1,endY);
                break;
            case DIR_DOWN:
                endY = y + RADIUS * 2;
                g.drawLine(x-1,y,endX-1,endY);
                g.drawLine(x+1,y,endX+1,endY);
                break;
            case DIR_LEFT:
                endX = x - 2 * RADIUS ;
                g.drawLine(x,y-1,endX,endY-1);
                g.drawLine(x,y+1,endX,endY+1);
                break;
            case DIR_RIGHT:
                endX = x + RADIUS * 2;
                g.drawLine(x,y-1,endX,endY-1);
                g.drawLine(x,y+1,endX,endY+1);
                break;
        }
        //画线，两点确定一条直线
        g.drawLine(x,y,endX,endY);
    }


    //坦克的逻辑处理
    private void logic(){
        switch (state){
            case STATE_STAND:
                break;
            case STATE_MOVE:
                move();
                break;
            case STATE_DIE:
                break;
        }
    }

    //坦克移动的功能
    private int oldX = -1, oldY = -1;
    private void move(){
        oldX = x;
        oldY = y;
        switch (dir){
            case DIR_UP:
                y -= speed;
                if (y < RADIUS + GameFrame.titleBarH){
                    y = RADIUS + GameFrame.titleBarH;
                }
                break;
            case DIR_DOWN:
                y +=speed;
                if (y > Constant.FRAME_HEIGHT - RADIUS){
                    y = Constant.FRAME_HEIGHT - RADIUS;
                }
                break;
            case DIR_LEFT:
                x -=speed;
                if (x < RADIUS){
                    x = RADIUS;
                }
                break;
            case DIR_RIGHT:
                x +=speed;
                if (x > Constant.FRAME_WIDTH - RADIUS){
                    x = Constant.FRAME_WIDTH - RADIUS;
                }
                break;
        }
    }
    private int tankLife=0;

    public static int getDirUpa(){
        return DIR_UP;
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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List getBullets() {
        return bullets;
    }

    public void setBullets(List bullets) {
        this.bullets = bullets;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public void setEnemy(boolean enemy) {
        isEnemy = enemy;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 坦克的功能，开火发射炮弹
     * 创建了一个子弹对象，子弹对象的属性信息通过坦克的信息获得
     * 然后创建的子弹添加到坦克管理的容器中
     */
    public void fire(){

        if (bullets.size()==0) {//上一发子弹消失

//            MusicUtil.playBomb();

            int bulletX = x;
            int bulletY = y;
            switch (dir) {
                case DIR_UP:
                    bulletY -= RADIUS;
                    break;
                case DIR_DOWN:
                    bulletY += RADIUS;
                    break;
                case DIR_LEFT:
                    bulletX -= RADIUS;
                    break;
                case DIR_RIGHT:
                    bulletX += RADIUS;
                    break;
            }

            //从对象池中获取子弹对象
            Bullet bullet = new Bullet();//Bullet.get();
//            //设置子弹属性
            bullet.setX(bulletX);
            bullet.setY(bulletY);
            bullet.setDir(dir);
            bullet.setAtk(atk);
            bullet.setColor(color);
            bullet.setVisible(true);
            bullets.add(bullet);

        }
    }

    /**
     * 将坦克当前发射的所有子弹绘制出来
     * @param g
     */
    private void drawBullets(Graphics g){

        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.draw(g);
        }

        //遍历所有的子弹，将不可见的子弹移除，并还原为对象池
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (!bullet.isVisible()) {
                Bullet remove = bullets.remove(i);
                i--;
//                BulletsPool.theReturn(remove);
            }
        }
    }

    /**
     * 坦克销毁的时候处理坦克所有的子弹
     */
    public void bulletsReturn(){
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
//            BulletsPool.theReturn(bullet);
        }
        bullets.clear();
    }



    //坦克和子弹碰撞的方法
    public void collideBullets(List<Bullet> bullets){
        //遍历所有的子弹，依次和当前坦克进行碰撞检测
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            int bulletX = bullet.getX();
            int bulletY = bullet.getY();
            if (MyUtil.isCollide(this.x,y,RADIUS,bulletX,bulletY)){
                //子弹消失
                bullet.setVisible(false);
                //坦克受到伤害
                hurt(bullet);
                //添加爆炸效果
                addExplode(x,y+RADIUS);

            }
        }
    }



    //子弹和子弹碰撞的方法
    public void BulletscollideBullets(List<Bullet> bullets){
        //遍历所有的子弹
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            int bulletX = bullet.getX();
            int bulletY = bullet.getY();
            for (int j = i; j < bullets.size(); j++) {
                if (j==i)
                    continue;;
                Bullet bullet1 = bullets.get(j);
                int bullet1X = bullet.getX();
                int bullet1Y = bullet.getY();
                if (bullet1X==bulletX && bullet1Y == bulletY){
                    //子弹消失
                    bullet.setVisible(false);
                    bullet1.setVisible(false);
                }
            }
        }
    }











    private void addExplode(int x,int y){
        //爆炸效果
        Explode explode = ExplodesPool.get();
        explode.setX(x);
        explode.setY(y);
        explode.setVisible(true);
        explode.setIndex(0);
        explodes.add(explode);
    }


    //坦克受到伤害
    private void hurt(Bullet bullet){
        final int atk = bullet.getAtk();
        hp -= atk;
        if (hp < 0){
            hp = 0;
            die();
        }
    }



    //坦克死亡需要处理的内容
    private void die(){



        if (isEnemy){
            GameFrame.killEnemyCount ++;
            EnemyTanksPool.theReturn(this);
            //本关是否结束
            if (GameFrame.isCrossLevel()){
                //判断游戏是否通过
                if(GameFrame.isLastLevel()){
                    //通关了
                    setGameState(Constant.STATE_WIN);
                }
                else{
                    DEFAULT_SPEED = DEFAULT_SPEED;
                    GameFrame.nextLevel();
                }
            }

        }
        else {
            if (tankLife > 0){
                tankLife--;
                hp=DEFAULT_HP;
                setGameState(STATE_RUNNING);
            }


            if(tankLife==0){
                delaySecondsToOver(500);
            }
        }
    }

    private void drawImgTank() {
    }


    /**
     * 判断当前坦克是否死亡
     * @return
     */
    public boolean isDie(){
        return hp <= 0;
    }


    /**
     * 绘制当前坦克上所有的爆炸效果
     */
    public void drawExplodes(Graphics g){
        for (int i = 0; i < explodes.size(); i++) {
            Explode explode = explodes.get(i);
            explode.draw(g);
        }
        //将不可见的爆炸效果删除，还回对象池
        for (int i = 0; i < explodes.size(); i++) {
            Explode explode = explodes.get(i);
            if (!explode.isVisible()){
                Explode remove = explodes.remove(i);
                ExplodesPool.theReturn(remove);
                i--;
            }
        }
    }



    //内部类表示血条
    class BloodBar{
        public static final int BAR_LENGTH = 30;
        public static final int BAR_HEIGHT = 3;

        public void draw(Graphics g){
            //血条底色
            g.setColor(Color.YELLOW);
            g.fillRect(x - RADIUS , y - RADIUS - BAR_HEIGHT*2,BAR_LENGTH,BAR_HEIGHT);
            //红色血条
            g.setColor(Color.RED);
            g.fillRect(x - RADIUS , y - RADIUS - BAR_HEIGHT*2,hp*BAR_LENGTH/maxHP,BAR_HEIGHT);
            //蓝色边框
            g.setColor(Color.WHITE);
            g.drawRect(x - RADIUS , y - RADIUS - BAR_HEIGHT*2,BAR_LENGTH,BAR_HEIGHT);
        }
    }

    //坦克的子弹和地图块的碰撞
    public void bulletsCollideMapTiles(List<MapTile> tiles){
//        for (MapTile tile : tiles) {
            for (int i = 0; i < tiles.size(); i++) {
                MapTile tile = tiles.get(i);
                if (tile.isCollideBullet(bullets)){
                    //添加爆炸效果
                    addExplode(tile.getX()+MapTile.radius,tile.getY()+MapTile.tileW+RADIUS*2);
                    //地图水泥块不被击毁
                    if (tile.getType() == MapTile.TYPE_HARD)
                        continue;
                    //设置地图块销毁
                    tile.setVisible(false);
                    //归还对象池
                    MapTilePool.theReturn(tile);
                    //老巢被击毁后，切换到游戏结束画面
                    if (tile.isHouse()){
                        delaySecondsToOver(500);
                    }
                }
            }

        }



    /**
     * 延迟若干毫秒切换到游戏结束
     * @param millisSecond
     */
    private void delaySecondsToOver(int millisSecond){
        new Thread(){
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setGameState(Constant.STATE_LOST);
            }
        }.start();
    }


    //一个地图块和坦克碰撞的方法,从tile中提取8个点来判断是否有其中任何一个点和当前坦克有碰撞
    public boolean isCollideTile(List<MapTile> tiles){
        final int len = 2;
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            if (!tile.isVisible() || tile.getType() == MapTile.TYPE_COVER)
                continue;
            //点1  左上角
            int tileX = tile.getX();
            int tileY = tile.getY();
            boolean collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            //碰上直接返回，否则继续判断下一个点
            if (collide){
                return true;
            }
            //点2  中上点
            tileX += MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide){
                return true;
            }
            //点3  右上角
            tileX += MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide){
                return true;
            }
            //点4  右中
            tileY += MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide){
                return true;
            }
            //点5  右下点
            tileY += MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide){
                return true;
            }
            //点6  中下点
            tileX -= MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide){
                return true;
            }
            //点7  左下点
            tileX -= MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide){
                return true;
            }
            //点8  左中点
            tileY -= MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide){
                return true;
            }
        }



        return false;
    }

    //坦克回退的方法
    public void back(){
        x = oldX;
        y = oldY;
    }


    public void reback(){
        x = 400;
        y = 200;
    }


//    public int getMaxHP() {
//        return maxHP;
//    }
//
//    public void setMaxHP(int maxHP) {
//        this.maxHP = maxHP;
//    }
}






