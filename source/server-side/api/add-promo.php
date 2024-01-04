<?php 
error_reporting(E_ALL);
ini_set('display_errors', '1');
include_once('config.php');
if(isset($_REQUEST['submit']) and $_REQUEST['submit']!=""){
	extract($_REQUEST);
	
		$userCount	=	$db->getQueryCount('promocje','id');
		if($userCount[0]['total']<1000){
			$data	=	array(
							'title'=>$title,
					'original_title'=>$orginal_title,
					'short_desc'=>$short_desc,
					'overview'=>$overview,
					 'price'=>$price,
					 'PriceType'=>$price_type,
                    'difficult'=>$difficult,
                    'how_long'=>$how_long,
                    'release_date'=>$release_date,
                    'promo_type'=>$promo_type,
					'step1'=>$step1,
					'step1_1'=>$step1_1,
					'step1_2'=>$step1_2,
					'step1_3'=>$step1_3,
					'step1_4'=>$step1_4,
					'step1_5'=>$step1_5,
					'step1_6'=>$step1_6,
					'step1_7'=>$step1_7,
					'step2'=>$step2,
					'step2_1'=>$step2_1,
					'step2_2'=>$step2_2,
					'step2_3'=>$step2_3,
					'step2_4'=>$step2_4,
					'step2_5'=>$step2_5,
					'step2_6'=>$step2_6,
					'step2_7'=>$step2_7,
					'step3'=>$step3,
					'step3_1'=>$step3_1,
					'step3_2'=>$step3_2,
					'step3_3'=>$step3_3,
					'step3_4'=>$step3_4,
					'step3_5'=>$step3_5,
					'step3_6'=>$step3_6,
					'step3_7'=>$step3_7,
					'step4'=>$step4,
					'step4_1'=>$step4_1,
					'step4_2'=>$step4_2,
					'step4_3'=>$step4_3,
					'step4_4'=>$step4_4,
					'step4_5'=>$step4_5,
					'step4_6'=>$step4_6,
					'step4_7'=>$step4_7,
					'step5'=>$step5,
					'step5_1'=>$step5_1,
					'step5_2'=>$step5_2,
					'step5_3'=>$step5_3,
					'step5_4'=>$step5_4,
					'step5_5'=>$step5_5,
					'step5_6'=>$step5_6,
					'step5_7'=>$step5_7,
					'step6'=>$step6,
					'step6_1'=>$step6_1,
					'step6_2'=>$step6_2,
					'step6_3'=>$step6_3,
					'step6_4'=>$step6_4,
					'step6_5'=>$step6_5,
					'step6_6'=>$step6_6,
					'step6_7'=>$step6_7,
					'step7'=>$step7,
					'step7_1'=>$step7_1,
					'step7_2'=>$step7_2,
					'step7_3'=>$step7_3,
					'step7_4'=>$step7_4,
					'step7_5'=>$step7_5,
					'step7_6'=>$step7_6,
					'step7_7'=>$step7_7,
					'stepX'=>$stepX,
						);
			$insert	=	$db->insert('promocje',$data);
			if($insert){
				header('location:browse-promos.php?msg=ras');
				exit;
			}else{
				header('location:browse-promos.php?msg=rna');
				exit;
			}
		}else{
			header('location:'.$_SERVER['PHP_SELF'].'?msg=dsd');
			exit;
		}
}
?>

<!doctype html>

<html lang="en-US" xmlns:fb="https://www.facebook.com/2008/fbml" xmlns:addthis="https://www.addthis.com/help/api-spec"  prefix="og: http://ogp.me/ns#" class="no-js">

<head>

	<meta charset="UTF-8">

	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

	<title>Promocje Zarabiaj Online - PANEL</title>

	

	<link rel="shortcut icon" href="https://demo.learncodeweb.com/favicon.ico">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css" integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">

    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.3/css/all.css" integrity="sha384-UHRtZLI+pbxtHCWp1t77Bi1L4ZtiqrqD80Kn4Z8NTSRyMA2Fd33n5dQ8lWUE00s/" crossorigin="anonymous">

	<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->

	<!--[if lt IE 9]>

	<script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>

	<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>

	<![endif]-->

	<script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>

	<script>

	  (adsbygoogle = window.adsbygoogle || []).push({

		google_ad_client: "ca-pub-6724419004010752",

		enable_page_level_ads: true

	  });

	</script>

	<!-- Global site tag (gtag.js) - Google Analytics -->

	<script async src="https://www.googletagmanager.com/gtag/js?id=UA-131906273-1"></script>
	<script src="https://cdn.tiny.cloud/1/2rmxirsk1bh4jqivmocnagomwq31j031pl4hysbypjxsfjjn/tinymce/5/tinymce.min.js" referrerpolicy="origin"></script>
	<script>

	  window.dataLayer = window.dataLayer || [];

	  function gtag(){dataLayer.push(arguments);}

	  gtag('js', new Date());

	  gtag('config', 'UA-131906273-1');

	</script>
	<style>
		.tox-tinymce-aux{
			display: none !important;
		}
		.tox-menubar{
			display: none !important;

		}
.pt-5, .py-5{
	    padding-top: 0rem!important;
}
.nav-pills-custom .nav-link {
    color: #aaa;
    background: #e0dede;
    position: relative;
}

.nav-pills-custom .nav-link.active {
    color: #e2b653;
    background: #e0dede;
}
.btn-primary{
	    background-color: #e2b653 !important;
    border-color: #e2b653 !important;
}
a{
	color: #e2b653;
}
.btn-danger {
    color: #fff !important;
    }

/* Add indicator arrow for the active tab */
@media (min-width: 992px) {
    .nav-pills-custom .nav-link::before {
        content: '';
        display: block;
        border-top: 8px solid transparent;
        border-left: 10px solid #fff;
        border-bottom: 8px solid transparent;
        position: absolute;
        top: 50%;
        right: -10px;
        transform: translateY(-50%);
        opacity: 0;
    }
}

.nav-pills-custom .nav-link.active::before {
    opacity: 1;
}

.mce-notification-inner {display:none!important;}



