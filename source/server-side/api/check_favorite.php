<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$user_id = $_GET['user_id'];
$promo_id = $_GET['promo_id'];
$promo_name = $_GET['promo_name'];
$is_airdrop = $_GET['is_airdrop'];
$airdrop_id = $_GET['airdrop_id'];
$airdrop_name = $_GET['airdrop_name'];

$myObj = new stdClass(); // Initialize $myObj

if ($is_airdrop == "1") {
    $query_check = mysqli_query($conn, "SELECT * FROM ulubione WHERE user_id='" . $user_id . "' AND airdrop_id='" . $airdrop_id . "' AND airdrop_name='" . $airdrop_name . "'");

    if (mysqli_num_rows($query_check) > 0) {
        $myObj->status_favorite = 2; 
    } else {
        $myObj->status_favorite = 1;
    }
} else {
 
    $query_check = mysqli_query($conn, "SELECT * FROM ulubione WHERE user_id='" . $user_id . "' AND promo_id='" . $promo_id . "' AND promo_name='" . $promo_name . "'");

    if (mysqli_num_rows($query_check) > 0) {
        $myObj->status_favorite = 2; 
    } else {
        $myObj->status_favorite = 1; 
    }
}

$myJSON = json_encode($myObj);
echo $myJSON;
?>
