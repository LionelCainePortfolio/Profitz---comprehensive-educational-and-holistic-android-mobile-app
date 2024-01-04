const express = require('express');
const app = express();
var http = require("http").Server(app);
var io = require("socket.io")(http);
var bodyParser = require('body-parser');
var cors = require('cors');
var fs = require("fs");

var users = [];
var groups = [];

app.use(bodyParser.urlencoded({
	extended: true,
	limit: "50mb"
}));
app.use(bodyParser.json());
app.use(cors({credentials: true, origin: true}));

app.get("/", function (request, result) {
    result.send("Your server is running");
});

http.listen(process.env.PORT || 3000, function () {
    // console.log('Your server is running');

    io.on("connection", function (socket) {
	    // console.log('User connected');

	    socket.on("KEY_EVENT_USER_CONNECTED", function (userId) {
	        users[userId] = socket.id;
	    });

	    socket.on("KEY_EVENT_GROUP_JOINED", function (groupId) {
	        socket.join(groupId);
	    });

	    socket.on("KEY_EVENT_GROUP_LEAVED", function (groupId) {
	        socket.leave(groupId);
	    });

	    socket.on("KEY_EVENT_GROUP_MESSAGE_RECEIVED", function (message, groupId) {
	    	message.createdAt = new Date().getTime();
	    	io.emit("KEY_EVENT_GROUP_MESSAGE_RECEIVED", message);
	    });

	    socket.on("KEY_EVENT_PRIVATE_MESSAGE_RECEIVED", function (message, userId) {
	        io.to(users[userId]).emit("KEY_EVENT_PRIVATE_MESSAGE_RECEIVED", message);
	    });

	    socket.on("KEY_EVENT_REQUEST_RECEIVED", function (requestTo, type) {
	        io.to(users[requestTo]).emit("KEY_EVENT_REQUEST_RECEIVED", type);
	    });

	    socket.on("KEY_EVENT_GROUP_DELETED", function (groupId) {
	        socket.broadcast.emit("KEY_EVENT_GROUP_DELETED", groupId);
	    });

	    socket.on("KEY_EVENT_GROUP_CREATED", function (groupName, admin) {
	    	socket.emit("KEY_EVENT_GROUP_CREATED", groupName);
	    });

	}); // socket io on connection
}); // http listen