<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$user_id = $_GET['user_id'];
$promo_id = $_GET['promo_id'];
$myObj = new stdClass(); // Initialize the object

$query = mysqli_query($conn, "SELECT * FROM wykonane WHERE user_id='".$user_id."' AND promo_id='".$promo_id."' ");

if(mysqli_num_rows($query) > 0) {
    $myObj->status = 1;
} else {
    $myObj->status = 0;
}

$myJSON = json_encode($myObj);
echo $myJSON;
?>
