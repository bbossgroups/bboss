<?php
$records = array();
$records['nodes'] = array();

$id = isset($_REQUEST['id']) ? $_REQUEST['id'] : '0';
$level = 1;
if ($id != '0') {
	$id = explode(':', $id);
	$level = $id[1] + 1;
}

for($i = 1; $i < 6; $i++) {
	$id_ = time() + rand(1000, 20000) . ':' . ($level);
	$records['nodes'][] = array('id' => $id_, 'parent' => $id, 'name' => 'Node - ' . $level . ' - ' . $i, 'level' => $level, 'type' => 'folder');
}

echo json_encode($records);
?>

