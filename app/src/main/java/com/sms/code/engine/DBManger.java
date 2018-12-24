package com.sms.code.engine;

import android.content.Context;

import com.sms.code.app.AppContext;
import com.sms.code.bean.DaoMaster;
import com.sms.code.bean.DaoSession;
import com.sms.code.bean.MsgBean;
import com.sms.code.bean.MsgBeanDao;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;

import java.util.List;

/**
 * GreenDao 示例
 * https://www.jianshu.com/p/cc7c1ecdfacf
 *
 * @author gao
 * @date 2018/11/18
 */
public class DBManger {

    private static final DBManger sDBManger = new DBManger();

    private DaoSession mDaoSession;

    private DBManger() {
        initDatabase(AppContext.getContext());
    }

    public static DBManger getInstance() {
        return sDBManger;
    }

    /**
     * 初始化greenDao，这个操作建议在Application初始化的时候添加；
     */
    private void initDatabase(Context context) {
        if (mDaoSession != null) {
            return;
        }
        if (context == null) {
            throw new IllegalArgumentException("You cannot start a load on a null Context");
        }
        DatabaseOpenHelper mHelper = new DaoMaster.DevOpenHelper(context.getApplicationContext(), "sms_db");
        Database db = mHelper.getWritableDb();
        DaoMaster mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    /**
     * 保存SMS
     *
     * @param bean
     */
    public void saveSmsTb(MsgBean bean) {
        mDaoSession.getMsgBeanDao().insert(bean);
    }

    public void deleteSmsTb(MsgBean bean) {
        mDaoSession.getMsgBeanDao().delete(bean);
    }

    public List<MsgBean> getSmsHistory() {
        return mDaoSession.getMsgBeanDao()
                .queryBuilder()
                .orderDesc(MsgBeanDao.Properties.Time)
                .limit(100)
                .list();
    }

    public List<MsgBean> queryKey(String key) {
        return mDaoSession.getMsgBeanDao()
                .queryBuilder()
                .where(MsgBeanDao.Properties.Duanx.like("%" + key + "%"))
                .orderDesc(MsgBeanDao.Properties.Time)
                .list();
    }

    public void handlesAsync(Runnable runnable) {
        mDaoSession.startAsyncSession().runInTx(runnable);
    }
}
