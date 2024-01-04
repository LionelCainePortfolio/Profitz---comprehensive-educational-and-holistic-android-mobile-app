<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
require_once 'Connection.php';

if(isset($_GET['user_id'])) {
    $user_id = $_GET['user_id'];

    $subArray = array();
    $stmt = mysqli_prepare($conn, "SELECT * FROM users WHERE id = ?");
    mysqli_stmt_bind_param($stmt, 's', $user_id);
    mysqli_stmt_execute($stmt);
    $result = mysqli_stmt_get_result($stmt);

    while ($row = mysqli_fetch_array($result)) {
        $subArray['user_id'] = $row['id'];
        $subArray['username'] = $row['username'];
        $subArray['favorite_count'] = $row['favourite_count'];
        $subArray['done_count'] = $row['done_count'];
        $subArray['first_introduction'] = $row['first_introduction'];
        $subArray['refferal_count'] = $row['refferal_count'];
        $subArray['refferal_success_count'] = $row['refferal_success_count'];
        $subArray['refferal_earn'] = $row['refferal_earn'];
        $subArray['points'] = $row['points'];
        $subArray['earned'] = $row['earned'];
        $subArray['user_code'] = $row['user_code'];
        $subArray['username'] = $row['username'];
        $subArray['user_level'] = $row['user_level'];
        $subArray['premium'] = $row['premium'];
        $subArray['image_url'] = $row['image_url'];
        $subArray['active_yearly_discount'] = $row['has_active_discount_premium_yearly'];
        $subArray['discount_to'] = $row['discount_to'];
        $subArray['how_much_procent'] = $row['discount_how_much_procent'];
        $subArray['airdrop_introduction'] = $row['airdrop_introduction'];
    }

    mysqli_stmt_close($stmt);

    echo json_encode($subArray);
} else {
    echo json_encode(array('error' => 'User ID not provided'));
}
?>