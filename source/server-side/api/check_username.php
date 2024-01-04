<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

// Check if the 'username' parameter is set in the GET request
if(isset($_GET['username'])){
    $username = $_GET['username'];

    // Use prepared statements to avoid SQL injection
    $stmt = $conn->prepare("SELECT * FROM users WHERE username = ?");
    $stmt->bind_param("s", $username);
    $stmt->execute();
    $result = $stmt->get_result();

    // Initialize $myObj as a new stdClass object
    $myObj = new stdClass();

    // Check if there are rows returned
    if($result->num_rows > 0){
        $myObj->username_valid = 1;
    } else {
        $myObj->username_valid = 0;
    }

    // Encode the object to JSON
    $myJSON = json_encode($myObj);

    // Output the JSON
    echo $myJSON;
} else {
    // Handle the case where 'username' parameter is not set
    echo "Username parameter not set.";
}

?>