
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <title>コミュニティ作成</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
    <script type="text/javascript" src="http://netdna.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <link href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="http://pingendo.github.io/pingendo-bootstrap/themes/default/bootstrap.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="/TeraNavi/js/fileup.js"></script>

</head>
<body>
    <%-- ヘッダー部分のHTMLを読み込み --%>
    <jsp:include page="/WEB-INF/jsp/header.jsp"/>


	<div class="section">
      <div class="container">
        <div class="row">
          <div class="col-xs-10 col-xs-offset-1">
			<form role="form" action="/TeraNavi/front/createcomm" method="post" id="commForm">
              <div class="form-group">
                <label class="control-label" for="name">コミュニティ名</label>
                <input class="form-control" id="name" name="commName" placeholder="例:情報処理科2年のコミュニティ">
              </div>
              <div class="form-group">
                <label class="control-label" for="profile">紹介文</label>
                <textarea class="form-control" id="profile" rows="4" name="commProfile" placeholder="例:情報処理科2年が入ることを推奨しますｗ"></textarea>
              </div>アイコン
              <div class="col-md-12 text-left">
                <img id="preIcon" src="" width="150px" height="150px">
              </div>
              <br>
              <input type='file' value="ファイル選択" id='iconFile' onchange="fileUpIcon();">
              <input id="icon" type="hidden" name="commIcon">
			  <br>
              <br>ヘッダ画像
              <br>
              <div class="col-md-12 text-left">
                <img id="preHeader" src="" width="450px" height="150px">
              </div>
              <input type='file' value="ファイル選択" id='headerFile' onchange="fileUpHeader();"><br>
              <br>
              <br>
			  <input id="commHeaderPath" type="hidden" name="commHeader">

			  <input type="hidden" name="userId" value="${sessionScope.loginUser.id}">
              <div class="text-center">
                <button type="button" class="btn btn-warning" id="csubmit"  data-toggle="modal" data-target="#myModal">コミュニティを作成</button>


				<!-- <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal">クリックするとモーダルウィンドウが開きます。</button> -->
                <!--モーダルがあったところ-->
              </div>
            </form>

            <!-- モーダルウィンドウの中身 -->
            <div class="fade modal text-justify" id="myModal">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                      <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title">確認画面</h4>
                  </div>
                  <div class="modal-body">
                      <div class="row">
                          <div class="col-xs-1"></div>
                          <div class="col-xs-10">


                            <h2>コミュニティ名</h2>
                            <h5 id="mTitle"></h5>
                            <h2>コミュニティ紹介文</h2>
                            <h5 id="mProfile"></h5>
                            <h2>アイコン画像</h2>
                            <img src="" class="img-thumbnail" width="110" height="110" id="mIcon">
                            <h2>ヘッダー画像</h2>
                            <img src="" class="img-rounded" width="480" height="150" id="mHeader">
                        </div>
                        <div class="col-md-1"></div>
                    </div>
                  </div>
                  <div class="modal-footer">
					  <button type="submit" class="btn btn-block btn-primary" form="commForm">OK</button>
					  <button type="submit" class="btn btn-block btn-primary" data-dismiss="modal">閉じる</button>
                  </div>
                </div>
              </div>
            </div>
            <!-- モーダル閉じるやつだよ -->

          </div>
        </div>
      </div>
    </div>


    <jsp:include page="/WEB-INF/jsp/footer.jsp"/>
	<script>
		var ajaxSettings;
		var ajax;
		$(function(){

			ajaxSettings = {
				type:'post',
				url:'/TeraNavi/upload',
				processData:false,
				contentType:false,
				cache:false,
				dataType:'json'
			}

			$("#csubmit").on("click",function(){
				$("#mTitle").empty();
				$("#mProfile").empty();


				$("#mHeader").attr("src",$("#commHeaderPath").val());
				$("#mTitle").append($("#name").val());
				$("#mProfile").append($("#profile").val());
				$("#mIcon").attr("src",$("#icon").val());

			});

		});

		function fileUpIcon(){
			var files = document.getElementById("iconFile").files;

			for(var i = 0;i < files.length;i++){
				console.log("for");
				var f = files[i];
				var formData = new FormData();
				formData.append("file",f);
				ajaxSettings.data = formData;
				ajaxSettings.url = "/TeraNavi/upload";
				ajaxSettings.success = function(data){
					$("#icon").val(data.result);
					$("#preIcon").attr("src",data.result);
				}

 				ajax = $.ajax(ajaxSettings);
			}

		}

		function fileUpHeader(){
			var files = document.getElementById("headerFile").files;

			for(var i = 0;i < files.length;i++){
				console.log("for");
				var f = files[i];
				var formData = new FormData();
				formData.append("file",f);
				ajaxSettings.data = formData;
				ajaxSettings.url = "/TeraNavi/upload/header";
				ajaxSettings.success = function(data){
					$("#commHeaderPath").val(data.result);
					$("#preHeader").attr("src",data.result);
				}

 				ajax = $.ajax(ajaxSettings);
			}
		}



	</script>
</body>
</html>
