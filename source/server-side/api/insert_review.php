
<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

$add_points = isset($_GET['add_points']) ? $_GET['add_points'] : null;
$user_id = isset($_GET['user_id']) ? $_GET['user_id'] : null;
$promo_id = isset($_GET['promo_id']) ? $_GET['promo_id'] : null;
$promo_name = isset($_GET['promo_name']) ? $_GET['promo_name'] : null;
$promo_earn_points = isset($_GET['promo_earn_points']) ? $_GET['promo_earn_points'] : null;
$promo_stars = isset($_GET['promo_stars']) ? $_GET['promo_stars'] : null;
$description = isset($_GET['description']) ? $_GET['description'] : null;

if ($user_id !== null && $promo_id !== null && $promo_name !== null && $promo_earn_points !== null && $promo_stars !== null && $description !== null) {

$stmt1 = mysqli_query($conn, "SELECT * FROM users WHERE id='" . $user_id . "'");
$row = $stmt1->fetch_assoc();
$user_nickname = $row['username'];
$user_points = $row['points'];
$url_img = "https://yoururl.com/api/images/users/" . $user_nickname . ".png";

$notification_img_url = "https://yoururl.com/api/images/notifications/promo_done_" . $promo_name . ".png";

$update_points = $user_points + $promo_earn_points;

$sql1 = mysqli_query($conn, "INSERT INTO opinie (promo_id, user_id, name, description, img, stars, status) VALUES ('" . $promo_id . "', '" . $user_id . "','" . $user_nickname . "', '" . $description . "', '" . $url_img . "', '" . $promo_stars . "','w trakcie weryfikacji')");

$done = "Użytkownik o ID:" . $user_id . " dodał opinię do promocji o nazwie: " . $promo_name . ". ID promocji:" . $promo_id . " Odebrał jednocześnie bonus: " . $promo_earn_points . " punktów";

//$notification_title = "Ukończono promocję: ".$promo_name;
//$notification_description = "Gratulacje. Udało Ci się ukończyć promocję, odwiedź zakładkę 'Ukończone promocje' i odbierz bonusowe punty."
$notification_title = "Nagroda: bonusowe punkty";
$notification_description = "Otrzymujesz " . $promo_earn_points . " bonusowych punktów za ukończenie promocji " . $promo_name . "!";

$sql2 = mysqli_query($conn, "INSERT INTO logi (user_id, what_did) VALUES ('" . $user_id . "', '" . $done . "')");
if ($add_points == "true")
{
    $sql3 = mysqli_query($conn, "UPDATE users SET points='" . $update_points . "' WHERE id='" . $user_id . "'");
    $sql4 = mysqli_query($conn, "UPDATE wykonane SET received_points='yes' WHERE user_id='" . $user_id . "' AND promo_id='" . $promo_id . "'");
    $sql5 = mysqli_query($conn, "INSERT INTO powiadomienia (user_id, title, description, type, earn, earn_type, img) VALUES ('" . $user_id . "', '" . $notification_title . "', '" . $notification_description . "','points','" . $promo_earn_points . "','PKT', '" . $notification_img_url . "')");

}

}
else{
	echo "Data misstype";
} 
?>