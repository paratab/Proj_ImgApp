<?php

	include 'logging.php';

    $con = mysqli_connect("localhost","root","1212312121","proj4d");
	mysqli_set_charset($con, "utf8");

  	$clue = array();
	
	$clue["resultDBConnection"] = 0;
	$clue["resultUpdateStatus"] = 0;
	$clue["resultClueStatus"] = 0;
	
	if(!$con) {
		echo json_encode($clue);
		exit;
	} else $clue["resultDBConnection"] = 1;
	
	$clue_id = $_POST["clue_id"];
	$username = $_POST["username"];

	$query = "UPDATE clue_notice_list SET isValid = 0 WHERE clue_id = ?";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "s",  $clue_id);
	
    $success = mysqli_stmt_execute($statement);
	
	if(!$success){
		echo json_encode($clue);
		exit;
	} else $clue["resultUpdateStatus"] = 1;

	$query = "SELECT isValid FROM clue_notice_list WHERE clue_id = ?";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "s", $clue_id);

    $success = mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $isValid);
	mysqli_stmt_fetch($statement);

	if(isValid == 0){
		$clue["resultClueStatus"] = 1;
	}
   	
    echo json_encode($clue);
	 
    mysqli_close($con);

    logging("Update_Clue_Delete", json_encode($_POST));
?>
