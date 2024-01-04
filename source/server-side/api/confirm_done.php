<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$user_id = $_GET['user_id'];
$promo_id = $_GET['promo_id'];
$promo_name = $_GET['promo_name'];
$promo_earn_before = $_GET['promo_earn'];

// Calculate promo earn
$promo_earn = $promo_earn_before / 4.34;

$sql_u1 = mysqli_query($conn, "UPDATE promocje SET dones=dones+1 WHERE id = '" . $promo_id . "'");
$sql_u2 = mysqli_query($conn, "UPDATE users SET done_count=done_count+1 WHERE id = '" . $user_id . "'");

$stmt1 = mysqli_query($conn, "SELECT * FROM users WHERE id='" . $user_id . "'");
$row = $stmt1->fetch_assoc();

if ($row) {
    $user_lvl = $row["user_level"];
    $user_dones = $row["done_count"];
    $points_user = $row["points"];
    $earned_user = $row["earned"];
    $dones_user = $row["dones"];
    $notification_token = $row["token_notifications"];
    $first_promo_user = $row["first_promo"];
    $refferal_user = $row["refferal_name"];

    $stmt2 = mysqli_query($conn, "SELECT * FROM wykonane WHERE user_id='" . $user_id . "' AND promo_id='" . $promo_id . "'");
    $row2 = $stmt2->fetch_assoc();

    if ($row2) {
        $user_earned_points = $row2["promo_earn"];
        $points_user_new = $points_user + $user_earned_points;
        $earned_user_new = $earned_user + $promo_earn_before;
        $dones_user_new = $dones_user + 1;

        $sql_update = mysqli_query($conn, "UPDATE wykonane SET status='zatwierdzono' WHERE promo_id='" . $promo_id . "' AND user_id = '" . $user_id . "'");
        $sql_u2 = mysqli_query($conn, "UPDATE users SET points='" . $points_user_new . "' WHERE id = '" . $user_id . "'");
        $sql_u4 = mysqli_query($conn, "UPDATE users SET earned='" . $earned_user_new . "' WHERE id = '" . $user_id . "'");
    } else {
        $dones_user_new = $dones_user + 1;
    }

    if ($first_promo_user == 0) {
        $sql_u5 = mysqli_query($conn, "UPDATE users SET first_promo=1 WHERE id = '" . $user_id . "'");
        $sql_u6 = mysqli_query($conn, "UPDATE users SET points=points+10 WHERE id = '" . $user_id . "'");
 

    }

    $query_check_ref_task = mysqli_query($conn, "SELECT * FROM ukonczone_zadania WHERE user_id='" . $user_id . "' AND task_id='2' ");

    if (mysqli_num_rows($query_check_ref_task) == 0) {
      
    }

    $done = "Zatwierdzono wykonanie promocji użytkownika o ID:" . $user_id . " nazwa promocji: " . $promo_name . ". ID promocji:" . $promo_id . "Zarobek: " . $promo_earn_bonus . " punktów";

    $sql1_1 = mysqli_query($conn, "INSERT INTO logi (user_id, what_did) VALUES ('" . $user_id . "', '" . $done . "')");

    if ($user_dones == '0') {
        $sql_u3 = mysqli_query($conn, "UPDATE users SET user_level='0' WHERE id = '" . $user_id . "'");
    } elseif ($user_dones == '1') {
        $sql_u3 = mysqli_query($conn, "UPDATE users SET user_level='1' WHERE id = '" . $user_id . "'");
    } elseif ($user_dones == '3') {
        $sql_u3 = mysqli_query($conn, "UPDATE users SET user_level='2' WHERE id = '" . $user_id . "'");
    } elseif ($user_dones == '5') {
        $sql_u3 = mysqli_query($conn, "UPDATE users SET user_level='3' WHERE id = '" . $user_id . "'");
    } elseif ($user_dones == '8') {
        $sql_u3 = mysqli_query($conn, "UPDATE users SET user_level='4' WHERE id = '" . $user_id . "'");
    } elseif ($user_dones == '12') {
        $sql_u3 = mysqli_query($conn, "UPDATE users SET user_level='5' WHERE id = '" . $user_id . "'");
    }

    // Send notification
    $title_notification = "Weryfikacja zakończona!";
    $message_notification = "Zaktualizowaliśmy status weryfikacji ukończenia przez Ciebie promocji " . $promo_name . "! Kliknij by sprawdzić.";

    $data = array(
        "to" => $notification_token,
        "notification" => array(
            "title" => $title_notification,
            "text" => $message_notification,
            "icon" => "icon.png"
        ) ,
        "data" => array(
            "keyname" => "test"
        )
    );

    $data_string = json_encode($data);
    echo "The Json Data : " . $data_string;

    $headers = array(
        'Authorization: key=AA:BBB-CCC',
        'Content-Type: application/json'
    );

    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $data_string);
    $result = curl_exec($ch);
    echo $result;

    $sql2 = mysqli_query($conn, "INSERT INTO sent_notifications_private (title, message, message_id) VALUES ('" . $title_notification . "', '" . $message_notification . "', '" . $result . "')");
    curl_close($ch);
} else {
    echo "User not found.";
}
?>
