<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>

$(function(){
	//goFtpService();
	initTree();
});

function goFtpService()
{
	//window.location.href='ftp://infcn:infcn@192.168.3.112:21/';
	//window.open("ftp://infcn:infcn@192.168.3.112:21/", "_self");
	
	var ftpIP=$("#ftpIP").val();
	var ftpUserName=$("#ftpUserName").val();
	var ftpPassword=$("#ftpPassword").val();
	var ftpPort=$("#ftpPort").val();
	
	if(ftpIP!="")
	{
		var index= ftpIP.indexOf("ftp://");
		if(index!=-1)
		{
			ftpIP=ftpIP.substring(index+6);
		}
	}
	if(ftpUserName=="")
	{
		ftpUserName="anonymous";
	}
	var ftpurl = "ftp://"+ftpUserName+":"+ftpPassword+"@"+ftpIP+":"+ftpPort;
	//alert(ftpurl)
	$("#preview_url").attr("src",ftpurl);
}

//展开下一层节点时，获取下一层节点的数据
$("#tt").tree({'onBeforeExpand':function(node){
	//$(this).tree('options').url = '${pageContext.request.contextPath}/ftpController/getTree?id='+node.id
	var url = '${pageContext.request.contextPath}/ftpController/getTree?id='+node.id;
	$.ajax({
		url:url,
		type:'post',
		async:false,
		data:$("#configForm").serialize(),
		success:function(data, textStatus, jqXHR)
		{
			$('#tt').tree('append', {
				parent: node.target,
				data: data
			});
		}
	});

}})


//初始化树
function initTree()
{
	
	var url = '${pageContext.request.contextPath}/ftpController/getTree?id=/';
	$.ajax({
		url:url,
		type:'post',
		data:$("#configForm").serialize(),
		success:function(data, textStatus, jqXHR)
		{
			$("#tt").tree('loadData',data);
		}
	});
	
	
}

</script>
<!-- <iframe id="preview_url" src="" width="100%" height="100%"></iframe> -->
    <div class="easyui-panel" style="padding:5px">
        <ul id="tt" class="easyui-tree" data-options="animate:true,checkbox:true,cascadeCheck:false,fit:true,lines:true"></ul>
    </div>
