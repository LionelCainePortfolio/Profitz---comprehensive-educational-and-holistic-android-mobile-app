<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
header('Content-Type: application/json');
require_once "db.php";

// sleep(1);

function secure_input($data)
{
    $data = trim($data);
    $data = stripslashes($data);
    $data = htmlspecialchars($data);
    return $data;
}

if (isset($_REQUEST["update_image_ios"]))
{
    $image_url = $_REQUEST["image_url"];
    $id = $_REQUEST["id"];
    $file_path = "profile/" . $id . ".jpg";

    if (empty($image_url))
    {
        $file_path = "";
    }
    else if ($image_url == "-1")
    { 
        unlink($file_path);
        $file_path = "";
    }
    else
    {
        $data = str_replace('data:image/png;base64,', '', $image_url);
        $data = str_replace(' ', '+', $data);
        $data = base64_decode($data);
        $success = file_put_contents($file_path, $data);
    }

    echo json_encode(array(
        "status" => "success",
        "message" => "Zdjęcie zostało zaaktualizowane."
    ));
    exit();
}

if (isset($_REQUEST["update_profile"]))
{
    $id = $_REQUEST["id"];
    $name = $_REQUEST["name"];
    $file_path = "profile/" . $id . ".png";

    $mime_type = "";
    $image_url = "";

    if (isset($_REQUEST["image_url"]))
    {
        $image_url = $_REQUEST["image_url"];
        if (empty($image_url))
        {
            $file_path = "";
        }
        else if ($image_url == $file_path)
        {
            //
        }
        else
        {
            if ($image_url == "-1")
            {
                unlink($file_path);
                $file_path = "";
            }
            else
            {
                $decodedImage = base64_decode($image_url);
                file_put_contents($file_path, $decodedImage);
            }
        }
    }

    $sql = "UPDATE `users` SET `name`='$name',`image_url`='$file_path' WHERE id='$id'";
    mysqli_query($conn, $sql);

    echo json_encode(array(
        "status" => "success",
        "message" => "Profil został zaaktualizowany",
        "file_path" => $file_path,
        "image_url" => $image_url
    ));
    exit();
}

if (isset($_REQUEST["update_password"]))
{
    $user_id = $_REQUEST["user_id"];
    $old_password = $_REQUEST["old_password"];

    $sql = "SELECT * FROM `users` WHERE `id` = '$user_id'";
    $result = mysqli_query($conn, $sql);
    $user_data = mysqli_fetch_object($result);

    if (password_verify($old_password, $user_data->password))
    {
        $new_password = password_hash($_REQUEST["new_password"], PASSWORD_DEFAULT);
        $sql = "UPDATE `users` SET `password`='$new_password' WHERE id='$user_id'";
        mysqli_query($conn, $sql);

        echo json_encode(array(
            "status" => "success",
            "message" => "Hasło zostało zmienione"
        ));
    }
    else
    {
        echo json_encode(array(
            "status" => "error",
            "message" => "Aktualne hasło jest niepoprawne."
        ));
    }
    exit();
}

if (isset($_REQUEST["registration"]))
{
    $username = $_REQUEST["username"];
    $name = $_REQUEST["name"];

    $sql = "SELECT * FROM `users` WHERE `username` = '$username'";
    $result = mysqli_query($conn, $sql);

    if (mysqli_num_rows($result) > 0)
    {
        echo json_encode(array(
            "status" => "error",
            "message" => "Nazwa użytkownika jest już zajęta."
        ));
        exit();
    }

    $password = password_hash($_REQUEST["password"], PASSWORD_DEFAULT);
    $fcm_token = "";
    $image_url = "";
    $is_online = "true";
    $is_email_verified = 0;
    $reset_token = "";

    $sql = "INSERT INTO `users`(`name`, `username`, `password`, `fcm_token`, `image_url`, `is_online`) VALUES ('$name', '$username', '$password', '$fcm_token', '$image_url', $is_online)";
    mysqli_query($conn, $sql);

    echo json_encode(array(
        "status" => "success",
        "message" => "Konto zostało utworzone.",
        "user_id" => mysqli_insert_id($conn) . ""
    ));
    exit();
}

if (isset($_REQUEST["login"]))
{
    $username = $_REQUEST["username"];
    $password = $_REQUEST["password"];

    $sql = "SELECT * FROM `users` WHERE `username` = '$username'";
    $result = mysqli_query($conn, $sql);

    if (mysqli_num_rows($result) > 0)
    {
        $row = mysqli_fetch_object($result);
        /*if ($row->is_email_verified == 0)
        {
            $response["status"] = "error";
            $response["message"] = "Hi " . $row->name . ", please confirm your email address";
        }
        else*/
        {
            if (password_verify($password, $row->password))
            {
                $sql = "UPDATE `users` SET `is_online`='true', `last_scene`=NOW() WHERE id='" . $row->id . "'";
                mysqli_query($conn, $sql);
                $row->is_online = "true";

                echo json_encode(array(
                    "status" => "success",
                    "message" => "Successfully logged in",
                    "user" => $row,
                    "user_id" => $row->id,
                    "username"=> $row->username
                ));
            }
            else
            {
                echo json_encode(array(
                    "status" => "error",
                    "message" => "Password does not match"
                ));
            }
        }
    }
    else
    {
        echo json_encode(array(
            "status" => "error",
            "message" => "Nie znaleziono użytkownika."
        ));
    }
    exit();
}

if (isset($_REQUEST["get_user_joined"]))
{
    // TODO: Needs to be optimized
    $user_id = $_REQUEST["user_id"];

    $sql = "SELECT * FROM `groups` ORDER BY id DESC";
    $result = mysqli_query($conn, $sql);

    $data = array();
    while ($row = mysqli_fetch_object($result))
    {
        $sql = "SELECT * FROM `group_members` WHERE user_id='$user_id' AND group_id='" . $row->id . "'";
        $result1 = mysqli_query($conn, $sql);
        if (mysqli_num_rows($result1) > 0 || $row->created_by == $user_id)
        {
            array_push($data, $row);
        }
    }
    echo json_encode($data);
    exit();
}

