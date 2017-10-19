package cn.nicolite.huthelper.utils;

import java.util.List;

/**
 * List工具类
 * Created by nicolite on 17-10-19.
 */

public class ListUtils {

    /**
     * 判空
     * @param list
     * @return
     */
    public static boolean isEmpty(List<?> list){
        return list == null || list.isEmpty();
    }
}
