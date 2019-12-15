package com.simon.appmanager.https;

import com.simon.appmanager.https.entity.AccountInfo;
import com.simon.appmanager.utils.ToastUtils;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库管理
 */
@SuppressWarnings("all")
public class DbManagers {
    private DbManager db;

    private DbManagers() {
        initDB();
    }

    public static DbManagers getInstance() {
        return SafeMode.dbManager;
    }

    public static class SafeMode {
        private static final DbManagers dbManager = new DbManagers();
    }

    //初始化数据库
    private DbManager initDB() {
        if (db == null) {
            synchronized (DbManagers.class) {
                if (db == null) {
                    DbManager.DaoConfig config = new DbManager.DaoConfig().setDbName("Hskj.db").setDbVersion(2).setAllowTransaction(true)
                            .setTableCreateListener(new DbManager.TableCreateListener() {
                                @Override
                                public void onTableCreated(DbManager db, TableEntity<?> table) {
                                }
                            }).setDbOpenListener(new DbManager.DbOpenListener() {
                                @Override
                                public void onDbOpened(DbManager db) {
                                    db.getDatabase().enableWriteAheadLogging();
                                }
                            }).setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                                @Override
                                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                                }
                            });
                    //  config.setDbDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                    try {
                        db = x.getDb(config);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return db;
    }


    /**
     * 插入用户信息
     */
    public void addAccountInfo(AccountInfo accountInfo) {
        ArrayList<AccountInfo> mDbAccountInfos = new ArrayList<>();
        mDbAccountInfos.add(accountInfo);
        try {
            db.saveOrUpdate(mDbAccountInfos);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除用户信息
     *
     * @return
     */
    public void delUserInfo(long account_id) {
        WhereBuilder b = WhereBuilder.b();
        b.and("account_id", "==", account_id);
        try {
            List<AccountInfo> accountInfos = db.selector(AccountInfo.class).where(b).findAll();
            if (accountInfos != null) {
                for (AccountInfo accountInfo : accountInfos) {
                    db.delete(accountInfo);
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        } finally {
            ToastUtils.getInstance().showShortToast("删除帐号成功!");
        }
    }

    /**
     * 查询用户信息
     *
     * @return
     */
    public List<AccountInfo> queryUserInfo(String apkName) {
        WhereBuilder b = WhereBuilder.b();
        b.and("apkName", "==", apkName);
        List<AccountInfo> mDbAccountInfos = new ArrayList<>();
        try {
            List<AccountInfo> dbUserInfos = db.selector(AccountInfo.class).where(b).findAll();
            if (dbUserInfos != null) {
                mDbAccountInfos.addAll(dbUserInfos);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return mDbAccountInfos;
    }

    /**
     * 数据库操作成功
     */
    public interface dbSuccessListener{
        public void onClick();
    }
}
