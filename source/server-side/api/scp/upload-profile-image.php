<?php
ini_set('display_errors', '0');
ini_set('display_startup_errors', '0');
$user_id = $_POST["user_id"];
move_uploaded_file($_FILES["picture"]["tmp_name"], "profile/" . $user_id . ".png");
?>