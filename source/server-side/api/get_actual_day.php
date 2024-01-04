<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
require_once 'Connection.php';

$user_id = $_GET['user_id'];

$subArray = array();
$sql_results = mysqli_query($conn, "SELECT * FROM users WHERE id='" . $user_id . "'");

while ($row = mysqli_fetch_array($sql_results)) {
    $subArray['actual_day'] = $row['actual_day'];
    $subArray['points_yesterday'] = $row['points_yesterday'];
}

echo json_encode($subArray);
?>
