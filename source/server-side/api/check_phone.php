<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

// Check if the 'phone_number' parameter is set in the GET request
if(isset($_GET['phone'])){
    $phone_number = $_GET['phone'];

    // Use prepared statements to avoid SQL injection
    $stmt = $conn->prepare("SELECT * FROM users WHERE phone_number = ?");
    $stmt->bind_param("s", $phone_number);
    $stmt->execute();
    $result = $stmt->get_result();

    // Initialize $myObj as a new stdClass object
    $myObj = new stdClass();

    // Check if there are rows returned
    if($result->num_rows > 0){
        $myObj->phone_valid = 1;
    } else {
        $myObj->phone_valid = 0;
    }

    // Encode the object to JSON
    $myJSON = json_encode($myObj);

    // Output the JSON
    echo $myJSON;
} else {
    // Handle the case where 'phone_number' parameter is not set
    echo "Phone number parameter not set.";
}

?>