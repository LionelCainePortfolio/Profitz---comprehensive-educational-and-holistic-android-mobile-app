<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$user_id = $_GET['user_id'];

$myObj = new stdClass(); 

if ($conn) {
    $stmt = mysqli_prepare($conn, "SELECT unread_messages FROM users WHERE id = ?");
    mysqli_stmt_bind_param($stmt, 's', $user_id);
    mysqli_stmt_execute($stmt);
    $result = mysqli_stmt_get_result($stmt);

    if ($result) {
        $row = mysqli_fetch_assoc($result);

        if ($row) {
            $myObj->unread_messages = $row['unread_messages'];
        } else {
            $myObj->error = 'User with given ID not found';
        }

        mysqli_free_result($result);
    } else {
        $myObj->error = 'Error executing query: ' . mysqli_error($conn);
    }

    mysqli_stmt_close($stmt);
} else {
    $myObj->error = 'Database connection error: ' . mysqli_connect_error();
}

$myJSON = json_encode($myObj);

echo $myJSON;
?>