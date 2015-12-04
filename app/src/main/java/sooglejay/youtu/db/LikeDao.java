package sooglejay.youtu.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sooglejay.youtu.bean.LikeBean;


public class LikeDao {
    private Dao<LikeBean, Integer> likeDaoOpe;
    private DatabaseHelper helper;

    @SuppressWarnings("unchecked")
    public LikeDao(Context context) {
        try {
            helper = DatabaseHelper.getHelper(context);
            likeDaoOpe = helper.getDao(LikeBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个LikeBean
     *
     * @param LikeBean
     */
    public void add(LikeBean LikeBean) {
        try {
            likeDaoOpe.create(LikeBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过Id得到一个LikeBean记录
     * @param id
     * @return
     */
    public LikeBean get(int id) {
        LikeBean bean = null;
        try {
            bean = likeDaoOpe.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }

    /**
     * 获取所有的LikeBean
     *
     * @return
     */
    public List<LikeBean> getAll() {
        try {
            return likeDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    /**
     * 获取表中数据记录个数
     *
     * @return
     */
    public int getCount() {
        int count =  0;
        try {
            count = likeDaoOpe.queryForAll().size();
        } catch (SQLException e) {
            e.printStackTrace();
            count = 0 ;
        }finally {
            return count;
        }
    }

    public void deleteByName(String imagePathName) {
        try {
            DeleteBuilder<LikeBean, Integer> deleteBuilder = likeDaoOpe.deleteBuilder();
            deleteBuilder.where().eq("imagePath", imagePathName);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        try {
            likeDaoOpe.deleteBuilder().delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void uodateGroupNameBean(LikeBean bean) {
        try {
            likeDaoOpe.update(bean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
