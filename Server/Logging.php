<?php
	
function logging($actionType, $actionData){

	$con = mysqli_connect("localhost","root","1212312121","proj4d");
	mysqli_set_charset($con, "utf8");


    $query = "INSERT INTO system_log(action_name, action_data) VALUES (?, ?)";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "ss", $actionType, $actionData);

    $success = mysqli_stmt_execute($statement);

    if($success){
    	echo "log success";
    }else{
    	printf("Errormessage: %s\n", mysqli_error($con));
    }

    mysqli_close($con);

}

?>