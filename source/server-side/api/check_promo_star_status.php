<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$promo_id = $_GET['promo_id'];
$myObj = new stdClass(); // Initialize $myObj

$query = mysqli_query($conn, "SELECT * FROM promocje WHERE id='" . $promo_id . "' AND promo_with_star='1' ");

if(mysqli_num_rows($query) > 0) {
    $myObj->status = 1;
} else {
    $myObj->status = 0;
}

$myJSON = json_encode($myObj);
echo $myJSON;
?>