<?php
    $con=mysqli_connect("localhost","root","1212312121","proj4d");
	mysqli_set_charset($con, "utf8");

	$allNotice = array();
	$noticeArray = array();
	$notice = array();
	
	$allNotice["resultDBConnection"] = 0;
	$allNotice["resultFetchUserNoticeListData"] = 0;
	
	if(!$con){
		echo json_encode($noticeArray);
		exit;
	} else $allNotice["resultDBConnection"] = 1;
	
	$username = $_POST["username"];

	$query = "SELECT A.id, A.name, A.lostDate, B.path FROM notice_detail AS A, notice_image AS B WHERE A.id = B.noticeId AND A.adder = ? AND A.isValid = 1";
	$statement = mysqli_prepare($con, $query);
	mysqli_stmt_bind_param($statement, "s", $username);	
	
    $success = mysqli_stmt_execute($statement);
	
	if(!$success){
		echo json_encode($noticeArray);
		exit;
	}else $allNotice["resultFetchUserNoticeListData"] = 1;
	
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $id, $name, $lostDate, $imagePath);
        
    while(mysqli_stmt_fetch($statement)){
		$notice["id"] = $id;
        $notice["name"] = $name;
		$notice["lostDate"] = $lostDate;
		$notice["imagePath"] = $imagePath;
		array_push($noticeArray,$notice);
    }
	
	$allNotice["gridItem"] = $noticeArray;
	
    echo json_encode($allNotice);

    $action_name = "Fetch_NoticeData_UserList";
    $action_data = json_encode($_POST);
    $query = "INSERT INTO system_log(action_name, action_data) VALUES (?, ?)";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "ss", $action_name, $action_data);
    $success = mysqli_stmt_execute($statement); 
	
    mysqli_close($con);
?>
