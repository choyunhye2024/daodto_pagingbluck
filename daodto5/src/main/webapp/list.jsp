<%@page import="com.glassis5.Board"%> 
<%@page import="com.glassis5.Dto"%> 
<%@page import="java.util.ArrayList"%> 
<%@page import="com.glassis5.Dao"%> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

글번호, 제목, 작성자<hr> <!-- 글번호, 제목, 작성자를 보여줄 부분을 나타냄 -->

<% 

// 페이지 번호를 가져옴. 만약 페이지 번호가 없으면 1페이지로 설정
String pageNum = request.getParameter("page");
if(pageNum == null){
	pageNum = "1"; // 페이지 번호가 없으면 1페이지로 설정
}

// Dao라는 데이터베이스와 통신하는 객체를 생성
Dao dao = new Dao(); 
int totalPage = 0; // 총 페이지 수를 저장할 변수
ArrayList<Dto> posts = null; // 글 목록을 저장할 리스트
String searchWord = request.getParameter("word"); // 검색어를 가져옴

// 만약 검색어가 없다면 전체 글 목록을 불러오고, 검색어가 있으면 해당 검색어에 맞는 글만 가져옴
if(searchWord == null || searchWord.equals("null")){
	posts=dao.list(pageNum); // 전체 글 목록 가져오기
	totalPage = dao.getTotalPageCount(); // 총 페이지 수 계산
}else{
	posts = dao.listSearch(searchWord, pageNum); // 검색어에 맞는 글 목록 가져오기
	totalPage = dao.getSearchTotalPageCount(searchWord); // 검색된 결과의 총 페이지 수 계산
}

// 글 목록을 화면에 출력하는 부분
for (int i=0; i<posts.size(); i=i+1){
%>
<%=posts.get(i).no%> <!-- 글 번호 출력 -->
<a href = "read.jsp?no=<%=posts.get(i).no %>"><%=posts.get(i).title %></a> <!-- 글 제목을 클릭하면 해당 글로 이동 -->
<%=posts.get(i).id %> <!-- 글 작성자 출력 -->
<hr>
<%	
}

// 페이지네이션(다음 페이지, 이전 페이지 버튼 만들기)을 위한 변수들 설정
int nPageNum = Integer.parseInt(pageNum);
int totalBlock = (int)Math.ceil((double)totalPage / Board.PAGE_LINK_AMOUNT); // 총 페이지 블록 수 계산
int currentBlockNo = (int)Math.ceil((double)nPageNum / Board.PAGE_LINK_AMOUNT); // 현재 보고 있는 페이지 블록 번호
int blockStartNo = (currentBlockNo-1)*Board.PAGE_LINK_AMOUNT + 1; // 블록의 시작 페이지 번호
int blockEndNo = currentBlockNo * Board.PAGE_LINK_AMOUNT ; // 블록의 마지막 페이지 번호
if (blockEndNo > totalPage){
	blockEndNo = totalPage; // 마지막 페이지 번호가 총 페이지 수를 넘지 않도록 함
}

int prevPage = 0;
int nextPage = 0;
boolean hasPrev = true; // 이전 페이지가 있는지 확인
if(currentBlockNo == 1){
	hasPrev = false; // 현재 블록이 첫 블록이면 이전 페이지 없음
}else{
	hasPrev = true;
	prevPage = (currentBlockNo - 1)*Board.PAGE_LINK_AMOUNT; // 이전 페이지 번호 설정
}

boolean hasNext = true; // 다음 페이지가 있는지 확인
if (currentBlockNo<totalBlock){
	hasNext = true;
	nextPage = currentBlockNo * Board.PAGE_LINK_AMOUNT + 1; // 다음 페이지 번호 설정
}else {
	hasNext = false; // 마지막 블록이면 다음 페이지 없음
}

// 이전 블록으로 가는 링크 생성
if(hasPrev){
	if(searchWord == null){
%>
	<a href = "list.jsp?page=<%=prevPage %>">이전블럭가기</a> <!-- 검색어가 없을 때, 이전 페이지 링크 -->
<%		
	}else{
%>
    <a href = "list.jsp?page=<%=prevPage %>&word=<%=searchWord %>">이전블럭가기</a> <!-- 검색어가 있을 때, 이전 페이지 링크 -->
<% 		
	}
}

// 현재 블록의 각 페이지 번호를 링크로 보여주는 부분
for (int i = blockStartNo; i<= blockEndNo; i++){
	if (nPageNum == i){ 
%>
	-<%=i %>- <!-- 현재 보고 있는 페이지 번호 표시 -->
<%
	}else{
		if(searchWord == null){ 
%>
	<a href = "list.jsp?page=<%=i%>"><%=i %></a> <!-- 검색어가 없을 때, 페이지 번호 링크 -->
<%		
		}else{
			String urlEncodedSearchWord = java.net.URLEncoder.encode(searchWord); // 검색어가 있을 때, 검색어를 인코딩해서 링크에 포함
%>
		<a href = "list.jsp?page=<%=i %>&word=<%=urlEncodedSearchWord %>"><%=i %></a> <!-- 검색어가 있을 때, 페이지 번호 링크 -->
<%
		}
	}
}

// 다음 블록으로 가는 링크 생성
if(hasNext){
	if(searchWord == null){
		
%>
	<a href = "list.jsp?page=<%=nextPage %>">다음블럭으로</a> <!-- 검색어가 없을 때, 다음 페이지 링크 -->
<% 
	}else{
%>
		<a href = "list.jsp?page=<%=nextPage %>&word=<%=searchWord %>">다음블럭으로</a> <!-- 검색어가 있을 때, 다음 페이지 링크 -->
<%	
	}
}
%>

<!-- 검색창과 글 작성 버튼 -->
<form action = "list.jsp">
<input name = "word"> <!-- 검색어 입력칸 -->
<input type = "submit" value="검색"> <!-- 검색 버튼 -->
</form>
<a href = "write.jsp">쓰기</a> <!-- 글 작성하기 버튼 -->
<a href="list.jsp">리스트로</a> <!-- 글 목록으로 돌아가기 버튼 -->

</body>
</html>
