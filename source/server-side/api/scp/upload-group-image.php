<?php
ini_set('display_errors', '0');
ini_set('display_startup_errors', '0');
$group_id = $_POST["group_id"];
move_uploaded_file($_FILES["picture"]["tmp_name"], "groups/" . $group_id . ".png");
?>