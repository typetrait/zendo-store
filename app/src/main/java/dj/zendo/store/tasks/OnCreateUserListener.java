package dj.zendo.store.tasks;

import dj.zendo.store.model.User;

public interface OnCreateUserListener {
    void onCreateUser(Boolean created);
}