if (isset($_REQUEST["create_group"]))
{
    $group_name = $_REQUEST["groupName"];
    $created_by = $_REQUEST["createdBy"];
    $image_url = isset($_REQUEST["image_url"]) ? $_REQUEST["image_url"] : "";

    $sql = "SELECT * FROM `groups` WHERE `group_name`='$group_name'";
    $result = mysqli_query($conn, $sql);

    if (mysqli_num_rows($result) > 0)
    {
        echo json_encode(array(
            "status" => "error",
            "message" => "Nazwa grupy jest już zajęta."
        ));
    }
    else
    {
        $sql = "INSERT INTO `groups` (`group_name`, `image_url`, `created_by`) VALUES ('$group_name', 'groups/default_group.png', '$created_by')";
        mysqli_query($conn, $sql);



        $id = mysqli_insert_id($conn);
        $file_path = "groups/" . $id . ".png";

        $sql2 = "INSERT INTO `group_members` (`group_id`, `user_id`, `is_ban`, `added_by`) VALUES ('$id', '$created_by', '0' , '$created_by')";
        mysqli_query($conn, $sql2);

        if (empty($image_url))
        {
            $file_path = "groups/default_group.png";
        }
        else if ($image_url == "-1")
        {
            unlink($file_path);
            $file_path = "";
        }
        else
        {
            if (!empty($image_url))
            {
                $data = str_replace('data:image/png;base64,', '', $image_url);
                $data = str_replace(' ', '+', $data);
                $data = base64_decode($data);
                file_put_contents($file_path, $data);
            }
        }

        $sql = "UPDATE `groups` SET `image_url` = '$file_path' WHERE id = '$id'";
        mysqli_query($conn, $sql);

        echo json_encode(array(
            "status" => "success",
            "message" => "Grupa została pomyślnie utworzona.",
            "group_id" => mysqli_insert_id($conn)
        ));
    }
    exit();
}

if (isset($_REQUEST["get_detail"])) {
    $group_id = $_REQUEST["group_id"];
    $data = new stdClass();

    /* Getting group details */
    $sql = "SELECT * FROM `groups` WHERE `id`=?";
    $stmt_details = mysqli_prepare($conn, $sql);
    
    if ($stmt_details) {
        mysqli_stmt_bind_param($stmt_details, "i", $group_id);
        mysqli_stmt_execute($stmt_details);
        
        $result_details = mysqli_stmt_get_result($stmt_details);
        $is_group_exists = mysqli_num_rows($result_details) > 0;
        
        if ($is_group_exists) {
            $details = mysqli_fetch_object($result_details);
            mysqli_stmt_close($stmt_details); // Close the first statement here

            /* Getting admin details */
            $sql = "SELECT * FROM `groups` INNER JOIN `users` ON `users`.`id`=`groups`.`created_by` WHERE `groups`.`id` = ?";
            $stmt_admin = mysqli_prepare($conn, $sql);
            
            if ($stmt_admin) {
                mysqli_stmt_bind_param($stmt_admin, "i", $group_id);
                mysqli_stmt_execute($stmt_admin);

                $result_admin = mysqli_stmt_get_result($stmt_admin);
                $row = mysqli_fetch_object($result_admin);
                $row->is_online = $row->is_online;
                $admin = $row;
                mysqli_stmt_close($stmt_admin); // Close the second statement here

                /* Getting members for FCM */
                $sql = "SELECT * FROM `group_members` INNER JOIN `users` ON `users`.`id`=`group_members`.`user_id` WHERE group_id=? ORDER BY `group_members`.`id` DESC";
                $stmt_members = mysqli_prepare($conn, $sql);
                
                if ($stmt_members) {
                    mysqli_stmt_bind_param($stmt_members, "i", $group_id);
                    mysqli_stmt_execute($stmt_members);
                    
                    $result_members = mysqli_stmt_get_result($stmt_members);
                    $members = array();
                    
                    while ($row = mysqli_fetch_object($result_members)) {
                        $row->is_online = $row->is_online;
                        $row->is_ban = $row->is_ban == 1 ? true : false;
                        array_push($members, $row);
                    }
                    mysqli_stmt_close($stmt_members); // Close the third statement here

                    /* Getting group chat */
                    $sql = "SELECT *, `users`.`id` AS user_id, `messages`.`id` AS message_id FROM `messages` INNER JOIN `users` ON `users`.`id`=`messages`.`send_from` WHERE message_type='group' AND send_to=? ORDER BY `messages`.`id` ASC";
                    $stmt_messages = mysqli_prepare($conn, $sql);
                    
                    if ($stmt_messages) {
                        mysqli_stmt_bind_param($stmt_messages, "i", $group_id);
                        mysqli_stmt_execute($stmt_messages);
                        
                        $result_messages = mysqli_stmt_get_result($stmt_messages);
                        $messages = array();
                        
                        while ($row = mysqli_fetch_object($result_messages)) {
                            $temp = new stdClass();
                            $temp->send_from = new stdClass();
                            $temp->send_from->id = $row->user_id;
                            $temp->send_from->name = $row->name;
                            $temp->send_from->username = $row->username;
                            $temp->send_from->image_url = $row->image_url;
                            $temp->send_from->is_online = $row->is_online;

                            $temp->id = $row->message_id;
                            $temp->content = $row->content;
                            $temp->system_message = $row->system_message;
                            $temp->attachment = $row->attachment;
                            $temp->is_read = $row->is_read == 1 ? true : false;
                            $temp->created = $row->created;
                            array_push($messages, $temp);
                        }
                        mysqli_stmt_close($stmt_messages); // Close the fourth statement here

                        echo json_encode(array(
                            "status" => "success",
                            "message" => "Pobrano rekordy z bazy danych.",
                            "details" => $details,
                            "admin" => $admin,
                            "members" => $members,
                            "messages" => $messages
                        ));
                    } else {
                        echo json_encode(array(
                            "status" => "error",
                            "message" => "Błąd przy przygotowywaniu zapytania SQL dla wiadomości grupy"
                        ));
                    }
                } else {
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Błąd przy przygotowywaniu zapytania SQL dla członków grupy"
                    ));
                }
            } else {
                echo json_encode(array(
                    "status" => "error",
                    "message" => "Błąd przy przygotowywaniu zapytania SQL dla administratora grupy"
                ));
            }
        } else {
            echo json_encode(array(
                "status" => "error",
                "message" => "Grupa nie istnieje"
            ));
        }
    } else {
        echo json_encode(array(
            "status" => "error",
            "message" => "Błąd przy przygotowywaniu zapytania SQL dla szczegółów grupy"
        ));
    }

    exit();
}



