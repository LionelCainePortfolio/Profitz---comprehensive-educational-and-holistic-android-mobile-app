<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';
$article_id = $_GET['article_id'];

$subArray = array();
$sql_results = mysqli_query($conn, "SELECT * FROM articles WHERE id='" . $article_id . "'");

if ($row = mysqli_fetch_array($sql_results)) {
    $subArray['article_name'] = $row['article_name'];
    $subArray['articleDesc'] = $row['article_description'];
    $subArray['article_was_helpful_yes'] = $row['article_was_helpful_yes'];
    $subArray['article_was_helpful_no'] = $row['article_was_helpful_no'];
} else {
    $subArray['article_name'] = "brak";
    $subArray['articleDesc'] = "0";
    $subArray['article_was_helpful_yes'] = "0";
    $subArray['article_was_helpful_no'] = "0";
}

echo json_encode($subArray);
?>
