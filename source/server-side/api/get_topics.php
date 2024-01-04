<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
header("Content-type: application/json");
 require_once 'Connection.php';

$user_id = $_GET['user_id']; 


$query_check = mysqli_query($conn, "SELECT * FROM articles_topics WHERE topic_visibility=1 ORDER by topic_show_as ASC");

$response = array();

while ($row = mysqli_fetch_assoc($query_check)){
	array_push($response,
	array('topic_img'=>$row['topic_img'],
		'topic_name'=>$row['topic_name'],
		'topic_desc'=>$row['topic_desc'],
		'topic_categories_id'=>$row['topic_categories_id'],
			'topic_show_as'=>$row['topic_show_as'],
			'topic_clicable'=>$row['topic_clicable'],
			'topic_visibility'=>$row['topic_visibility'],
			'topic_clicks'=>$row['topic_clicks']
			)
	);
}
echo json_encode($response);


 ?>

