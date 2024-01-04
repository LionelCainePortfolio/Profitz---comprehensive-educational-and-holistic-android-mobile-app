<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$user_id = $_GET['user_id'];

$myObj = new stdClass(); // Initialize the object

$query_check = mysqli_query($conn, "SELECT * FROM ulubione WHERE user_id='" . $user_id . "'");

if (mysqli_num_rows($query_check) > 0) {
    $myObj->count_favorite = 1; /
} else {
    $myObj->count_favorite = 0; 
}

$myJSON = json_encode($myObj);
echo $myJSON;
?>
