package com.tank.map;


import com.tank.game.GameFrame;
import com.tank.game.LevelInof;
import com.tank.tank.Tank;
import com.tank.util.Constant;
import com.tank.util.MapTilePool;
import com.tank.util.MyUtil;

import java.awt.*;
import java.io.FileInputStream;
import java.util.*;
import java.util.List;

/**
 * 游戏地图类
 */
public class GameMap {

    public static final int MAP_X = Tank.RADIUS*3;
    public static final int MAP_Y = Tank.RADIUS*3 + GameFrame.titleBarH;
    public static final int MAP_WIDTH = Constant.FRAME_WIDTH-Tank.RADIUS*6;
    public static final int MAP_HEIGHT = Constant.FRAME_HEIGHT-Tank.RADIUS*8-GameFrame.titleBarH;

    //地图元素块的容器
    private List<MapTile> tiles = new ArrayList<>();

    //大本营
    private TankHouse house;


    public GameMap(){

    }

    /**
     * 初始化地图元素块,level关卡
     */
    public void initMap(int level){
        tiles.clear();
        try {
            loadLevel(level);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //初始化大本营
        house = new TankHouse();
        addHouse();
    }

    /**
     * 加载关卡信息
     */
    private void loadLevel(int level) throws Exception{
        //获得关卡信息的唯一实例
        LevelInof levelInof = LevelInof.getInstance();
        //设置关卡
        levelInof.setLevel(level);

        Properties prop = new Properties();
        prop.load(new FileInputStream("level/lv_"+level));
        //将所有地图信息都加载进来
        int enemyCount = Integer.parseInt(prop.getProperty("enemyCount"));
        //设置敌人数量
        levelInof.setEnemyCount(enemyCount);

        //0,1  对敌人类型进行解析
//        String[] enemyType = prop.getProperty("enemyType").split(",");

//        int[] type = new int[enemyType.length];
//        for (int i = 0; i < type.length; i++) {
//            type[i] = Integer.parseInt(prop.getProperty("invokeCount"));
//        }
        //设置敌人类型
//        levelInof.setEnemyType(type);
        //关卡难度,没有难度默认1
//        String levelType = prop.getProperty("levelType");
//        levelInof.setLevelType(Integer.parseInt(levelType == null?"1" : levelType));

        String methodName = prop.getProperty("method");
        int invokeCount = Integer.parseInt(prop.getProperty("invokeCount"));
        //把实参都读取到数组之中
        String[] params = new String[invokeCount];
        for (int i = 1; i <= invokeCount; i++) {
            params[i-1] = prop.getProperty("param"+i);
        }


        invokeMethod(methodName,params);

    }


    private void invokeMethod(String name,String[] params){
        for (String param : params) {
            //获得每一行方法的参数，解析
            String[] split = param.split(",");
            //使用int数组保存解析后的内容
            int[] arr = new int[split.length];
            for (int i = 0; i < split.length; i++) {
                arr[i] = Integer.parseInt(split[i]);
            }


            //块之间的间隔是地图块的倍数
            final int DIS = MapTile.tileW;
            switch (name){
                case "addRow":
                    addRow(MAP_X+arr[0]*DIS,MAP_Y+arr[1]*DIS,MAP_X+MAP_WIDTH-arr[2]*DIS,arr[3],DIS*arr[4]);
                    break;
                case "addCol":
                    addCol(MAP_X+arr[0]*DIS,MAP_Y+arr[1]*DIS,MAP_Y+MAP_HEIGHT-arr[2]*DIS,arr[3],DIS);
                    break;
                case "addRect":
                    addRect(MAP_X+arr[0]*DIS,MAP_Y+arr[1]*DIS,MAP_X+MAP_WIDTH-arr[2]*DIS,MAP_Y+MAP_HEIGHT-arr[3]*DIS,arr[4],DIS);
                    break;
            }
        }
    }

    //将老巢的所有的元素块添加到地图的容器中
    private void addHouse(){
        tiles.addAll(house.getTiles());
    }


    /**
     * 某一个点是否和tiles集合中的块有重叠的
     * @param tiles
     * @param x
     * @param y
     * @return 有重叠返回true，否则false
     */
    private boolean isCollide(List<MapTile>tiles,int x,int y){
        for (MapTile tile : tiles) {
            int tileX = tile.getX();
            int tileY = tile.getY();
            if (Math.abs(tileX-x) < MapTile.tileW && Math.abs(tileY-y) < MapTile.tileW){
                return true;
            }
        }
        return false;
    }


    //没有遮挡效果的块的绘制
    public void drawBK(Graphics g){
        for (MapTile tile : tiles) {
            if (tile.getType() != MapTile.TYPE_COVER)
            tile.draw(g);
        }
    }

    public void drawCover(Graphics g){
        for (MapTile tile : tiles) {
            if (tile.getType() == MapTile.TYPE_COVER)
                tile.draw(g);
        }
    }

    public List<MapTile> getTiles() {
        return tiles;
    }

    /**
     * 将所有的不可见的地图块从容器中移除
     */
    public void clearDestoryTile(){
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            if (!tile.isVisible())
                tiles.remove(i);
        }
    }


    /**
     * 地图块容器中添加一行指定类型地图块
     * @param starX     添加地图块的开始x坐标
     * @param starY     添加地图块的开始Y坐标
     * @param endX      添加地图块的结束x坐标
     * @param type  地图块类型
     * @param DIS   地图块间隔 如果是块宽，则连续
     */
    public void addRow(int starX,int starY,int endX,int type,final int DIS){
        int count = (endX - starX)/(MapTile.tileW+DIS);
        for (int i = 0; i < count; i++) {
            MapTile tile = MapTilePool.get();
            tile.setType(type);
            tile.setX(starX + i * (MapTile.tileW+DIS));
            tile.setY(starY);
            tiles.add(tile);
        }

        }


    /**
     * 往地图元素块容器中添加一列元素
     * @param starX 起始x
     * @param starY
     * @param endY 结束y
     * @param type  类型
     * @param DIS   间距
     */
    public void addCol(int starX,int starY,int endY,int type,final int DIS){
        int count = (endY - starY)/(MapTile.tileW+DIS);
        for (int i = 0; i < count; i++) {
            MapTile tile = MapTilePool.get();
            tile.setType(type);
            tile.setX(starX);
            tile.setY(starY + i * (MapTile.tileW+DIS));
            tiles.add(tile);
        }
    }


    //对指定矩形区域添加元素块
    public void addRect(int starX,int starY,int endX,int endY,int type,final int DIS){
        int rows = (endY - starY)/(MapTile.tileW+DIS);
        for (int i = 0; i < rows; i++) {
            addRow(starX,starY+i*(MapTile.tileW+DIS),endX,type,DIS);
        }

        int cols = (endX - starX)/(MapTile.tileW+DIS);
    }


}


