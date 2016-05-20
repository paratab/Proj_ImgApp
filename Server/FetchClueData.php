<?php

    $con = mysqli_connect("localhost","root","1212312121","proj4d");
	mysqli_set_charset($con, "utf8");

  	$clue = array();
	
	$clue["resultDBConnection"] = 0;
	$clue["resultFetchClueData"] = 0;
	$clue["resultFetchClueImage"] = 0;
	
	if(!$con) {
		echo json_encode($clue);
		exit;
	} else $clue["resultDBConnection"] = 1;
	
	$id = $_POST["id"];

	$query = "SELECT A.id, A.sex, A.seenDate, A.seenPlace, A.detail, A.adder ,B.name, B.telephone FROM clue_detail AS A, user_detail AS B WHERE A.id = ? AND A.adder = B.username";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "s", $id);
	
    $success = mysqli_stmt_execute($statement);
	
	if(!$success){
		echo json_encode($clue);
		exit;
	} else $clue["resultFetchClueData"] = 1;
     
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $id, $sex, $seenDate, $seenPlace, $detail, $adderUsername, $adderName, $telephone);
    
    while(mysqli_stmt_fetch($statement)){
		$clue["id"] = $id;
		$clue["sex"] = $sex;
        $clue["seenDate"] = $seenDate;
		$clue["seenPlace"] = $seenPlace;
		$clue["detail"] = $detail;
		$clue["adderUsername"] = $adderUsername;
		$clue["adderName"] = $adderName;
		$clue["telephone"] = $telephone;
    }
	
	$query = "SELECT path FROM clue_image WHERE clueId = ?";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "s", $id);
	
    $success = mysqli_stmt_execute($statement);
	
	if(!$success){
		echo json_encode($clue);
		exit;
	} else $clue["resultFetchClueImage"] = 1;
	
	mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $path);
	
	while(mysqli_stmt_fetch($statement)){
        $clue["imagePath"] = $path;
    }
	
    echo json_encode($clue);
	 
    mysqli_close($con);

?>
