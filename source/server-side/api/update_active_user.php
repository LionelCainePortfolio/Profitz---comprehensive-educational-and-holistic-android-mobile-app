<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');

require_once 'Connection.php';

$user_id = mysqli_real_escape_string($conn, $_GET['user_id']);

$query = mysqli_query($conn, "SELECT * FROM users WHERE id='".$user_id."'");

if(mysqli_num_rows($query) > 0){
    $query2=mysqli_query($conn, "UPDATE users SET is_online='true' WHERE id='".$user_id."'");
    
    // Retrieve last_scene from the database
    $result = mysqli_query($conn, "SELECT last_scene FROM users WHERE id='".$user_id."'");
    $row = mysqli_fetch_assoc($result);
    $last_scene = $row['last_scene'];

    // Update last_scene with the current timestamp
    $query3=mysqli_query($conn, "UPDATE users SET last_scene=NOW() WHERE id='".$user_id."'");

    $myObj = new stdClass();
    $myObj->response = 1;
    $myObj->last_scene = $last_scene; // Include last_scene in the response
    $myJSON = json_encode($myObj);
    echo $myJSON;
}
?>