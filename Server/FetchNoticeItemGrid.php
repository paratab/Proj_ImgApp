<?php
    $con=mysqli_connect("localhost","root","1212312121","proj4d");
	mysqli_set_charset($con, "utf8");

	$allNotice = array();
	$noticeArrray = array();
	$notice = array();
	
	$allNotice["resultDBConnection"] = 0;
	$allNotice["resultFetchNoticeGridData"] = 0;
	
	if(!$con){
		echo json_encode($allNotice);
		exit;
	} else $allNotice["resultDBConnection"] = 1;

	$query = "SELECT A.id, A.name, A.birthDate, B.path FROM notice_detail AS A, notice_image AS B WHERE A.id = B.noticeId AND A.isValid = 1";
	$statement = mysqli_prepare($con, $query);
	
    $success = mysqli_stmt_execute($statement);
	
	if(!$success){
		echo json_encode($allNotice);
		exit;
	}else $allNotice["resultFetchNoticeGridData"] = 1;
	
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
	
    mysqli_close($con);
?>
