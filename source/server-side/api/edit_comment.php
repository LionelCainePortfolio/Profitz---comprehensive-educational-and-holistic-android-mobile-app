<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$comment_id = $_GET['comment_id'];
$promo_id = $_GET['promo_id'];
$username = $_GET['username'];
$content = $_GET['content'];

$myObj = new stdClass(); 

$myObj->response = 2;
$myJSON = json_encode($myObj);

$query = mysqli_query($conn, "UPDATE comments SET content='" . $content . "' WHERE promo_id='" . $promo_id . "' AND id='" . $comment_id . "' AND username='" . $username . "'");

echo $myJSON;
?>
