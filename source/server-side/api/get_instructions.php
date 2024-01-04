<?php
ini_set('display_errors', '0');
ini_set('display_startup_errors', '0');
header("Content-type: application/json");
if ($_GET['simply']){
$promo_name = "simply_".$_GET['promo_name']; 

}
else{
	$promo_name = $_GET['promo_name']; 

}


$host = "host";  
$username = "username";  
$password = "pass"; 
$database = "db_instructions";    

// Create a new database connection
$conn = new mysqli($host, $username, $password, $database);

// Check the database connection
if ($conn->connect_error) {
    die("Database connection failed: " . $conn->connect_error);
}

// Build and execute the SQL query
$promo_name_to_lower = strtolower($promo_name);
$sql = 'SELECT * FROM ' . $promo_name_to_lower;

$query_check = mysqli_query($conn, $sql);

// Check for SQL query execution errors
if (!$query_check) {
    die('Error in SQL query: ' . mysqli_error($conn));
}

// Fetch and format results
$response = array();

while ($row = mysqli_fetch_assoc($query_check)) {
    array_push($response, array(
        'promo_name' => $row['promo_name'],
        'promo_id' => $row['promo_id'],
        'promo_title' => $row['promo_title'],
        'promo_step_number' => $row['step'],
        'promo_step_description' => $row['step_description'],
        'promo_step_description2' => $row['step_description2'],
        'promo_step_description3' => $row['step_description3'],
        'promo_url_screen1' => $row['url_screen1'],
        'promo_url_screen2' => $row['url_screen2']
    ));
}

// Output JSON response
echo json_encode($response);

// Close the database connection
mysqli_close($conn);

 ?>

