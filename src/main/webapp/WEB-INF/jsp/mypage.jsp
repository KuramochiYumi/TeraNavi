<%@ page
   contentType="text/html ; charset=UTF-8"
   pageEncoding="UTF-8"
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ja">
<head>

    <meta charset="UTF-8">

    <title>マイページ</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
</head>
<body>
    <%-- ヘッダー部分のHTMLを読み込み --%>
    <jsp:include page="/WEB-INF/jsp/header.jsp"/>

    <div class="container">
       <div class="row">
		   <img src="${sessionScope.loginUser.headerPath}" alt="" />

		   <img src="${sessionScope.loginUser.iconPath}" alt="" style="position:absolute;top:10%;left:20%;" /> ${sessionScope.loginUser.userName} さんのマイページです

		   <h2>自己紹介文</h2>
		   <textarea>${sessionScope.loginUser.profile}</textarea>

		   <a href="userSetting">プロフィールを編集</a>

           <div class="col-sm-3 col-xs-6">
               <a href="articlepost">記事を投稿</a><br><br>
               <a href="後でいれｔ">下書き</a><br><br>
               <a href="blogSetting">ブログ設定</a><br><br>
               <a href="commmy">コミュニティの管理</a><br><br>
               <a href="後でいれｔ">コメント管理</a><br><br>
               <a href="dmsend">DMの送信</a><br><br>
			   <a href="front/dmreceive">DMの受信</a><br><br>
               <a href="chatwrite">チャット送信</a><br><br>
               <form action="front/showchat" method="post">
                   チャット受信<input type="text" name="topicId">
               </form>
               <a href="後でいれｔ">退会</a><br><br>
               <a href="search">検索</a><br><br>

           </div>
           <div class="col-sm-3 col-xs-6">
               <a href="openBlog">ブログ開設</a><br><br>
               <a href="blogSetting">ブログ設定</a><br><br>
			   <a href="blogDelete">ブログ閉鎖</a><br><br>
               <a href="createcomm">コミュニティの作成</a><br><br>
               <a href="joincomm">コミュニティの参加</a><br><br>
           </div>



       </div><!--end row-->
    </div><!--end container-->
    <jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