if (isset($_REQUEST["get_meta_detail"]))
{
    $group_id = $_REQUEST["group_id"];

    /* Getting group details */
    $sql = "SELECT * FROM `groups` WHERE `id`='$group_id'";
    $result = mysqli_query($conn, $sql);
    $is_group_exists = mysqli_num_rows($result) > 0;
    
    if ($is_group_exists)
    {
        $details = mysqli_fetch_object($result);
        $members = array();

        /* Getting admin details */
        $sql = "SELECT *, users.id AS user_id FROM `groups` INNER JOIN `users` ON `users`.`id`=`groups`.`created_by` WHERE `groups`.`id`='$group_id'";
        $result = mysqli_query($conn, $sql);
        $row = mysqli_fetch_object($result);
        $row->is_online = $row->is_online;
        $admin = $row;

        /* Getting members */
        $sql = "SELECT * FROM `group_members` INNER JOIN `users` ON `users`.`id`=`group_members`.`user_id` WHERE group_id='$group_id' ORDER BY `group_members`.`id` DESC";
        $result = mysqli_query($conn, $sql);
        while ($row = mysqli_fetch_object($result))
        {
            $row->is_online;
            $row->is_ban;
            array_push($members, $row);
        }

        echo json_encode(array(
            "status" => "success",
            "message" => "Pobrano rekordy z bazy danych.",
            "details" => $details,
            "admin" => $admin,
            "members" => $members
        ));
    }
    else
    {
        echo json_encode(array(
            "status" => "error",
            "message" => "Grupa została usunięta"
        ));
    }

    exit();
}

if (isset($_REQUEST["send_message"]))
{
    $content = mysqli_real_escape_string($conn, $_REQUEST["content"]);
    $attachment = $_REQUEST["attachment"];
    $attachmentExtension = $_REQUEST["attachmentExtension"];
    $send_from = $_REQUEST["send_from"];
    $send_to = $_REQUEST["send_to"];
    $message_type = $_REQUEST["message_type"];

    $sql = "INSERT INTO `messages`(`content`, `send_from`, `send_to`, `message_type`, attachment) VALUES ('$content', '$send_from', '$send_to', '$message_type', '')";
    mysqli_query($conn, $sql);
    if (mysqli_errno($conn)) {
        echo json_encode(array(
            "status" => "error",
            "message" => mysqli_error($conn)
        ));
        exit();
    }
    $message_id = mysqli_insert_id($conn);

   $date_to_check = "null";
   $sql_created = "SELECT * FROM `messages` WHERE `id` = $message_id";
    $result_created = mysqli_query($conn, $sql_created);
    $roww = mysqli_fetch_assoc($result_created);
    $date_to_check = $roww['created'];



    $file_path = "attachment/" . $message_id . "." . $attachmentExtension;
    if (empty($attachment))
    {
        $file_path = "";
    }
    else
    {
        $decodedImage = base64_decode($attachment);
        file_put_contents($file_path, $decodedImage);

        $sql = "UPDATE `messages` SET `attachment`='$file_path' WHERE id='$message_id'";
        mysqli_query($conn, $sql);
    }

    if ($message_type == "group")
    {
        $sql = "SELECT * FROM `group_members` WHERE group_id='$send_to' AND user_id != '$send_from'";
        $result = mysqli_query($conn, $sql);
        while ($row = mysqli_fetch_object($result))
        {
            $sql = "INSERT INTO group_chat(group_id, send_to, is_read) VALUES('$send_to', '" . $send_from. "', '0')";
            mysqli_query($conn, $sql);
        }

        $sql = "SELECT * FROM `groups` WHERE id='$send_to'";
        $result = mysqli_query($conn, $sql);
        $group_detail = mysqli_fetch_object($result);

        if ($group_detail->created_by != $send_from)
        {
            $sql = "INSERT INTO group_chat(group_id, send_to, is_read) VALUES('$send_to', '" . $group_detail->created_by . "', '0')";
            mysqli_query($conn, $sql);
        }
    }




    $first_message = "null";
    $last_message = "null";
    $many_messages_in_one_time = "null";

   $date_new = DateTime::createFromFormat('Y-m-d  H:i:s', $date_to_check)->format('Y-m-d H:i');


      $sql_messages_in_one_time = "SELECT * FROM messages WHERE ((send_from='$send_from' AND send_to='" . $send_to . "' AND message_type='$message_type') OR (send_to='$send_from' AND send_from='" . $send_to . "' AND message_type='$message_type')) AND (created LIKE  '".$date_new."%')";
    $result_count_rows = mysqli_query($conn, $sql_messages_in_one_time);
 
    if (mysqli_num_rows($result_count_rows) > 1)
    {
    $many_messages_in_one_time = "true";

     $sql_messages_first_message = "SELECT * FROM messages WHERE ((send_from='$send_from' AND send_to='" . $send_to  . "' AND message_type='$message_type') OR (send_to='$send_from' AND send_from='" . $send_to . "' AND message_type='$message_type')) AND (created LIKE  '".$date_new."%') ORDER BY created ASC LIMIT 1";
      $result_first_message = mysqli_query($conn, $sql_messages_first_message);
    $row_first_message = mysqli_fetch_object($result_first_message);
    $id_first_message = $row_first_message->id;

    if ($id_first_message == $id_actual){
          $first_message = "true";
          $last_message = "false";

    }
    else{
            $first_message = "false";
                $last_message = "true";
    }
    }
    else{
     $many_messages_in_one_time = "false";
     //
     $first_message = "true";
         $last_message = "true";


    }

    

    echo json_encode(array(
        "status" => "success",
        "message" => "Wiadomość została wysłana.",
        "message_id" => $message_id,
        "first_message" => $first_message,
        "last_message" => $last_message,
        "many_messages_in_one_time" => $many_messages_in_one_time,
        "file_path" => $file_path
    ));
    exit();
}

if (isset($_REQUEST["get_user_for_adding_member"]))
{
    $group_id = $_REQUEST["group_id"];
    $username = $_REQUEST["username"];

    // Check if user exists
    $sql = "SELECT * FROM `users` WHERE `username`='$username'";
    $result = mysqli_query($conn, $sql);

    if (mysqli_num_rows($result) > 0)
    {
        $response = mysqli_fetch_object($result);
        $response->is_online;

        /* Check if user is admin */
        $sql = "SELECT * FROM `groups` WHERE id='$group_id'";
        $result = mysqli_query($conn, $sql);
        $row = mysqli_fetch_object($result);
        if ($row->created_by == $response->id)
        {
            echo json_encode(array(
                "status" => "error",
                "message" => "Użytkownik jest administratorem."
            ));
            exit();
        }

        /* Checking if user is already a member */
        $sql = "SELECT * FROM `group_members` WHERE user_id='" . $response->id . "' AND group_id='$group_id'";
        $result = mysqli_query($conn, $sql);
        if (mysqli_num_rows($result) > 0)
        {
            echo json_encode(array(
                "status" => "error",
                "message" => "Użytkownik jest już członkiem."
            ));
            exit();
        }

        echo json_encode(array(
            "status" => "success",
            "message" => "Pobrano rekordy z bazy danych.",
            "user" => $response
        ));
        exit();
    }

    echo json_encode(array(
        "status" => "error",
        "message" => "Użytkownik nie istnieje."
    ));
    exit();
}

