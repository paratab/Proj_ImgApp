<?php

	include 'logging.php';

    $con = mysqli_connect("localhost","root","1212312121","proj4d");
	mysqli_set_charset($con, "utf8");

  	$clue = array();
	
	$clue["resultDBConnection"] = 0;
	$clue["resultClueSave"] = 0;
	
	if(!$con) {
		echo json_encode($clue);
		exit;
	} else $clue["resultDBConnection"] = 1;
	
	$clue_id = $_POST["clue_id"];
	$notice_id = $_POST["notice_id"];

	$query = "INSERT INTO clue_notice_list(notice_id, clue_id) VALUES (?, ?)";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "ss",$notice_id, $clue_id);
	
    $success = mysqli_stmt_execute($statement);
	
	if(!$success){
		echo json_encode($clue);
		exit;
	} else $clue["resultClueSave"] = 1;
   	
    echo json_encode($clue);
	 
    mysqli_close($con);

    logging("Store_Clue_Save", json_encode($_POST));
?>
