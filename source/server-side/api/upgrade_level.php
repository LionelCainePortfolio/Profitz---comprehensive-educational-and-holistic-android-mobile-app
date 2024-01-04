<?php
require_once 'Connection.php';

$user_id = $_GET['user_id']; 
$code = $_GET['safety_code'];
$level = $_GET['level'];
$points = $_GET['points'];

//$user_lvl = $_POST['user_lvl'];
$myObj = new stdClass(); 

if ($code == "A4S3D234S3Aar3K4aC3a3sm3fk2aDNA44SDFA1a35sd5aKASD4033323413KA56DLARO2r55JDAOS24DLCNZ") {

    $stmt1 = mysqli_query($conn, "SELECT * FROM users WHERE id='" . $user_id . "'");
    $row = $stmt1->fetch_assoc();
    $points_user = $row["points"];
    $points_new = $points_user - $points;

    $sql1 = "UPDATE users SET user_level='" . $level . "' WHERE id='" . $user_id . "'";
    $sql2 = "UPDATE users SET points='" . $points_new . "' WHERE id='" . $user_id . "'";

    if ((mysqli_query($conn, $sql1)) && (mysqli_query($conn, $sql2))) {

        $done1 = "Użytkownik o ID:" . $user_id . " ulepszył swoje konto do poziomu " . $level . " i zapłacił " . $points . " punktów.";
        $sql3 = mysqli_query($conn, "INSERT INTO logi (user_id, what_did) VALUES ('" . $user_id . "', '" . $done1 . "')");

        $done2 = "Zaktualizowano level użytkownika o ID:" . $user_id . ". Obecnie: " . $level . " poziom.";
        $sql4 = mysqli_query($conn, "INSERT INTO logi (user_id, what_did) VALUES ('" . $user_id . "', '" . $done2 . "')");

        $done3 = "Pobrano od użytkownika ID: " . $user_id . " " . $points . " puntków za ulepszenie do poziomu " . $level;
        $sql3 = mysqli_query($conn, "INSERT INTO logi (user_id, what_did) VALUES ('" . $user_id . "', '" . $done3 . "')");

        $myObj->status = 1; // true
        $myJSON = json_encode($myObj);

        echo $myJSON;

    } else {

        $myObj->status = 0; // false
        $myJSON = json_encode($myObj);

        echo $myJSON;
    }
}
?>