package sooglejay.youtu.utils;

/**
 * Created by JammyQtheLab on 2015/11/30.
 */
public class GetTagUtil {
    public static final String reg = "_sooglejay_";

    public static String getTag(String nameStr, String phoneStr, String groupids) {
        return nameStr + reg + phoneStr + reg + groupids;
    }

    public static String getName(String tagStr) {
        String[] array = tagStr.split(reg);
        if (array != null && array.length > 0) {
            return array[0];
        } else {
            return "";
        }
    }


    public static String getPhoneNumber(String tagStr) {
        String[] array = tagStr.split(reg);
        if (array != null && array.length > 1) {
            return array[1];
        } else {
            return "";
        }
    }

    public static String getGroupIds(String tagStr) {
        String[] array = tagStr.split(reg);
        if (array != null && array.length > 2) {
            return array[2];
        } else {
            return "";
        }
    }
}
