<?php
// Enable error reporting for debugging
error_reporting(E_ALL);
ini_set('display_errors', 1);

// Include the database connection file
require_once 'Connection.php';

// Get the username from the query parameters
$username = $_GET['username'];

// Get the file details
$uploadedFile = $_FILES["uploaded_file"];
$fileName = $uploadedFile["name"];
$fileExtension = "." . pathinfo($fileName, PATHINFO_EXTENSION);

// Define the target path for storing the uploaded file
$targetDirectory = "../images/users/";
$targetFilePath = $targetDirectory . $username . ".png";

// Define the URL for the uploaded image
$imageUrl = "https://yoururl.com/api/images/users/" . $username . ".png";

// Move the uploaded file to the target path
if (move_uploaded_file($uploadedFile['tmp_name'], $targetFilePath)) {
    // File upload success
    echo "success";

    // Update the database with the new image URL
    $updateQuery = "UPDATE users SET image_url='" . $imageUrl . "' WHERE username = '" . $username . "'";
    $updateResult = mysqli_query($conn, $updateQuery);

    if (!$updateResult) {
        // Handle database update failure
        echo "Database update failed";
    }
} else {
    // File upload failure
    echo "fail";
}
?>
