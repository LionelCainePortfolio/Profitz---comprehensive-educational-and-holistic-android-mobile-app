package com.profitz.app.promos.managers;


import android.util.Log;

import com.profitz.app.data.model.GroupsModel;
import com.profitz.app.data.model.MessageModel;
import com.profitz.app.promos.interfaces.SocketListener;
import com.profitz.app.util.Utility;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

public class SocketManager {
    private static Socket socket;

    private static final String KEY_EVENT_GROUP_MESSAGE_RECEIVED = "KEY_EVENT_GROUP_MESSAGE_RECEIVED";
    private static final String KEY_EVENT_PRIVATE_MESSAGE_RECEIVED = "KEY_EVENT_PRIVATE_MESSAGE_RECEIVED";
    private static final String KEY_EVENT_USER_CONNECTED = "KEY_EVENT_USER_CONNECTED";
    private static final String KEY_EVENT_GROUP_JOINED = "KEY_EVENT_GROUP_JOINED";
    private static final String KEY_EVENT_GROUP_LEAVED = "KEY_EVENT_GROUP_LEAVED";
    private static final String KEY_EVENT_REQUEST_RECEIVED = "KEY_EVENT_REQUEST_RECEIVED";
    private static final String KEY_EVENT_GROUP_DELETED = "KEY_EVENT_GROUP_DELETED";
    private static final String KEY_EVENT_GROUP_CREATED = "KEY_EVENT_GROUP_CREATED";

    /**
     * Create the socket instance.
     */
    public synchronized static void getInstance() {
        if (socket == null) {
            try {
//                IO.Options options = new IO.Options();
//                options.timeout = -1;

                socket = IO.socket(Utility.NODE_ROOT_URL);
//                socket.io().timeout(-1);
                socket.connect();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void onActivityDestroyed() {
        if (socket != null) {
            socket.off();
            socket.disconnect();
            socket = null;
        }
    }

    public static void attachIncomingGroupMessageListener(final SocketListener socketListener) {
        if (socket != null) {
            socket.on(KEY_EVENT_GROUP_MESSAGE_RECEIVED, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    socketListener.onResponse(args[0].toString());

                }
            });
        }
    }

    public static void attachIncomingPrivateMessageListener(final SocketListener socketListener) {
        if (socket != null) {
            socket.on(KEY_EVENT_PRIVATE_MESSAGE_RECEIVED, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    socketListener.onResponse(args[0].toString());
                    Log.d("Socket", "KEY_EVENT_PRIVATE_MESSAGE_RECEIVED");
                    Log.d("Socket", args[0].toString());
                }

            });
        }
    }

    public static void sendOutgoingGroupMessage(MessageModel messageModel, String groupId) {
        if (socket != null) {
            socket.emit(KEY_EVENT_GROUP_MESSAGE_RECEIVED, new Gson().toJson(messageModel), groupId);

        }
    }

    public static void sendOutgoingPrivateMessage(MessageModel messageModel, String userId) {
        if (socket != null) {
            socket.emit(KEY_EVENT_PRIVATE_MESSAGE_RECEIVED, new Gson().toJson(messageModel), userId);
        }
    }

    public static void userConnected(String userId) {
        if (socket != null) {
            socket.emit(KEY_EVENT_USER_CONNECTED, userId);
        }
    }

    public static void userJoinGroup(String groupId) {
        if (socket != null) {
            socket.emit(KEY_EVENT_GROUP_JOINED, groupId);
        }
    }

    public static void userLeaveGroup(String groupId) {
        if (socket != null) {
            socket.emit(KEY_EVENT_GROUP_LEAVED, groupId);
        }
    }

    public static void sendRequest(String requestTo, String type) {
        if (socket != null) {
            socket.emit(KEY_EVENT_REQUEST_RECEIVED, requestTo, type);
        }
    }

    public static void attachIncomingRequestListener(final SocketListener socketListener) {
        if (socket != null) {
            socket.on(KEY_EVENT_REQUEST_RECEIVED, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    socketListener.onResponse(args[0].toString());
                }
            });
        }
    }

    public static void groupDeleted(String groupId) {
        if (socket != null) {
            socket.emit(KEY_EVENT_GROUP_DELETED, groupId);
        }
    }

    public static void attachGroupDeletedListener(final SocketListener socketListener) {
        if (socket != null) {
            socket.on(KEY_EVENT_GROUP_DELETED, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    socketListener.onResponse(args[0].toString());
                }
            });
        }
    }

    public static void groupCreated(GroupsModel groupsModel) {
        if (socket != null) {
            socket.emit(KEY_EVENT_GROUP_CREATED, new Gson().toJson(groupsModel));
        }
    }

    public static void attachGroupCreatedListener(final SocketListener socketListener) {
        if (socket != null) {
            socket.on(KEY_EVENT_GROUP_CREATED, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    socketListener.onResponse(args[0].toString());
                }
            });
        }
    }
}
