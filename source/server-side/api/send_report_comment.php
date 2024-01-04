<?php
require_once 'Connection.php';

$comment_id = $_GET['comment_id'];
$user_id = $_GET['user_id'];
$promo_id = $_GET['promo_id'];
$username = $_GET['username'];
$reason = $_GET['reason'];

$query = mysqli_query($conn, "INSERT INTO comments_reports (`comment_id`, `promo_id`, `username`, `user_id`, `reason`) VALUES ('" . $comment_id . "', '" . $promo_id . "', '" . $username . "', '" . $user_id . "', '" . $reason . "')");
$query2 = mysqli_query($conn, "UPDATE comments SET reports=reports+1 WHERE promo_id='" . $promo_id . "' AND id='" . $comment_id . "'");

$myObj = new stdClass(); 
$myObj->response = 1;

$myJSON = json_encode($myObj);
echo $myJSON;
?>
