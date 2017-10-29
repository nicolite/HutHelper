package cn.nicolite.huthelper.db;

import cn.nicolite.huthelper.app.MApplication;
import cn.nicolite.huthelper.model.bean.Configure;
import cn.nicolite.huthelper.model.bean.Menu;
import cn.nicolite.huthelper.model.bean.TimeAxis;
import cn.nicolite.huthelper.model.bean.User;
import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by nicolite on 17-9-6.
 */

public class BoxHelper {
    private static BoxHelper instance;
    private static BoxStore boxStore;

    private BoxHelper(){
    }

    public static BoxHelper getBoxHelper(MApplication application) {
        if (instance == null) {
            synchronized (BoxHelper.class) {
                if (instance == null) {
                    instance = new BoxHelper();
                    boxStore = application.getBoxStore();
                }
            }
        }
        return instance;
    }

    public Box<User> getUserBox() {
        return boxStore.boxFor(User.class);
    }

    public Box<Menu> getMenuBox(){
       return boxStore.boxFor(Menu.class);
    }

    public Box<Configure> getConfigureBox() {
        return boxStore.boxFor(Configure.class);
    }

    public Box<TimeAxis> getTimeAxisBox() {
        return boxStore.boxFor(TimeAxis.class);
    }
}
