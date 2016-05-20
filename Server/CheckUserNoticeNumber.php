<?php
    $con=mysqli_connect("localhost","root","1212312121","proj4d");
	mysqli_set_charset($con, "utf8");

	$usernameCheck = array();
	
	$usernameCheck["resultDBConnection"] = 0;
	$usernameCheck["resultUserNoticeCheck"] = 0;
	$usernameCheck["isAbleToCreateNotice"] = false;
	
	if(!$con){
		echo json_encode($usernameCheck);
		exit;
	} else $usernameCheck["resultDBConnection"] = 1;
	
	$username = $_POST["username"];

	$query = "SELECT count(*) FROM notice_detail WHERE adder = ? AND isValid = 1";
	$statement = mysqli_prepare($con, $query);
	mysqli_stmt_bind_param($statement, "s", $username);	
	
    $success = mysqli_stmt_execute($statement);
	
	if(!$success){
		echo json_encode($usernameCheck);
		exit;
	} else $usernameCheck["resultUserNoticeCheck"] = 1;
	
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $count);
        
    mysqli_stmt_fetch($statement);
	if($count < 3){
		$usernameCheck["isAbleToCreateNotice"] = true;
	}
		
    echo json_encode($usernameCheck);
	
    mysqli_close($con);
?>
