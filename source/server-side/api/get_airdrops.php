<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
header("Content-type: application/json");

require_once 'Connection.php';

$featured = $_GET['featured'];


$conditions = [];

if ($featured == "yes") {
    $conditions[] = "visibility='1' AND featured='1'";
} else {
$new = $_GET['show_new'];
$hot = $_GET['show_hot'];
$exclusive = $_GET['show_exclusive'];
$upline = $_GET['show_upline'];
$inactive = $_GET['show_inactive'];
    if ($new == "1") {
        $conditions[] = "is_new='1'";
    }
    if ($hot == "1") {
        $conditions[] = "is_hot='1'";
    }
    if ($exclusive == "1") {
        $conditions[] = "exclusive='1'";
    }
    if ($upline == "1") {
        $conditions[] = "is_upline='1'";
    }
    if ($inactive == "1") {
        $conditions[] = "is_inactive='1'";
    }
}

$conditions_str = implode(' OR ', $conditions);

$query_drops = mysqli_query($conn, "SELECT * FROM airdrops WHERE $conditions_str ORDER BY date_add DESC");

if (!$query_drops) {
    die('Query failed: ' . mysqli_error($conn));
}

$response = array();

if (mysqli_num_rows($query_drops) > 0) {
    while ($row = mysqli_fetch_assoc($query_drops)) {
        $row['img'] = str_replace(array("\n", "\r"), '', $row['img']);

        $response[] = array(
            'airdrop_id' => $row['id'],
            'airdrop_title' => $row['title'],
            'article_description' => $row['description'],
            'airdrop_image' => $row['img'],
            'airdrop_value' => $row['value'],
            'airdrop_website' => $row['website'],
            'airdrop_exclusive' => $row['exclusive'],
            'airdrop_hot' => $row['is_hot'],
            'airdrop_new' => $row['is_new'],
            'airdrop_token' => $row['token'],
            'airdrop_upline' => $row['is_upline'],
            'airdrop_is_in_active' => $row['is_inactive'],
            'airdrop_featured' => $row['featured'],
            'airdrop_likes' => $row['likes'],
            'airdrop_views' => $row['views'],
            'airdrop_total_supply' => $row['total_supply'],
            'airdrop_kyc_required' => $row['kyc_required'],
            'airdrop_mail_required' => $row['mail_required'],
            'airdrop_twitter_required' => $row['twitter_required'],
            'airdrop_facebook_required' => $row['fb_required'],
            'airdrop_telegram_required' => $row['telegram_required'],
            'airdrop_platform' => $row['platform'],
            'airdrop_whitepaper' => $row['whitepaper'],
            'airdrop_twitter' => $row['twitter'],
            'airdrop_telegram_group' => $row['telegram_group'],
            'airdrop_medium' => $row['medium'],
            'airdrop_coinmarketcap' => $row['coinmarketcap'],
            'airdrop_reddit' => $row['reddit'],
            'airdrop_discord' => $row['discord'],
            'airdrop_exchanges' => $row['exchanges'],
            'airdrop_date_add' => $row['date_add'],
            'airdrop_date_finish' => $row['date_finish']
        );
    }
}

echo json_encode($response);

mysqli_close($conn);
?>
