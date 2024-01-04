<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$user_id = isset($_GET['user_id']) ? $_GET['user_id'] : null; 
$myObj = new stdClass(); 

$query_check = mysqli_query($conn, "SELECT * FROM promocje WHERE status='1'");

$myObj->count = mysqli_num_rows($query_check); 

$myJSON = json_encode($myObj);

echo $myJSON;
?>