<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$article_id = $_GET['article_id'];
$user_id = $_GET['user_id'];
$type = $_GET['type'];

// Check if the "status" key exists in the $_GET array
$status = isset($_GET['status']) ? $_GET['status'] : '';

$newuserid = "X" . $user_id . "X";

$query = mysqli_query($conn, "SELECT * FROM articles WHERE id='" . $article_id . "'");

$myObj = new stdClass(); // Inicjalizacja obiektu

if (mysqli_num_rows($query) > 0) {
    $ids = mysqli_fetch_assoc($query);

    if ($type == "yes") {
        $check_id = $ids["user_voted_yes_ids"];
        if (strpos($check_id, $newuserid) !== false) {
            $myObj->user_id_contains_article_yes = 1;
        } else {
            if ($check_id == "0") {
                $check_id2 = $newuserid . ",";
            } else {
                $check_id2 = $check_id . "," . $newuserid;
            }

            // Check if the "status" is not equal to "dontaddid"
            if ($status !== "dontaddid") {
                $query2 = mysqli_query($conn, "UPDATE articles SET user_voted_yes_ids='" . $check_id2 . "' WHERE id='" . $article_id . "'");
            }

            $myObj->user_id_contains_article_yes = 0;
        }
    } elseif ($type == "no") {
        $check_id3 = $ids["user_voted_no_ids"];
        if (strpos($check_id3, $newuserid) !== false) {
            $myObj->user_id_contains_article_no = 1;
        } else {
            if ($check_id3 == "0") {
                $check_id4 = $newuserid . ",";
            } else {
                $check_id4 = $check_id3 . "," . $newuserid;
            }

            // Check if the "status" is not equal to "dontaddid"
            if ($status !== "dontaddid") {
                $query2 = mysqli_query($conn, "UPDATE articles SET user_voted_no_ids='" . $check_id4 . "'  WHERE id='" . $article_id . "'");
            }

            $myObj->user_id_contains_article_no = 0;
        }
    }
} else {
    $myObj->user_id_contains_article = 0;
}

$myJSON = json_encode($myObj);
echo $myJSON;
?>
