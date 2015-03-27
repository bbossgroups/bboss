<?php
$parent = $_REQUEST["parent"];

$data = array();

$states = array(
  	"success",
  	"info",
  	"danger",
  	"warning"
);

if ($parent == "#") {
	for($i = 1; $i < rand(4, 7); $i++) {
		$data[] = array(
			"id" => "node_" . time() . rand(1, 100000),  
			"text" => "Node #" . $i, 
			"icon" => "fa fa-folder icon-lg icon-state-" . ($states[rand(0, 3)]),
			"children" => true, 
			"type" => "root"
		);
	}
} else {
	if (rand(1, 5) === 3) {
		$data[] = array(
			"id" => "node_" . time() . rand(1, 100000), 
			"icon" => "fa fa-file fa-large icon-state-default",
			"text" => "No childs ", 
			"state" => array("disabled" => true),
			"children" => false
		);
	} else {
		for($i = 1; $i < rand(2, 4); $i++) {
			$data[] = array(
				"id" => "node_" . time() . rand(1, 100000), 
				"icon" => ( rand(0, 3) == 2 ? "fa fa-file icon-lg" : "fa fa-folder icon-lg")." icon-state-" . ($states[rand(0, 3)]),
				"text" => "Node " . time(), 
				"children" => ( rand(0, 3) == 2 ? false : true)
			);
		}
	}
}

header('Content-type: text/json');
header('Content-type: application/json');
echo json_encode($data);
?>