if (isset($_REQUEST["add_member"]))
{
    $user_id = $_REQUEST["user_id"];
    $group_id = $_REQUEST["group_id"];
    $added_by = $_REQUEST["added_by"];

    $sql = "SELECT * FROM `group_invitations` WHERE `group_id`='$group_id' AND `user_id`='$user_id'";
    $result = mysqli_query($conn, $sql);
    if (mysqli_num_rows($result) > 0)
    {
        echo json_encode(array(
            "status" => "error",
            "message" => "Zaproszenie zostało wysłane już do tego użytkownika."
        ));
    }
    else
    {
        $sql = "INSERT INTO `group_invitations`(`group_id`, `user_id`) VALUES ('$group_id', '$user_id')";
        mysqli_query($conn, $sql);

        echo json_encode(array(
            "status" => "success",
            "message" => "Zaproszenie zostało wysłane."
        ));
    }
    exit();
}

if (isset($_REQUEST["change_user_state"]))
{
    $user_id = $_REQUEST["user_id"];
    $user_state = $_REQUEST["user_state"];

    $is_online = $user_state == "online" ? 1 : 0;
    $sql = "UPDATE `users` SET `is_online`=$is_online, `last_scene`=NOW() WHERE id='$user_id'";
    mysqli_query($conn, $sql);

    echo json_encode(array(
        "status" => "success",
        "message" => "Status aktywności został zmieniony."
    ));
    exit();
}

if (isset($_REQUEST["delete_group"]))
{
    $group_id = $_REQUEST["group_id"];

    /* Deleting members */
    $sql = "DELETE FROM `group_members` WHERE group_id='$group_id'";
    mysqli_query($conn, $sql);

    /* Remove attachments from folder */
    $sql = "SELECT * FROM `messages` WHERE send_to='$group_id' AND message_type='group'";
    $result = mysqli_query($conn, $sql);
    while ($row = mysqli_fetch_object($result))
    {
        if (file_exists($row->attachment))
            unlink($row->attachment);
    }

    /* Remove group image from folder */
    $sql = "SELECT * FROM `groups` WHERE id='$group_id'";
    $result = mysqli_query($conn, $sql);
    $row = mysqli_fetch_object($result);
    if (file_exists($row->image_url))
        unlink($row->image_url);

    /* Deleting messages */
    $sql = "DELETE FROM `messages` WHERE send_to='$group_id' AND message_type='group'";
    mysqli_query($conn, $sql);

    /* Deleting requests */
    $sql = "DELETE FROM `group_join_requests` WHERE group_id='$group_id'";
    mysqli_query($conn, $sql);

    /* Deleting group */
    $sql = "DELETE FROM `groups` WHERE id='$group_id'";
    mysqli_query($conn, $sql);

    echo json_encode(array(
        "status" => "success",
        "message" => "Grupa została usunięta."
    ));
    exit();
}

if (isset($_REQUEST["get_user_profile"]))
{
    $user_id = $_REQUEST["user_id"];
    $sql = "SELECT * FROM `users` WHERE `id`='$user_id'";
    $result = mysqli_query($conn, $sql);
    $row = mysqli_fetch_object($result);
    $row->is_online;
    echo json_encode($row);
    exit();
}

if (isset($_REQUEST["delete_group_member"]))
{
    $user_id = $_REQUEST["user_id"];
    $group_id = $_REQUEST["group_id"];

    $sql = "DELETE FROM `group_members` WHERE group_id='$group_id' AND `user_id`='$user_id'";
    mysqli_query($conn, $sql);

    echo json_encode(array(
        "status" => "success",
        "message" => "Członek został usunięty z grupy."
    ));
    exit();
}

if (isset($_REQUEST["update_fcm_token"]))
{
    $user_id = $_REQUEST["user_id"];
    $fcm_token = $_REQUEST["fcm_token"];

    $sql = "UPDATE `users` SET `fcm_token`='$fcm_token' WHERE id='$user_id'";
    mysqli_query($conn, $sql);

    echo json_encode(array(
        "status" => "success",
        "message" => "Zaktualizowano token powiadomień."
    ));
    exit();
}
if (isset($_REQUEST["get_all_messages"]))
{
    $requested_by = $_REQUEST["requested_by"];

    $sql = "SELECT * FROM `messages` WHERE ((send_from='" . $requested_by . "') OR (send_to='" . $requested_by . "')) AND (message_type='private') ORDER BY id DESC";
    $result = mysqli_query($conn, $sql);
    $num_rows = mysqli_num_rows($result);

    if ($num_rows>=1){
    $sql2 = "SELECT * FROM `users` WHERE id='" . $requested_by . "'";
    $result2 = mysqli_query($conn, $sql2);
    $data = array();

        while ($row2 = mysqli_fetch_object($result2))
        {
        $temp_data = new stdClass();
            $sql3 = "SELECT * FROM `messages` WHERE ((send_from='" . $requested_by . "') OR (send_to='" . $requested_by . "')) AND ((message_type='private') AND (is_read='0'))";

            $result3 = mysqli_query($conn, $sql3);
            $temp_data->num_unread = mysqli_num_rows($result3);
            $temp_data->username = $row2->username;

        // Setting user data
            array_push($data, $temp_data);
        }
    }


    echo json_encode($data);
    exit();
}

if (isset($_REQUEST["get_all_groups_when_im_member"]))
{
    $requested_by = $_REQUEST["requested_by"];

    $sql = "SELECT * FROM `groups` ORDER BY created_at DESC";
    $result = mysqli_query($conn, $sql);
    $data = array();
    while ($row = mysqli_fetch_object($result))
    {
        $sql1 = "SELECT * FROM `group_members` WHERE group_id='" . $row->id . "' AND user_id='$requested_by'";
        $result1 = mysqli_query($conn, $sql1);

        if (mysqli_num_rows($result1) >=1){
        $sql2 = "SELECT * FROM `group_members` WHERE group_id='" . $row->id . "'";
        $result2 = mysqli_query($conn, $sql2);
        $row->members = mysqli_num_rows($result2);
        $row->am_I_member = true;
        array_push($data, $row);
        }
        
    }
    echo json_encode($data);
    exit();
}


