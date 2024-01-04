<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');

require_once 'Connection.php';

$user_id = mysqli_real_escape_string($conn, $_GET['user_id']);
$token = mysqli_real_escape_string($conn, $_GET['token']);

$sql_u2 = mysqli_query($conn, "UPDATE users SET token_notifications='".$token."' WHERE id='".$user_id."'");

$myObj = new stdClass();
$myObj->status_update_notification = 1; 

$myJSON = json_encode($myObj);

echo $myJSON;
?>