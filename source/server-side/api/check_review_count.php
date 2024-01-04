<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';


if(isset($_GET['promo_id'])) {
    $promo_id = $_GET['promo_id'];

// Zapytanie SQL
$sql = "SELECT COUNT(*) as row_count FROM opinie WHERE promo_id = $promo_id";
    $result = mysqli_query($conn, $sql);

    if ($result) {
        $row = mysqli_fetch_assoc($result);

        $response = array('count_reviews' => $row['row_count']);
        echo json_encode($response);
    } else {
        // Handle the case when the query fails
        $error_response = array('error' => 'Error executing the query');
        echo json_encode($error_response);
    }
} else {
    // Handle the case when 'promo_id' is not set in the request
    $error_response = array('error' => 'Parameter "promo_id" is missing');
}
$conn->close();

?>