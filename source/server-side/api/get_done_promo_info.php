<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
require_once 'Connection.php';

$user_id = $_GET['user_id'];
$promo_id = $_GET['promo_id'];

$subArray = array();
$sql_results = mysqli_query($conn, "SELECT * FROM wykonane WHERE user_id='" . $user_id . "' AND promo_id='" . $promo_id . "'");

if ($row = mysqli_fetch_array($sql_results)) {
    $subArray['promoStatus'] = $row['status'];
    $subArray['promoBonus'] = $row['promo_refback'];
    $subArray['promoAdditionalInfo'] = $row['status_additional_info'];
    $subArray['promoReceivedPoints'] = $row['received_points'];
} else {
    $subArray['promoStatus'] = "brak";
    $subArray['promoBonus'] = "0";
    $subArray['promoAdditionalInfo'] = "0";
    $subArray['promoReceivedPoints'] = "0";
}

echo json_encode($subArray);
?>
