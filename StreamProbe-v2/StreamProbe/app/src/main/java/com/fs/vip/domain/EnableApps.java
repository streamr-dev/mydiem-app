package com.fs.vip.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class EnableApps {
    @Id(autoincrement = true)//设置自增长
    private Long id;

    @Index(unique = true)//设置唯一性
    private String packageName;//人员编号

    @Generated(hash = 591353833)
    public EnableApps(Long id, String packageName) {
        this.id = id;
        this.packageName = packageName;
    }

    @Generated(hash = 39081127)
    public EnableApps() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }


}
