package com.profitz.app.promos.interfaces;

public interface FriendRequestListener {
    void onAccept(String userId, int position);
    void onDecline(String userId, int position);
}
