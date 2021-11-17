package com.tank.util;

import com.tank.game.Explode;
import java.util.ArrayList;
import java.util.List;

/**
 *爆炸效果对象池
 */
public class ExplodesPool {
    public static final int DEFAULT_POOL_SIZE = 20;
    public static final int POOL_MAX_SIZE = 20;
    //用于保存所有爆炸效果的容器
    private static List<Explode> pool = new ArrayList<>();
    //
    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new Explode());
        }
    }

    /**
     * 从池中获取一个爆炸对象
     * @return
     */
    public static Explode get(){
        Explode explode = null;
        if (pool.size() == 0){
            explode = new Explode();
        }

        else{
            explode = pool.remove(0);
        }
        return explode;
    }
    //销毁时，归还到池中
    public static void theReturn(Explode explode){
        //池塘爆炸个数到达最大值，不再归还
        if (pool.size() == POOL_MAX_SIZE){
            return;
        }
        pool.add( explode );
    }
}
