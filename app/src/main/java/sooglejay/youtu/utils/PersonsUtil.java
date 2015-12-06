package sooglejay.youtu.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import sooglejay.youtu.api.delperson.DelPersonResponseBean;
import sooglejay.youtu.api.delperson.DelPersonUtil;
import sooglejay.youtu.api.getgroupids.*;
import sooglejay.youtu.api.getpersonids.GetPersonIdsResponseBean;
import sooglejay.youtu.api.getpersonids.GetPersonIdsUtil;
import sooglejay.youtu.constant.NetWorkConstant;
import sooglejay.youtu.model.NetCallback;

/**
 * Created by JammyQtheLab on 2015/12/6.
 */
public class PersonsUtil {
    private ArrayList<String> groupidsList = new ArrayList<>();
    private ArrayList<String> personidsList = new ArrayList<>();

    private static Activity activity;

    private static PersonsUtil personsUtil = new PersonsUtil();

    private PersonsUtil() {

    }

    public static void delAllPersons(Activity a) {
        if (activity == null) {
            activity = a;
        }
        personsUtil.delAllPersonsByGroupIds();
    }


    private void delPersons(List<String> person_ids) {
        for (String person_id : person_ids) {
            DelPersonUtil.delPerson(activity, NetWorkConstant.APP_ID, person_id, new NetCallback<DelPersonResponseBean>(activity) {
                @Override
                public void onFailure(RetrofitError error, String message) {

                }

                @Override
                public void success(DelPersonResponseBean delPersonResponseBean, Response response) {

                }
            });
        }
    }

    private void delAllPersonsByGroupIds() {
        sooglejay.youtu.api.getgroupids.GetGroupIdsUtil.getGroupIds(activity, NetWorkConstant.APP_ID, new NetCallback<GetGroupIdsResponseBean>(activity) {
            @Override
            public void onFailure(RetrofitError error, String message) {

            }

            @Override
            public void success(GetGroupIdsResponseBean getGroupIdsResponseBean, Response response) {
                //获取到所有的组ids
                if (getGroupIdsResponseBean.getGroup_ids() != null) {
                    groupidsList.clear();
                    groupidsList.addAll(getGroupIdsResponseBean.getGroup_ids());
                }
                personidsList.clear();

                //根据每个组id 去获取所有用户的信息
                for (int i = 0; i < groupidsList.size(); i++) {
                    getAllPersonIds(groupidsList.get(i));
                }
            }
        });
    }

    private void getAllPersonIds(String group_id) {
        GetPersonIdsUtil.getPersonIds(activity, NetWorkConstant.APP_ID, group_id, new NetCallback<GetPersonIdsResponseBean>(activity) {
            @Override
            public void onFailure(RetrofitError error, String message) {

            }

            @Override
            public void success(GetPersonIdsResponseBean getPersonIdsResponseBean, Response response) {
                //获取了一个组用户的信息之后就删除这个组的用户
                personidsList.addAll(getPersonIdsResponseBean.getPerson_ids());
                delPersons(getPersonIdsResponseBean.getPerson_ids());
            }
        });
    }


}
