<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$comment_id = $_GET['comment_id'];
$user_id = $_GET['user_id'];
$myObj = new stdClass(); // Initialize the object

$query = mysqli_query($conn, "SELECT * FROM comments_likes_ids WHERE comment_id='" . $comment_id . "' AND user_id='" . $user_id . "'");

if (mysqli_num_rows($query) > 0) {
    $myObj->response = 1; // Row exists
} else {
    $myObj->response = 0; // Row does not exist
}

$myJSON = json_encode($myObj);
echo $myJSON;
?>
