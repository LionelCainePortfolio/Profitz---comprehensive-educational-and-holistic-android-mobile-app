<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
require_once 'Connection.php';

$article_id = $_GET['article_id'];
$user_id = $_GET['user_id'];
$newuserid = "X" . $user_id . "X,";
$type_article = $_GET['type_article'];
$query = mysqli_query($conn, "SELECT * FROM articles WHERE id='" . $article_id . "'");

$myObj = new stdClass(); 

if (mysqli_num_rows($query) > 0) {
    $ids = mysqli_fetch_assoc($query);

    if ($type_article == "yes") {
        $article_was_helpful_yes = $ids["article_was_helpful_yes"];
        $article_was_helpful_no = $ids["article_was_helpful_no"];
        $check_id = $ids["user_voted_yes_ids"];
        
        if (($article_was_helpful_yes == 1) && ($article_was_helpful_no == 0)) {
            $newphrase = str_replace($newuserid, "", "0");
        } else if (($article_was_helpful_yes == 0) && ($article_was_helpful_no == 1)) {
            $newphrase = str_replace($newuserid, "", "0");
        } else {
            $newphrase = str_replace($newuserid, "", $check_id);
        }

        $query2 = mysqli_query($conn, "UPDATE articles SET user_voted_yes_ids='" . $newphrase . "' WHERE id='" . $article_id . "'");
        $myObj->response = 1;
        $myJSON = json_encode($myObj);
        echo $myJSON;

    } else if ($type_article == "no") {
        $article_was_helpful_yes = $ids["article_was_helpful_yes"];
        $article_was_helpful_no = $ids["article_was_helpful_no"];
        $check_id = $ids["user_voted_no_ids"];
        
        if (($article_was_helpful_yes == 1) && ($article_was_helpful_no == 0)) {
            $newphrase = str_replace($newuserid, "", "0");
        } else if (($article_was_helpful_yes == 0) && ($article_was_helpful_no == 1)) {
            $newphrase = str_replace($newuserid, "", "0");
        } else {
            $newphrase = str_replace($newuserid, "", $check_id);
        }

        $query2 = mysqli_query($conn, "UPDATE articles SET user_voted_no_ids='" . $newphrase . "' WHERE id='" . $article_id . "'");
        $myObj->response = 1;
        $myJSON = json_encode($myObj);
        echo $myJSON;
    }
} else {
    $myObj->response = 0;
    $myJSON = json_encode($myObj);
    echo $myJSON;
}
?>