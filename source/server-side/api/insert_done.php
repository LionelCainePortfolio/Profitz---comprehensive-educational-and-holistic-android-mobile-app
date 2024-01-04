<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$requiredKeys = ['user_id', 'user_nickname', 'user_email', 'promo_id', 'promo_name', 'promo_email', 'promo_type2', 'promo_earn', 'promo_number_phone', 'optional', 'done_date'];

// Check if all required keys exist in $_POST
if (array_diff($requiredKeys, array_keys($_POST)) === []) {
    // All required keys are present
    $user_id = $_POST['user_id'];
    $user_nickname = $_POST['user_nickname'];
    $user_email = $_POST['user_email'];
    $promo_id = $_POST['promo_id'];
    $promo_name = $_POST['promo_name'];
    $promo_email = $_POST['promo_email'];
    $promo_type = $_POST['promo_type2'];
    $promo_earn_before = $_POST['promo_earn'];
    $promo_number_phone = $_POST['promo_number_phone'];
    $optional = $_POST['optional'];
    $done_date = $_POST['done_date'];
    $url_img = "https://yoururl.com/api/images/promos/" . $promo_name . ".png";

    $stmt1 = mysqli_query($conn, "SELECT * FROM users WHERE id='" . $user_id . "'");
    $row = $stmt1->fetch_assoc();
    $user_lvl = $row["user_level"];
    $user_dones = $row["done_count"];
    $points_user = $row["points"];
    if ($user_lvl == '0') {
        $promo_before_calculate = $promo_earn_before * 0.01;
        $promo_earn_bonus = $promo_before_calculate / 4.35;
    } elseif ($user_lvl == '1') {
        $promo_before_calculate = $promo_earn_before * 0.10;
        $promo_earn_bonus = $promo_before_calculate / 4.35;
    } elseif ($user_lvl == '2') {
        $promo_before_calculate = $promo_earn_before * 0.20;
        $promo_earn_bonus = $promo_before_calculate / 4.35;
    } elseif ($user_lvl == '3') {
        $promo_before_calculate = $promo_earn_before * 0.30;
        $promo_earn_bonus = $promo_before_calculate / 4.35;
    } elseif ($user_lvl == '4') {
        $promo_before_calculate = $promo_earn_before * 0.40;
        $promo_earn_bonus = $promo_before_calculate / 4.35;
    } elseif ($user_lvl == '5') {
        $promo_before_calculate = $promo_earn_before * 0.50;
        $promo_earn_bonus = $promo_before_calculate / 4.35;
    }
    $decimal_bonus = round($promo_earn_bonus, 2);
    $points_user_new = $points_user + $decimal_bonus;

    $stmt1 = mysqli_query($conn, "SELECT * FROM promocje WHERE id='" . $promo_id . "'");
    $row = $stmt1->fetch_assoc();
    $promo_with_star = $row["promo_with_star"];

    if ($promo_with_star == "1") {
        $sql1 = mysqli_query($conn, "INSERT INTO wykonane (user_id, user_nickname, user_email, promo_id, promo_name, promo_email, promo_type, promo_earn, promo_refback, promo_img, user_lvl, done_date, status, phone_number, optional_info) VALUES ('" . $user_id . "', '" . $user_nickname . "', '" . $user_email . "', '" . $promo_id . "', '" . $promo_name . "', '" . $promo_email . "', '" . $promo_type . "','" . $promo_earn_before . "', '" . $decimal_bonus . "','" . $url_img . "', '" . $user_lvl . "', '" . $done_date . "', 'w trakcie weryfikacji', '" . $promo_number_phone . "', '" . $optional . "')");
        if (!$sql1) {
            die("Error: " . mysqli_error($conn));
        }


        $done = "Użytkownik o ID:" . $user_id . " oznaczył iż wykonał promocję " . $promo_name . ". ID promocji:" . $promo_id . " Zarobek: " . $decimal_bonus . " punktów";
        $sql2 = mysqli_query($conn, "INSERT INTO logi (user_id, what_did) VALUES ('" . $user_id . "', '" . $done . "')");
    } else {
        $sql1 = mysqli_query($conn, "INSERT INTO wykonane (user_id, user_nickname, user_email, promo_id, promo_name, promo_email, promo_type, promo_earn, promo_refback, promo_img, user_lvl, done_date, status, phone_number, optional_info) VALUES ('" . $user_id . "', '" . $user_nickname . "', '" . $user_email . "', '" . $promo_id . "', '" . $promo_name . "', '" . $promo_email . "', '" . $promo_type . "','" . $promo_earn_before . "', '" . $decimal_bonus . "','" . $url_img . "', '" . $user_lvl . "', '" . $done_date . "', 'zatwierdzono', 'brak', '" . $optional . "')");
        if (!$sql1) {
            die("Error: " . mysqli_error($conn));
        }
        $done = "Użytkownik o ID:" . $user_id . " oznaczył iż wykonał promocję " . $promo_name . ". ID promocji:" . $promo_id;
        $sql2 = mysqli_query($conn, "INSERT INTO logi (user_id, what_did) VALUES ('" . $user_id . "', '" . $done . "')");
    }
} else {
    // Some required keys are missing
    $missingKeys = array_diff($requiredKeys, array_keys($_POST));
    echo "Error: Required keys are missing in the POST data: " . implode(', ', $missingKeys);
}
?>
