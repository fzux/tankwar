package com.tank.util;

import java.awt.*;

/**
 * 游戏常量都在该类中维护，便于后期管理
 */
public class Constant {
    /*************************游戏窗口相关**************************/
    public static final String GAME_TITLE = "TANKWAR_x";

    //设置窗口长和宽
    public static final int FRAME_WIDTH = 900;
    public static final int FRAME_HEIGHT = 650;

    //获得当前系统的长和高
    public static final int SCREEN_W = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int SCREEN_H = Toolkit.getDefaultToolkit().getScreenSize().height;


    //窗口显示位置。根据系统窗口的宽度来设置
    //本机电脑分辨率1280*720。    >>1表示是右移一位，就是除以2的意思
    public static final int FRAME_X = SCREEN_W-FRAME_WIDTH>>1;
    public static final int FRAME_Y = SCREEN_H-FRAME_HEIGHT>>1;

    /**********************游戏菜单相关的************************/
    public static final int STATE_MENU = 0;
    public static final int STATE_MUSIC = 1;
    public static final int STATE_TWORUN = 2;
    public static final int STATE_RUNNING = 3;
    public static final int STATE_LOST = 4;
    public static final int STATE_WIN = 5;

    public static boolean MUSIC_STATE = true;


    public  static final String[] MENUS = {
            "单人游戏",
            "双人游戏",
            "音乐开关",
            "退出游戏"
    };

    public static final String OVER_STR0 = "ESC键返回主菜单";
    public static final String OVER_STR1 = "ENTER键重新开始";



    //字体设置
    public static final Font GAME_FONT = new Font("宋体",Font.BOLD,24);

    public static final Font SMALL_FONT = new Font("宋体",Font.BOLD,15);

    //刷新窗口，定为30ms一 次
    public static final int REPAINT_INTERVAL = 30;

    public static final int ENEMY_MAX_COUNT = 30;

    //每500ms产生一辆坦克
    public static final int ENEMY_BORN_INTERVAL = 500;


    public static final int ENEMY_AI_INTERVAL = 5000;
    public static final double ENEMY_FIRE_PERCENT = 0.1;

    //三条命
    public static int Life = 3;




}
