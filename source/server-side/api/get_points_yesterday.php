<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
header("Content-type: application/json");
require_once 'Connection.php';

// Check if the required parameter is set
if (!isset($_GET['user_id'])) {
    echo json_encode(array('error' => 'Missing user_id parameter.'));
    exit;
}

$user_id = $_GET['user_id'];

$query_check = mysqli_query($conn, "SELECT * FROM users WHERE id='" . $user_id . "'");

// Check if the query was successful
if ($query_check) {
    // Check if any rows were returned
    if (mysqli_num_rows($query_check) > 0) {
        // Fetch the first row
        $row_points_yesterday = mysqli_fetch_assoc($query_check);
        $points_yesterday = $row_points_yesterday["points_yesterday"];

        // Create an object
        $myObj = new stdClass();

        $myObj->points_yesterday = $points_yesterday;
        $myJSON = json_encode($myObj);
        echo $myJSON;
    } else {
        echo json_encode(array('error' => 'No rows found for user_id: ' . $user_id));
    }
} else {
    echo json_encode(array('error' => 'Error in query: ' . mysqli_error($conn)));
}
?>
