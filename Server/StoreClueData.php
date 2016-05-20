<?php

	include 'NotificationCenter.php';
	include 'logging.php';
	include 'ImageExeCalling.php';

    $con=mysqli_connect("localhost","root","1212312121","proj4d");
	mysqli_set_charset($con, "utf8");

    $clue = array();
	
	$clue["resultDBConnection"] = 0;
	$clue["resultStoreClueData"] = 0;
	$clue["resultStoreClueImage"] = 0;
		
	if(!$con) {
		echo json_encode($clue);
		exit;
	} else $clue["resultDBConnection"] = 1;
	
	$sex = $_POST["sex"];
	$seenDate = $_POST["seenDate"];
	$seenPlace = $_POST["seenPlace"];
	$detail = $_POST["detail"];
	$adder = $_POST["adderUsername"];
	
	$image = $_POST["imageString"];		
	$decodeImage = base64_decode($image);

	$notice_id = $_POST["noticeId"];
		
	$query = "INSERT INTO clue_detail(sex, seenDate, seenPlace, detail, adder) VALUES (?, ?, ?, ?, ?)";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "sssss", $sex, $seenDate, $seenPlace, $detail, $adder);
	$success = mysqli_stmt_execute($statement);
	
	if(!$success){
		echo json_encode($clue);
		exit;
	} else $clue["resultStoreClueData"] = 1;
	
	$query = "SELECT id FROM clue_detail WHERE seenDate = ? AND seenPlace = ? AND adder = ?";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "sss", $seenDate, $seenPlace, $adder);
	$success = mysqli_stmt_execute($statement);
	
	mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $clue_id);
    
    while(mysqli_stmt_fetch($statement)){
		$clue["clueId"] = $clue_id;
    }
	
	$path = "/image/clue/".$clue_id.".jpg";
	file_put_contents(getcwd(). $path, $decodeImage);
	$_POST["imageString"] = $path;
	
	$query = "INSERT INTO clue_image(clueId, path) VALUES (?, ?)";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "ss",$clue_id, $path);
	
    $success = mysqli_stmt_execute($statement);
	
	if(!$success) {
		echo json_encode($clue);
		exit;
	} else $clue["resultStoreClueImage"] = 1;
	
    //echo json_encode($clue);

    mysqli_stmt_close($statement);
    
    mysqli_close($con);

    echo json_encode($clue);

    $clue["logging"] = logging("Store_Clue_Data", json_encode($_POST));

    if($notice_id != -1){
    	createDatabaseImageOnly("clue",$clue_id);
    	pushNotifiactionForDirectClueAdd($clue_id , $notice_id);
    }else{
    	$extFile = createExtFile("notice");
    	callExeOfImageFindNotice("clue", $clue_id, $extFile); //sendToImgPart();
    }
    	
?>
