<?php
    $con=mysqli_connect("localhost","root","1212312121","proj4d");
	mysqli_set_charset($con, "utf8");

	$tokenCheck = array();
	
	$tokenCheck["resultDBConnection"] = 0;
	$tokenCheck["resultTokenCheck"] = 0;
	$tokenCheck["resultStoreToken"] = 0;
	$tokenCheck["resultMethod"] = "";
	
	if(!$con){
		echo json_encode($tokenCheck);
		exit;
	} else $tokenCheck["resultDBConnection"] = 1;
	
	$username = $_POST["username"];
	$token = $_POST["token"];

	$query = "SELECT count(*),token FROM gcm_token_list WHERE username = ?";
	$statement = mysqli_prepare($con, $query);
	mysqli_stmt_bind_param($statement, "s", $username);	
	
    $success = mysqli_stmt_execute($statement);
	
	if(!$success){
		echo json_encode($tokenCheck);
		exit;
	} else $tokenCheck["resultTokenCheck"] = 1;
	
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $count, $existToken);
        
    mysqli_stmt_fetch($statement);

	if($count == 0) {
		$query = "INSERT INTO gcm_token_list(username, token) VALUES (?, ?)";
		$statement = mysqli_prepare($con, $query);
		mysqli_stmt_bind_param($statement, "ss", $username, $token);
		$success = mysqli_stmt_execute($statement);	
		$tokenCheck["resultMethod"] = "Insert";
	}
	else if(strcmp($existToken,$token)!=0){
		$query = "UPDATE gcm_token_list SET token = ? WHERE username = ?";
		$statement = mysqli_prepare($con, $query);
		mysqli_stmt_bind_param($statement, "ss", $token, $username);
		$success = mysqli_stmt_execute($statement);	
		$tokenCheck["resultMethod"] = "Update";	
	}else{
		$success = true;
		$tokenCheck["resultMethod"] = "Exist";
	}

	if(!$success){
		echo json_encode($tokenCheck);
		exit;
	} else $tokenCheck["resultStoreToken"] = 1;

    echo json_encode($tokenCheck);
	
    mysqli_close($con);
?>
