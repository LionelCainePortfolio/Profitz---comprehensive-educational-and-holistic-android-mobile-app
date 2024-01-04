<?php
ini_set('display_errors', '0');
ini_set('display_startup_errors', '0');
header("Content-type: application/json");
 require_once 'Connection.php';

$user_id = $_GET['user_id']; 


$query_check = mysqli_query($conn, "SELECT * FROM wykonane WHERE user_id='".$user_id."'");

$response = array();

while ($row = mysqli_fetch_assoc($query_check)){
	array_push($response,
	array('promo_img'=>$row['promo_img'],
		'promo_name'=>$row['promo_name'],
		'promo_earn'=>$row['promo_earn'],
		'promo_bonus'=>$row['promo_refback'],
			'promo_id'=>$row['promo_id'],
			'promo_date'=>$row['done_date'],
			'promo_status'=>$row['status'],
			'promo_received_points'=>$row['received_points'],
			'promo_status_additional_info'=>$row['status_additional_info'],
			)
	);
}
echo json_encode($response);


 ?>

