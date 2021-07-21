package net.spirangle.ladybird;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class LevelFactory implements Data {

    public enum ObjectType {
        HFLAT(0,0,0,2,"T1"),
        VFLAT(1,0,0,2,"T1"),
        BLOCK(2,0,0,2,"T1"),
        BLOCK2(3,0,0,2,"T1"),
        BLOCK3(4,0,0,2,"T1"),
        BUSH(5,0,0,6,"T1"),
        CLOUD(6,0,0,6,"T1"),
        DOOR(7,0,0,4,"TE1"),
        HEART(8,0,0,1,"BL1"),
        APPLE(9,0,0,1,"BA1"),
        CHEST(10,0,0,1,"IG1"),
        BOTTLE(11,0,0,1,"BP1"),
        BOTTLE_G(11,0,0,1,"BP1"),
        BOTTLE_V(12,0,0,1,"BS1"),
        BOTTLE_O(13,0,0,1,"BP1"),
        BOTTLE_B(14,0,0,1,"BP1"),
        BOOGIE(15,0,0,5,"CL1"),
        GRULL(16,0,0,5,"CL1"),
        TROLL(17,0,0,3,"P1");

        public int index;
        public int x;
        public int y;
        public int z;
        public String data;

        ObjectType(int index,int x,int y,int z,String data) {
            this.index = index;
            this.x = x;
            this.y = y;
            this.z = z;
            this.data = data;
        }
    }

    public enum PlayerData {
        P1(0,2,6,0,0,5,0,MOVING,"first level"),
        P2(0,2,6,0,0,0,0,MOVING,"facing right"),
        P3(0,2,6,0,0,0,0,MOVING|FLIP,"facing left");

        public int action;
        public int speed;
        public int power;
        public int jump;
        public int chests;
        public int life;
        public int ammo;
        public int stat;
        public String descr;

        PlayerData(int action,int speed,int power,int jump,int chests,int life,int ammo,int stat,String descr) {
            this.action = action;
            this.speed = speed;
            this.power = power;
            this.jump = jump;
            this.chests = chests;
            this.life = life;
            this.ammo = ammo;
            this.stat = stat;
            this.descr = descr;
        }
    }

    public enum CreatureData {
        C1(0,0,0,0,2,AGGRO,"standing, facing right"),
        C2(0,0,0,0,2,AGGRO|FLIP,"standing, facing left"),
        CL1(LEFT,1,0,0,2,AGGRO|MOBILE|FLIP,"move left, facing left"),
        CR1(RIGHT,1,0,0,2,AGGRO|MOBILE,"move right, facing right"),
        CU1(UP,1,0,0,2,AGGRO|MOBILE,"move up, facing right"),
        CU2(UP,1,0,0,2,AGGRO|MOBILE|FLIP,"move up, facing left"),
        CD1(DOWN,1,0,0,2,AGGRO|MOBILE,"move down, facing right"),
        CD2(DOWN,1,0,0,2,AGGRO|MOBILE|FLIP,"move down, facing left");

        public int action;
        public int speed;
        public int effect;
        public int value;
        public int life;
        public int stat;
        public String descr;

        CreatureData(int action,int speed,int effect,int value,int life,int stat,String descr) {
            this.action = action;
            this.speed = speed;
            this.effect = effect;
            this.value = value;
            this.life = life;
            this.stat = stat;
            this.descr = descr;
        }
    }

    public enum ItemData {
        IG1(0,0,GOLD,1,0,BUFF,"buff: gold"),
        BL1(0,0,LIFE,1,0,BUFF,"buff: life"),
        BA1(0,0,AMMO,5,0,BUFF,"buff: ammo"),
        BP1(0,0,POWER,1,0,BUFF,"buff: power"),
        BS1(0,0,SPEED,1,0,BUFF,"buff: speed");

        public int action;
        public int speed;
        public int effect;
        public int value;
        public int life;
        public int stat;
        public String descr;

        ItemData(int action,int speed,int effect,int value,int life,int stat,String descr) {
            this.action = action;
            this.speed = speed;
            this.effect = effect;
            this.value = value;
            this.life = life;
            this.stat = stat;
            this.descr = descr;
        }
    }

    public enum TileData {
        T1(0,0,0,0,0,SOLID,"solid"),
        TL1(LEFT,1,0,0,0,SOLID|MOBILE,"solid, move left"),
        TR1(RIGHT,1,0,0,0,SOLID|MOBILE,"solid, move right"),
        TU1(UP,1,0,0,0,SOLID|MOBILE,"solid, move up"),
        TD1(DOWN,1,0,0,0,SOLID|MOBILE,"solid, move down"),
        TE1(0,0,EXIT,0,0,BUFF,"buff: level exit");

        public int action;
        public int speed;
        public int effect;
        public int value;
        public int life;
        public int stat;
        public String descr;

        TileData(int action,int speed,int effect,int value,int life,int stat,String descr) {
            this.action = action;
            this.speed = speed;
            this.effect = effect;
            this.value = value;
            this.life = life;
            this.stat = stat;
            this.descr = descr;
        }
    }

    private static LevelFactory instance = null;

    public static LevelFactory getInstance() {
        if(instance==null) instance = new LevelFactory();
        return instance;
    }

    private final Map<String,PlayerData> playerData;
    private final Map<String,CreatureData> creatureData;
    private final Map<String,ItemData> itemData;
    private final Map<String,TileData> tileData;
    private final Map<String,ObjectType> objectTypes;

    private LevelFactory() {
        playerData = new HashMap<>();
        for(PlayerData e : PlayerData.values())
            playerData.put(e.name(),e);
        creatureData = new HashMap<>();
        for(CreatureData e : CreatureData.values())
            creatureData.put(e.name(),e);
        itemData = new HashMap<>();
        for(ItemData e : ItemData.values())
            itemData.put(e.name(),e);
        tileData = new HashMap<>();
        for(TileData e : TileData.values())
            tileData.put(e.name(),e);
        objectTypes = new HashMap<>();
        for(ObjectType e : ObjectType.values())
            objectTypes.put(e.name(),e);
    }

    public Level createLevel(JsonObject levels,String levelId) {
        LadybirdGame game = LadybirdGame.getInstance();
        JsonValue levelValue = levels.get(levelId);
        if(levelValue==null) return null;
        JsonObject levelData = levelValue.asObject();
        if(levelData==null) return null;
        Player player = game.getPlayer();
        Level level = new Level(game,levelData,levelId);
        createObjects(levelData,level,"players",PLAYER);
        createObjects(levelData,level,"creatures",CREATURE);
        createObjects(levelData,level,"items",ITEM);
        createObjects(levelData,level,"tiles",TILE);
        player.setCollect(level.getChests());
        System.gc();
        return level;
    }

    public void createObjects(JsonObject levelData,Level level,String id,int group) {
        JsonValue objects = levelData.get(id);
        if(objects==null) return;
        objects.asArray()
               .values()
               .stream()
               .map(JsonValue::asObject)
               .forEach(o -> createObject(level,group,o));
    }

    public void createObject(Level level,int group,JsonObject json) {
        String typeName = json.getString("type",null);
        if(typeName==null) return;
        ObjectType objectType = objectTypes.get(typeName);
        if(objectType==null) return;
        int x = json.getInt("x",objectType.x);
        int y = json.getInt("y",objectType.y);
        int z = json.getInt("z",objectType.z);
        String data = json.getString("data",objectType.data);
        Map<String,String> params = null;
        JsonValue paramsvalue = json.get("params");
        if(paramsvalue!=null) {
            params = new HashMap<>();
            JsonObject paramsObject = paramsvalue.asObject();
            for(String key : paramsObject.names()) {
                String value = paramsObject.getString(key,null);
                if(value!=null) params.put(key,value);
            }
        }
        createObject(level,group,objectType.index,x,y,z,data,params);
    }

    public void createObject(Level level,int group,int type,int x,int y,int z,String data,Map<String,String> params) {
        LadybirdGame main = LadybirdGame.getInstance();
        GameObject gameObject;
        if(level==null) level = main.getLevel();
        switch(group) {
            case PLAYER:
                Player player = main.getPlayer();
                player.setType(level,type);
                player.transport(x,y,z,false);
                player.setData(playerData.get(data));
                gameObject = player;
                break;
            case CREATURE:
                gameObject = new Creature(level,x,y,z,type);
                gameObject.setData(creatureData.get(data));
                break;
            case ITEM:
                gameObject = new Item(level,x,y,z,type);
                gameObject.setData(itemData.get(data));
                if(gameObject.effect==GOLD) level.addChest();
                break;
            case TILE:
                gameObject = new Tile(level,x,y,z,type);
                gameObject.setData(tileData.get(data));
                break;
            default:return;
        }
        if(params!=null && !params.isEmpty())
            gameObject.setParams(params);
        level.addObject(gameObject);
    }
}
