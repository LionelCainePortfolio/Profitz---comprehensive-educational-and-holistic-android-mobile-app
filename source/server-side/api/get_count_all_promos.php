<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
require_once 'Connection.php';

$user_id = $_GET['user_id'];

$myObj = new stdClass(); 

if ($conn) {
    $query_check = mysqli_query($conn, "SELECT * FROM promocje");

    if ($query_check) {
        $myObj->count = mysqli_num_rows($query_check);
    } else {
        $myObj->error = 'Error executing query: ' . mysqli_error($conn);
    }
} else {
    $myObj->error = 'Database connection error: ' . mysqli_connect_error();
}

$myJSON = json_encode($myObj);

echo $myJSON;
?>
