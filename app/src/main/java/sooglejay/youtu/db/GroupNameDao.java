package sooglejay.youtu.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sooglejay.youtu.bean.GroupBean;


public class GroupNameDao {
    private Dao<GroupBean, Integer> groupNameDaoOpe;
    private DatabaseHelper helper;

    @SuppressWarnings("unchecked")
    public GroupNameDao(Context context) {
        try {
            helper = DatabaseHelper.getHelper(context);
            groupNameDaoOpe = helper.getDao(GroupBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个GroupBean
     *
     * @param groupBean
     */
    public void add(GroupBean groupBean) {
        try {
            groupNameDaoOpe.create(groupBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过Id得到一个GroupBean记录
     * @param id
     * @return
     */
    public GroupBean get(int id) {
        GroupBean groupNameBean = null;
        try {
            groupNameBean = groupNameDaoOpe.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupNameBean;
    }

    /**
     * 获取所有的GroupBean
     *
     * @return
     */
    public List<GroupBean> getAll() {
        try {
            return groupNameDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<GroupBean> getAllByIsUsedForIdentify(boolean isUsedForIdentify) {
        try {
            QueryBuilder<GroupBean, Integer> queryBuilder = groupNameDaoOpe.queryBuilder();
            queryBuilder.where().eq("isUsedForIdentify", isUsedForIdentify);
            queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteByName(String groupNameStr) {
        try {
            DeleteBuilder<GroupBean, Integer> deleteBuilder = groupNameDaoOpe.deleteBuilder();
            deleteBuilder.where().eq("name", groupNameStr);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        try {
            groupNameDaoOpe.deleteBuilder().delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGroupNameBean(GroupBean bean) {
        try {
            groupNameDaoOpe.update(bean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
