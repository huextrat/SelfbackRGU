package com.traxivity.selfback.traxivity.database.activity;

import io.realm.RealmModel;

/**
 * Created by 1707795 on 28/04/2017.
 */

public enum ActivityType implements RealmModel {
    Running,
    Walking,
    OnFoot,
    Inactive,
    Unknown
}
