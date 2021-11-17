package com.tank.util;

import com.tank.map.MapTile;
import com.tank.tank.EnemyTank;
import com.tank.tank.MyTank;
import com.tank.tank.Tank;

import java.util.ArrayList;
import java.util.List;

public class MapTilePool {
    public static final int DEFAULT_POOL_SIZE = 50;
    public static final int POOL_MAX_SIZE = 70;
    private static List<MapTile> pool = new ArrayList<>();
    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new MapTile());
        }
    }

    /**
     * 从池中获取一个对象
     * @return
     */
    public static MapTile get(){
        MapTile tile = null;
        if (pool.size() == 0){
            tile = new MapTile();
        }
        //池塘中还有对象，拿走第一给位置的子弹对象
        else{
            tile = pool.remove(0);
        }
        return tile;
    }
    //子弹销毁时，归还到池中
    public static void theReturn(MapTile tile){
        //池塘子弹个数到达最大值，不再归还
        if (pool.size() == POOL_MAX_SIZE){
            return;
        }
        pool.add( tile );
    }
}
