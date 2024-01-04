<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
 
require_once 'Connection.php';
$show = $_REQUEST["show"];
$user_id = $_REQUEST["user_id"];


if ($show == "undone") {
    $sql = "SELECT t.*
FROM tasks t
LEFT JOIN ukonczone_zadania u ON t.task_id = u.task_id AND u.user_id = $user_id
WHERE u.task_id IS NULL;";
} else {
    $sql = "SELECT t.* FROM tasks t INNER JOIN ukonczone_zadania u ON t.task_id = u.task_id WHERE u.user_id = $user_id";
}

$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $tasks = array();

    while ($row = $result->fetch_assoc()) {
        $taskData = array(
            "task_id" => $row["task_id"],
            "task_image" => $row["task_image"],
            "task_title" => $row["task_title"],
            "task_description" => $row["task_description"],
            "task_points" => $row["task_points"],
            "task_shortdesc" => $row["task_shortdesc"],
            "task_isactive" => $row["task_isactive"],
            "task_isdone" => $row["task_isdone"]
        );

        $tasks[] = $taskData;
    }

    $conn->close();

echo json_encode($tasks, JSON_UNESCAPED_SLASHES);
} else {
    echo "No tasks found";
}
?>
