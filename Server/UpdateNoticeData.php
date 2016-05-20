<?php

	include 'logging.php';
	include 'ImageExeCalling.php';

    $con=mysqli_connect("localhost","root","1212312121","proj4d");
	mysqli_set_charset($con, "utf8");
	
	$notice = array();
	$notice["resultDBConnection"] = 0;
	$notice["resultUpdateNoticeData"] = 0;
	$notice["resultUpdateNoticeImage"] = 0;
	
	if(!$con) {
		echo json_encode($notice);
		exit;
	} else $notice["resultDBConnection"] = 1;

    $id = $_POST["id"];
	$name = $_POST["name"];
	$sex = $_POST["sex"];
	$birthDate = $_POST["birthDate"];
	$lostPlace = $_POST["lostPlace"];
	$lostDate = $_POST["lostDate"];
	$detail = $_POST["detail"];
	$adderUsername	= $_POST["adderUsername"];
	
	$image = $_POST["imageString"];	
	$decodeImage = base64_decode($image);
	$path = "/image/notice/".$id.".jpg";
	file_put_contents(getcwd(). $path, $decodeImage);
		
	$query = "UPDATE notice_detail SET name = ?,sex = ?, birthDate = ?, lostPlace = ?, lostDate = ?, detail = ? WHERE id = ? AND adder = ?";
	$statement = mysqli_prepare($con, $query);
	mysqli_stmt_bind_param($statement, "ssssssss", $name, $sex, $birthDate, $lostPlace, $lostDate, $detail, $id, $adderUsername);
	$success = mysqli_stmt_execute($statement);
	
	if(!$success){
		echo json_encode($notice);
		exit;
	} else $notice["resultUpdateNoticeData"] = 1;
    
	$query = "SELECT path FROM notice_image WHERE noticeId = ?";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "s", $id);
    $success = mysqli_stmt_execute($statement);
	
	if(!$success) {
		echo json_encode($notice);
		exit;
	} else $notice["resultUpdateNoticeImage"] = 1;
	
	mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $path);
	
	while(mysqli_stmt_fetch($statement)){
        $notice["imagePath"] = $path;
        $_POST["imageString"] = $path;
    }
	
    echo json_encode($notice);
	
    mysqli_close($con);

    logging("Update_Notice_Data", json_encode($_POST));

    $extFileName = createExtFile("clue");

    callExeOfImageFindClue("notice", $id, $extFileName);	
?>