body {
    min-height: 100vh;
      background-color: #facc6b;
    background-image: linear-gradient(315deg, #facc6b 0%, #fabc3c 74%);
}

	</style>

</head>



<body >

	

	<div class="bg-light border-bottom shadow-sm sticky-top" style="display: none;">

		<div class="container">

			<header class="blog-header py-1">

				<nav class="navbar navbar-expand-lg navbar-light bg-light"> <a class="navbar-brand text-muted p-0 m-0" href="https://learncodeweb.com"><img src='https://learncodeweb.com/wp-content/uploads/2019/01/logo.png' alt='LearnCodeWeb'></a>

					<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation"> <span class="navbar-toggler-icon"></span> </button>

					<div class="collapse navbar-collapse" id="navbarSupportedContent">

						<ul class="navbar-nav mr-auto">

							<li itemscope="itemscope" itemtype="https://www.schema.org/SiteNavigationElement" id="menu-item-17" class="active nav-item"><a title="Home" href="https://learncodeweb.com/" class="nav-link">Home</a></li>

							<li itemscope="itemscope" itemtype="https://www.schema.org/SiteNavigationElement" id="menu-item-16" class="nav-item"><a title="Web Development" href="https://learncodeweb.com/learn/web-development/" class="nav-link">Web Development</a></li>

							<li itemscope="itemscope" itemtype="https://www.schema.org/SiteNavigationElement" id="menu-item-558" class="nav-item"><a title="PHP" href="https://learncodeweb.com/learn/php/" class="nav-link">PHP</a></li>

							<li itemscope="itemscope" itemtype="https://www.schema.org/SiteNavigationElement" id="menu-item-14" class="nav-item"><a title="Bootstrap" href="https://learncodeweb.com/learn/bootstrap-framework/" class="nav-link">Bootstrap</a></li>

							<li itemscope="itemscope" itemtype="https://www.schema.org/SiteNavigationElement" id="menu-item-559" class="nav-item"><a title="WordPress" href="https://learncodeweb.com/learn/wordpress/" class="nav-link">WordPress</a></li>

							<li itemscope="itemscope" itemtype="https://www.schema.org/SiteNavigationElement" id="menu-item-15" class="nav-item"><a title="Snippets" href="https://learncodeweb.com/learn/snippets/" class="nav-link">Snippets</a></li>

						</ul>

						<form method="get" action="https://learncodeweb.com" class="form-inline my-2 my-lg-0">

							<div class="input-group input-group-md">

								<input type="text" class="form-control search-width" name="s" id="search" value="" placeholder="Search..." aria-label="Search">

								<div class="input-group-append">

									<button type="submit" class="btn btn-primary" id="searchBtn"><i class="fa fa-search"></i></button>

								</div>

							</div>

						</form>

					</div>

				</nav>

			</header>

		</div> <!--/.container-->

	</div>

	<div class="container my-4">

		<script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>

		<!-- demo top banner -->

		<ins class="adsbygoogle"

			 style="display:block"

			 data-ad-client="ca-pub-6724419004010752"

			 data-ad-slot="6737619771"

			 data-ad-format="auto"

			 data-full-width-responsive="true"></ins>

		<script>

		(adsbygoogle = window.adsbygoogle || []).push({});

		</script>

	</div>

	

   	<div class="container">

		<h1 style="display: none;"><a href="https://learncodeweb.com/php/php-crud-in-bootstrap-4-with-search-functionality/">PHP CRUD in Bootstrap 4 with search functionality</a></h1>

		<?php

		if(isset($_REQUEST['msg']) and $_REQUEST['msg']=="un"){

			echo	'<div class="alert alert-danger"><i class="fa fa-exclamation-triangle"></i> User name is mandatory field!</div>';

		}elseif(isset($_REQUEST['msg']) and $_REQUEST['msg']=="ue"){

			echo	'<div class="alert alert-danger"><i class="fa fa-exclamation-triangle"></i> User email is mandatory field!</div>';

		}elseif(isset($_REQUEST['msg']) and $_REQUEST['msg']=="up"){

			echo	'<div class="alert alert-danger"><i class="fa fa-exclamation-triangle"></i> User phone is mandatory field!</div>';

		}elseif(isset($_REQUEST['msg']) and $_REQUEST['msg']=="ras"){

			echo	'<div class="alert alert-success"><i class="fa fa-thumbs-up"></i> Promocja została dodana pomyślnie!</div>';

		}elseif(isset($_REQUEST['msg']) and $_REQUEST['msg']=="rna"){

			echo	'<div class="alert alert-danger"><i class="fa fa-exclamation-triangle"></i> Promocja nie została dodana <strong>Spróbuj ponownie!</strong></div>';

		}elseif(isset($_REQUEST['msg']) and $_REQUEST['msg']=="dsd"){

			echo	'<div class="alert alert-danger"><i class="fa fa-exclamation-triangle"></i>r Usuń promocję z bazy danych i spróbuj ponownie!<strong>Jest dodany limit promocji!</strong></div>';

		}

		?>

		<div class="card">

			<div class="card-header"><i class="fa fa-fw fa-plus-circle"></i> <strong>Dodaj promocję</strong> <a href="browse-promos.php" class="float-right btn btn-dark btn-sm"><i class="fa fa-fw fa-globe"></i> Przeglądaj promocje</a></div>

			<div class="card-body">



				<div class="col-sm-12">

					<h5 class="card-title">Pola z <span class="text-danger">*</span> są wymagane.</h5>

				
<!-- Demo header-->
<section class="py-5 header">
	<form method="post" name = "myform">
    <div class="container py-4">
    
	 <div class="form-group">
							<input type="button" value="Wyczyść" id="submit" onclick="deleteAllCookies();">
	 					    <input type="button" value="Zapisz" id="submit" onclick="WriteCookie();">
							<input type="hidden" name="editId" id="editId" value="<?php echo $_REQUEST['editId']?>">
							<button type="submit" name="submit" value="submit" id="submit" style="float: right;" class="btn btn-primary"><i class="fa fa-fw fa-edit"></i> Dodaj</button>
						</div>
        <div class="row">


            <div class="col-md-4">
                <!-- Tabs nav -->
                <div class="nav flex-column nav-pills nav-pills-custom" id="v-pills-tab" role="tablist" aria-orientation="vertical">
                    <a class="nav-link mb-3 p-3 shadow active" id="v-pills-home-tab" data-toggle="pill" href="#v-pills-home" role="tab" aria-controls="v-pills-home" aria-selected="true">
                        <i class="fa fa-user-circle-o mr-2"></i>
                        <span class="font-weight-bold small text-uppercase">Podstawowe informacje</span></a>

                            <a class="nav-link mb-3 p-3 shadow" id="v-pills-krok1-tab" data-toggle="pill" href="#v-pills-krok1" role="tab" aria-controls="v-pills-krok1" aria-selected="false">
                        <i class="fa fa-check mr-2"></i>
                        <span class="font-weight-bold small text-uppercase">Krok 1</span></a>

                           <a class="nav-link mb-3 p-3 shadow" id="v-pills-krok2-tab" data-toggle="pill" href="#v-pills-krok2" role="tab" aria-controls="v-pills-krok2" aria-selected="false">
                        <i class="fa fa-check mr-2"></i>
                        <span class="font-weight-bold small text-uppercase">Krok 2</span></a>

                            <a class="nav-link mb-3 p-3 shadow" id="v-pills-krok3-tab" data-toggle="pill" href="#v-pills-krok3" role="tab" aria-controls="v-pills-krok3" aria-selected="false">
                        <i class="fa fa-check mr-2"></i>
                        <span class="font-weight-bold small text-uppercase">Krok 3</span></a>
                    
                            <a class="nav-link mb-3 p-3 shadow" id="v-pills-krok4-tab" data-toggle="pill" href="#v-pills-krok4" role="tab" aria-controls="v-pills-krok4" aria-selected="false">
                        <i class="fa fa-check mr-2"></i>
                        <span class="font-weight-bold small text-uppercase">Krok 4</span></a>
                   
                    <a class="nav-link mb-3 p-3 shadow" id="v-pills-krok5-tab" data-toggle="pill" href="#v-pills-krok5" role="tab" aria-controls="v-pills-krok5" aria-selected="false">
                        <i class="fa fa-check mr-2"></i>
                        <span class="font-weight-bold small text-uppercase">Krok 5</span></a>
                    <a class="nav-link mb-3 p-3 shadow" id="v-pills-krok6-tab" data-toggle="pill" href="#v-pills-krok6" role="tab" aria-controls="v-pills-krok6" aria-selected="false">
                        <i class="fa fa-check mr-2"></i>
                        <span class="font-weight-bold small text-uppercase">Krok 6</span></a>
                    <a class="nav-link mb-3 p-3 shadow" id="v-pills-krok7-tab" data-toggle="pill" href="#v-pills-krok7" role="tab" aria-controls="v-pills-krok7" aria-selected="false">
                        <i class="fa fa-check mr-2"></i>
                        <span class="font-weight-bold small text-uppercase">Krok 7</span></a>
                    <a class="nav-link mb-3 p-3 shadow" id="v-pills-krokx-tab" data-toggle="pill" href="#v-pills-krokx" role="tab" aria-controls="v-pills-krokx" aria-selected="false">
                        <i class="fa fa-check mr-2"></i>
                        <span class="font-weight-bold small text-uppercase">Krok X</span></a>
                    </div>
                     </div>

            <div class="col-md-8">
                <!-- Tabs content -->
                <div class="tab-content" id="v-pills-tabContent">
                    <div class="tab-pane fade shadow rounded bg-white show active p-5" id="v-pills-home" role="tabpanel" aria-labelledby="v-pills-home-tab">
                        <h4 class="font-italic mb-4">Podstawowe informacje</h4>
                        
                      	<div class="form-group">
							<label style="font-weight: bold;">Tytuł promocji <span class="text-danger">*</span></label>
							<input type="text" name="title" id="title" class="form-control" value="<?php echo $row[0]['title']; ?>" placeholder="Edytuj tytuł promocji" required>
							
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Nazwa promocji <span class="text-danger">*</span></label>
							<input type="text" name="orginal_title" id="orginal_title" class="form-control" value="<?php echo $row[0]['original_title']; ?>" placeholder="Edytuj nazwę promocji" required>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krótki opis promocji <span class="text-danger">*</span></label>
							<textarea name="short_desc" id="short_desc" class="form-control" value="<?php echo $row[0]['short_desc']; ?>" placeholder="Edytuj krótki opis promocji" required> </textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Opis promocji <span class="text-danger">*</span></label>
							<textarea name="overview" id="overview" class="form-control" value="<?php echo $row[0]['overview']; ?>" placeholder="Edytuj opis promocji" required> </textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Zarobek <span class="text-danger">*</span></label>
							<input type="text" name="price" id="price" class="form-control" value="<?php echo $row[0]['price']; ?>" placeholder="Edytuj zarobek promocji, tylko liczby" required>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Typ zarobku<span class="text-danger">*</span></label>
							<input type="text" name="price_type" id="price_type" class="form-control" value="<?php echo $row[0]['price_type']; ?>" placeholder="Edytuj typ zarobku, np. zł/euro" required>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Poziom trudności <span class="text-danger">*</span></label>
							<input type="text" name="difficult" id="difficult" class="form-control" value="<?php echo $row[0]['difficult']; ?>" placeholder="Edytuj poziom trudności promocji" required>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Ile zajmie <span class="text-danger">*</span></label>
							<input type="text" name="how_long" id="how_long" class="form-control" value="<?php echo $row[0]['how_long']; ?>" placeholder="Edytuj wartość ile zajmie promocja" required>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Data zakończenia <span class="text-danger">*</span></label>
							<input type="text" name="release_date" id="release_date" class="form-control" value="<?php echo $row[0]['release_date']; ?>" placeholder="Wzór: Rok-miesiąc-dzień 00 Przykład: 2020-06-06 00 " required>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Typ promocji <span class="text-danger">*</span></label>
							<input type="text" name="promo_type" id="promo_type" class="form-control" value="<?php echo $row[0]['promo_type']; ?>" placeholder="promocja/airdrop" required>
						</div>
                    </div>

                    	 
                    <div class="tab-pane fade shadow rounded bg-white p-5" id="v-pills-krok1" role="tabpanel" aria-labelledby="v-pills-krok1-tab">
                        <h4 class="font-italic mb-4">Krok 1</h4>
                    <div class="form-group">
							<label style="font-weight: bold;">Krok 1 <span class="text-danger">*</span></label>
							<textarea name="step1" id="step1" class="form-control" value="" placeholder="Edytuj krok 1 promocji"><?php echo $row[0]['step1']; ?></textarea>
				
						</div>
							
						<div class="form-group">
							<label style="font-weight: bold;">Krok 1 podpunkt 1 <span class="text-danger">*</span></label>
							<textarea name="step1_1" id="step1_1" class="form-control" value="" placeholder="Edytuj krok 1 podpunkt 1 promocji"><?php echo $row[0]['step1_1']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 1 podpunkt 2 <span class="text-danger">*</span></label>
							<textarea name="step1_2" id="step1_2" class="form-control" value="" placeholder="Edytuj krok 1 podpunkt 2 promocji"><?php echo $row[0]['step1_2']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 1 podpunkt 3 <span class="text-danger">*</span></label>
							<textarea name="step1_3" id="step1_3" class="form-control" value="" placeholder="Edytuj krok 1 podpunkt 3 promocji"><?php echo $row[0]['step1_3']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 1 podpunkt 4 <span class="text-danger">*</span></label>
							<textarea name="step1_4" id="step1_4" class="form-control" value="" placeholder="Edytuj krok 1 podpunkt 4 promocji"><?php echo $row[0]['step1_4']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 1 podpunkt 5 <span class="text-danger">*</span></label>
							<textarea name="step1_5" id="step1_5" class="form-control" value="" placeholder="Edytuj krok 1 podpunkt 5 promocji"><?php echo $row[0]['step1_5']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 1 podpunkt 6 <span class="text-danger">*</span></label>
							<textarea name="step1_6" id="step1_6" class="form-control" value="" placeholder="Edytuj krok 1 podpunkt 6 promocji"><?php echo $row[0]['step1_6']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 1 podpunkt 7 <span class="text-danger">*</span></label>
							<textarea name="step1_7" id="step1_7" class="form-control" value="" placeholder="Edytuj krok 1 podpunkt 7 promocji"><?php echo $row[0]['step1_7']; ?></textarea>
						</div>
                    </div>
                    
                    <div class="tab-pane fade shadow rounded bg-white p-5" id="v-pills-krok2" role="tabpanel" aria-labelledby="v-pills-krok2-tab">
                        <h4 class="font-italic mb-4">Krok 2</h4>
                    <div class="form-group">
							<label style="font-weight: bold;">Krok 2 <span class="text-danger">*</span></label>
							<textarea name="step2" id="step2" class="form-control" value="" placeholder="Edytuj krok 2 promocji"><?php echo $row[0]['step2']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 2 podpunkt 1 <span class="text-danger">*</span></label>
							<textarea name="step2_1" id="step2_1" class="form-control" value="" placeholder="Edytuj krok 2 podpunkt 1 promocji"><?php echo $row[0]['step2_1']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 2 podpunkt 2 <span class="text-danger">*</span></label>
							<textarea npuname="step2_2" id="step2_2" class="form-control" value="" placeholder="Edytuj krok 2 podpunkt 2 promocji"><?php echo $row[0]['step2_2']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 2 podpunkt 3 <span class="text-danger">*</span></label>
							<textarea name="step2_3" id="step2_3" class="form-control" value="" placeholder="Edytuj krok 2 podpunkt 3 promocji"><?php echo $row[0]['step2_3']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 2 podpunkt 4 <span class="text-danger">*</span></label>
							<textarea name="step2_4" id="step2_4" class="form-control" value="" placeholder="Edytuj krok 2 podpunkt 4 promocji"><?php echo $row[0]['step2_4']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 2 podpunkt 5 <span class="text-danger">*</span></label>
							<textarea name="step2_5" id="step2_5" class="form-control" value="" placeholder="Edytuj krok 2 podpunkt 5 promocji"><?php echo $row[0]['step2_5']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 2 podpunkt 6 <span class="text-danger">*</span></label>
							<textarea name="step2_6" id="step2_6" class="form-control" value="" placeholder="Edytuj krok 2 podpunkt 6 promocji"><?php echo $row[0]['step2_6']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 2 podpunkt 7 <span class="text-danger">*</span></label>
							<textarea name="step2_7" id="step2_7" class="form-control" value="" placeholder="Edytuj krok 2 podpunkt 7 promocji"><?php echo $row[0]['step2_7']; ?></textarea>
						</div>
                    </div>
                    
                <div class="tab-pane fade shadow rounded bg-white p-5" id="v-pills-krok3" role="tabpanel" aria-labelledby="v-pills-krok3-tab">
                        <h4 class="font-italic mb-4">Krok 3</h4>
                    <div class="form-group">
							<label style="font-weight: bold;">Krok 3 <span class="text-danger">*</span></label>
							<textarea name="step3" id="step3" class="form-control" value="" placeholder="Edytuj krok 3 promocji"><?php echo $row[0]['step3']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 3 podpunkt 1 <span class="text-danger">*</span></label>
							<textarea name="step3_1" id="step3_1" class="form-control" value="" placeholder="Edytuj krok 3 podpunkt 1 promocji"><?php echo $row[0]['step3_1']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 3 podpunkt 2 <span class="text-danger">*</span></label>
							<textarea name="step3_2" id="step3_2" class="form-control" value="" placeholder="Edytuj krok 3 podpunkt 2 promocji"><?php echo $row[0]['step3_2']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 3 podpunkt 3 <span class="text-danger">*</span></label>
							<textarea name="step3_3" id="step3_3" class="form-control" value="" placeholder="Edytuj krok 3 podpunkt 3 promocji"><?php echo $row[0]['step3_3']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 3 podpunkt 4 <span class="text-danger">*</span></label>
							<textarea name="step3_4" id="step3_4" class="form-control" value="" placeholder="Edytuj krok 3 podpunkt 4 promocji"><?php echo $row[0]['step3_4']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 4 podpunkt 5 <span class="text-danger">*</span></label>
							<textarea name="step3_5" id="step3_5" class="form-control" value="" placeholder="Edytuj krok 3 podpunkt 5 promocji"><?php echo $row[0]['step3_5']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 4 podpunkt 6 <span class="text-danger">*</span></label>
							<textarea name="step3_6" id="step3_6" class="form-control" value="" placeholder="Edytuj krok 3 podpunkt 6 promocji"><?php echo $row[0]['step3_6']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 3 podpunkt 7 <span class="text-danger">*</span></label>
							<textarea name="step3_7" id="step3_7" class="form-control" value="" placeholder="Edytuj krok 3 podpunkt 7 promocji"><?php echo $row[0]['step3_7']; ?></textarea>
						</div>
                    </div>
 <div class="tab-pane fade shadow rounded bg-white p-5" id="v-pills-krok4" role="tabpanel" aria-labelledby="v-pills-krok4-tab">
                        <h4 class="font-italic mb-4">Krok 4</h4>
                    <div class="form-group">
							<label style="font-weight: bold;">Krok 4 <span class="text-danger">*</span></label>
							<textarea name="step4" id="step4" class="form-control" value="" placeholder="Edytuj krok 4 promocji"><?php echo $row[0]['step4']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 4 podpunkt 1 <span class="text-danger">*</span></label>
							<textarea name="step4_1" id="step4_1" class="form-control" value="" placeholder="Edytuj krok 4 podpunkt 1 promocji"><?php echo $row[0]['step4_1']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 4 podpunkt 2 <span class="text-danger">*</span></label>
							<textarea name="step4_2" id="step4_2" class="form-control" value="" placeholder="Edytuj krok 4 podpunkt 2 promocji"><?php echo $row[0]['step4_2']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 4 podpunkt 3 <span class="text-danger">*</span></label>
							<textarea name="step4_3" id="step4_3" class="form-control" value="" placeholder="Edytuj krok 4 podpunkt 3 promocji"><?php echo $row[0]['step4_3']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 4 podpunkt 4 <span class="text-danger">*</span></label>
							<textarea name="step4_4" id="step4_4" class="form-control" value="" placeholder="Edytuj krok 4 podpunkt 4 promocji"><?php echo $row[0]['step4_4']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 4 podpunkt 5 <span class="text-danger">*</span></label>
							<textarea name="step4_5" id="step4_5" class="form-control" value="" placeholder="Edytuj krok 4 podpunkt 5 promocji"><?php echo $row[0]['step4_5']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 4 podpunkt 6 <span class="text-danger">*</span></label>
							<textarea name="step4_6" id="step4_6" class="form-control" value="" placeholder="Edytuj krok 4 podpunkt 6 promocji"><?php echo $row[0]['step4_6']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 4 podpunkt 7 <span class="text-danger">*</span></label>
							<textarea name="step4_7" id="step4_7" class="form-control" value="" placeholder="Edytuj krok 4 podpunkt 7 promocji"><?php echo $row[0]['step4_7']; ?></textarea>
						</div>
                    </div>
                     <div class="tab-pane fade shadow rounded bg-white p-5" id="v-pills-krok5" role="tabpanel" aria-labelledby="v-pills-krok5-tab">
                        <h4 class="font-italic mb-4">Krok 5</h4>
                         <div class="form-group">
							<label style="font-weight: bold;">Krok 5 <span class="text-danger">*</span></label>
							<textarea name="step5" id="step5" class="form-control" value="<?php echo $row[0]['step5']; ?>" placeholder="Edytuj krok 5 promocji"><?php echo $row[0]['step5']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 5 podpunkt 1 <span class="text-danger">*</span></label>
							<textarea name="step5_1" id="step5_1" class="form-control" value="" placeholder="Edytuj krok 5 podpunkt 1 promocji"><?php echo $row[0]['step5_1']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 5 podpunkt 2 <span class="text-danger">*</span></label>
							<textarea name="step5_2" id="step5_2" class="form-control" value="" placeholder="Edytuj krok 5 podpunkt 2 promocji"><?php echo $row[0]['step5_2']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 5 podpunkt 3 <span class="text-danger">*</span></label>
							<textarea name="step5_3" id="step5_3" class="form-control" value="" placeholder="Edytuj krok 5 podpunkt 3 promocji"><?php echo $row[0]['step5_3']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 5 podpunkt 4 <span class="text-danger">*</span></label>
							<textarea name="step5_4" id="step5_4" class="form-control" value="" placeholder="Edytuj krok 5 podpunkt 4 promocji"><?php echo $row[0]['step5_4']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 5 podpunkt 5 <span class="text-danger">*</span></label>
							<textarea name="step5_5" id="step5_5" class="form-control" value="" placeholder="Edytuj krok 5 podpunkt 5 promocji"><?php echo $row[0]['step5_5']; ?></textarea>
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 5 podpunkt 6 <span class="text-danger">*</span></label>
							<textarea name="step5_6" id="step5_6" class="form-control" value="" placeholder="Edytuj krok 5 podpunkt 6 promocji"><?php echo $row[0]['step5_6']; ?></textarea> 
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 5 podpunkt 7 <span class="text-danger">*</span></label>
							<textarea name="step5_7" id="step5_7" class="form-control" value="" placeholder="Edytuj krok 5 podpunkt 7 promocji"><?php echo $row[0]['step5_7']; ?></textarea> 
						</div>
                    </div>
                     <div class="tab-pane fade shadow rounded bg-white p-5" id="v-pills-krok6" role="tabpanel" aria-labelledby="v-pills-krok6-tab">
                        <h4 class="font-italic mb-4">Krok 6</h4>
                       <div class="form-group">
							<label style="font-weight: bold;">Krok 6 <span class="text-danger">*</span></label>
							<textarea name="step6" id="step6" class="form-control" value="" placeholder="Edytuj krok 6 promocji"><?php echo $row[0]['step6']; ?></textarea> 
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 6 podpunkt 1 <span class="text-danger">*</span></label>
							<textarea name="step6_1" id="step6_1" class="form-control" value="" placeholder="Edytuj krok 6 podpunkt 1 promocji"><?php echo $row[0]['step6_1']; ?></textarea> 
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 6 podpunkt 2 <span class="text-danger">*</span></label>
							<textarea name="step6_2" id="step6_2" class="form-control" value="" placeholder="Edytuj krok 6 podpunkt 2 promocji"><?php echo $row[0]['step6_2']; ?></textarea> 
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 6 podpunkt 3 <span class="text-danger">*</span></label>
							<textarea name="step6_3" id="step6_3" class="form-control" value="" placeholder="Edytuj krok 6 podpunkt 3 promocji"><?php echo $row[0]['step6_3']; ?></textarea> 
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 6 podpunkt 4 <span class="text-danger">*</span></label>
							<textarea name="step6_4" id="step6_4" class="form-control" value="" placeholder="Edytuj krok 6 podpunkt 4 promocji"><?php echo $row[0]['step6_4']; ?></textarea> 
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 6 podpunkt 5 <span class="text-danger">*</span></label>
							<textarea name="step6_5" id="step6_5" class="form-control" value="" placeholder="Edytuj krok 6 podpunkt 5 promocji"><?php echo $row[0]['step6_5']; ?></textarea> 
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 6 podpunkt 6 <span class="text-danger">*</span></label>
							<textarea name="step6_6" id="step6_6" class="form-control" value="" placeholder="Edytuj krok 6 podpunkt 6 promocji"><?php echo $row[0]['step6_6']; ?></textarea> 
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 6 podpunkt 7 <span class="text-danger">*</span></label>
							<textarea name="step6_7" id="step6_7" class="form-control" value="" placeholder="Edytuj krok 6 podpunkt 7 promocji"><?php echo $row[0]['step6_7']; ?></textarea> 
						</div>
                    </div>
                     <div class="tab-pane fade shadow rounded bg-white p-5" id="v-pills-krok7" role="tabpanel" aria-labelledby="v-pills-krok7-tab">
                        <h4 class="font-italic mb-4">Krok 7</h4>
                      <div class="form-group">
							<label style="font-weight: bold;">Krok 7 <span class="text-danger">*</span></label>
							<textarea name="step7" id="step7" class="form-control" value="" placeholder="Edytuj krok 7 promocji"><?php echo $row[0]['step7']; ?></textarea> 
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 7 podpunkt 1 <span class="text-danger">*</span></label>
							<textarea name="step7_1" id="step7_1" class="form-control" value="" placeholder="Edytuj krok 7 podpunkt 1 promocji"><?php echo $row[0]['step7_1']; ?></textarea> 
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 7 podpunkt 2 <span class="text-danger">*</span></label>
							<textarea name="step7_2" id="step7_2" class="form-control" value="" placeholder="Edytuj krok 7 podpunkt 2 promocji"><?php echo $row[0]['step7_2']; ?></textarea> 
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 7 podpunkt 3 <span class="text-danger">*</span></label>
							<textarea name="step7_3" id="step7_3" class="form-control" value="" placeholder="Edytuj krok 7 podpunkt 3 promocji"><?php echo $row[0]['step7_3']; ?></textarea> 
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 7 podpunkt 4 <span class="text-danger">*</span></label>
							<textarea name="step7_4" id="step7_4" class="form-control" value="" placeholder="Edytuj krok 7 podpunkt 4 promocji"><?php echo $row[0]['step7_4']; ?></textarea> 
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 7 podpunkt 5 <span class="text-danger">*</span></label>
							<textarea name="step7_5" id="step7_5" class="form-control" value="" placeholder="Edytuj krok 6 podpunkt 5 promocji"><?php echo $row[0]['step7_5']; ?></textarea> 
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 7 podpunkt 6 <span class="text-danger">*</span></label>
							<textarea name="step7_6" id="step7_6" class="form-control" value="" placeholder="Edytuj krok 7 podpunkt 6 promocji"><?php echo $row[0]['step7_6']; ?></textarea> 
						</div>
						<div class="form-group">
							<label style="font-weight: bold;">Krok 7 podpunkt 7 <span class="text-danger">*</span></label>
							<textarea name="step7_7" id="step7_7" class="form-control" value="" placeholder="Edytuj krok 7 podpunkt 7 promocji"><?php echo $row[0]['step7_7']; ?></textarea> 
						</div>
                    </div>
                        <div class="tab-pane fade shadow rounded bg-white p-5" id="v-pills-krokx" role="tabpanel" aria-labelledby="v-pills-krokx-tab">
                        <h4 class="font-italic mb-4">Krok X</h4>
                          <div class="form-group">
							<label style="font-weight: bold;">Krok X <span class="text-danger">*</span></label>
							<textarea name="stepX" id="stepX" class="form-control" value="" placeholder="Edytuj krok X promocji"><?php echo $row[0]['stepX']; ?></textarea> 
						</div>
                    </div>




                </div>
            </div>
        </div>
    </div>
</form>
</section>
				</div>

			</div>

		</div>

	</div>

    

	<div class="container my-4">

		<script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>

		<!-- demo left sidebar -->

		<ins class="adsbygoogle"

			 style="display:block"

			 data-ad-client="ca-pub-6724419004010752"

			 data-ad-slot="7706376079"

			 data-ad-format="auto"

			 data-full-width-responsive="true"></ins>

		<script>

		(adsbygoogle = window.adsbygoogle || []).push({});

		</script>

	</div>

	

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.6/umd/popper.min.js" integrity="sha384-wHAiFfRlMFy6i5SRaxvfOCifBUQy1xHdJ/yoi7FRNXMRBu5WHdZYu1hA6ZOblgut" crossorigin="anonymous"></script>

    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js" integrity="sha384-B0UglyR+jN6CkvvICOB2joaf5I4l3gm9GU6Hc1og6Ls7i6U/mkkaduKaBhlAXv9k" crossorigin="anonymous"></script>
	<script src="https://cdn.jsdelivr.net/jquery.caret/0.1/jquery.caret.js"></script>
	
 <script>
    tinymce.init({
      selector: 'textarea',
      forced_root_block : '',
force_br_newlines : true,
force_p_newlines : false
    });
  </script>
    
<script type="text/javascript">


//////////////
   function WriteCookie() {
            
            if( document.myform.title.value == "" ) {
				document.myform.title.value = "0";
				document.cookie = "title=" + title;
               }
            else{
               title = escape(document.myform.title.value) + ";";
               document.cookie = "title=" + title;
            }
            if( document.myform.original_title.value == "" ) {
				document.myform.original_title.value = "0";
				document.cookie = "original_title=" + original_title;
               }
            else{
                original_title= escape(document.myform.original_title.value) + ";";
                document.cookie = "original_title=" + original_title;
            }

            if( document.myform.short_desc.value == "" ) {
				document.myform.short_desc.value = "0";
				document.cookie = "short_desc=" + short_desc;
               }
            else{
				short_desc = escape(document.myform.short_desc.value) + ";";
				document.cookie = "short_desc=" + short_desc;
            }
              if( document.myform.overview.value == "" ) {
				document.myform.overview.value = "0";
               }
            else{
                overview = escape(document.myform.overview.value) + ";";
            }   
              if( document.myform.price.value == "" ) {
				document.myform.price.value = "0";
               }
            else{
                price = escape(document.myform.price.value) + ";";
            }   
                if( document.myform.price_type.value == "" ) {
				document.myform.price_type.value = "0";
               }
            else{
                PriceType = escape(document.myform.price_type.value) + ";";
            } 
                if( document.myform.difficult.value == "" ) {
				document.myform.difficult.value = "0";
               }
            else{
                difficult = escape(document.myform.difficult.value) + ";";
            } 
                if( document.myform.how_long.value == "" ) {
				document.myform.how_long.value = "0";
               }
            else{
                how_long = escape(document.myform.how_long.value) + ";";
            } 
               if(document.myform.release_date.value == "" ) {
				document.myform.release_date.value = "0";
               }
            else{
                release_date = escape(document.myform.release_date.value) + ";";
            }    
               if(document.myform.promo_type.value == "" ) {
				document.myform.promo_type.value = "0";
               }
            else{
				promo_type = escape(document.myform.promo_type.value) + ";";
            }      






                if(document.myform.step1.value == "" ) {
				document.myform.step1.value = "0";
               }
            else{
 				step1 = escape(document.myform.step1.value) + ";";
             }                    
            if(document.myform.step1_1.value == "" ) {
				document.myform.step1_1.value = "0";
               }
            else{
 				step1_1 = escape(document.myform.step1_1.value) + ";";
             }   
                      if(document.myform.step1_2.value == "" ) {
				document.myform.step1_2.value = "0";
               }
            else{
 				step1_2 = escape(document.myform.step1_2.value) + ";";
             }       

                      if(document.myform.step1_3.value == "" ) {
				document.myform.step1_3.value = "0";
               }
            else{
 				step1_3 = escape(document.myform.step1_3.value) + ";";
             } 
                                   if(document.myform.step1_4.value == "" ) {
				document.myform.step1_4.value = "0";
               }
            else{
 				step1_4 = escape(document.myform.step1_4.value) + ";";
             } 
                                   if(document.myform.step1_5.value == "" ) {
				document.myform.step1_5.value = "0";
               }
            else{
 				step1_5 = escape(document.myform.step1_5.value) + ";";
             } 
                                   if(document.myform.step1_6.value == "" ) {
				document.myform.step1_6.value = "0";
               }
            else{
 				step1_6 = escape(document.myform.step1_6.value) + ";";
             } 
                                   if(document.myform.step1_7.value == "" ) {
				document.myform.step1_7.value = "0";
               }
            else{
 				step1_7 = escape(document.myform.step1_7.value) + ";";
             } 
                          if(document.myform.step2.value == "" ) {
				document.myform.step2.value = "0";
               }
            else{
             	step2 = escape(document.myform.step2.value) + ";";
             } 

                        if(document.myform.step2_1.value == "" ) {
				document.myform.step2_1.value = "0";
               }
            else{
                step2_1 = escape(document.myform.step2_1.value) + ";";
             } 


                        if(document.myform.step2_2.value == "" ) {
				document.myform.step2_2.value = "0";
               }
            else{
                step2_2 = escape(document.myform.step2_2.value) + ";";
             }      
 
                        if(document.myform.step2_3.value == "" ) {
				document.myform.step2_3.value = "0";
               }
            else{
                step2_3 = escape(document.myform.step2_3.value) + ";";
             }      


                 if(document.myform.step2_4.val == "" ) {
				document.myform.step2_4.val = "0";
               }
            else{
                step2_4 = escape(document.myform.step2_4.value) + ";";
             }    


                 if(document.myform.step2_5.value == "" ) {
				document.myform.step2_5.value = "0";
               }
            else{
                step2_5 = escape(document.myform.step2_5.value) + ";";
             }    

                 if(document.myform.step2_6.value == "" ) {
				document.myform.step2_6.value = "0";
               }
            else{
                step2_6 = escape(document.myform.step2_6.value) + ";";
             }   

                 if(document.myform.step2_7.value == "" ) {
				document.myform.step2_7.value = "0";
               }
            else{
                step2_7 = escape(document.myform.step2_7.value) + ";";
             }  




                          if(document.myform.step3.value == "" ) {
				document.myform.step3.value = "0";
               }
            else{
             	step3 = escape(document.myform.step3.value) + ";";
             } 

                        if(document.myform.step3_1.value == "" ) {
				document.myform.step3_1.value = "0";
               }
            else{
                step3_1 = escape(document.myform.step3_1.value) + ";";
             } 
               if(document.myform.step3_2.value == "" ) {
				document.myform.step3_2.value = "0";
               }
            else{
                step3_2 = escape(document.myform.step3_2.value) + ";";
             } 
                        if(document.myform.step3_3.value == "" ) {
				document.myform.step3_3.value = "0";
               }
            else{
                step3_3 = escape(document.myform.step3_3.value) + ";";
             }      


                 if(document.myform.step3_4.val == "" ) {
				document.myform.step3_4.val = "0";
               }
            else{
                step3_4 = escape(document.myform.step3_4.value) + ";";
             }    


                 if(document.myform.step3_5.value == "" ) {
				document.myform.step3_5.value = "0";
               }
            else{
                step3_5 = escape(document.myform.step3_5.value) + ";";
             }    




                 if(document.myform.step3_6.value == "" ) {
				document.myform.step3_6.value = "0";
               }
            else{
                step3_6 = escape(document.myform.step3_6.value) + ";";
             }   

                 if(document.myform.step3_7.value == "" ) {
				document.myform.step3_7.value = "0";
               }
            else{
                step3_7 = escape(document.myform.step3_7.value) + ";";
             }  



                          if(document.myform.step4.value == "" ) {
				document.myform.step4.value = "0";
               }
            else{
             	step4 = escape(document.myform.step4.value) + ";";
             } 

                        if(document.myform.step4_1.value == "" ) {
				document.myform.step4_1.value = "0";
               }
            else{
                step4_1 = escape(document.myform.step4_1.value) + ";";
             } 
              if(document.myform.step4_2.value == "" ) {
				document.myform.step4_2.value = "0";
               }
            else{
                step4_2 = escape(document.myform.step4_2.value) + ";";
             }      

                        if(document.myform.step4_3.value == "" ) {
				document.myform.step4_3.value = "0";
               }
            else{
                step4_3 = escape(document.myform.step4_3.value) + ";";
             }      


                 if(document.myform.step4_4.val == "" ) {
				document.myform.step4_4.val = "0";
               }
            else{
                step4_4 = escape(document.myform.step4_4.value) + ";";
             }    


                 if(document.myform.step4_5.value == "" ) {
				document.myform.step4_5.value = "0";
               }
            else{
                step4_5 = escape(document.myform.step4_5.value) + ";";
             }    


                 if(document.myform.step4_6.value == "" ) {
				document.myform.step4_6.value = "0";
               }
            else{
                step4_6 = escape(document.myform.step4_6.value) + ";";
             }   

                 if(document.myform.step4_7.value == "" ) {
				document.myform.step4_7.value = "0";
               }
            else{
                step4_7 = escape(document.myform.step4_7.value) + ";";
             }  

                          if(document.myform.step5.value == "" ) {
				document.myform.step5.value = "0";
               }
            else{
             	step5 = escape(document.myform.step5.value) + ";";
             } 

                        if(document.myform.step5_1.value == "" ) {
				document.myform.step5_1.value = "0";
               }
            else{
                step5_1 = escape(document.myform.step5_1.value) + ";";
             } 
                if(document.myform.step5_2.value == "" ) {
				document.myform.step5_2.value = "0";
               }
            else{
                step5_2 = escape(document.myform.step5_2.value) + ";";
             }      

                        if(document.myform.step5_3.value == "" ) {
				document.myform.step5_3.value = "0";
               }
            else{
                step5_3 = escape(document.myform.step5_3.value) + ";";
             }      


                 if(document.myform.step5_4.val == "" ) {
				document.myform.step5_4.val = "0";
               }
            else{
                step5_4 = escape(document.myform.step5_4.value) + ";";
             }    


                 if(document.myform.step5_5.value == "" ) {
				document.myform.step5_5.value = "0";
               }
            else{
                step5_5 = escape(document.myform.step5_5.value) + ";";
             }    

                 if(document.myform.step5_6.value == "" ) {
				document.myform.step5_6.value = "0";
               }
            else{
                step5_6 = escape(document.myform.step5_6.value) + ";";
             }   

                 if(document.myform.step5_7.value == "" ) {
				document.myform.step5_7.value = "0";
               }
            else{
                step5_7 = escape(document.myform.step5_7.value) + ";";
             }  
                
                          if(document.myform.step6.value == "" ) {
				document.myform.step6.value = "0";
               }
            else{
             	step6 = escape(document.myform.step6.value) + ";";
             } 

                        if(document.myform.step6_1.value == "" ) {
				document.myform.step6_1.value = "0";
               }
            else{
                step6_1 = escape(document.myform.step6_1.value) + ";";
             } 
 

                        if(document.myform.step6_2.value == "" ) {
				document.myform.step6_2.value = "0";
               }
            else{
                step6_2 = escape(document.myform.step6_2.value) + ";";
             } 
 
                        if(document.myform.step6_3.value == "" ) {
				document.myform.step6_3.value = "0";
               }
            else{
                step6_3 = escape(document.myform.step6_3.value) + ";";
             }      


                 if(document.myform.step6_4.val == "" ) {
				document.myform.step6_4.val = "0";
               }
            else{
                step6_4 = escape(document.myform.step6_4.value) + ";";
             }    


                 if(document.myform.step6_5.value == "" ) {
				document.myform.step6_5.value = "0";
               }
            else{
                step6_5 = escape(document.myform.step6_5.value) + ";";
             }    

                 if(document.myform.step6_6.value == "" ) {
				document.myform.step6_6.value = "0";
               }
            else{
                step6_6 = escape(document.myform.step6_6.value) + ";";
             }   

                 if(document.myform.step6_7.value == "" ) {
				document.myform.step6_7.value = "0";
               }
            else{
                step6_7 = escape(document.myform.step6_7.value) + ";";
             }  
                
                  if(document.myform.step7.value == "" ) {
				document.myform.step7.value = "0";
               }
            else{
             	step7= escape(document.myform.step7.value) + ";";
             } 

                        if(document.myform.step7_1.value == "" ) {
				document.myform.step7_1.value = "0";
               }
            else{
                step7_1 = escape(document.myform.step7_1.value) + ";";
             } 
             if(document.myform.step7_2.value == "" ) {
				document.myform.step7_2.value = "0";
               }
            else{
                step7_2 = escape(document.myform.step7_2.value) + ";";
             } 
                        if(document.myform.step7_3.value == "" ) {
				document.myform.step7_3.value = "0";
               }
            else{
                step7_3 = escape(document.myform.step7_3.value) + ";";
             }      


                 if(document.myform.step7_4.val == "" ) {
				document.myform.step7_4.val = "0";
               }
            else{
                step7_4 = escape(document.myform.step7_4.value) + ";";
             }    


                 if(document.myform.step7_5.value == "" ) {
				document.myform.step7_5.value = "0";
               }
            else{
                step7_5 = escape(document.myform.step7_5.value) + ";";
             }    

                 if(document.myform.step7_6.value == "" ) {
				document.myform.step7_6.value = "0";
               }
            else{
                step7_6 = escape(document.myform.step7_6.value) + ";";
             }   

                 if(document.myform.step7_7.value == "" ) {
				document.myform.step7_7.value = "0";
               }
            else{
                step7_7 = escape(document.myform.step_7.value) + ";";
             }  
                
   if(document.myform.stepX.value == "" ) {
				document.myform.stepX.value = "0";
               }
            else{
                stepX = escape(document.myform.stepX.value) + ";";
             }  

               
               
               document.cookie = "overview=" + overview;
               document.cookie = "price=" + price;
               document.cookie = "PriceType=" + price_type;
               document.cookie = "difficult=" + difficult;
               document.cookie = "how_long=" + how_long;
               document.cookie = "release_date=" + release_date;
               document.cookie = "promo_type=" + promo_type;

               document.cookie = "step1=" + step1;
               document.cookie = "step1_1=" + step1_1;
               document.cookie = "step1_2=" + step1_2;
               document.cookie = "step1_3=" + step1_3;
               document.cookie = "step1_4=" + step1_4;
               document.cookie = "step1_5=" + step1_5;
               document.cookie = "step1_6=" + step1_6;
               document.cookie = "step1_7=" + step1_7;

               document.cookie = "step2=" + step2;
               document.cookie = "step2_1=" + step2_1;
               document.cookie = "step2_2=" + step2_2;
               document.cookie = "step2_3=" + step2_3;
               document.cookie = "step2_4=" + step2_4;
               document.cookie = "step2_5=" + step2_5;
               document.cookie = "step2_6=" + step2_6;
               document.cookie = "step2_7=" + step2_7;

               document.cookie = "step3=" + step3;
               document.cookie = "step3_1=" + step3_1;
               document.cookie = "step3_2=" + step3_2;
               document.cookie = "step3_3=" + step3_3;
               document.cookie = "step3_4=" + step3_4;
               document.cookie = "step3_5=" + step3_5;
               document.cookie = "step3_6=" + step3_6;
               document.cookie = "step3_7=" + step3_7;

               document.cookie = "step4=" + step4;
               document.cookie = "step4_1=" + step4_1;
               document.cookie = "step4_2=" + step4_2;
               document.cookie = "step4_3=" + step4_3;
               document.cookie = "step4_4=" + step4_4;
               document.cookie = "step4_5=" + step4_5;
               document.cookie = "step4_6=" + step4_6;
               document.cookie = "step4_7=" + step4_7;

               document.cookie = "step5=" + step5;
               document.cookie = "step5_1=" + step5_1;
               document.cookie = "step5_2=" + step5_2;
               document.cookie = "step5_3=" + step5_3;
               document.cookie = "step5_4=" + step5_4;
               document.cookie = "step5_5=" + step5_5;
               document.cookie = "step5_6=" + step5_6;
               document.cookie = "step5_7=" + step5_7;

               document.cookie = "step6=" + step6;
               document.cookie = "step6_1=" + step6_1;
               document.cookie = "step6_2=" + step6_2;
               document.cookie = "step6_3=" + step6_3;
               document.cookie = "step6_4=" + step6_4;
               document.cookie = "step6_5=" + step6_5;
               document.cookie = "step6_6=" + step6_6;
               document.cookie = "step6_7=" + step6_7;

               document.cookie = "step7=" + step7;
               document.cookie = "step7_1=" + step7_1;
               document.cookie = "step7_2=" + step7_2;
               document.cookie = "step7_3=" + step7_3;
               document.cookie = "step7_4=" + step7_4;
               document.cookie = "step7_5=" + step7_5;
               document.cookie = "step7_6=" + step7_6;
               document.cookie = "step7_7=" + step7_7;




            }
////////////////////////


  function deleteAllCookies() {
    var cookies = document.cookie.split(";");

    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        var eqPos = cookie.indexOf("=");
        var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    }
}
</script>

</body>

</html>