if (isset($_REQUEST["get_all_groups"]))
{
    $requested_by = $_REQUEST["requested_by"];

    $sql = "SELECT * FROM `groups` ORDER BY created_at DESC";
    $result = mysqli_query($conn, $sql);
    $data = array();
    while ($row = mysqli_fetch_object($result))
    {
        $sql = "SELECT * FROM `group_chat` WHERE group_id='" . $row->id . "' AND send_to='$requested_by' AND is_read='0'";
        $result1 = mysqli_query($conn, $sql);
        $row->num_unread = mysqli_num_rows($result1);

        if ($row->created_by == $requested_by)
        {
            $row->am_I_member = true;
        }
        else
        {
            $sql = "SELECT * FROM `group_members` WHERE group_id='" . $row->id . "' AND user_id='$requested_by'";
            $result1 = mysqli_query($conn, $sql);
            $row->am_I_member = mysqli_num_rows($result1) > 0 ? true : false;
        }

        array_push($data, $row);
    }
    echo json_encode($data);
    exit();
}

if (isset($_REQUEST["edit_group"]))
{
    $group_name = $_REQUEST["group_name"];
    $group_id = $_REQUEST["group_id"];
    $file_path = "groups/" . $group_id . ".jpg";

    if (isset($_REQUEST["image_url"]))
    {
        $image_url = $_REQUEST["image_url"];
        if (empty($image_url))
        {
            $file_path = "";
        }
        else if ($image_url == $file_path)
        {
            //
        }
        else
        {
            if ($image_url == "-1")
            {
                unlink($file_path);
                $file_path = "";
            }
            else
            {
                if (file_exists($file_path))
                {
                    unlink($file_path);
                }
                $data = str_replace('data:image/png;base64,', '', $image_url);
                $data = str_replace(' ', '+', $data);
                $data = base64_decode($data);
                file_put_contents($file_path, $data);
            }
        }
    }

    $sql = "UPDATE `groups` SET `group_name`='$group_name', image_url='$file_path' WHERE id='$group_id'";
    mysqli_query($conn, $sql);

    echo json_encode(array(
        "status" => "success",
        "message" => "Grupa została zaaktualizowana.",
        "file_path" => $file_path
    ));
    exit();
}

if (isset($_REQUEST["get_group_requests_user_manage"]))
{
    $user_id = $_REQUEST["user_id"];

    $sql = "SELECT *, `users`.`id` AS user_id, `users`.`image_url` AS user_image_url, `groups`.`image_url` AS group_image_url FROM `group_join_requests` INNER JOIN `users` ON `users`.`id`=`group_join_requests`.`user_id` INNER JOIN `groups` ON `groups`.`id`=`group_join_requests`.`group_id` WHERE `groups`.`created_by`='$user_id' ORDER BY `group_join_requests`.`id` DESC";
    $result = mysqli_query($conn, $sql) or die(mysqli_error($conn));
    $data = array();
    while ($row = mysqli_fetch_object($result))
    {
        $temp_data = new stdClass();
        $temp_data->user = new stdClass();
        $temp_data->group = new stdClass();

        // Setting user data
        $temp_data->user->id = $row->user_id;
        $temp_data->user->name = $row->name;
        $temp_data->user->username = $row->username;
        $temp_data->user->phone = $row->phone_number;
        $temp_data->user->fcm_token = $row->token_notifications;
        $temp_data->user->image_url = $row->user_image_url;
        $temp_data->user->is_online = $row->is_online;

        // Setting group data
        $temp_data->group->id = $row->group_id;
        $temp_data->group->group_name = $row->group_name;
        $temp_data->group->image_url = $row->image_url;

        array_push($data, $temp_data);
    }
    echo json_encode($data);
    exit();
}

if (isset($_REQUEST["toggle_group_member_ban"]))
{
    $user_id = $_REQUEST["user_id"];
    $group_id = $_REQUEST["group_id"];
    $is_ban = $_REQUEST["is_ban"];

    $sql = "UPDATE `group_members` SET `is_ban`='$is_ban' WHERE `group_id`='$group_id' AND `user_id`='$user_id'";
    mysqli_query($conn, $sql);

    if ($is_ban == "1")
    {
        echo json_encode(array(
            "status" => "success",
            "message" => "Użytkownik został zablokowany."
        ));
    }
    else
    {
        echo json_encode(array(
            "status" => "success",
            "message" => "Użytkownik został odblokowany."
        ));
    }
    exit();
}

if (isset($_REQUEST["request_join_group"]))
{
    $group_id = $_REQUEST["group_id"];
    $user_id = $_REQUEST["user_id"];

    $sql = "SELECT * FROM `group_join_requests` WHERE `group_id`='$group_id' AND `user_id`='$user_id'";
    $result = mysqli_query($conn, $sql);
    $is_requested = mysqli_num_rows($result) > 0;

    $sql = "SELECT * FROM `group_invitations` WHERE `group_id`='$group_id' AND `user_id`='$user_id'";
    $result = mysqli_query($conn, $sql);
    $is_invited = mysqli_num_rows($result) > 0;

    if ($is_requested || $is_invited)
    {
        if ($is_invited)
        {
            echo json_encode(array(
                "status" => "error",
                "message" => "Wysłano już zaproszenie o dołączenie do tej grupy."
            ));
        }
        else
        {
            echo json_encode(array(
                "status" => "error",
                "message" => "Wysłano już prośbę o dołączenie."
            ));
        }
    }
    else
    {
        $sql = "INSERT INTO `group_join_requests`(`group_id`, `user_id`) VALUES ('$group_id', '$user_id')";
        mysqli_query($conn, $sql);
        echo json_encode(array(
            "status" => "success",
            "message" => "Wysłano prośbę o dołączenie do grupy."
        ));
    }
    exit();
}

if (isset($_REQUEST["respond_group_request"]))
{
    $response = array();

    $user_id = $_REQUEST["user_id"];
    $group_id = $_REQUEST["group_id"];
    $status = $_REQUEST["status"];
    $added_by = $_REQUEST["added_by"];

    if ($status == "accept")
    {
        // Add in members
        $sql = "INSERT INTO `group_members`(`group_id`, `user_id`, `added_by`, `created_at`) VALUES ('$group_id', '$user_id', '$added_by',NOW())";
        mysqli_query($conn, $sql);

        $response = array(
            "status" => "success",
            "message" => "Zaakceptowano prośbę o dołączenie."
        );
    }
    else if ($status == "decline")
    {
        $response = array(
            "status" => "success",
            "message" => "Prośba o dołączenie została odrzucona."
        );
    }

    // Delete from request
    $sql = "DELETE FROM group_join_requests WHERE user_id='$user_id' AND group_id='$group_id'";
    mysqli_query($conn, $sql);

    echo json_encode($response);
    exit();
}

