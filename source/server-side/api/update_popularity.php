<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
require_once 'Connection.php';

// Check if the required keys exist in the $_POST array
if (isset($_POST['id']) && isset($_POST['is_airdrop']) && isset($_POST['promo_type'])) {
    $id = $_POST['id'];
    $is_airdrop = $_POST['is_airdrop'];

    if ($is_airdrop == "1") {
        $sql = mysqli_query($conn, "UPDATE airdrops SET views = views+1 WHERE id = '" . $id . "'");
    } else {
        $promo_type = $_POST['promo_type'];

        if ($promo_type == "promocja") {
            $sql = mysqli_query($conn, "UPDATE promocje SET popularity = popularity+1 WHERE id = '" . $id . "'");
        } else {
            $sql = mysqli_query($conn, "UPDATE airdropy SET popularity = popularity+1 WHERE id = '" . $id . "'");
        }
    }
} else {
    // Handle the case when required keys are not set
    echo json_encode(array('error' => 'Missing required parameters'));
}
?>
