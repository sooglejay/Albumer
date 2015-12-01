package sooglejay.youtu.utils;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by JammyQtheLab on 2015/12/1.
 */
public class GetGroupIdsUtil {
    public static final String reg = "_groupids_";
    public static String getGroupIds(ArrayList<String>groupids)
    {
        String groupidsStr = "";
        for (int i = 0 ; i <groupids.size();i++)
        {
            groupidsStr += groupids.get(i)+reg;
        }
        return groupidsStr;
    }

    public static ArrayList<String> getGroupIdArrayList(String groupIds)
    {
        if(TextUtils.isEmpty(groupIds))
        {
            return null;
        }

        String []array = groupIds.split(reg);
        ArrayList<String>resultList = new ArrayList<>();
        for(int j = 0;j<array.length;j++)
        {
            if(!TextUtils.isEmpty(array[j]))
            {
                resultList.add(array[j]);
            }
        }
        return  resultList;
    }
}
