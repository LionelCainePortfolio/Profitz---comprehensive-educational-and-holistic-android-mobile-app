<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
require_once 'Connection.php';
header('Content-Type: application/json');

$promo_id = $_GET["promo_id"];

$response = new stdClass();

$stmt = mysqli_prepare($conn, "SELECT AVG(stars) AS avg FROM opinie WHERE status='zatwierdzono' AND promo_id=?");

if ($stmt === false) {
    $response->status = "error";
    $response->message = "mysqli_prepare error: " . mysqli_error($conn);
    echo json_encode($response);
    exit();
}

mysqli_stmt_bind_param($stmt, "s", $promo_id);

mysqli_stmt_execute($stmt);

$result = mysqli_stmt_get_result($stmt);

if ($result === false) {
    $response->status = "error";
    $response->message = "mysqli_stmt_get_result error: " . mysqli_error($conn);
    echo json_encode($response);
    exit();
}

$roww = mysqli_fetch_assoc($result);
$avg = $roww['avg'];

if ($avg !== null) {
    $avg_round = bcdiv($avg, 1, 1);
} else {
    $avg_round = 0.0; 
}

mysqli_stmt_close($stmt);

$stmt_update = $conn->prepare("UPDATE promocje SET vote_average=? WHERE id = ?");
$stmt_update->bind_param("si", $avg_round, $promo_id);
$stmt_update->execute();

if ($stmt_update->affected_rows > 0) {
    $response->status = "success";
    $response->average_rating = $avg_round;
} else {
    $response->status = "error";
    $response->message = "No update needed or an error occurred.";
    $response->error = mysqli_error($conn);
}

$stmt_update->close();

echo json_encode($response);
?>
