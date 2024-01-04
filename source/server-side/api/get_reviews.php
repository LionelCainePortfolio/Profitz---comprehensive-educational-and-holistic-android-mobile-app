<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
header("Content-type: application/json");
require_once 'Connection.php';

$promo_id = isset($_GET['promo_id']) ? $_GET['promo_id'] : null;
$review_type = isset($_GET['review_type']) ? $_GET['review_type'] : "other";
$limit = isset($_GET['limit']) ? $_GET['limit'] : null;
$type_points = "PKT";

if ($limit == "yes") {
    if ($review_type == "other") {
        $query_check = mysqli_query($conn, "SELECT * FROM opinie WHERE status='zatwierdzono' AND promo_id='" . $promo_id . "' ORDER BY date_add DESC LIMIT 5");
    } else if ($review_type == "points") {
        $query_check = mysqli_query($conn, "SELECT * FROM opinie WHERE status='zatwierdzono' AND  earn_type='" . $type_points . "' AND promo_id='" . $promo_id . "' ORDER BY date_add DESC LIMIT 5");
    } else {
        $query_check = mysqli_query($conn, "SELECT * FROM opinie WHERE status='zatwierdzono' AND promo_id='" . $promo_id . "' ORDER BY date_add DESC LIMIT 5");
    }
} else {
    if ($review_type == "other") {
        $query_check = mysqli_query($conn, "SELECT * FROM opinie WHERE status='zatwierdzono' AND promo_id='" . $promo_id . "' ORDER BY date_add");
    } else {
        $query_check = mysqli_query($conn, "SELECT * FROM opinie WHERE status='zatwierdzono' AND promo_id='" . $promo_id . "' ORDER BY date_add");
    }
}

$response = array();

while ($row = mysqli_fetch_assoc($query_check)) {
    array_push($response, array(
        'review_id' => $row['id'],
        'review_name' => $row['name'],
        'review_description' => $row['description'],
        'review_img' => $row['img'],
        'review_stars' => $row['stars'],
        'review_date_add' => $row['date_add']
    ));
}
echo json_encode($response);
?>
