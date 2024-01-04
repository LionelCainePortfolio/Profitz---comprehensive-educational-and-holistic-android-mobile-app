<?php 
require_once 'Connection.php';

	
	//creating a query
	$stmt = $conn->prepare("SELECT id, title, original_title, short_desc, overview, tutorial, step1, step1_1, step1_2, step1_3, step1_4, step1_5, step1_6, step1_7, step2, step2_1, step2_2, step2_3, step2_4, step2_5, step2_6, step2_7, step3, step3_1, step3_2, step3_3, step3_4, step3_5, step3_6, step3_7, step4, step4_1, step4_2, step4_3, step4_4, step4_5, step4_6, step4_7, step5, step5_1, step5_2, step5_3, step5_4, step5_5, step5_6, step5_7, step6, step6_1, step6_2, step6_3, step6_4, step6_5, step6_6, step6_7, step7, step7_1, step7_2, step7_3, step7_4, step7_5, step7_6, step7_7, stepX, steps, popularity, xp, dones, promo_type, vote_average, vote_count, price, PriceType, difficult, original_language, how_long, release_date, poster_path, backdrop_path, likes, comments, reviews, adult, video, instruction_type, promo_rules_url, date_add FROM promocje");
	
	//executing the query 
	$stmt->execute();
	
	//binding results to the query 
	$stmt->bind_result($id, $title, $original_title, $short_desc, $overview, $tutorial, $step1, $step1_1, $step1_2, $step1_3, $step1_4, $step1_5, $step1_6, $step1_7, $step2, $step2_1, $step2_2, $step2_3, $step2_4, $step2_5, $step2_6, $step2_7, $step3, $step3_1, $step3_2, $step3_3, $step3_4, $step3_5, $step3_6, $step3_7, $step4, $step4_1, $step4_2, $step4_3, $step4_4, $step4_5, $step4_6, $step4_7, $step5, $step5_1, $step5_2, $step5_3, $step5_4, $step5_5, $step5_6, $step5_7, $step6, $step6_1, $step6_2, $step6_3, $step6_4, $step6_5, $step6_6, $step6_7, $step7, $step7_1, $step7_2, $step7_3, $step7_4, $step7_5, $step7_6, $step7_7, $stepX, $steps, $popularity, $xp, $dones, $promo_type, $vote_average, $vote_count, $price, $PriceType, $difficult, $original_language, $how_long, $release_date, $poster_path, $backdrop_path, $likes, $comments, $reviews, $adult, $video, $instruction_type, $promo_rules_url, $date_add);
	
	$products = array(); 
	
	//traversing through all the result 
	while($stmt->fetch()){
		$temp = array();
		$temp['id'] = $id; 
		$temp['title'] = $title; 
		$temp['original_title'] = $original_title;
		$temp['short_desc'] = $short_desc;
		$temp['overview'] = $overview; 
		$temp['tutorial'] = $tutorial;
		$temp['step1'] = $step1;
		$temp['step1_1'] = $step1_1;
		$temp['step1_2'] = $step1_2;
		$temp['step1_3'] = $step1_3;
		$temp['step1_4'] = $step1_4;
		$temp['step1_5'] = $step1_5;
		$temp['step1_6'] = $step1_6;
		$temp['step1_7'] = $step1_7;
		$temp['step2'] = $step2;
		$temp['step2_1'] = $step2_1;
		$temp['step2_2'] = $step2_2;
		$temp['step2_3'] = $step2_3;
		$temp['step2_4'] = $step2_4;
		$temp['step2_5'] = $step2_5;
		$temp['step2_6'] = $step2_6;
		$temp['step2_7'] = $step2_7;
		$temp['step3'] = $step3;
		$temp['step3_1'] = $step3_1;
		$temp['step3_2'] = $step3_2;
		$temp['step3_3'] = $step3_3;
		$temp['step3_4'] = $step3_4;
		$temp['step3_5'] = $step3_5;
		$temp['step3_6'] = $step3_6;
		$temp['step3_7'] = $step3_7;
		$temp['step4'] = $step4;
		$temp['step4_1'] = $step4_1;
		$temp['step4_2'] = $step4_2;
		$temp['step4_3'] = $step4_3;
		$temp['step4_4'] = $step4_4;
		$temp['step4_5'] = $step4_5;
		$temp['step4_6'] = $step4_6;
		$temp['step4_7'] = $step4_7;
		$temp['step5'] = $step5;
		$temp['step5_1'] = $step5_1;
		$temp['step5_2'] = $step5_2;
		$temp['step5_3'] = $step5_3;
		$temp['step5_4'] = $step5_4;
		$temp['step5_5'] = $step5_5;
		$temp['step5_6'] = $step5_6;
		$temp['step5_7'] = $step5_7;
		$temp['step6'] = $step6;
		$temp['step6_1'] = $step6_1;
		$temp['step6_2'] = $step6_2;
		$temp['step6_3'] = $step6_3;
		$temp['step6_4'] = $step6_4;
		$temp['step6_5'] = $step6_5;
		$temp['step6_6'] = $step6_6;
		$temp['step6_7'] = $step6_7;
		$temp['step7'] = $step7;
		$temp['step7_1'] = $step7_1;
		$temp['step7_2'] = $step7_2;
		$temp['step7_3'] = $step7_3;
		$temp['step7_4'] = $step7_4;
		$temp['step7_5'] = $step7_5;
		$temp['step7_6'] = $step7_6;
		$temp['step7_7'] = $step7_7;
		$temp['stepX'] = $stepX;
		$temp['steps'] = $steps;
		$temp['popularity'] = $popularity;
		$temp['xp'] = $xp;
		$temp['dones'] = $dones;
		$temp['promo_type'] = $promo_type;
		$temp['vote_average'] = $vote_average;
		$temp['vote_count'] = $vote_count;
		$temp['price'] = $price;
		$temp['PriceType'] = $PriceType;
		$temp['difficult'] = $difficult;
		$temp['original_language'] = $original_language; 
		$temp['how_long'] = $how_long;
		$temp['release_date'] = $release_date;
		$temp['poster_path'] = $poster_path;
		$temp['backdrop_path'] = $backdrop_path;
		$temp['likes'] = $likes;
		$temp['comments'] = $comments;
		$temp['reviews'] = $reviews;
		//$temp['genre_ids'] = $genre_ids;
		$temp['adult'] = $adult;
		$temp['video'] = $video;
		$temp['instruction_type'] = $instruction_type;
		$temp['promo_rules_url'] = $promo_rules_url;
		$tem['date_add'] = $date_add;

 		//$temp['image'] = $image; 
		array_push($products, $temp);
	}
	
	//displaying the result in json format 
	echo '{"page":1,"total_results":10000,"total_pages":500,"results":'.json_encode($products).'}';
	
	