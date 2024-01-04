<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
   require_once 'Connection.php';

  $name_of_file_here = $_GET['username'];
 $fileName = $_FILES["uploaded_file"]["name"];
 $ext = "." . end(explode(".", $fileName));
   $target_path1 = "../images/users/";

$url = "https://yoururl.com/api/images/users/".$name_of_file_here.".png";
 $target_path1 = $target_path1 . $name_of_file_here .".png";
 if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $target_path1)) {
                echo "success";
                $sql = mysqli_query($conn, "UPDATE users SET image_url='".$url."' WHERE username = '".$name_of_file_here."'");	

    } else{
        echo "fail";
    }
     
 
 ?>