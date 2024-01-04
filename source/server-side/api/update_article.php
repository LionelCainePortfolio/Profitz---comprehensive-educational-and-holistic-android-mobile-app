<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';
$article_id = $_GET['article_id'];
//$user_id = $_GET['user_id'];
$type = $_GET['type'];
$type_article = $_GET['type_article'];
$query = mysqli_query($conn, "SELECT * FROM articles WHERE id='".$article_id."'");

if(mysqli_num_rows($query) > 0){
$myObj = new stdClass(); // Inicjalizacja obiektu


$ids = mysqli_fetch_assoc($query);


if ($type_article=="yes"){
if ($type == "down"){
		$query2=mysqli_query($conn, "UPDATE articles SET article_was_helpful_yes=article_was_helpful_yes-1 WHERE id='".$article_id."'");
		$myObj->response = 1;
		$myJSON = json_encode($myObj);
		echo $myJSON;
	}

	else if ($type == "up"){
				$query2=mysqli_query($conn, "UPDATE articles SET article_was_helpful_yes=article_was_helpful_yes+1 WHERE id='".$article_id."'");
		$myObj->response = 1;
		$myJSON = json_encode($myObj);
		echo $myJSON;
		
	}
}
else if($type_article=="no"){
if ($type == "down"){
		$query2=mysqli_query($conn, "UPDATE articles SET article_was_helpful_no=article_was_helpful_no-1 WHERE id='".$article_id."'");
		$myObj->response = 1;
		$myJSON = json_encode($myObj);
		echo $myJSON;
	}

	else if ($type == "up"){
				$query2=mysqli_query($conn, "UPDATE articles SET article_was_helpful_no=article_was_helpful_no+1 WHERE id='".$article_id."'");
		$myObj->response = 1;
		$myJSON = json_encode($myObj);
		echo $myJSON;
		
	}
}
	

}

 ?>