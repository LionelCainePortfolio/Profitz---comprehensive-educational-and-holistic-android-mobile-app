<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
require_once 'Connection.php';

// Validate and sanitize input parameters
$user_id = isset($_GET['user_id']) ? mysqli_real_escape_string($conn, $_GET['user_id']) : '';
$promo_id = isset($_GET['promo_id']) ? mysqli_real_escape_string($conn, $_GET['promo_id']) : '';

$myObj = new stdClass(); // Initialize $myObj

// Use prepared statements to prevent SQL injection
$query = mysqli_prepare($conn, "SELECT * FROM licencje WHERE user_id = ? AND promo_id = ?");

if ($query === false) {
    die(mysqli_error($conn));  // Print the error message
}

mysqli_stmt_bind_param($query, 'ss', $user_id, $promo_id);

if (!mysqli_stmt_execute($query)) {
    die(mysqli_stmt_error($query));  // Print the error message
}

$result = mysqli_stmt_get_result($query);

if ($result && mysqli_num_rows($result) > 0) {
    $myObj->status = 1;
} else {
    $myObj->status = 0;
}

$myJSON = json_encode($myObj);
echo $myJSON;

// Close the prepared statement and database connection
mysqli_stmt_close($query);
mysqli_close($conn);
?>
