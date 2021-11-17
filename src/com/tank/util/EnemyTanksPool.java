package com.tank.util;

import com.tank.tank.EnemyTank;
import com.tank.tank.Tank;

import java.util.ArrayList;
import java.util.List;

/**
 * 敌人坦克对象池
 */
public class EnemyTanksPool {
    public static final int DEFAULT_POOL_SIZE = 30;
    public static final int POOL_MAX_SIZE = 30;
    private static List<Tank> pool = new ArrayList<>();
    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new EnemyTank());
        }
    }

    /**
     * 从池中获取一个对象
     * @return
     */
    public static Tank get(){
        Tank tank = null;
        if (pool.size() == 0){
            tank = new EnemyTank();
        }
        //池塘中还有对象，拿走第一给位置的子弹对象
        else{
            tank = pool.remove(0);
        }
        return tank;
    }
    //子弹销毁时，归还到池中
    public static void theReturn(Tank tank){
        //池塘子弹个数到达最大值，不再归还
        if (pool.size() == POOL_MAX_SIZE){
            return;
        }
        pool.add( tank );
    }
}