if (isset($_REQUEST["get_friends_chat"])) {
$requested_by = $_REQUEST["requested_by"];

$sql = "SELECT * FROM friends WHERE (requested_to='$requested_by' OR requested_from='$requested_by') AND is_accepted='1'";
$result = mysqli_query($conn, $sql);

$data = array();

while ($row = mysqli_fetch_object($result)) {
    $row1 = new stdClass();

    if ($row->requested_to == $requested_by) {
        $userId = $row->requested_from;
    } else {
        $userId = $row->requested_to;
    }

    $sql_user = "SELECT * FROM users WHERE id='" . $userId . "'";
    $result_user = mysqli_query($conn, $sql_user);
    $row_user = mysqli_fetch_object($result_user);
    $row1->is_online = $row_user->is_online;
                                $row1->id = $row_user->id;
                            $row1->name= $row_user->name;
                              $row1->email= $row_user->email;
                            $row1->username = $row_user->username;
                            $row1->image_url = $row_user->image_url;

    $sql_messages = "SELECT * FROM messages WHERE message_type='private' AND is_read='0' AND send_from='$userId' AND send_to='$requested_by'";
    $result_messages = mysqli_query($conn, $sql_messages);
    $row1->unread_messages = mysqli_num_rows($result_messages);

    $sql_last_msg = "SELECT * FROM messages WHERE message_type='private' AND ((send_from='$userId' AND send_to='$requested_by') OR (send_from='$requested_by' AND send_to='$userId')) ORDER BY created DESC LIMIT 1";
    $result_last_msg = mysqli_query($conn, $sql_last_msg);
    $row_last_msg = mysqli_fetch_object($result_last_msg);

    $check_who_send_last_msg = $row_last_msg->send_from;
    $row1->last_msg = $row_last_msg->content;
    $row1->last_msg_date = $row_last_msg->created;

    if ($check_who_send_last_msg == $requested_by) {
        $row1->last_msg_sent_by_me = "true";
    } else {
        $row1->last_msg_sent_by_me = "false";
    }

    $row1->requested_by = $requested_by;

    array_push($data, $row1);
}

echo json_encode($data, JSON_PRETTY_PRINT);
exit();


}

if (isset($_REQUEST["get_users_not_in_contacts"]))
{
    $requested_by = $_REQUEST["requested_by"];
    $sql22 = "SELECT * FROM users WHERE id='$requested_by'";
    $result22 = mysqli_query($conn, $sql22);
    $row22 = mysqli_fetch_assoc($result22);
    $user_refferal_username = $row22['refferal_name'];


    $sql = "SELECT * FROM users WHERE refferal_name = '$user_refferal_username' AND NOT EXISTS (SELECT 1 FROM `friends` 
        WHERE (`friends`.`requested_to` = '$requested_by'
            AND `friends`.`requested_from` = `users`.`id`)
                OR (`friends`.`requested_from` = '$requested_by'
            AND `friends`.`requested_to` = `users`.`id`))
        AND `users`.`id` != '$requested_by'";
    $result = mysqli_query($conn, $sql);
    $data = array();
    while ($row = mysqli_fetch_object($result))
    {
        array_push($data, $row);
    }
    echo json_encode($data);
    exit();
}

if (isset($_REQUEST["get_message_content"]))
{
    $message_id = $_REQUEST["message_id"];
    $sql = "SELECT * FROM messages WHERE id='$message_id'";
    $result = mysqli_query($conn, $sql);
    echo json_encode(mysqli_fetch_object($result));
    exit();
}

if (isset($_REQUEST["send_friend_request"]))
{
    $requested_to = $_REQUEST["requested_to"];
    $requested_from = $_REQUEST["requested_from"];

    $sql = "SELECT * FROM `friends` WHERE (`requested_to`='$requested_to' AND `requested_from`='$requested_from') OR (`requested_to`='$requested_from' AND `requested_from`='$requested_to')";
    $result = mysqli_query($conn, $sql);
    if (mysqli_num_rows($result) > 0)
    {
        echo json_encode(array(
            "status" => "error",
            "message" => "Wysłano już prośbę o kontakt."
        ));
    }
    else
    {
        $sql = "INSERT INTO `friends`(`requested_to`, `requested_from`, `is_accepted`) VALUES ('$requested_to', '$requested_from', 0)";
        mysqli_query($conn, $sql);



    $content = "Prośba o kontakt została wysłana.";
    $send_from = $requested_from;
    $send_to = $requested_to;
    $message_type = 'private';
    $sql2 = "INSERT INTO `messages`(`content`, `send_from`, `send_to`, `message_type`, `is_read`, `created`,`system_message`, attachment) VALUES ('$content', '$send_from', '$send_to', '$message_type', 0, NOW(), '1', '')";
    mysqli_query($conn, $sql2);

        echo json_encode(array(
            "status" => "success",
            "message" => "Wysłano prośbę o kontakt."
        ));




    }
    exit();
}

if (isset($_REQUEST["respond_friend_request"]))
{
$user_id = $_REQUEST["user_id"];
$status = $_REQUEST["status"];
$response_by = $_REQUEST["response_by"];



if ($status == "accept") {
    $sql = "UPDATE `friends` SET is_accepted='1' WHERE requested_to='$response_by' AND requested_from='$user_id'";
    $content = "Prośba o kontakt została zaakceptowana.";
    $send_from = $response_by;
    $send_to = $user_id;
    $message_type = 'private'; 
    mysqli_query($conn, $sql);   
    $sql2 = "INSERT INTO `messages`(`content`, `send_from`, `send_to`, `message_type`, `is_read`, `created`, `system_message`, `attachment`) VALUES ('$content', '$send_from', '$send_to', '$message_type', '0', NOW(), '1', '')";
    mysqli_query($conn, $sql2);
        $response = array(
        "status" => "success",
        "message" => "Prośba o kontakt została zaakceptowana."
    );
    echo json_encode($response);
    exit();
} else if ($status == "reject") {
    $sql = "DELETE FROM `friends` WHERE requested_to='$response_by' AND requested_from='$user_id'";
    $content = "Prośba o kontakt została odrzucona.";
    $send_from = $response_by;
    $send_to = $user_id;
    $message_type = 'private';
    mysqli_query($conn, $sql);
        $sql2 = "INSERT INTO `messages`(`content`, `send_from`, `send_to`, `message_type`, `is_read`, `created`, `system_message`, `attachment`) VALUES ('$content', '$send_from', '$send_to', '$message_type', '0', NOW(), '1', '')";
    mysqli_query($conn, $sql2);
    $response = array(
        "status" => "error",
        "message" => "Prośba o kontakt została odrzucona."
    );
    echo json_encode($response);
    exit();
}

$response = array(
    "status" => "error",
    "message" => "Invalid status."
);

echo json_encode($response);
exit();

if ($status == "accept") {


} else if ($status == "reject") {
    $sql = "DELETE FROM `friends` WHERE requested_to='$response_by' AND requested_from='$user_id'";


}

echo json_encode($response);
exit();
}

