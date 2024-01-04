<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
ini_set("allow_url_fopen", 1);

$currency = $_GET['currency'];

$json = file_get_contents('https://exchange-rates.abstractapi.com/v1/live/?api_key=2735a3db6424494ab1fe5725529c90e6&base=' . $currency . '&target=PLN');
$json_array = json_decode($json, true);

if ($json_array && isset($json_array['exchange_rates']['PLN'])) {
    $value = $json_array['exchange_rates']['PLN'];

    $var_decimal = number_format((float)$value, 2, '.', '');
    $myObj = new stdClass();
    $myObj->exchange_cur = $var_decimal;
    $myJSON = json_encode($myObj);
    echo $myJSON;
} else {
    echo json_encode(array('error' => 'Błąd w zdekodowaniu JSONa lub brak wartości PLN.'));
}
?>
