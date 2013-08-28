<%@page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.inc.jsp"%>
<html>
<head>
<%@ include file="/common/head.inc.jsp"%>
</head>
<meta http-equiv="Expires" content="0" /> 
<body>
<h2>Hello World!</h2>

<a href="IndexAction.action?method=usualRequest">Redirector Action</a>
<a id="ajaxLint" href="#">Ajax Request</a>
<div id="area" style="display: none; width: 200px; height: 100px; border: 1px red;"></div>
<script type="text/javascript" src="${webroot}/styles/js/jquery/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
$(function() {

$('#ajaxLint').click(function() {
	$.ajax({
		url:contextpath + '/IndexAction.action?method=ajaxRequest',
		data:{},
		type:'POST',
		dataType:'json',
		success:function(data) {
			alert(data.flag + ", " + data.msg);			
		}
	});	
});
	
});

</script>
</body>
</html>
