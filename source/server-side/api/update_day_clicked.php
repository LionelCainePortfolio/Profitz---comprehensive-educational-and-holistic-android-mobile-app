<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
header("Content-type: application/json");
require_once 'Connection.php';

$user_id = $_GET['user_id'];
$random_points = $_GET['random_points'];

$query1 = mysqli_query($conn, "UPDATE users SET actual_day=actual_day+1 WHERE id=$user_id");
$query2 = mysqli_query($conn, "UPDATE users SET day_to_reset=day_to_reset+1 WHERE id=$user_id");
$query3 = mysqli_query($conn, "UPDATE users SET actual_day_clicked='yes' WHERE id=$user_id");
$query4 = mysqli_query($conn, "UPDATE users SET points=points+$random_points WHERE id=$user_id");
$query5 = mysqli_query($conn, "UPDATE users SET points_yesterday=$random_points WHERE id=$user_id");

$myObj = new stdClass(); 
$myObj->status_check_update_day_clicked = "yes";

$myJSON = json_encode($myObj);

echo $myJSON;
?>