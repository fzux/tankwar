package com.tank.game;

import com.tank.map.GameMap;
import com.tank.map.MapTile;
import com.tank.tank.EnemyTank;
import com.tank.tank.MyTank;
import com.tank.tank.Tank;
import com.tank.util.MusicUtil;
import com.tank.util.MyUtil;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static com.tank.util.Constant.*;//引入常数类

/**
 * 游戏主窗口
 * 所有的游戏展示都在该类中实现
 */
public class GameFrame extends Frame implements Runnable{

    //第一次使用结束图片的时候加载，不是直接加载，效率高
    private Image overImg = null;

    //定义一张和屏幕大小一致的图片
    private BufferedImage bufImg = new BufferedImage(FRAME_WIDTH,FRAME_HEIGHT,BufferedImage.TYPE_4BYTE_ABGR);

    //游戏状态
    private static int gameState;
    //菜单选中状态
    private static int menuIndex;
    //标题栏高度
    public static int titleBarH;

    //定义坦克对象
    private static Tank myTank;
    private static EnemyTank myTank1;

    //敌人的坦克容器
    private static ArrayList<Tank> enemies = new ArrayList<>();

    //用来记录本关产生了多少敌人
    private static int bornEnemyCount;

    public static int killEnemyCount;

    //定义地图相关内容
    private static GameMap gameMap = new GameMap();


    /**
     * 对窗口进行初始化
     */
    public GameFrame() {

        MusicUtil.playBackgroundmusic();


        initFrame();
        initEventListener();
        //启动用于刷新窗口的线程
        new Thread(this).start();
    }

    /**
     * 进入下一关的方法
     */
    public static void nextLevel() {
        startGame(LevelInof.getInstance().getLevel()+1);
    }

    /**
     * 对游戏进行初始化
     */
    private void initGame(){
        gameState = STATE_MENU;
    }

    /**
     * 对属性进行初始化
     */
    private void initFrame(){
        //设置窗口标题,通过引入类，也可以括号内双引号放字符串，但是建立常量类便于维护
        setTitle(GAME_TITLE);
        //设置窗口大小
        setSize(FRAME_WIDTH,FRAME_HEIGHT);
        //设置窗口左上角坐标，默认左上角为（0，0），往左为x轴正方向，往下为y轴正方向
        setLocation(FRAME_X,FRAME_Y);
        //设置窗口大小不可改变
        setResizable(false);
        //设置窗口可见
        setVisible(true);
        //求标题栏的高度
        titleBarH = getInsets().top;
    }




    /**
     * 是Frame类的方法，是继承下来的方法，该方法负责了所有绘制的内容，所有需要在屏幕中显示的内容都要在该方法内调用
     * 该方法不能主动调用。必须通过调用repaint()；去回调该方法。
     * @param g1  系统提供的画笔，系统进行初始化
     */
    public void update(Graphics g1) {
        //得到图片的画笔
        Graphics g = bufImg.getGraphics();

        g.setFont(GAME_FONT);
        switch (gameState){
            case STATE_MENU:
                drawMenu(g);
                break;
            case STATE_MUSIC:
                drawMusic(g);
                break;
            case STATE_TWORUN:
                drawTworun(g);
                break;
            case STATE_RUNNING:
                drawRunning(g);
                break;
            case STATE_LOST:
                drawLost(g,"lose");
                break;
            case STATE_WIN:
                drawWin(g);
                break;
        }

        //用系统画笔，将图片绘制到frame上
        g1.drawImage(bufImg,0,0,null);
    }

    /**
     * 绘制游戏结束
     * @param g
     */
    private void drawLost(Graphics g,String str) {
        if (overImg == null){
            overImg = MyUtil.createImage("res/over.gif");
        }

        //黑背景填充
        g.setColor(Color.black);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);

        //居中绘制
        int imgW = overImg.getWidth(null);
        int imgH = overImg.getHeight(null);

        g.drawImage(overImg,FRAME_WIDTH - imgW >> 1,FRAME_HEIGHT - imgH >> 1,null);

        //添加按键提示信息
        g.setColor(Color.WHITE);
        g.drawString(OVER_STR0,10,FRAME_HEIGHT-20);
        g.drawString(OVER_STR1,FRAME_WIDTH-200,FRAME_HEIGHT-20);

