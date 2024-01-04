<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
header("Content-type: application/json");
require_once 'Connection.php';

$query_check = mysqli_query($conn, "SELECT * FROM airdrops WHERE featured='1'");
$rows = mysqli_num_rows($query_check);

if ($rows >= 1) {
    $subArray = array();
    $subArray['count_airdrops'] = "1";
    echo json_encode($subArray);
} else {
    $subArray = array();
    $subArray['count_airdrops'] = "0";
    echo json_encode($subArray);
}
?>
