<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
header("Content-type: application/json");
require_once 'Connection.php';

$promo_id = $_GET['promo_id'];

$query_check = mysqli_query($conn, "SELECT * FROM comments WHERE promo_id ='" . $promo_id . "' AND global_comm='1' ORDER BY date DESC");

$response = array();

while ($row = mysqli_fetch_assoc($query_check))
{

    if ($row['hasReply'] == "1")
    {
        $query_check2 = mysqli_query($conn, "SELECT * FROM comments WHERE promo_id ='" . $promo_id . "' AND reply_to_id='" . $row['id'] . "' ORDER BY date DESC LIMIT 1");
        $row2 = mysqli_fetch_assoc($query_check2);

        $first_reply_img = $row2['img'];
        $first_reply_username = $row2['username'];
        $first_reply_content = $row2['content'];

        array_push($response, array(
            'comment_id' => $row['id'],
            'comment_img' => $row['img'],
            'comment_username' => $row['username'],
            'comment_content' => $row['content'],
            'comment_date' => $row['date'],
            'comment_has_reply' => '1',
            'comment_first_reply_img' => $first_reply_img,
            'comment_first_reply_username' => $first_reply_username,
            'comment_first_content_reply' => $first_reply_content,
            'likes' => $row['likes'],
            'reply_to_id' => $row['reply_to_id'],
            'reply_to_reply_id' => $row['reply_to_reply_id'],
            'reply_to_username' => $row['reply_to_username']

        ));

    }
    else
    {

        array_push($response, array(
            'comment_id' => $row['id'],
            'comment_img' => $row['img'],
            'comment_username' => $row['username'],
            'comment_content' => $row['content'],
            'comment_date' => $row['date'],
            'comment_has_reply' => '0',
            'comment_first_reply_img' => 'none',
            'comment_first_reply_username' => 'none',
            'comment_first_content_reply' => 'none',
            'likes' => $row['likes'],
            'reply_to_id' => $row['reply_to_id'],
            'reply_to_reply_id' => $row['reply_to_reply_id'],
            'reply_to_username' => $row['reply_to_username']
        ));

    }
}
echo json_encode($response);

?>
