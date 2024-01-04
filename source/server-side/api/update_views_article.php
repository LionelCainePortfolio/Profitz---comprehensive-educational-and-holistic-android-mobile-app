<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';
$article_id = $_GET['article_id'];
$user_id = $_GET['user_id'];
$newuserid = "X".$user_id."X";
$query = mysqli_query($conn, "SELECT * FROM articles WHERE id='".$article_id."'");

if(mysqli_num_rows($query) > 0){
$ids = mysqli_fetch_assoc($query);
$check_id_yes = $ids["user_voted_yes_ids"];
$check_id_no = $ids["user_voted_no_ids"];
$myObj = new stdClass(); 

	if(strpos($check_id_yes, $newuserid) !== false){
		$myObj->response = 1;
		$myJSON = json_encode($myObj);
		echo $myJSON;

	}
	else if(strpos($check_id_no, $newuserid) !== false)
	{
		$myObj->response = 2;
		$myJSON = json_encode($myObj);
		echo $myJSON;

	}
	else{
		$myObj->response = 0;
		$myJSON = json_encode($myObj);
		echo $myJSON;
	}
		$query2=mysqli_query($conn, "UPDATE articles SET article_clicks=article_clicks+1 WHERE id='".$article_id."'");

}
 ?>