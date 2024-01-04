<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';
header('Content-Type: application/json');

$jsonInput = file_get_contents('php://input');

$obj = json_decode($jsonInput, true);

$user_id =  $obj['data']['user_id'];
$question1_type = $obj['data']['question1_type'];
$question2_type = $obj['data']['question2_type'];
$question3_type = $obj['data']['question3_type'];
$question4_type = $obj['data']['question4_type'];
$question5_type = $obj['data']['question5_type'];
$title = $obj['data']['title'];
$short_desc = $obj['data']['short_desc'];
$logo_url = $obj['data']['logo_url'];
$website = $obj['data']['website'];
$form_url = $obj['data']['form_url'];
$coinmarketcap_url = $obj['data']['coinmarketcap_url'];
$whitepaper_url = $obj['data']['whitepaper_url'];
$twitter_url = $obj['data']['twitter_url'];
$telegram_url = $obj['data']['telegram_url'];
$facebook_url = $obj['data']['facebook_url'];
$medium_url = $obj['data']['medium_url'];
$reddit_url = $obj['data']['reddit_url'];
$discord_url = $obj['data']['discord_url'];
$token = $obj['data']['token'];
$value = $obj['data']['value'];
$exchanges = $obj['data']['exchanges'];
$supply = $obj['data']['supply'];
$date_end = $obj['data']['date_end'];
$date_distribution = $obj['data']['date_distribution'];
$desc = $obj['data']['desc'];
$instruction = $obj['data']['instruction'];

$value_double = doubleval($value);
$sql_u2 = mysqli_query($conn, "UPDATE users SET airdrops_count=airdrops_count+1 WHERE id = '".$user_id."'");	

$sql2 =  mysqli_query($conn, "INSERT INTO airdrops_pending (user_id, title, description, short_desc, img, value, website, token, total_supply, kyc_required, mail_required, twitter_required, telegram_required, fb_required, whitepaper, twitter, telegram_group, medium, coinmarketcap, reddit, discord, exchanges, date_end, date_finish) VALUES ('".$user_id."','".$title."', '".$instruction."', '".$short_desc."',  '".$logo_url."', '".$value_double."', '".$website."', '".$token."', '".$supply."', '".$question1_type."', '".$question2_type."', '".$question3_type."', '".$question4_type."', '".$question5_type."', '".$whitepaper_url."', '".$twitter_url."', '".$telegram_url."', '".$medium_url."', '".$coinmarketcap_url."', '".$reddit_url."', '".$discord_url."', '".$exchanges."', '".$date_end."', '".$date_distribution."')");

$query_drops = mysqli_query($conn, "SELECT * FROM airdrops_pending WHERE user_id='".$user_id."' AND title='".$title."'");
$row = mysqli_fetch_assoc($query_drops);
$airdrop_id= $row['id'];

$add = "Użytkownik o ID: ".$user_id." dodał airdrop. ID airdropu:".$airdrop_id;

$sql2_1 =  mysqli_query($conn, "INSERT INTO logi (user_id, what_did) VALUES ('".$user_id."', '".$add."')");

$myObj = new stdClass(); 
$myObj->status_airdrop= 1; 
$myJSON = json_encode($myObj);

echo $myJSON;
?>
