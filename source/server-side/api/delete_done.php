<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$user_id = $_GET['user_id'];
$promo_id = $_GET['promo_id']; 
$promo_name = $_GET['promo_name']; 


$sql1 = "DELETE FROM wykonane WHERE user_id='".$user_id."' AND promo_id='".$promo_id."'";



if(mysqli_query($conn, $sql1)){
$done =  "Użytkownik o ID: ".$user_id." usunął promocję ".$promo_name." z listy wykonanych. ID promocji:".$promo_id;

$sql2 = mysqli_query($conn, "INSERT INTO logi (user_id, what_did) VALUES ('".$user_id."', '".$done."')");


$myObj->deleted_status = 1; 

$myJSON = json_encode($myObj);

echo $myJSON;

}else{

$myObj->deleted_status = 0;
$myJSON = json_encode($myObj);

echo $myJSON;
}


?>

