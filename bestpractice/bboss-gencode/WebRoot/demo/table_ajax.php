<?php
  /* 
   * Paging
   */
  $iTotalRecords = 200;
  $iDisplayLength = intval($_GET['iDisplayLength']);
  $iDisplayLength = $iDisplayLength < 0 ? $iTotalRecords : $iDisplayLength; 
  $iDisplayStart = intval($_GET['iDisplayStart']);
  $sEcho = intval($_GET['sEcho']);
  
  $records = array();
  $records["aaData"] = array(); 

  $end = $iDisplayStart + $iDisplayLength;

  $status_list = array(
    array("success" => "Approved"),
    array("warning" => "Pending"),
    array("danger" => "Rejected")
  );

  for($i = $iDisplayStart; $i < $end; $i++) {
    $status = $status_list[rand(0, 2)];
    $id = ($i + 1);
    $records["aaData"][] = array(
      $id,
      'metrouser',
      ($id * 12295),
      '<span class="label label-sm label-'.(key($status)).'">'.(current($status)).'</span>',
      '<a href="javascript:;" data-id="'.$id.'" class="btn btn-xs blue btn-editable"><i class="fa fa-pencil"></i> Edit</a> <a href="javascript:;" data-id="'.$id.'" class="btn btn-xs red btn-removable"><i class="fa fa-times"></i> Delete</a> <div class="btn-group"><button class="btn default btn-xs dropdown-toggle" type="button" data-toggle="dropdown">More <i class="fa fa-angle-down"></i></button><ul class="dropdown-menu pull-right" role="menu"><li><a href="#">Action</a></li><li><a href="#">Another action</a></li></ul></div>',
    );
  }

  $records["sEcho"] = $sEcho;
  $records["iTotalRecords"] = $iTotalRecords;
  $records["iTotalDisplayRecords"] = $iTotalRecords;
  
  echo json_encode($records);
?>