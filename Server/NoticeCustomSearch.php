<?php
    $con=mysqli_connect("localhost","root","1212312121","proj4d");
	mysqli_set_charset($con, "utf8");

	$allNotice = array();
	$noticeArrray = array();
	$notice = array();
	
	$allNotice["resultDBConnection"] = 0;
	$allNotice["resultFetchNoticeData"] = 0;
	
	if(!$con){
		echo json_encode($allNotice);
		exit;
	} else $allNotice["resultDBConnection"] = 1;

	$mode = $_POST["mode"];
	$sex = $_POST["sex"];
	$minAge = $_POST["minAge"];
	$maxAge = $_POST["maxAge"];

	if($mode == 1){ // Sex Only
		$query = "SELECT A.id, A.name, A.birthDate, B.path FROM notice_detail AS A, notice_image AS B WHERE A.sex = ? AND A.id = B.noticeId AND A.isValid = 1";
		$statement = mysqli_prepare($con, $query);
		mysqli_stmt_bind_param($statement, "s", $sex);
	}else if($mode == 2){ // Age Only
		$query = "SELECT A.id, A.name, A.birthDate, B.path FROM notice_detail AS A, notice_image AS B WHERE (LEFT(NOW(),4) - RIGHT(birthDate,4)) >= ? AND (LEFT(NOW(),4) - RIGHT(birthDate,4))-1 <= ? AND A.id = B.noticeId AND A.isValid = 1";
		$statement = mysqli_prepare($con, $query);
		mysqli_stmt_bind_param($statement, "ii", $minAge, $maxAge);
	}else{ // Sex and Age
		$query = "SELECT A.id, A.name, A.birthDate, B.path FROM notice_detail AS A, notice_image AS B WHERE A.sex = ? AND (LEFT(NOW(),4) - RIGHT(birthDate,4)) >= ? AND (LEFT(NOW(),4) - RIGHT(birthDate,4)) <= ? AND A.id = B.noticeId AND A.isValid = 1";
		$statement = mysqli_prepare($con, $query);
		mysqli_stmt_bind_param($statement, "sii", $sex, $minAge, $maxAge);
	}
	
    $success = mysqli_stmt_execute($statement);
	
	if(!$success){
		echo json_encode($allNotice);
		exit;
	}else $allNotice["resultFetchNoticeData"] = 1;
	
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $id, $name, $birthDate, $imagePath);
        
    while(mysqli_stmt_fetch($statement)){
		$notice["id"] = $id;
        $notice["name"] = $name;
		$notice["birthDate"] = $birthDate;
		$notice["imagePath"] = $imagePath;
		array_push($noticeArrray,$notice);
    }
	
	$allNotice["gridItem"] = $noticeArrray;
	
    echo json_encode($allNotice);

    $action_name = "Fetch_NoticeData_CustomSearch";
    $action_data = json_encode($_POST);
    $query = "INSERT INTO system_log(action_name, action_data) VALUES (?, ?)";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "ss", $action_name, $action_data);
    $success = mysqli_stmt_execute($statement); 
	
    mysqli_close($con);
?>
