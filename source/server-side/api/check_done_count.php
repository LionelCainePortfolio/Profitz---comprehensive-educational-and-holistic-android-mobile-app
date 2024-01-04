<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$user_id = $_GET['user_id'];

$myObj = new stdClass(); // Initialize $myObj

$query_check = mysqli_query($conn, "SELECT * FROM wykonane WHERE user_id='" . $user_id . "'");

if (mysqli_num_rows($query_check) > 0) {
    $myObj->count_done = 1; // znajduje sie
} else {
    $myObj->count_done = 0; // nie ma
}

$myJSON = json_encode($myObj);
echo $myJSON;
?>
