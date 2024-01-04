<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$user_id = $_GET['user_id'];
$code = $_GET['safety_code'];
$bonus = $_GET['bonus'];

// Hardcoded safety code for demonstration purposes (replace with your actual safety code)
$validSafetyCode = "A4S3D234S3Aar3K4aC3a3sm3fk2aDNA44SDFA1a35sd5aKASD4033323413KA56DLARO2r55JDAOS24DLCNZ";

if ($code == $validSafetyCode) {
    $stmt = mysqli_query($conn, "SELECT * FROM users WHERE id='" . $user_id . "'");
    $row = $stmt->fetch_assoc();

    $points_user = $row["points"];
    $ads_count = $row["ads_count"];
    $ads_count_new = $ads_count + 1;
    $decimal_bonus = round($bonus, 2);

    $points_new = $points_user + $decimal_bonus;

    // Update points and ads count
    $updatePointsQuery = mysqli_query($conn, "UPDATE users SET points='" . $points_new . "', ads_count='" . $ads_count_new . "' WHERE id='" . $user_id . "'");

    // Log the action
    $done = "User ID: " . $user_id . " watched their " . $ads_count_new . " ad and earned " . $decimal_bonus . " points.";
    $logActionQuery = mysqli_query($conn, "INSERT INTO logi (user_id, what_did) VALUES ('" . $user_id . "', '" . $done . "')");

    // Send a notification to the user
    $img_notification = "https://yoururl.com/api/images/notifications/ads_earn.png";
    $notification_title = "Reward: Bonus Points";
    $notification_description = "You earned " . $decimal_bonus . " bonus points for watching an ad!";
    $sendNotificationQuery = mysqli_query($conn, "INSERT INTO powiadomienia (user_id, title, description, type, earn, earn_type, img) VALUES ('" . $user_id . "' ,'" . $notification_title . "', '" . $notification_description . "', 'points', '" . $decimal_bonus . "','PKT', '" . $img_notification . "')");

    // Check if queries were successful (add more checks if needed)
    if ($updatePointsQuery && $logActionQuery && $sendNotificationQuery) {
        echo "Success"; // Or any other success response
    } else {
        echo "Error updating data"; // Or any other error response
    }
} else {
    echo "Invalid safety code";
}
?>
