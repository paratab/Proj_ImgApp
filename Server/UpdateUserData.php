<?php

	include 'logging.php';

    $con=mysqli_connect("localhost","root","1212312121","proj4d");
	mysqli_set_charset($con, "utf8");
	
	$user = array();
	$user["resultDBConnection"] = 0;
	$user["resultUpdateUserData"] = 0;
	$user["resultUpdateUserImage"] = 0;
	$user["resultFetchUserData"] = 1;
	
	if(!$con) {
		echo json_encode($user);
		exit;
	} else $user["resultDBConnection"] = 1;
	
	$username = $_POST["username"];
    $password = $_POST["password"];
	$user["username"] = $username;
	$user["password"] = $password;
	$name = $_POST["name"];
	$email = $_POST["email"];
	$telephone = $_POST["telephone"];
	$image = $_POST["imageString"];
		
	$decodeImage = base64_decode($image);
	$path = "/image/user/".$username.".jpg";
	
	file_put_contents(getcwd(). $path, $decodeImage);
		
	$query = "UPDATE user_detail SET name = ?,email = ?, telephone = ? WHERE username = ? AND password = ?";
	$statement = mysqli_prepare($con, $query);
	mysqli_stmt_bind_param($statement, "sssss", $name, $email, $telephone, $username, $password);
	$success = mysqli_stmt_execute($statement);
	
	if(!$success){
		echo json_encode($user);
		exit;
	} else $user["resultUpdateUserData"] = 1;
		
	$query = "SELECT path FROM user_image WHERE username = ?";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "s", $username);
	
    $success = mysqli_stmt_execute($statement);
	
	if(!$success){
		echo json_encode($user);
		exit;
	}else $user["resultUpdateUserImage"] = 1;
	
	mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $path);
	
	while(mysqli_stmt_fetch($statement)){
        $user["imagePath"] = $path;
        $_POST["imageString"] = $path;
    }

    echo json_encode($user);

    mysqli_close($con);

    logging("Update_User_Data", json_encode($_POST));
?>
