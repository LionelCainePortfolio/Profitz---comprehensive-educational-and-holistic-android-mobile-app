<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
header("Content-type: application/json");
 require_once 'Connection.php';

$user_id = $_GET['user_id']; 
$notification_type = $_GET['notification_type']; 
$limit = $_GET['limit'];
$type_points = "PKT";

if ($limit == "yes")
{

if ($notification_type == "other"){
$query_check = mysqli_query($conn, "SELECT * FROM powiadomienia WHERE user_id='".$user_id."' ORDER BY date_add DESC LIMIT 5");
}
else if ($notification_type == "points"){
	$query_check = mysqli_query($conn, "SELECT * FROM powiadomienia WHERE earn_type='".$type_points."' AND user_id='".$user_id."' ORDER BY date_add DESC LIMIT 5");
}
else{
	$query_check = mysqli_query($conn, "SELECT * FROM powiadomienia WHERE type='".$notification_type."' AND user_id='".$user_id."' ORDER BY date_add DESC LIMIT 5");
}
}
else{
	if ($notification_type == "other"){
$query_check = mysqli_query($conn, "SELECT * FROM powiadomienia WHERE user_id='".$user_id."' ORDER BY date_add");
}
else{
	$query_check = mysqli_query($conn, "SELECT * FROM powiadomienia WHERE user_id='".$user_id."' AND type='".$notification_type."' ORDER BY date_add");
}
}

$response = array();

while ($row = mysqli_fetch_assoc($query_check)){
	array_push($response,
	array('notification_id'=>$row['id'],
		'notification_title'=>$row['title'],
			'notification_description'=>$row['description'],
			'notification_earn'=>$row['earn'],
	'notification_earn_type'=>$row['earn_type'],
			'notification_img'=>$row['img'],
	'notification_date_add'=>$row['date_add'])

	);
}
echo json_encode($response);


 ?>

