package cn.nicolite.huthelper.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 说说点赞缓存
 * Created by nicolite on 17-11-15.
 */

public class SayLikedCache {
    private static List<String> likedList = new ArrayList<>();

    public static void setLikedList(List<String> list) {
        likedList = list;
    }

    public static boolean isHave(String id) {
        for (String s : likedList) {
            if (s.equals(id))
                return true;
        }
        return false;
    }

    public static void clear() {
        likedList.clear();
    }

    public static void addLike(String id) {
        likedList.add(id);
    }

    public static void removeLike(String id) {
        for (String s : likedList) {
            if (s.equals(id)) {
                likedList.remove(id);
                break;
            }
        }
    }
}
