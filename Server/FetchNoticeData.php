<?php

    $con=mysqli_connect("localhost","root","1212312121","proj4d");
	mysqli_set_charset($con, "utf8");

	$notice = array();
	$notice["resultDBConnection"] = 0;
	$notice["resultFetchNoticeData"] = 0;
	$notice["resultFetchNoticeImage"] = 0;
	
	if(!$con) {
		echo json_encode($notice);
		exit;
	} else $notice["resultDBConnection"] = 1;
	
	$id = $_POST["id"];
    
	$query = "SELECT A.id, A.name, A.sex, A.birthDate, A.lostPlace, A.lostDate, A.detail, A.adder, B.name, B.telephone FROM notice_detail AS A, user_detail AS B WHERE A.id = ? AND A.adder = B.username";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "s", $id);
	
    $success = mysqli_stmt_execute($statement);
	
	if(!$success) $notice["resultFetchNoticeData"] = "0";	
	else $notice["resultFetchNoticeData"] = "1";
    
	mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $id, $name, $sex, $birthDate, $lostPlace, $lostDate, $detail, $adderUsername, $adderName, $telephone);
    
    while(mysqli_stmt_fetch($statement)){
		$notice["id"] = $id;
        $notice["name"] = $name;
		$notice["sex"] = $sex;
		$notice["birthDate"] = $birthDate;
		$notice["lostPlace"] = $lostPlace;
		$notice["lostDate"] = $lostDate;
		$notice["detail"]= $detail;
		$notice["adderUsername"]= $adderUsername;
		$notice["adderName"]= $adderName;
		$notice["telephone"]= $telephone;
    }
	
	$query = "SELECT path FROM notice_image WHERE noticeId = ?";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "s", $id);
	
    $success = mysqli_stmt_execute($statement);
	
	if(!$success) $notice["resultFetchNoticeImage"] = "0";	
	else $notice["resultFetchNoticeImage"] = "1";
	
	mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $path);
	
	while(mysqli_stmt_fetch($statement)){
        $notice["imagePath"] = $path;
    }
	
    echo json_encode($notice);	
    mysqli_close($con);

?>
