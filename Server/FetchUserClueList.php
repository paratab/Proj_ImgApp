<?php
    $con=mysqli_connect("localhost","root","1212312121","proj4d");
	mysqli_set_charset($con, "utf8");

	$allClue = array();
	$clueArray = array();
	$clue = array();
	
	$allClue["resultDBConnection"] = 0;
	$allClue["resultFetchUserClueListData"] = 0;
	
	if(!$con){
		echo json_encode($allClue);
		exit;
	} else $allClue["resultDBConnection"] = 1;
	
	$username = $_POST["username"];

	$query = "SELECT A.id, A.sex, A.seenDate, B.path FROM clue_detail AS A, clue_image AS B WHERE A.id = B.clueId AND A.adder = ? AND A.isValid = 1";
	$statement = mysqli_prepare($con, $query);
	mysqli_stmt_bind_param($statement, "s", $username);	
	
    $success = mysqli_stmt_execute($statement);
	
	if(!$success){
		echo json_encode($allClue);
		exit;
	} else $allClue["resultFetchUserClueListData"] = 1;
	
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $id, $sex, $seenDate, $imagePath);
        
    while(mysqli_stmt_fetch($statement)){
		$clue["id"] = $id;
        $clue["sex"] = $sex;
		$clue["seenDate"] = $seenDate;
		$clue["imagePath"] = $imagePath;
		array_push($clueArray,$clue);
    }
	
	$allClue["gridItem"] = $clueArray;
	
    echo json_encode($allClue);

    $action_name = "Fetch_ClueData_UserList";
    $action_data = json_encode($_POST);
    $query = "INSERT INTO system_log(action_name, action_data) VALUES (?, ?)";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "ss", $action_name, $action_data);
    $success = mysqli_stmt_execute($statement); 
	
    mysqli_close($con);
?>
