package sooglejay.youtu.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sooglejay.youtu.bean.ContactBean;
import sooglejay.youtu.bean.ContactBean;


public class ContactDao {
    private Dao<ContactBean, Integer> contactDao;
    private DatabaseHelper helper;

    @SuppressWarnings("unchecked")
    public ContactDao(Context context) {
        try {
            helper = DatabaseHelper.getHelper(context);
            contactDao = helper.getDao(ContactBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个ContactBean
     *
     * @param ContactBean
     */
    public void add(ContactBean ContactBean) {
        try {
            contactDao.create(ContactBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过Id得到一个ContactBean记录
     * @param id
     * @return
     */
    public ContactBean get(int id) {
        ContactBean bean = null;
        try {
            bean = contactDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }
    /**
     * 获取表中数据记录个数
     *
     * @return
     */
    public int getCount() {
        int count =  0;
        try {
            count = contactDao.queryForAll().size();
        } catch (SQLException e) {
            e.printStackTrace();
            count = 0 ;
        }finally {
            return count;
        }
    }
    /**
     * 获取所有的ContactBean
     *
     * @return
     */
    public List<ContactBean> getAll() {
        try {
            return contactDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void deleteByName(String imagePathName) {
        try {
            DeleteBuilder<ContactBean, Integer> deleteBuilder = contactDao.deleteBuilder();
            deleteBuilder.where().eq("imagePath", imagePathName);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        try {
            contactDao.deleteBuilder().delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void uodateGroupNameBean(ContactBean bean) {
        try {
            contactDao.update(bean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
