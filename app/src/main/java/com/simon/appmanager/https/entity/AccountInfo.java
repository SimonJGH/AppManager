package com.simon.appmanager.https.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@SuppressWarnings("all")
@Table(name = "AccountInfo", onCreated = "")
public class AccountInfo {
    @Column(name = "account_id", isId = true, autoGen = false, property = "NOT NULL")
    private long account_id;

    @Column(name = "apkName")
    private String apkName;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public String getAppName() {
        return apkName;
    }

    public void setAppName(String appName) {
        this.apkName = appName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AccountInfo{" +
                "account_id=" + account_id +
                ", nickname='" + nickname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
