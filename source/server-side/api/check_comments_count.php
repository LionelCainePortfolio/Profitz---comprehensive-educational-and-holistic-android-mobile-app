<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$promo_id = $_GET['promo_id'];
$myObj = new stdClass(); // Initialize the object

$query_check = mysqli_query($conn, "SELECT * FROM comments WHERE promo_id='" . $promo_id . "'");

if (mysqli_num_rows($query_check) > 0) {
    $myObj->count_comments = mysqli_num_rows($query_check); // Found
} else {
    $myObj->count_comments = 0; // Not found
}

$myJSON = json_encode($myObj);
echo $myJSON;
?>
