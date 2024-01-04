<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$comment_id = $_GET['comment_id'];
$promo_id = $_GET['promo_id'];
$user_id = $_GET['userid'];
$username = $_GET['username'];
$is_reply = $_GET['isreply'];

$response = new stdClass(); 

$response->response = 0;
$myJSON = json_encode($response);

if ($is_reply == "yes") {
    $query_check1 = mysqli_query($conn, "SELECT * FROM comments WHERE promo_id='" . $promo_id . "' AND id='" . $comment_id . "' AND username='" . $username . "'");
    $row = mysqli_fetch_assoc($query_check1);

    if ($row) {
        $reply_to_username = $row['reply_to_username'];

        $query_check2 = mysqli_query($conn, "SELECT * FROM comments WHERE promo_id='" . $promo_id . "' AND id='" . $comment_id . "' AND username='" . $reply_to_username . "'");
        $row2 = mysqli_fetch_assoc($query_check2);

        if ($row2) {
            $replytoid = $row2['reply_to_id'];
            $replytoreplyid = $row2['reply_to_reply_id'];

            $query2 = mysqli_query($conn, "UPDATE comments SET replies=replies-1 WHERE promo_id='" . $promo_id . "' AND id='" . $replytoid . "'");

            if ($replytoreplyid != 0 && $replytoreplyid != $replytoid) {
                $query22 = mysqli_query($conn, "UPDATE comments SET replies=replies-1 WHERE promo_id='" . $promo_id . "' AND id='" . $replytoreplyid . "'");
            }

            $query_check32 = mysqli_query($conn, "SELECT * FROM comments WHERE promo_id='" . $promo_id . "' AND id='" . $replytoid . "'");
            $row22 = mysqli_fetch_assoc($query_check32);
            $replies = $row22['replies'];

            if ($replies <= 0) {
                $query3 = mysqli_query($conn, "UPDATE comments SET hasReply=0 WHERE promo_id='" . $promo_id . "' AND id='" . $replytoid . "' AND username='" . $username . "'");
                $query4 = mysqli_query($conn, "UPDATE comments SET replies=0 WHERE promo_id='" . $promo_id . "' AND id='" . $replytoid . "' AND username='" . $username . "'");
            }

            $querydel = mysqli_query($conn, "DELETE FROM comments WHERE promo_id='" . $promo_id . "' AND id='" . $comment_id . "' AND username='" . $username . "'");

            $done = "Użytkownik o ID:" . $user_id . " usunął swój komentarz o id: " . $comment_id . ". ID promocji:" . $promo_id . " ";

            $sql_log = mysqli_query($conn, "INSERT INTO logi (user_id, what_did) VALUES ('" . $user_id . "', '" . $done . "')");

            $get_data_comm_to_del = mysqli_query($conn, "SELECT * FROM comments WHERE promo_id='" . $promo_id . "' AND id='" . $comment_id . "'");
            $row_get_data = mysqli_fetch_assoc($get_data_comm_to_del);

            if ($row_get_data) {
                $get_data_global_comm_username = $row_get_data['username'];
                $get_data_global_comm_user_id = $row_get_data['user_id'];
                $get_data_global_comm_content = $row_get_data['content'];
                $get_data_global_comm_hasReply = $row_get_data['hasReply'];
                $get_data_global_comm_img = $row_get_data['img'];
                $get_data_global_comm_reply_to_reply_id = $row_get_data['reply_to_reply_id'];
                $get_data_global_comm_reply_to_username = $row_get_data['reply_to_username'];
                $get_data_global_comm_global_comm = $row_get_data['global_comm'];

                $sql_deleted_comm = mysqli_query($conn, "INSERT INTO comments_deleted (`promo_id`, `username`, `user_id`, `content`,`img`,`hasReply`,`reply_to_id`,`reply_to_reply_id`,`reply_to_username`,`global_comm`) VALUES ('" . $promo_id . "', '" . $get_data_global_comm_username . "', '" . $get_data_global_comm_user_id . "', '" . $get_data_global_comm_content . "','" . $get_data_global_comm_img . "','" . $get_data_global_comm_hasReply . "','" . $comment_id . "','" . $get_data_global_comm_reply_to_reply_id . "', '" . $get_data_global_comm_reply_to_username . "', '" . $get_data_global_comm_global_comm . "')");
            }
        }
    }
} else {
    $querydel = mysqli_query($conn, "DELETE FROM comments WHERE promo_id='" . $promo_id . "' AND id='" . $comment_id . "' AND username='" . $username . "'");
    $querydel2 = mysqli_query($conn, "DELETE FROM comments WHERE promo_id='" . $promo_id . "' AND reply_to_id='" . $comment_id . "' AND username='" . $username . "'");

    $done = "Użytkownik o ID:" . $user_id . " usunął swój komentarz o id: " . $comment_id . ". ID promocji:" . $promo_id . " ";

    $sql_log = mysqli_query($conn, "INSERT INTO logi (user_id, what_did) VALUES ('" . $user_id . "', '" . $done . "')");

    $get_data_comm_to_del = mysqli_query($conn, "SELECT * FROM comments WHERE promo_id='" . $promo_id . "' AND id='" . $comment_id . "'");
    $row_get_data = mysqli_fetch_assoc($get_data_comm_to_del);

    if ($row_get_data) {
        $get_data_global_comm_username = $row_get_data['username'];
        $get_data_global_comm_user_id = $row_get_data['user_id'];
        $get_data_global_comm_content = $row_get_data['content'];
        $get_data_global_comm_hasReply = $row_get_data['hasReply'];
        $get_data_global_comm_img = $row_get_data['img'];
        $get_data_global_comm_reply_to_reply_id = $row_get_data['reply_to_reply_id'];
        $get_data_global_comm_reply_to_username = $row_get_data['reply_to_username'];
        $get_data_global_comm_global_comm = $row_get_data['global_comm'];

        $sql_deleted_comm = mysqli_query($conn, "INSERT INTO comments_deleted (`promo_id`, `username`, `user_id`, `content`,`img`,`hasReply`,`reply_to_id`,`reply_to_reply_id`,`reply_to_username`,`global_comm`) VALUES ('" . $promo_id . "', '" . $get_data_global_comm_username . "', '" . $get_data_global_comm_user_id . "', '" . $get_data_global_comm_content . "','" . $get_data_global_comm_img . "','" . $get_data_global_comm_hasReply . "','" . $comment_id . "','" . $get_data_global_comm_reply_to_reply_id . "', '" . $get_data_global_comm_reply_to_username . "', '" . $get_data_global_comm_global_comm . "')");
    }

    $get_data_reply_comm_to_del = mysqli_query($conn, "SELECT * FROM comments WHERE promo_id='" . $promo_id . "' AND id='" . $comment_id . "'");
    while ($row_get_data2 = mysqli_fetch_assoc($get_data_reply_comm_to_del)) {
        $get_data_reply_comm_username = $row_get_data2['username'];
        $get_data_reply_comm_user_id = $row_get_data2['user_id'];
        $get_data_reply_comm_content = $row_get_data2['content'];
        $get_data_reply_comm_hasReply = $row_get_data2['hasReply'];
        $get_data_reply_comm_img = $row_get_data2['img'];
        $get_data_reply_comm_reply_to_reply_id = $row_get_data2['reply_to_reply_id'];
        $get_data_reply_comm_reply_to_username = $row_get_data2['reply_to_username'];
        $get_data_reply_comm_global_comm = $row_get_data2['global_comm'];

        $done2 = "Komentarz użytkownika o ID:" . $user_id . " został usunięty. Ponieważ użytkownik o ID usunął swój komentarz o id: " . $comment_id . ". ID promocji:" . $promo_id . " ";

        $sql_deleted_comm = mysqli_query($conn, "INSERT INTO comments_deleted (`promo_id`, `username`, `user_id`, `content`,`img`,`hasReply`,`reply_to_id`,`reply_to_reply_id`,`reply_to_username`,`global_comm`) VALUES ('" . $promo_id . "', '" . $get_data_reply_comm_username . "', '" . $get_data_reply_comm_user_id . "', '" . $get_data_reply_comm_content . "','" . $get_data_reply_comm_img . "','" . $get_data_reply_comm_hasReply . "','" . $comment_id . "','" . $get_data_reply_comm_reply_to_reply_id . "', '" . $get_data_reply_comm_reply_to_username . "', '" . $get_data_reply_comm_global_comm . "')");
    }
}

echo $myJSON;
?>
