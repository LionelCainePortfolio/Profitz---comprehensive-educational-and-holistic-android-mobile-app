<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$user_id = $_GET['user_id'];
$promo_id = $_GET['promo_id'];
$admin_id = 2;

// Initialize variables
$refferal_name = $refferal_id = $refferal_url = $refferal_url_admin = '';

// Get refferal_name
$query = mysqli_query($conn, "SELECT * FROM users WHERE id='" . $user_id . "'");
if ($query && $row_refferal_name = mysqli_fetch_assoc($query)) {
    $refferal_name = $row_refferal_name["refferal_name"];

    // Get refferal_id
    $query2 = mysqli_query($conn, "SELECT * FROM users WHERE username='" . $refferal_name . "'");
    if ($query2 && $row_refferal_id = mysqli_fetch_assoc($query2)) {
        $refferal_id = $row_refferal_id["id"];

        // Get refferal_url
        $query3 = mysqli_query($conn, "SELECT * FROM instrukcje WHERE user_id='" . $refferal_id . "'");
        if ($query3 && $row_refferal_url = mysqli_fetch_assoc($query3)) {
            $refferal_url = $row_refferal_url["promo_url"];
        }

        // Get admin's refferal_url
        $query_admin = mysqli_query($conn, "SELECT * FROM instrukcje WHERE user_id='" . $admin_id . "'");
        if ($query_admin && $row_refferal_url_admin = mysqli_fetch_assoc($query_admin)) {
            $refferal_url_admin = $row_refferal_url_admin["promo_url"];
        }

        // Check if refferal_url is defined
        if (!empty($refferal_url)) {
            $myObj->url = $refferal_url;
        } else {
            $myObj->url = $refferal_url_admin;
        }

        $myJSON = json_encode($myObj);
        echo $myJSON;
    } else {
        echo "Error in query2: " . mysqli_error($conn);
    }
} else {
    echo "Error in query: " . mysqli_error($conn);
}
?>
