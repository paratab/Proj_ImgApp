<?php

    $con=mysqli_connect("localhost","root","1212312121","proj4d");
	mysqli_set_charset($con, "utf8");
	
	$user = array();
	$user["resultDBConnection"] = 0;
	$user["resultFetchUserData"] = 0;
	$user["resultFetchUserImage"] = 0;
	$user["resultUserData"] = 0;
	
	if (!$con) {
		echo json_encode($user);
		exit;
	} else $user["resultDBConnection"] = 1;
     
    $username = $_POST["username"];
    $password = $_POST["password"];
   
	$query = "SELECT * FROM user_detail WHERE username = ? AND password = ?";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "ss", $username, $password);
	
    $success = mysqli_stmt_execute($statement);
	
	if(!$success) {
		echo json_encode($user);
		exit;
	}else $user["resultFetchUserData"] = 1;
    
	$username = "";
    $password = "";
	
	mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $user_id ,$username, $password, $name, $nationId, $email, $telephone, $isAdmin,$timestamp);
        
    while(mysqli_stmt_fetch($statement)){
		$user["name"] = $name;
		$user["nationId"] = $nationId;
		$user["email"] = $email;
		$user["telephone"] = $telephone;
		if($isAdmin == 1) $user["isAdmin"] = true;
		else $user["isAdmin"] = false;
    }
	
	if($name != null){
		$user["resultUserData"] = 1;
	}
	
    $query = "SELECT path FROM user_image WHERE username = ?";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "s", $username);
	
    $success = mysqli_stmt_execute($statement);
	
	if(!$success) {
		echo json_encode($user);
		exit;
	}else $user["resultFetchUserImage"] = 1;
	
	mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $path);
	
	while(mysqli_stmt_fetch($statement)){
        $user["imagePath"] = $path;
    }
	
    echo json_encode($user);

    mysqli_close($con);

?>
