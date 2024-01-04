<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$sql1 = mysqli_query($conn, "UPDATE users SET day_to_reset=day_to_reset-1 WHERE actual_day_clicked='no'");
$sql2 = mysqli_query($conn, "UPDATE users SET actual_day_clicked='no' WHERE id>=1");
$sql3 = mysqli_query($conn, "UPDATE users SET actual_day=IF (day_to_reset<=(actual_day-1), 0,actual_day) WHERE actual_day_clicked='no'");
$sql4 = mysqli_query($conn, "UPDATE users SET actual_day=IF(day_to_reset=0,0,actual_day) WHERE actual_day=1");
$sql4 = mysqli_query($conn, "UPDATE users SET actual_day=IF(day_to_reset=1,0,actual_day) WHERE actual_day=2 AND actual_day_clicked='no'");

$sql5 = mysqli_query($conn, "UPDATE users SET day_to_reset=0 WHERE day_to_reset<=0");
$sql6 = mysqli_query($conn, "UPDATE users SET day_to_reset=0 WHERE actual_day=0");

?>
