<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
header("Content-type: application/json");
require_once 'Connection.php';

$article_category = $_GET['category'];

$query_check = mysqli_query($conn, "SELECT * FROM articles WHERE article_visibility=1 AND article_category='" . $article_category . "' ORDER by article_show_as ASC");

$response = array();

while ($row = mysqli_fetch_assoc($query_check))
{
    array_push($response, array(
        'article_id' => $row['id'],
        'article_name' => $row['article_name'],
        'article_category' => $row['article_category'],
        'article_description' => $row['article_description'],
        'article_description' => $row['article_description'],
        'article_category_id' => $row['article_category_id'],
        'article_show_as' => $row['article_show_as'],
        'article_clicable' => $row['article_clicable'],
        'article_visibility' => $row['article_visibility'],
        'article_clicks' => $row['article_clicks'],
        'article_was_helpful_yes' => $row['article_was_helpful_yes'],
        'article_was_helpful_no' => $row['article_was_helpful_no']
    ));
}
echo json_encode($response);

?>