if (isset($_REQUEST["get_friend_requests"]))
{
    $requested_from = $_REQUEST["requested_from"];
    $sql = "SELECT * FROM `friends` INNER JOIN `users` ON `users`.`id`=`friends`.`requested_from` WHERE requested_to='$requested_from' AND is_accepted='0'";
    $result = mysqli_query($conn, $sql);

    $data = array();
    while ($row = mysqli_fetch_object($result))
    {
        $row->is_online;
        array_push($data, $row);
    }

    echo json_encode($data);
    exit();
}

if (isset($_REQUEST["get_private_chat"]))
{
 $data = array();
    $data["messages"] = array();

    $user_id = $_REQUEST["userId"];
    $requested_by = $_REQUEST["requested_by"];


    $sql = "SELECT * FROM users WHERE id='$user_id'";
    $result = mysqli_query($conn, $sql);
    $data["user_data"] = mysqli_fetch_object($result);
    $data["user_data"]->is_online = $data["user_data"]->is_online;

    $sql = "SELECT * FROM users WHERE id='$requested_by'";
    $result = mysqli_query($conn, $sql);
    $my_data = mysqli_fetch_object($result);

    $sql = "SELECT * FROM messages WHERE (send_from='$user_id' AND send_to='$requested_by' AND message_type='private') OR (send_to='$user_id' AND send_from='$requested_by' AND message_type='private')";
    $result = mysqli_query($conn, $sql);
    $temp = array();
    while ($row = mysqli_fetch_object($result))
    {

    $id_actual = $row->id;

   $date_to_check = $row->created;
   $date_new = DateTime::createFromFormat('Y-m-d  H:i:s', $date_to_check)->format('Y-m-d H:i');
      $sql_messages_in_one_time = "SELECT * FROM messages WHERE ((send_from='$user_id' AND send_to='$requested_by' AND message_type='private') OR (send_to='$user_id' AND send_from='$requested_by' AND message_type='private')) AND (created LIKE  '".$date_new."%')";
    $result_count_rows = mysqli_query($conn, $sql_messages_in_one_time);

    if (mysqli_num_rows($result_count_rows) > 1)
    {
    $row->many_messages_in_one_time = "true";



     $sql_messages_first_message = "SELECT * FROM messages WHERE ((send_from='$user_id' AND send_to='$requested_by' AND message_type='private') OR (send_to='$user_id' AND send_from='$requested_by' AND message_type='private')) AND (created LIKE  '".$date_new."%') ORDER BY created ASC LIMIT 1";
      $result_first_message = mysqli_query($conn, $sql_messages_first_message);
    $row_first_message = mysqli_fetch_object($result_first_message);
    $id_first_message = $row_first_message->id;

    if ($id_first_message == $id_actual){
          $row->first_message = "true";
    }
    else{
            $row->first_message = "false";
    }


    $sql_messages_last_message = "SELECT * FROM messages WHERE ((send_from='$user_id' AND send_to='$requested_by' AND message_type='private') OR (send_to='$user_id' AND send_from='$requested_by' AND message_type='private')) AND (created LIKE  '".$date_new."%') ORDER BY created DESC LIMIT 1";
      $result_last_message = mysqli_query($conn, $sql_messages_last_message);
    $row_last_message = mysqli_fetch_object($result_last_message);
    $id_last_message = $row_last_message->id;

    if ($id_last_message == $id_actual){
          $row->last_message = "true";
    }
    else{
            $row->last_message = "false";
    }

    }
    else{
     $row->many_messages_in_one_time = "false";
     //
     $row->first_message = "false";

    }




        if ($row->send_from == $requested_by)
        {
            $row->send_from = $my_data;
            $row->send_to = $data["user_data"];
        }
        else
        {
            $row->send_from = $data["user_data"];
            $row->send_to = $my_data;
        }

        array_push($data["messages"], $row);
    }

    $sql = "SELECT * FROM friends WHERE (requested_to='$user_id' AND requested_from='$requested_by') OR (requested_from='$user_id' AND requested_to='$requested_by')";
    $result = mysqli_query($conn, $sql);
    $row = mysqli_fetch_assoc($result);
    $friend_id = $row['id'];
    $data["friend_id"] = $friend_id;

    /*$sql = "SELECT * FROM block_list WHERE friend_id='$friend_id'";
    $result = mysqli_query($conn, $sql);
    if (mysqli_num_rows($result) > 0)
    {
        $data["is_block"] = true;
        $temp = array();
        while ($block_object = mysqli_fetch_object($result))
        {
            array_push($temp, $block_object);
        }

        $data["block_by"] = "other";
        foreach ($temp as $t)
        {
            if ($t->block_by == $requested_by)
            {
                $data["block_by"] = "me";
                break;
            }
        }
    }
    else*/
        $data["is_block"] = false;

    echo json_encode($data);
    exit();

}

if (isset($_REQUEST["mark_private_chat_as_read"]))
{
    $user_id = $_REQUEST["userId"];
    $requested_by = $_REQUEST["requested_by"];

    $sql = "UPDATE messages SET is_read='1' WHERE send_from='$user_id' AND send_to='$requested_by'";
    mysqli_query($conn, $sql);

    echo json_encode(array(
        "status" => "success",
        "message" => "Prywatne rozmowy zostały oznaczone jako przeczytane."
    ));
    exit();
}

if (isset($_REQUEST["mark_group_chat_as_read"]))
{
    $group_id = $_REQUEST["groupId"];
    $requested_by = $_REQUEST["requested_by"];

    $sql = "DELETE FROM group_chat WHERE group_id='$group_id' AND send_to='$requested_by'";
    mysqli_query($conn, $sql);

    echo json_encode(array(
        "status" => "success",
        "message" => "Grupowe rozmowy zostały oznaczone jako przeczytane."
    ));
    exit();
}

