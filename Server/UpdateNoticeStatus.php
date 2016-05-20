<?php

	include 'logging.php';

    $con = mysqli_connect("localhost","root","1212312121","proj4d");
	mysqli_set_charset($con, "utf8");

  	$notice = array();
	
	$notice["resultDBConnection"] = 0;
	$notice["resultUpdateStatus"] = 0;
	$notice["resultNoticeStatus"] = 0;
	
	if(!$con) {
		echo json_encode($notice);
		exit;
	} else $notice["resultDBConnection"] = 1;
	
	$id = $_POST["id"];
	$username = $_POST["username"];

	$query = "UPDATE notice_detail SET isValid = 0 WHERE id = ? AND adder = ?";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "ss", $id, $username);
	
    $success = mysqli_stmt_execute($statement);
	
	if(!$success){
		echo json_encode($notice);
		exit;
	} else $notice["resultUpdateStatus"] = 1;

	$query = "SELECT isValid FROM notice_detail WHERE id = ? AND adder = ?";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "ss", $id, $username);

    $success = mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $isValid);
	mysqli_stmt_fetch($statement);

	if(isValid == 0){
		$notice["resultNoticeStatus"] = 1;
	}
    	
    echo json_encode($notice);
	 
    mysqli_close($con);

    logging("Update_Notice_Status", json_encode($_POST));
?>
