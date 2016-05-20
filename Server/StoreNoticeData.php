<?php

	include 'logging.php';
	include 'ImageExeCalling.php';

    $con=mysqli_connect("localhost","root","1212312121","proj4d");
	mysqli_set_charset($con, "utf8");

    $notice = array();
	$notice["resultDBConnection"] = 0;
	$notice["resultStoreNoticeData"] = 0;
	$notice["resultStoreNoticeImage"] = 0;
	
	if(!$con) {
		echo json_encode($notice);
		exit;
	} else $notice["resultDBConnection"] = 1;
	
	$name = $_POST["name"];
	$sex = $_POST["sex"];
	$birthDate = $_POST["birthDate"];
	$lostPlace = $_POST["lostPlace"];
	$lostDate = $_POST["lostDate"];
	$detail = $_POST["detail"];
	
	$username = $_POST["username"];
	
	$image = $_POST["imageString"];		
	$decodeImage = base64_decode($image);
		
	$query = "INSERT INTO notice_detail(name, sex, birthDate, lostPlace, lostDate, detail, adder) VALUES (?, ?, ?, ?, ?, ?, ?)";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "sssssss", $name, $sex, $birthDate, $lostPlace, $lostDate, $detail, $username);
	$success = mysqli_stmt_execute($statement);
	
	if(!$success){
		echo json_encode($notice);
		exit;
	}else $notice["resultStoreNoticeData"] = 1;
	
	$query = "SELECT id FROM notice_detail WHERE name = ? AND lostDate = ? AND adder = ?";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "sss", $name, $lostDate, $username);
	$success = mysqli_stmt_execute($statement);

	mysqli_stmt_store_result($statement);
	
    mysqli_stmt_bind_result($statement, $id);
    
    while(mysqli_stmt_fetch($statement)){
		$notice["id"] = $id;
    }
	
	$path = "/image/notice/".$id.".jpg";
	file_put_contents(getcwd(). $path, $decodeImage);
	
	$query = "INSERT INTO notice_image(noticeId, path) VALUES (?, ?)";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "ss", $id, $path);
	
    $success = mysqli_stmt_execute($statement);
	
	if(!$success){
		echo json_encode($notice);
		exit;
	}else $notice["resultStoreNoticeImage"] = 1;

	$_POST["imageString"] = $path;
	
    echo json_encode($notice);

    mysqli_stmt_close($statement);
    
    mysqli_close($con);

    logging("Store_Notice_Data", json_encode($_POST));

    //createDatabaseImageFile("notice",$id);

    $extFileName = createExtFile("clue");
    callExeOfImageFindClue("notice", $id, $extFileName);	
?>
