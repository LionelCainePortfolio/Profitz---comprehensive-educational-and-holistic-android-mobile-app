<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
header("Content-type: application/json");
require_once 'Connection.php';

$comment_id = $_GET['comment_id'];

$query_check = mysqli_query($conn, "SELECT * FROM comments WHERE reply_to_id='" . $comment_id . "' AND global_comm='0' ORDER BY date ASC");

$response = array();
while ($row = mysqli_fetch_assoc($query_check))
{
    $response[] = array(
        'reply_id' => $row['id'],
        'reply_img' => $row['img'],
        'reply_username' => $row['username'],
        'reply_content' => $row['content'],
        'reply_date' => $row['date'],
        'reply_has_reply' => '0',
        'reply_first_reply_img' => 'none',
        'reply_first_reply_username' => 'none',
        'reply_first_content_reply' => 'none',
        'likes' => $row['likes'],
        'reply_to_id' => $row['reply_to_id'],
        'reply_to_reply_id' => $row['reply_to_reply_id'],
        'reply_to_username' => $row['reply_to_username']
    );
}

echo json_encode($response);
?>