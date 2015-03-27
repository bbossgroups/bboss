<?php
$username = $_REQUEST["username"];
if ($username == 'user1' || $username == 'user2') {
	echo json_encode(array('status' => 'OK', 'message' => 'Username <b>' . $username . '</b> is available. You can just pick it up!'));
} else {
	echo json_encode(array('status' => 'ERROR', 'message' => 'Username <b>' . $username . '</b> is not available. Please choose another one.'));
}
?>