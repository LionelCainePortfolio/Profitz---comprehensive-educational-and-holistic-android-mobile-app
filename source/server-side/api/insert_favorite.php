<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$user_id = $_GET['user_id'];
$promo_id = $_GET['promo_id'];
$promo_name = $_GET['promo_name'];
$url_img = "https://yoururl.com/api/images/promos/" . $promo_name . ".png";
$promo_earn = $_GET['promo_earn'];

$delete = "Usunięto promocję z ulubionych. ID promocji: " . $promo_id;
$add = "Dodano promocję do ulubionych. ID promocji:" . $promo_id;

// Initialize $myObj before using it
$myObj = new stdClass();

$query_check = mysqli_query($conn, "SELECT * FROM ulubione WHERE user_id='" . $user_id . "' AND promo_id='" . $promo_id . "' AND promo_name='" . $promo_name . "'");

if (mysqli_num_rows($query_check) > 0)
{
    $sql_u1 = mysqli_query($conn, "UPDATE promocje SET likes=likes-1 WHERE id = '" . $promo_id . "'");
    $sql_u2 = mysqli_query($conn, "UPDATE users SET favourite_count=favourite_count-1 WHERE id = '" . $user_id . "'");
    $sql1 = mysqli_query($conn, "DELETE FROM ulubione WHERE user_id='" . $user_id . "' AND promo_id='" . $promo_id . "'");

    $sql1_1 = mysqli_query($conn, "INSERT INTO logi (user_id, what_did) VALUES ('" . $user_id . "', '" . $delete . "')");
    $myObj->status_favorite = 2; //usunieto
    $myJSON = json_encode($myObj);

    echo $myJSON;
}
else
{
    $sql_u1 = mysqli_query($conn, "UPDATE promocje SET likes=likes+1 WHERE id = '" . $promo_id . "'");
    $sql_u2 = mysqli_query($conn, "UPDATE users SET favourite_count=favourite_count+1 WHERE id = '" . $user_id . "'");

    $sql2 = mysqli_query($conn, "INSERT INTO ulubione (promo_img, user_id, promo_id, promo_name, promo_earn) VALUES ('" . $url_img . "', '" . $user_id . "', '" . $promo_id . "', '" . $promo_name . "', '" . $promo_earn . "')");

    $sql2_1 = mysqli_query($conn, "INSERT INTO logi (user_id, what_did) VALUES ('" . $user_id . "', '" . $add . "')");
    $myObj->status_favorite = 1; //dodano
    $myJSON = json_encode($myObj);

    echo $myJSON;
}
?>
