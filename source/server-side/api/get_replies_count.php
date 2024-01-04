<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
header("Content-type: application/json");
require_once 'Connection.php';

$comment_id = $_GET['comment_id'];
$query_check = mysqli_query($conn, "SELECT * FROM comments WHERE reply_to_id='" . $comment_id . "' AND global_comm='0'");
$rows = mysqli_num_rows($query_check);

$subArray = array();
$subArray['count_replies'] = $rows;
echo json_encode($subArray);
?>