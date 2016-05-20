<?php

	include 'logging.php';

    $con=mysqli_connect("localhost","root","1212312121","proj4d");
	mysqli_set_charset($con, "utf8");

	$user = array();
	$user["resultDBConnection"] = 0;
	$user["resultStoreUserData"] = 0;
	$user["resultStoreUserImage"] = 0;
	
	if(!$con) {
		echo json_encode($user);
		exit;
	} else $user["resultDBConnection"] = 1;
	
    $username = $_POST["username"];
    $password = $_POST["password"];
	$name = $_POST["name"];
	$nationId = $_POST["nationId"];
	$email = $_POST["email"];
	$telephone = $_POST["telephone"];
	$image = $_POST["imageString"];
		
	$decodeImage = base64_decode($image);
	$path = "/image/user/".$username.".jpg";
	
    $query = "INSERT INTO user_detail(username, password, name, nation_id, email, telephone) VALUES (?, ?, ?, ?, ?, ?)";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "ssssss",$username, $password, $name, $nationId, $email, $telephone);
	
    $success = mysqli_stmt_execute($statement);
	
	if(!$success){
		echo json_encode($user);
		exit;
	} else $user["resultStoreUserData"] = 1;
	
	file_put_contents(getcwd(). $path, $decodeImage);
	$_POST["imageString"] = $path;
			
	$query = "INSERT INTO user_image(username, path) VALUES (?, ?)";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "ss",$username, $path);
	
    $success = mysqli_stmt_execute($statement);
	
	if(!$success){
		echo json_encode($user);
		exit;
	} else $user["resultStoreUserImage"] = 1;
    
    echo json_encode($user);	
	
    mysqli_stmt_close($statement);
    
    mysqli_close($con);

    logging("Store_User_Data", json_encode($_POST));
	
?>
