<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$user_id = $_GET['user_id'];
$type = $_GET['type'];

$myObj = new stdClass(); // Initialize $myObj

if ($type == "awards" || $type == "points") {
    $query_check = mysqli_query($conn, "SELECT * FROM powiadomienia WHERE user_id='" . $user_id . "'");

    if (mysqli_num_rows($query_check) > 0) {
        $myObj->count_notifications = 1; 
    } else {
        $myObj->count_notifications = 0; 
    }
} else if ($type == "none" || $type == "other") {
    $query_check = mysqli_query($conn, "SELECT * FROM powiadomienia WHERE user_id='" . $user_id . "'");

    if (mysqli_num_rows($query_check) > 0) { 
        $query_check3 = mysqli_query($conn, "SELECT * FROM powiadomienia WHERE user_id='" . $user_id . "' AND status='0'");

        if (mysqli_num_rows($query_check3) > 0) {
            $myObj->count_notifications = 1; 
        } else {
            $myObj->count_notifications = 2;
        }
    } else {
        $myObj->count_notifications = 0; 
    }
} else {
    $query_check = mysqli_query($conn, "SELECT * FROM powiadomienia WHERE user_id='" . $user_id . "'");

    if (mysqli_num_rows($query_check) > 0) { 
        $query_check3 = mysqli_query($conn, "SELECT * FROM powiadomienia WHERE user_id='" . $user_id . "' AND status='0'");

        if (mysqli_num_rows($query_check3) > 0) {
            $myObj->count_notifications = 1; 
        } else {
            $myObj->count_notifications = 2; 
    } else {
        $myObj->count_notifications = 0; 
    }
}

$myJSON = json_encode($myObj);
echo $myJSON;
?>
