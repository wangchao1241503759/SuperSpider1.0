<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<body> 
<div>
	<object 
		id="BrowserControl" 
		TYPE="application/x-itst-activex"
		ALIGN="baseline" BORDER="0" 
		WIDTH="100%" HEIGHT="100%"
		clsid="{6CB7BF91-C158-41EE-A38E-A79F3708584A}"
		event_OnElementSelected="OnSelected">
	</object>
</div>
<div id="test">xxxxxxxxxxxx</div>
<input type='button' onclick='alert(browserInstalled());var xpath=BrowserControl.getXpath();alert(xpath)'; value='Click'/>
</body> 
</html>

<script>
var str = "";
	function OnSelected(xpath)
	{ 
		parent.append(xpath)
		str += xpath + "<br/>";
		document.getElementById("test").innerHTML=str;
		alert(xpath);
	}

	function browserInstalled()
	{
		var result = true;
		try
		{		
			BrowserControl.getXpath();
		}
		catch(e){ alert(e.message); result = false;}

		return result;
	}
	
</script>
