<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
 require_once 'Connection.php';

$myObj = new stdClass(); // Create an instance of stdClass


$query_check = mysqli_query($conn, "SELECT * FROM prices WHERE title = 'premium_month'");

$row = mysqli_fetch_assoc($query_check);
$premium_month_pln_price = $row['price_pln'];
$premium_month_monthly_pln_price = $row['price_pln_monthly'];

$myObj->premium_month_pln_price = $premium_month_pln_price; 
$myObj->premium_month_monthly_pln_price = $premium_month_monthly_pln_price; 



$query_check2 = mysqli_query($conn, "SELECT * FROM prices WHERE title = 'premium_year'");

$row2 = mysqli_fetch_assoc($query_check2);

$premium_year_pln_price = $row2['price_pln'];
$premium_year_monthly_pln_price = $row2['price_pln_monthly'];

$myObj->premium_year_pln_price = $premium_year_pln_price; 
$myObj->premium_year_monthly_pln_price = $premium_year_monthly_pln_price; 


$query_check3 = mysqli_query($conn, "SELECT * FROM prices WHERE title = 'premium_lifetime'");

$row3 = mysqli_fetch_assoc($query_check3);

$premium_lifetime_pln_price = $row3['price_pln'];

$myObj->premium_lifetime_pln_price = $premium_lifetime_pln_price; 


$myJSON = json_encode($myObj);

echo $myJSON;


 ?>