        //失败
        g.setColor(Color.WHITE);
        g.drawString(str,FRAME_WIDTH/2-30,50);

    }

    //绘制游戏胜利
    private void drawWin(Graphics g){
        drawLost(g,"win!");

    }

    //游戏运行状态的绘制内容
    private void drawRunning(Graphics g) {


        //绘制黑色的背景
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);

        //绘制地图碰撞部分
        gameMap.drawBK(g);

        drawEnemies(g);

        myTank.draw(g);

        //绘制地图的遮挡部分

        gameMap.drawCover(g);

        drawExplodes(g);

        //子弹和坦克的碰撞方法
        bulletCollideTank();

        //子弹和所有的地图块的碰撞
        bulletAndTanksCollideMapTile();
    }

    
    //绘制所有敌人的坦克,若死亡，则从容器中移除
    private void drawEnemies(Graphics g){
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            if (enemy.isDie()){
                enemies.remove(i);
                i--;
                continue;
            }
            enemy.draw(g);
        }

    }


    private void drawTworun(Graphics g) {


    }

    private void drawMusic(Graphics g) {

    }


    /**
     * 绘制菜单状态下的内容
     * @param g    画笔对象，由系统提供
     */
    private void drawMenu(Graphics g){
        //绘制黑色的背景
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);

        //预估一个字符串长度，进行计算
        final int STR_WIDTH = 90;
        int x = FRAME_WIDTH - STR_WIDTH >>1;
        int y = FRAME_HEIGHT / 3;
        //行间距
        final int DIS = 50;
        g.setColor(Color.WHITE);
        for (int i = 0; i < MENUS.length; i++) {
            if(i == menuIndex){
                //选中的菜单项设置为红色
                g.setColor(Color.RED);
            }
            else{
                //其他设置为白色
                g.setColor(Color.WHITE);
            }
            g.drawString(MENUS[i],x,y + DIS * i);
        }
    }


    /**
     * 初始化窗口的事件监听
     */
    private void initEventListener(){
        //注册监听事件
        addWindowListener(new WindowAdapter() {
            //jdk提供的一个类，类里有很多方法，windowclosing是其中一个方法。
            // 点击关闭按钮的时候，方法会被自动调用
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //添加按键监听
        addKeyListener(new KeyAdapter() {
            //按键被按下的时候被回调的方法
            @Override
            public void keyPressed(KeyEvent e) {
                //获得被按下键的键值
                int keyCode = e.getKeyCode();
                //不同的游戏状态，给出不同的处理方法
                switch (gameState){
                    case STATE_MENU:
                        keyPressedEventMenu(keyCode);
                        break;
                    case STATE_TWORUN:
                        keyPressedEventAbout(keyCode);
                        break;
                    case STATE_RUNNING:
                        keyPressedEventRunning(keyCode);
                        break;
                    case STATE_LOST:
                        keyPressedEventLost(keyCode);
                        break;
                    case STATE_WIN:
                        keyPressedEventWIN(keyCode);
                        break;
                }
            }

            //游戏通关的按键处理
            private void keyPressedEventWIN(int keyCode) {
                keyPressedEventWin(keyCode);
            }

            //按键松开的时候回调的内容
            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                //不同的游戏状态，给出不同的处理方法
                if (gameState == STATE_RUNNING){
                    keyReleasedEventRunning(keyCode);
                }
            }
        });
    }

    //按键松开的时候，游戏中的处理方法.   手松开方向键，坦克停止移动
    private void keyReleasedEventRunning(int keyCode) {
        switch (keyCode){
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                myTank.setState(Tank.STATE_STAND);
                break;
        }
    }


    /**
     * 游戏结束按键处理
     * @param keyCode
     */
    private void keyPressedEventLost(int keyCode) {
        //结束游戏
        if (keyCode == KeyEvent.VK_ESCAPE){
            setGameState(STATE_MENU);
            resetGame();
        }
        else if (keyCode == KeyEvent.VK_ENTER){
            setGameState(STATE_RUNNING);
            resetGame();
            startGame(LevelInof.getInstance().getLevel());
            //游戏属性重置
        }
    }


    private void keyPressedEventWin(int keyCode) {
        //结束游戏
        if (keyCode == KeyEvent.VK_ESCAPE){
            setGameState(STATE_MENU);
            resetGame();
        }
        else if (keyCode == KeyEvent.VK_ENTER){
            setGameState(STATE_RUNNING);
            resetGame();
            startGame(1);
            //游戏属性重置
        }
    }

    //重置游戏
    private void resetGame(){
        killEnemyCount = 0;
        menuIndex = 0;
        //先让自己的子弹还回对象池
        myTank.bulletsReturn();
        //销毁自己的坦克
        myTank = null;
        //清空敌人坦克相关资源
        for (Tank enemy : enemies) {
            enemy.bulletsReturn();
        }
        enemies.clear();
        //清空地图资源
        gameMap = null;
    }

    //键盘控制方向,游戏运行过程中的按键处理方法
    private void keyPressedEventRunning(int keyCode) {
        switch (keyCode){
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                myTank.setDir(Tank.DIR_UP);
                //改变状态，从站立到移动
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                myTank.setDir(Tank.DIR_DOWN);
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                myTank.setDir(Tank.DIR_LEFT);
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                myTank.setDir(Tank.DIR_RIGHT);
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_SPACE:
                myTank.fire();
                break;
            case KeyEvent.VK_NUMPAD0:
                myTank.fire();
                break;
        }
    }

    private void keyPressedEventAbout(int keyCode) {

    }

    private void keyPressedEventMusic(int keyCode) {

        switch (keyCode){
            case KeyEvent.VK_ENTER:
                if(MUSIC_STATE){
                        MusicUtil.stopMusic();
                    MUSIC_STATE = false;
                }else {
                    MusicUtil.playBackgroundmusic();
                    MUSIC_STATE = true;
                }
                break;




        }

    }

    //菜单状态下的按键的处理
    private void keyPressedEventMenu(int keyCode) {
        switch (keyCode){
            //up和w实现功能相同，down和s实现功能相同
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                if (--menuIndex < 0){
                    menuIndex = MENUS.length-1;
                }
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                if(++menuIndex > MENUS.length -1){
                    menuIndex = 0;
                }
                break;
            case KeyEvent.VK_ENTER:
                switch (menuIndex){
                    case 0:
                        startGame(1);
                        break;
                    case 1:
                        startGame2(1);
                        break;
                    case 2:
                        keyPressedEventMusic(keyCode);
                        break;
                    case 3:
                        System.exit(0);
                        break;
//                    case 4:
//                        System.exit(0);
//                        break;
                }
                break;
        }
    }

    /**
     * 开始新游戏的方法
     */
    private static void startGame(int level){
        enemies.clear();
        if (gameMap == null){
            gameMap = new GameMap();
        }
        gameMap.initMap(level);
        killEnemyCount = 0;

        MusicUtil.playStart();
        bornEnemyCount = 0;

        gameState = STATE_RUNNING;
        //创建坦克对象,x坐标=400，y=200
        myTank = new MyTank(FRAME_WIDTH/3,FRAME_HEIGHT-Tank.RADIUS*2,Tank.DIR_UP);

        //使用单独的线程用于控制生产敌人的坦克
        new Thread(){
            @Override
            public void run() {
                while (true){
                    if (LevelInof.getInstance().getEnemyCount()>bornEnemyCount&&enemies.size() < ENEMY_MAX_COUNT){
                        Tank enemy = EnemyTank.createEnemy();
                        enemies.add(enemy);
                        bornEnemyCount ++;
                    }
                    try {
                        Thread.sleep(ENEMY_BORN_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //游戏running状态才创建敌人坦克
                    if (gameState != STATE_RUNNING){
                        break;
                    }
                }
            }
        }.start();
    }



    /**
     * 开始双人新游戏的方法
     */
    private static void startGame2(int level){
        enemies.clear();
        if (gameMap == null){
            gameMap = new GameMap();
        }
        gameMap.initMap(level);
        killEnemyCount = 0;

        MusicUtil.playStart();
        bornEnemyCount = 0;

        gameState = STATE_RUNNING;
        //创建坦克对象,x坐标=400，y=200
        myTank = new MyTank(FRAME_WIDTH/3,FRAME_HEIGHT-Tank.RADIUS*2,Tank.DIR_UP);

        myTank1 = new EnemyTank(FRAME_WIDTH/5,FRAME_HEIGHT-Tank.RADIUS*2,Tank.DIR_UP);


        //使用单独的线程用于控制生产敌人的坦克
        new Thread(){
            @Override
            public void run() {
                while (true){
                    if (LevelInof.getInstance().getEnemyCount()>bornEnemyCount&&enemies.size() < ENEMY_MAX_COUNT){
                        Tank enemy = EnemyTank.createEnemy();
                        enemies.add(enemy);
                        bornEnemyCount ++;
                    }
                    try {
                        Thread.sleep(ENEMY_BORN_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //游戏running状态才创建敌人坦克
                    if (gameState != STATE_RUNNING){
                        break;
                    }
                }
            }
        }.start();
    }


    @Override
    public void run() {
        while (true){
            //在此调用repaint,回调update
            repaint();
            try {
                Thread.sleep(REPAINT_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //敌人的坦克的子弹和我的坦克的碰撞
    //我的坦克的子弹和所有敌人的碰撞
    private void bulletCollideTank(){
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            enemy.collideBullets(myTank.getBullets());
        }
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            myTank.collideBullets(enemy.getBullets());
        }
    }


    //所有的子弹和地图块的碰撞
    private void bulletAndTanksCollideMapTile(){
        //自己坦克的子弹和地图块的碰撞
        myTank.bulletsCollideMapTiles(gameMap.getTiles());

        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            enemy.bulletsCollideMapTiles(gameMap.getTiles());
        }
        //我方坦克和地图的碰撞
        if (myTank.isCollideTile(gameMap.getTiles())){
            myTank.back();
        }
        //敌人坦克与地图的碰撞
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            if (enemy.isCollideTile(gameMap.getTiles())){
                enemy.back();
            }
        }

        //清理所有被销毁的地图块
        gameMap.clearDestoryTile();

    }


    //所有坦克上的爆炸效果
    private void drawExplodes(Graphics g){
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            enemy.drawExplodes(g);
        }
        myTank.drawExplodes(g);
    }


    //获得游戏状态和修改游戏状态
    public static void setGameState(int gameState) {
        GameFrame.gameState = gameState;
    }

    public static int getGameState() {
        return gameState;
    }

    /**
     * 游戏是否最后一关
     * @return
     */
    public static boolean isLastLevel(){
        int currLevel = LevelInof.getInstance().getLevel();
        int levelCount = GameInfo.getLevelCount();
            return currLevel == levelCount;

        }


    /**
     * 判断是否过关了
     * @return
     */
    public static boolean isCrossLevel(){
        return killEnemyCount == LevelInof.getInstance().getEnemyCount();
    }

}
