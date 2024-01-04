<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$comment_id = $_GET['comment_id'];
$user_id = $_GET['user_id'];
$promo_id = $_GET['promo_id'];

$query = mysqli_query($conn, "SELECT * FROM comments_likes_ids WHERE comment_id='" . $comment_id . "' AND user_id='" . $user_id . "'");
$queryy = mysqli_query($conn, "SELECT * FROM comments WHERE id='" . $comment_id . "' AND promo_id='" . $promo_id . "'");

if (mysqli_num_rows($query) > 0) {
    $query1 = mysqli_query($conn, "UPDATE comments SET likes=likes-1 WHERE id='" . $comment_id . "'");
    $query2 = mysqli_query($conn, "DELETE FROM comments_likes_ids WHERE comment_id='" . $comment_id . "' AND user_id='" . $user_id . "'");

    $row = mysqli_fetch_assoc($queryy);
    $likes2 = $row ? $row['likes'] : 0;

    $myObj = new stdClass();
    $myObj->response = 0;
    $myObj->likes = $likes2;

    $myJSON = json_encode($myObj);
    echo $myJSON;

} else {
    $query1 = mysqli_query($conn, "UPDATE comments SET likes=likes+1 WHERE id='" . $comment_id . "'");
    $query2 = mysqli_query($conn, "INSERT INTO comments_likes_ids (`comment_id`, `user_id`) VALUES ('" . $comment_id . "','" . $user_id . "')");

    $row = mysqli_fetch_assoc($queryy);
    $likes2 = $row ? $row['likes'] : 0;

    $myObj = new stdClass();
    $myObj->response = 1;
    $myObj->likes = $likes2;

    $myJSON = json_encode($myObj);
    echo $myJSON;
}
?>