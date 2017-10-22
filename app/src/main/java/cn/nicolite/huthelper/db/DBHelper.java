package cn.nicolite.huthelper.db;

/**
 * Created by nicolite on 17-9-6.
 */

public class DBHelper {
    private static DBHelper instance;

    private DBHelper(){
    }

    public static DBHelper getBoxHelper() {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper();
                }
            }
        }
        return instance;
    }

}
