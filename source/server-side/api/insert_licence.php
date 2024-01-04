<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once "Connection.php";

$user_id = $_GET["user_id"];
$user_name = $_GET["user_name"];
$promo_id = $_GET["promo_id"];
$promo_name = $_GET["promo_name"];
$promo_url = isset($_GET["url"]) ? $_GET["url"] : null; // Check if "url" is set
$licence_cost = $_GET["licence_cost"];

if ($promo_url !== null) { // Proceed only if "url" is set
    $query_check = mysqli_query(
        $conn,
        "SELECT * FROM instrukcje WHERE user_id='" .
            $user_id .
            "' AND promo_id='" .
            $promo_id .
            "' AND promo_name='" .
            $promo_name .
            "'"
    );

    if (mysqli_num_rows($query_check) > 0) {
        $myObj->status_promo_favorite = 1;
    } else {
        $sql_results = mysqli_query(
            $conn,
            "SELECT * FROM users WHERE id='" . $user_id . "'"
        );
        $row = mysqli_fetch_array($sql_results);
        $new_points = $row["points"] - $licence_cost;

        $sql1 = mysqli_query(
            $conn,
            "UPDATE users SET instructions_count=instructions_count+1, points='" .
                $new_points .
                "' WHERE id = '" .
                $user_id .
                "'"
        );

        // Ensure that 'user_id' is a valid column in the 'instrukcje' table
        $sql2 = mysqli_query(
            $conn,
            "INSERT INTO instrukcje (user_id, user_name, promo_id, promo_name, promo_url, date_add) VALUES ('" .
                $user_id .
                "', '" .
                $user_name .
                "', '" .
                $promo_id .
                "', '" .
                $promo_name .
                "', '" .
                $promo_url .
                "', NOW())"
        );

        $myObj->status_promo_favorite = 0; //dodano
    }

    $myJSON = json_encode($myObj);
    echo $myJSON;
} else {
    // Handle the case when "url" is not set
    // You might want to return an error response or take appropriate action
    echo "Error: 'url' parameter is not set.";
}
?>