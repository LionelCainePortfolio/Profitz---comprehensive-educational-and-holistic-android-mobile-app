<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

// Check if the 'email' parameter is set in the GET request
if(isset($_GET['email'])){
    $email = $_GET['email'];

    // Use prepared statements to avoid SQL injection
    $stmt = $conn->prepare("SELECT * FROM users WHERE email = ?");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();

    // Check if there are rows returned
    if($result->num_rows > 0){
        $myObj = new stdClass(); // Initialize $myObj as a new stdClass object

        $myObj->email_valid = 1;
    } else {
        $myObj = new stdClass(); // Initialize $myObj as a new stdClass object

        $myObj->email_valid = 0;
    }

    // Encode the object to JSON
    $myJSON = json_encode($myObj);

    // Output the JSON
    echo $myJSON;
} else {
    // Handle the case where 'email' parameter is not set
    echo "Email parameter not set.";
}

 ?>