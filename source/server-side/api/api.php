<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
 
require_once 'Connection.php';

 $response = array();
 if(isset($_GET['action'])) {
    switch($_GET['action']){
        case 'signup':
            
     
            $name = $_POST['name']; 
            $lastname = $_POST['lastname']; 
            $username = $_POST['username']; 
            $email = $_POST['email']; 
            $password = md5($_POST['password']);
            $phone_number = $_POST['phone_number'];
             $country = $_POST['country'];           
             $phone_number_verification_id = $_POST['phone_number_mVerificationId'];
             $refferal_code = $_POST['refferal_code'];
             $stmt = $conn->prepare("SELECT id FROM users WHERE username = ? OR email = ?");
            $stmt->bind_param("ss", $username, $email);
            $stmt->execute();
            $stmt->store_result();

            if($stmt->num_rows == 0) {
                 //if user is new creating an insert
$permitted_chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ';
// Output: video-g6swmAP8X5VG4jCi.mp4
$user_code = substr(str_shuffle($permitted_chars), 0, 5);



if ($refferal_code == '') {
$refferal_code="ZARABIAJONLINE2020";

$stmttt = "INSERT INTO users (name, username, email, password, phone_number, phone_number_verification_id, country, refferal_name, refferal_code, user_code) VALUES ('".$name."','".$username."','".$email."','".$password."','".$phone_number."', '".$phone_number_verification_id."','".$country."','Cashprom','".$refferal_code."','".$user_code."')";

$stmt3 = mysqli_query($conn,"SELECT id FROM users WHERE user_code='".$refferal_code."'");
$row = $stmt3->fetch_assoc();
$refferal_user_id=$row["id"];
  $stmt6 = mysqli_query($conn,"UPDATE users SET refferal_count_waiting=refferal_count_waiting+1 WHERE id=1");
$stmt5 =  mysqli_query($conn, "UPDATE users SET refferal_count=refferal_count+1 WHERE id=1");


}
else{
    $stmt33 = mysqli_query($conn,"SELECT * FROM users WHERE user_code='".$refferal_code."'");
$row2 = $stmt33->fetch_assoc();
$refferal_user_id=$row2["id"];
$refferal_username=$row2["username"];
$stmttt = "INSERT INTO users (name, username, email, password, phone_number, phone_number_verification_id, country, refferal_name, refferal_code, user_code) VALUES ('".$name."','".$username."','".$email."','".$password."','".$phone_number."', '".$phone_number_verification_id."', '".$country."', '".$refferal_username."','".$refferal_code."','".$user_code."')";

$stmt61 = mysqli_query($conn,"UPDATE users SET refferal_count_waiting=refferal_count_waiting+1 WHERE id='".$refferal_user_id."'");
$stmt51 = mysqli_query($conn,"UPDATE users SET refferal_count=refferal_count+1 WHERE id='".$refferal_user_id."'");

}
                //if the user is successfully added to the database 
                if ($conn->query($stmttt) === TRUE) {
                      $stmt22 = $conn->prepare("SELECT id, name, lastname, username, email, favourite_count, done_count, points, earned, power_level, premium, is_blocked, is_online, unread_messages, phone_number,country, token_notifications, last_scene, image_url FROM users WHERE username = ?");
                    $stmt22->bind_param("s",$username);
                    $stmt22->execute();
                    $stmt22->bind_result($id, $name, $lastname, $username, $email, $favourite_count, $done_count, $points, $earned, $power_level, $premium, $is_ban, $is_online, $unread_messages, $phone, $country, $fcm_token, $last_scene, $image_url);
                    $stmt22->fetch();



                    $user = array(
                        'id'=>$id, 
                        'name'=>$name,
                        'lastname'=>$lastname,
                        'username'=>$username, 
                        'email'=>$email,
                        'favourite_count'=>$favourite_count,
                        'done_count'=>$done_count,
                        'points'=>$points,
                        'earned'=>$earned,
                        'power_level'=>$power_level,
                        'premium'=>$premium,
                        'is_ban' => $is_ban,
                        'is_online' => $is_online,
                        'unread_messages' => $unread_messages,
                        'phone' => $phone,
                        'country' => $country,
                        'fcm_token' => $fcm_token,
                        'last_scene' => $last_scene,
                        'image_url' => $image_url,
                        'last_msg' => 'null',
                        'last_msg_date' => 'null',
                        'last_msg_sent_by_me' => 'null'               
                    );
                    //adding the user data in response 
                    $response['error'] = false; 
                    $response['message'] = 'Zarejestrowano pomyślnie.'; 
                    $response['user'] = $user; 
                } else {
                    $response['error'] = true; 
                    $response['message'] = 'Wystąpił błąd podczas rejestracji. Spróbuj ponownie.'; 
                     $response['user'] = $user; 
                }
            } else {
                $response['error'] = true;
                $response['message'] = 'Użytkownik o takiej nazwie, lub emailu już istnieje. Wybierz inną/inny.';
            }
            $stmt->close();
        break; 
        case 'login':
            //getting values 
$username = $_POST['username'];
            $password = md5($_POST['password']); 
            
try {
    $stmt = $conn->prepare("SELECT id, name, lastname, username, email, favourite_count, done_count, points, earned, power_level, premium, is_blocked, is_online, unread_messages, phone_number, country, token_notifications, last_scene, image_url FROM users WHERE username = ? AND password = ?");
} catch (PDOException $e) {
    die("Error preparing query: " . $e->getMessage());
}

            //creating the check query 
            $stmt->bind_param("ss",$username, $password);
            $stmt->execute();
            $stmt->store_result();
            
            //if the user exist with given credentials 
            if($stmt->num_rows > 0) {
                $stmt->bind_result($id, $name, $lastname, $username, $email, $favourite_count, $done_count, $points, $earned, $power_level, $premium, $is_blocked, $is_online, $unread_messages, $phone, $country, $fcm_token, $last_scene, $image_url);
                $stmt->fetch();
                $user = array(
                'id'=>$id, 
                'name'=>$name,
                  'lastname'=>$lastname,
                'username'=>$username, 
                'email'=>$email,
                'favourite_count'=>$favourite_count,
                'done_count'=>$done_count,
                'points'=>$points,
                'earned'=>$earned,
                'power_level'=>$power_level,
                'premium'=>$premium,
                'is_ban' => $is_blocked,
                'is_online' => $is_online,
                'unread_messages' => $unread_messages,
                'phone' => $phone,
                'country'=>$country,
                'fcm_token' => $fcm_token,
                'last_scene' => $last_scene,
                'image_url' => $image_url,
                'last_msg' => 'null',
                'last_msg_date' => 'null',
                'last_msg_sent_by_me' => 'null'    
                );
                $response['error'] = false; 
                $response['message'] = 'Zalogowano pomyślnie'; 
                $response['user'] = $user; 


            
            $login_date = date('Y-m-d H:i:s');
            $what_did = "Użytkownik o ID:".$id." pomyślnie zalogował się do aplikacji.";
            $sql_login1 =  mysqli_query($conn, "UPDATE users SET last_login='".$login_date."' WHERE id='".$id."'");

            $sql_login2 =  mysqli_query($conn, "INSERT INTO logi (user_id, what_did) VALUES ('".$id."', '".$what_did."')");
            }else{
                //if the user not found 
                $response['error'] = true; 
                $response['message'] = 'Nieprawidłowa nazwa użytkownika, lub hasło.';
            }
       
        break;
        default;
            $response['error'] = true; 
            $response['message'] = 'Nieprawidłowa akcja.';
        break;
    }
 } else {
    $response['error'] = true; 
    $response['message'] = 'Nieprawidłowe żądanie.';
 }
 function isValid($params){
    foreach($params as $param) {
        //if the paramter is not available or empty
        if(isset($_POST[$param])) {
            if(empty($_POST[$param])){
                return false;
            }
        } else {
            return false;
        }
    }
    //return true if every param is available and not empty 
    return true; 
}
echo json_encode($response);
?>