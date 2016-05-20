<?php

//include 'logging.php';

function prepareNotificationOneClueManyNotice($clue_id, $notice_id)
{
	$con=mysqli_connect("localhost","root","1212312121","proj4d");
	mysqli_set_charset($con, "utf8");
	
	$notify_data = array();
	$notify_ids = array();
	$adder_notice = array();

	$notice_list = explode(",", $notice_id);

	for ($i = 0; $i < sizeof($notice_list); $i++) { 

		$query = "SELECT A.adder, A.name, B.token FROM notice_detail AS A, gcm_token_list AS B WHERE A.id = ? AND A.adder = B.username";
		$statement = mysqli_prepare($con, $query);
	    mysqli_stmt_bind_param($statement, "s", $notice_list[$i]);
		$success = mysqli_stmt_execute($statement);
	
		mysqli_stmt_store_result($statement);
		mysqli_stmt_bind_result($statement, $adder, $lostName , $token);
		mysqli_stmt_fetch($statement);

		$adder_notice["" . $adder] = "" . $notice_list[$i] . "," . $lostName;
		
		array_push($notify_ids , $token);

	}

	$notify_data["clue_id"] = $clue_id;
	$notify_data["notice_id_data"] = json_encode($adder_notice);

	mysqli_stmt_close($statement);
    
    mysqli_close($con);

    return sendGoogleCloudMessage($notify_data,$notify_ids);
}

function prepareNotificationOneClueOneNotice($clue_id, $notice_id)
{
	$con=mysqli_connect("localhost","root","1212312121","proj4d");
	mysqli_set_charset($con, "utf8");

	$notify_data = array();
	$notify_ids = array();
	$adder_notice = array();

	$query = "SELECT A.adder, A.name, B.token FROM notice_detail AS A, gcm_token_list AS B WHERE A.id = ? AND A.adder = B.username";
	$statement = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($statement, "s", $notice_id);
	$success = mysqli_stmt_execute($statement);

	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $adder, $lostName , $token);
	mysqli_stmt_fetch($statement);

	array_push($notify_ids , $token);
	$adder_notice["" . $adder] = "" . $notice_id . "," . $lostName;

	$notify_data["clue_id"] = $clue_id;
	$notify_data["notice_id_data"] = json_encode($adder_notice);

	mysqli_stmt_close($statement);
    
    mysqli_close($con);

	return sendGoogleCloudMessage($notify_data,$notify_ids);

}

function sendGoogleCloudMessage( $data, $ids )
{     
	//echo "Push\n";
    $apiKey = 'AIzaSyDOuSpNEoCGMB_5SXaA1Zj7Dtnzyyt6TPc';
    $url = 'https://gcm-http.googleapis.com/gcm/send';

    // Set GCM post variables (device IDs and push payload)     
    $post = array('registration_ids'  => $ids, 'data' => $data);

    // Set CURL request headers (authentication and type)       
    $headers = array('Authorization: key=' . $apiKey, 'Content-Type: application/json');

    // Initialize curl handle       
    $ch = curl_init();

    // Set URL to GCM endpoint      
    curl_setopt( $ch, CURLOPT_URL, $url );

    // Set request method to POST       
    curl_setopt( $ch, CURLOPT_POST, true );

    // Set our custom headers       
    curl_setopt( $ch, CURLOPT_HTTPHEADER, $headers );

    // Get the response back as string instead of printing it       
    curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );

    // Set JSON post data
    curl_setopt( $ch, CURLOPT_POSTFIELDS, json_encode( $post ) );

	// echo json_encode( $headers) . "\n";
    logging("dataGCM", json_encode( $post ));
 //    echo "\n";

    // Actually send the push   
    $result = curl_exec( $ch );

    // Error handling
    if ( curl_errno( $ch ) )
    {
        //echo 'GCM error: ' . curl_error( $ch );
        return 'GCM error: ' . curl_error( $ch );
    }

    // Close curl handle
    curl_close( $ch );

    // Debug GCM response       
    //echo $result;
    return $result;
}

?>