if (isset($_REQUEST["get_group_invitation"]))
{
    $user_id = $_REQUEST["user_id"];

    $sql = "SELECT *, `groups`.`image_url` AS group_image_url, `users`.`id` AS sender_id FROM `group_invitations` INNER JOIN `groups` ON `groups`.`id`=`group_invitations`.`group_id` INNER JOIN `users` ON `users`.`id`=`groups`.`created_by` WHERE `group_invitations`.`user_id`='$user_id' ORDER BY `group_invitations`.`id` DESC";
    $result = mysqli_query($conn, $sql) or die(mysqli_error($conn));
    $data = array();
    while ($row = mysqli_fetch_object($result))
    {
        $temp_data = new stdClass();
        $temp_data->user = new stdClass();
        $temp_data->group = new stdClass();

        // Setting user data
        $temp_data->user->id = $row->sender_id;
        $temp_data->user->name = $row->name;
        $temp_data->user->username = $row->username;
        $temp_data->user->fcm_token = $row->token_notifications;
        $temp_data->user->is_online = $row->is_online;

        // Setting group data
        $temp_data->group->id = $row->group_id;
        $temp_data->group->group_name = $row->group_name;
        $temp_data->group->image_url = $row->group_image_url;
        $temp_data->group->created_by = $row->created_by;

        array_push($data, $temp_data);
    }
    echo json_encode($data);
    exit();
}

if (isset($_REQUEST["respond_group_invite"]))
{
    $response = array();

    $user_id = $_REQUEST["user_id"];
    $group_id = $_REQUEST["group_id"];
    $status = $_REQUEST["status"];
    $added_by = $_REQUEST["added_by"];

    if ($status == "accept")
    {
        // Add in members
        $sql = "INSERT INTO `group_members`(`group_id`, `user_id`, `added_by`, `created_at`, is_ban) VALUES ('$group_id', '$user_id', '$added_by',NOW(), 0)";
        mysqli_query($conn, $sql);
        $response = array(
            "status" => "success",
            "message" => "Zaproszenie zaakceptowane."
        );
    }
    else if ($status == "decline")
    {
        $response = array(
            "status" => "success",
            "message" => "Zaproszenie odrzucone."
        );
    }

    // Delete from request
    $sql = "DELETE FROM `group_invitations` WHERE user_id='$user_id' AND group_id='$group_id'";
    mysqli_query($conn, $sql);

    echo json_encode($response);
    exit();
}

if (isset($_REQUEST["get_home_metadata"])) {
    $data = array();
    
    // Sprawdzenie, czy 'requested_by' jest ustawione
    if (!isset($_REQUEST["requested_by"])) {
        die('Error: Missing parameter "requested_by"');
    }

    $requested_by = $_REQUEST["requested_by"];
    // Ustawienie parametru dla prepared statement
    $stmt1 = mysqli_prepare($conn, "SELECT * FROM `group_join_requests` INNER JOIN `groups` ON `group_join_requests`.`group_id` = `groups`.id WHERE `groups`.created_by = ?");
    if (!$stmt1) {
        die("Error in prepare statement: " . mysqli_error($conn));
    }

    mysqli_stmt_bind_param($stmt1, "s", $requested_by);

    if (!mysqli_stmt_execute($stmt1)) {
        die("Error in execute statement: " . mysqli_stmt_error($stmt1));
    }

    $result1 = mysqli_stmt_get_result($stmt1);

    if (!$result1) {
        die("Error in get result: " . mysqli_error($conn));
    }

    $data["group_requests"] = mysqli_num_rows($result1);


    // Close the prepared statements
    mysqli_stmt_close($stmt1);

    // Process results
    if ($count_rows_group > 0) {
        $with_groups = $count_rows_group;
        $data["group_counts"] = $without_groups + $with_groups;
    } else {
        $data["group_counts"] = 0;
    }


    $sql_group_invitations = "SELECT COUNT(*) AS group_invitations_count FROM group_invitations WHERE `user_id` = $requested_by";
    $result_group_invitations =  mysqli_query($conn, $sql_group_invitations);
    $row_group_invitations = mysqli_fetch_assoc($result_group_invitations);
    $group_invitations_count = $row_group_invitations['group_invitations_count'];


    $data["group_invites"] = $group_invitations_count;

    
    $sql_friends = "SELECT COUNT(*) as friends_count FROM friends WHERE requested_to = $requested_by AND `is_accepted` = 0";
    $result_friends =  mysqli_query($conn, $sql_friends);
    $row_friends = mysqli_fetch_assoc($result_friends);
    $friends_count = $row_friends['friends_count'];

    $data["friend_requests"] = $friends_count;


    // Output JSON
    header('Content-Type: application/json');
    echo json_encode($data, JSON_PRETTY_PRINT); // Ustawienie JSON_PRETTY_PRINT dla czytelniejszego formatowania
    exit();
}
if (isset($_REQUEST["block_user"]))
{
    $friend_id = $_REQUEST["friend_id"];
    $block_by = $_REQUEST["block_by"];

    $sql = "INSERT INTO block_list(friend_id, block_by) VALUES('$friend_id', '$block_by')";
    mysqli_query($conn, $sql);

    echo json_encode(array(
        "status" => "success",
        "message" => "Użytkownik został zablokowany."
    ));
    exit();
}

if (isset($_REQUEST["unblock_user"]))
{
    $friend_id = $_REQUEST["friend_id"];
    $unblock_by = $_REQUEST["unblock_by"];

    $sql = "DELETE FROM block_list WHERE friend_id='$friend_id' AND block_by='$unblock_by'";
    mysqli_query($conn, $sql);

    echo json_encode(array(
        "status" => "success",
        "message" => "Użytkownik został odblokowany."
    ));
    exit();
}

if (isset($_REQUEST["leave_group"]))
{
    $group_id = $_REQUEST["group_id"];
    $requested_by = $_REQUEST["requested_by"];

    $sql = "DELETE FROM group_members WHERE group_id='$group_id' AND user_id='$requested_by'";
    mysqli_query($conn, $sql);

    echo json_encode(array(
        "status" => "success",
        "message" => "Opuściłeś(aś) grupę."
    ));

    exit();
}

if (isset($_REQUEST["get_sent_friend_requests"]))
{
    $requested_from = $_REQUEST["requested_from"];
    $sql = "SELECT * FROM `friends` INNER JOIN `users` ON `users`.`id`=`friends`.`requested_to` WHERE requested_from='$requested_from' AND is_accepted='0'";
    $result = mysqli_query($conn, $sql);

    $data = array();
    while ($row = mysqli_fetch_object($result))
    {
        $row->is_online;
        array_push($data, $row);
    }

    echo json_encode($data);
    exit();
}

if (isset($_REQUEST["respond_sent_friend_request"]))
{
    $user_id = $_REQUEST["user_id"];
    $status = $_REQUEST["status"];
    $response_by = $_REQUEST["response_by"];

    $response = array();

    if ($status == "reject")
    {
        $sql = "DELETE FROM `friends` WHERE requested_from='$response_by' AND requested_to='$user_id'";
        mysqli_query($conn, $sql);
        $response = array(
            "status" => "error",
            "message" => "Prośba o kontakt została usunięta.",
            "sql" => $sql
        );
    }
    echo json_encode($response);
    exit();
}
