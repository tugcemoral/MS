<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Province / County AutoCompleter</title>
<script type="text/javascript" src="scripts/autocomplete.js">
	
</script>

</head>
<body onload="initRequest()">
	<center>
	<form id="autofillform" method="get">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>Enter Province Name:</td>
				<td width="175"><input type="text" autocomplete="off"
					id="complete-field" name="id" onkeyup="doCompletion();">
				</td>
			</tr>
			<tr>
				<td id="auto-row" colspan="2"><td />
			</tr>
			<tr>
				<td>Counties of Selected City: </td>
				<td width="175">
					<select id="counties" style="width: inherit">
					</select>
				</td>
			</tr>
		</table>
	</form>
	<div style="align: center; position: absolute;"
		id="menu-popup">
		<table id="completeTable"border="1" bordercolor="black" bgcolor="#99CCFF"
			cellpadding="0" cellspacing="0" width="175"></table>
	</div>
	</center>
</body>
</html>