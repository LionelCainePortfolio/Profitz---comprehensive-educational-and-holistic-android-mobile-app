<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$response = array();
$myObj = new stdClass(); // Initialize $myObj

// Check if the required keys exist in the $_GET array
if (
    isset($_GET['comment_id']) &&
    isset($_GET['user_id']) &&
    isset($_GET['promo_id']) &&
    isset($_GET['username']) &&
    isset($_GET['content']) &&
    isset($_GET['is_reply']) &&
    isset($_GET['global_comm']) &&
    isset($_GET['reply_username']) &&
    isset($_GET['reply_username_true']) &&
    isset($_GET['reply_id']) &&
    isset($_GET['comment_action'])
) {
    $comment_id = $_GET['comment_id'];
    $user_id = $_GET['user_id'];
    $promo_id = $_GET['promo_id'];
    $username = $_GET['username'];
    $content = $_GET['content'];
    $is_reply = $_GET['is_reply'];
    $global_comm = $_GET['global_comm'];
    $reply_username = $_GET['reply_username'];
    $reply_username_true = $_GET['reply_username_true'];
    $reply_id = $_GET['reply_id'];
    $comment_action = $_GET['comment_action'];

    $query_check_image = mysqli_query($conn, "SELECT * FROM users WHERE id = '" . $user_id . "'");
    $row = mysqli_fetch_assoc($query_check_image);
    $img_row = $row['image_url'];
    $check_img = 'https:/yoururl.com/api/images/users/default.png?type=small';

    if ($img_row == $check_img) {
        $img = 'https://yoururl.com/api/images/users/default.png?type=small';
    } else {
        $img = 'https://yoururl.com/api/images/users/' . $username . '.png';
    }

    if ($comment_action == 'edit') {
        $query = mysqli_query($conn, "UPDATE comments SET content='" . $content . "' WHERE promo_id='" . $promo_id . "' AND id='" . $comment_id . "' AND username='" . $username . "'");

        $myObj->response = 2;
    } else {
        if ($is_reply == 'yes') {
            $query = mysqli_query($conn, "INSERT INTO comments (`promo_id`, `username`, `user_id`, `content`,`img`,`hasReply`,`reply_to_id`,`reply_to_reply_id`,`reply_to_username`,`global_comm`) VALUES ('" . $promo_id . "', '" . $username . "', '" . $user_id . "', '" . $content . "','" . $img . "','0','" . $comment_id . "','" . $reply_id . "', '" . $reply_username . "', '0')");

            $query_check3 = mysqli_query($conn, "SELECT * FROM comments WHERE promo_id='" . $promo_id . "' AND id='" . $comment_id . "'");
            $row = mysqli_fetch_assoc($query_check3);
            $hasReply = $row['hasReply'];

            $query3 = mysqli_query($conn, "UPDATE comments SET hasReply=1 WHERE promo_id='" . $promo_id . "' AND id='" . $comment_id . "'");
            $query2 = mysqli_query($conn, "UPDATE comments SET replies=replies+1 WHERE promo_id='" . $promo_id . "' AND id='" . $comment_id . "'");

            if ($comment_id != $reply_id) {
                $query3 = mysqli_query($conn, "UPDATE comments SET replies=replies+1 WHERE promo_id='" . $promo_id . "' AND id='" . $reply_id . "'");
            }
        } else {
            $query = mysqli_query($conn, "INSERT INTO comments (`promo_id`, `username`, `user_id`, `content`,`img`,`hasReply`,`global_comm`) VALUES ('" . $promo_id . "', '" . $username . "', '" . $user_id . "', '" . $content . "','" . $img . "','0', '1')");
        }

        $myObj->response = 1;
    }

    $myJSON = json_encode($myObj);
    echo $myJSON;
} else {
    // Handle the case when required keys are not set
    $response['error'] = 'Missing required parameters';
    echo json_encode($response);
}
?>
