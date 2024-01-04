<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
header("Content-type: application/json");
require_once 'Connection.php';

$user_id = $_GET['user_id'];

$query_check = mysqli_query($conn, "SELECT * FROM users WHERE id='" . $user_id . "'");

$row_actual_day_clicked = mysqli_fetch_assoc($query_check);

$actual_day_clicked = $row_actual_day_clicked ? $row_actual_day_clicked["actual_day_clicked"] : null;

$myObj = new stdClass(); 

$myObj->actual_day_clicked = $actual_day_clicked;

$myJSON = json_encode($myObj);

echo $myJSON;
?>
