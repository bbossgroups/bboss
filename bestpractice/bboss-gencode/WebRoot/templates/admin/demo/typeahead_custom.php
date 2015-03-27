<?php
$query = $_REQUEST["query"];

$max = rand(5, 10);
$results = array();

for($i = 0; $i <= $max; $i++) {
	$results[] = array(
		"value" => $query . ' - ' . rand(10, 100),
		"desc" => "some description goes here...",
		"img" => "http://lorempixel.com/50/50/?" . (rand(1, 10000) . rand(1, 10000)),
		"tokens" => array($query, $query . rand(1, 10))
	);
}

echo json_encode($results);
?>