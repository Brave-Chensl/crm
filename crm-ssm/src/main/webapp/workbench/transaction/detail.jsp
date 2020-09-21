<%@ page import="java.util.List" %>
<%@ page import="com.chen.crm.settings.domain.DicValue" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page import="com.chen.crm.workbench.domain.Tran" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
	//需要准备数据字典中类型为stage的字典值列表
	List<DicValue> dicValueList = (List<DicValue>) application.getAttribute("stageList");
	//取出可能性和阶段的对照关系，
	Map<String,String> pMap= (Map<String, String>) application.getAttribute("pMap");
	//取出pMap中的key
	Set<String> set = pMap.keySet();
	//准备前面正常阶段和后面两个阶段的分界节点
	Integer point = null;//分界点
	for (int i = 0; i < dicValueList.size(); i++) {
		//取得每一个dicvalue对象（字典值）
		DicValue dicValue = dicValueList.get(i);
		//从字典对象中取value
		String value = dicValue.getValue();
		//取出可能性
		String possibility = pMap.get(value);
		//判断一下，如果可能性为0，则找到分界点
		if ("0".equals(possibility)){
			point = i;
			//结束循环
			break;
		}
	}
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />

<style type="text/css">
.mystage{
	font-size: 20px;
	vertical-align: middle;
	cursor: pointer;
}
.closingDate{
	font-size : 15px;
	cursor: pointer;
	vertical-align: middle;
}
</style>
	
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript">

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){
		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});
		
		$(".remarkDiv").mouseover(function(){
			$(this).children("div").children("div").show();
		});
		
		$(".remarkDiv").mouseout(function(){
			$(this).children("div").children("div").hide();
		});
		
		$(".myHref").mouseover(function(){
			$(this).children("span").css("color","red");
		});
		
		$(".myHref").mouseout(function(){
			$(this).children("span").css("color","#E6E6E6");
		});
		
		
		//阶段提示框
		$(".mystage").popover({
            trigger:'manual',
            placement : 'bottom',
            html: 'true',
            animation: false
        }).on("mouseenter", function () {
                    var _this = this;
                    $(this).popover("show");
                    $(this).siblings(".popover").on("mouseleave", function () {
                        $(_this).popover('hide');
                    });
                }).on("mouseleave", function () {
                    var _this = this;
                    setTimeout(function () {
                        if (!$(".popover:hover").length) {
                            $(_this).popover("hide")
                        }
                    }, 100);
                });


		selectTranHistoryList();

	});
	
     function selectTranHistoryList(){
     	$.ajax({
			url:"tran/selectTranHistoryList.do",
			type:"get",
			dataType:"json",
			data:{
				"id":"${tran.id}"
			},
			success:function (data) {
			var html="";
			//var list = data["tranList"]
				//alert(list)
			$.each(data.tranList,function (i,n) {
				html+='<tr>' +
						'<td>'+n.stage+'</td>' +
						'<td>'+n.money+'</td>' +
						'<td>'+data.s+'</td>' +
						'<td>'+n.expectedDate+'</td>' +
						'<td>'+n.createTime+'</td>' +
						'<td>'+n.createBy+'</td>' +
						'</tr>'
			})
				$("#tranHistoryTbody").html(html)
			}

		})
	 }


	 //改变阶段的方法
function changeStage(stage,i) {
	/*alert(stage)
	alert(i)*/
	$.ajax({
		url:"tran/changeStage.do",
		type:"post",
		dataType:"json",
		data:{
			"id":"${tran.id}",
			"stage":stage,
			"money":"${tran.money}",//生成历史需要
			"expectedDate":"${tran.expectedDate}"
		},
		success:function (data) {
			if (data.success){
			//修改成功后，刷新数据
			$("#possibility").html(data.possibility)
			$("#editTime").html(data.t.editTime)
			$("#editBy").html(data.t.editBy)
			$("#stage").html(data.t.stage)

				//修改图标，所有图标重新判断
				changeIcon(stage,i);
			}else {
				alert("修改失败")
			}
		}
	})
}
	//图标转换方法
	/**
	 *
	 * @param stage  当前阶段
	 * @param i  阶段下标
	 */
	function changeIcon(stage,i) {
		//当前可能性
		var possibility = $("#possibility").html();//因为是局部刷新所以不能从req域中取
		//前面正常阶段的分界点下标
		var index = "<%=point%>"
		//alert(index)

		//如果可能性为0，则前七个都是黑圈
		if (possibility=="0"){
			//遍历前七个
			for (var j=0;j<index;j++){
				//黑圈
				$("#"+j).removeClass()//移除旧样式
				$("#"+j).addClass("glyphicon glyphicon-record mystage")//添加新样式
				$("#"+j).css("color","#000000")

			}

			for (var j=index;j< <%=dicValueList.size()%>;j++){

				if (j==i){
					//红叉
					$("#"+j).removeClass()
					$("#"+j).addClass("glyphicon glyphicon-remove mystage")
					$("#"+j).css("color","#ff0000")
				}else {
					//黑叉
					$("#"+j).removeClass()
					$("#"+j).addClass("glyphicon glyphicon-remove mystage")
					$("#"+j).css("color","#000000")
				}

			}


		}else {

			//遍历前七个
			for (var j=0;j<index;j++){
				if (j==i){
					//绿标记
					$("#"+j).removeClass()
					$("#"+j).addClass("glyphicon glyphicon-map-marker mystage")
					$("#"+j).css("color","#00FF00")
				}
				else if (j>i){
					//黑圈
					$("#"+j).removeClass()//移除旧样式
					$("#"+j).addClass("glyphicon glyphicon-record mystage")//添加新样式
					$("#"+j).css("color","#000000")
				}
				else if (j<i){
					//绿圈
					$("#"+j).removeClass()
					$("#"+j).addClass("glyphicon glyphicon-ok-circle mystage")
					$("#"+j).css("color","#00FF00")
				}


			}

			//遍历后两个
			for (var j=index;j< <%=dicValueList.size()%>;j++){
				//黑叉
				$("#"+j).removeClass()
				$("#"+j).addClass("glyphicon glyphicon-remove mystage")
				$("#"+j).css("color","#000000")
			}


		}
	}
</script>

</head>
<body>
	
	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>${tran.customerId}-${tran.name} <small>￥${tran.money}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
			<button type="button" class="btn btn-default" onclick="window.location.href='edit.html';"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>

	<!-- 阶段状态 -->
	<div style="position: relative; left: 40px; top: -50px;">
		阶段&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<%
		//准备当前阶段
			Tran tran = (Tran) request.getAttribute("tran");
			String stage = tran.getStage();
			//准备当前可能性
			//String possibility = (String) request.getAttribute("s");
			String possibility = pMap.get(stage);
			//如果可能性为0
			if ("0".equals(possibility)){
				for (int i = 0; i <dicValueList.size() ; i++) {
					//取出每一个dv
					DicValue dicValue = dicValueList.get(i);
						//取得dv的Value
					String value = dicValue.getValue();//当前阶段
					//根据value取得可能性
					 String poss = pMap.get(value);
					 //判断是否可能性为0，如果是说明是后两个
					if ("0".equals(poss)){
						//判断是否是当前阶段
						if (stage.equals(value)){
							//-----红叉
			%>
			<span id="<%=i%>" onclick="changeStage('<%=value%>','<%=i%>')" class="glyphicon glyphicon-remove mystage"
			  data-toggle="popover" data-placement="bottom"
			  data-content="<%=dicValue.getText()%>" style="color: #ff0000;"></span>
		 -----------

		    <%
						}else {
							//----黑叉
					%>

	        	<span id="<%=i%>" onclick="changeStage('<%=value%>','<%=i%>')" class="glyphicon glyphicon-remove mystage"
			    data-toggle="popover" data-placement="bottom"
			    data-content="<%=dicValue.getText()%>" style="color: #000000;"></span>
		        -----------

					<%
						}
					}else {
						//可能性不为0------黑圈

			%>

		      <span id="<%=i%>" onclick="changeStage('<%=value%>','<%=i%>')" class="glyphicon glyphicon-record mystage"
			   data-toggle="popover" data-placement="bottom"
			   data-content="<%=dicValue.getText()%>" style="color: #000000;"></span>
		      -----------

           <%

					}
				}

			//可能性不为0,前七个可能是绿圈，也可能是绿色标记，黑圈，后两个一定是黑叉
			}else {
			//准备当前阶段的下标
				Integer index = null;
				for (int i = 0; i <dicValueList.size(); i++) {
					DicValue dv = dicValueList.get(i);
					String value =  dv.getValue();//阶段
//					String possibility2 = pMap.get(value);//可能性

					if (value.equals(stage)){
						index = i;
						break;
					}
				}

				for (int i = 0; i <dicValueList.size() ; i++) {
					DicValue dv = dicValueList.get(i);
					String value =  dv.getValue();//阶段
					String possibility2 = pMap.get(value);//可能性

					if ("0".equals(possibility2)){
						//---黑叉

		    %>

		<span id="<%=i%>" onclick="changeStage('<%=value%>','<%=i%>')" class="glyphicon glyphicon-remove mystage"
			  data-toggle="popover" data-placement="bottom"
			  data-content="<%=dv.getText()%>" style="color: #000000;"></span>
		      -----------

		    <%

					}else if (i==index){
							//绿色标记

		    %>

		<span id="<%=i%>" onclick="changeStage('<%=value%>','<%=i%>')" class="glyphicon glyphicon-map-marker mystage"
			  data-toggle="popover" data-placement="bottom"
			  data-content="<%=dv.getText()%>" style="color: #90F790;"></span>
		      -----------

		    <%

						}else if (i>index){
							//黑圈---
	         %>

		<span id="<%=i%>" onclick="changeStage('<%=value%>','<%=i%>')" class="glyphicon glyphicon-record mystage"
			  data-toggle="popover" data-placement="bottom"
			  data-content="<%=dv.getText()%>" style="color: #000000;"></span>
		-----------

		     <%
						}else {
							//绿勾


		%>

		<span id="<%=i%>" onclick="changeStage('<%=value%>','<%=i%>')" class="glyphicon glyphicon-ok-circle mystage"
			  data-toggle="popover" data-placement="bottom"
			  data-content="<%=dv.getText()%>" style="color: #90F790;"></span>
		      -----------

		<%

						}
					}

			}

		%>

		<%--<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="资质审查" style="color: #90F790;"></span>
		-----------
		<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="需求分析" style="color: #90F790;"></span>
		-----------
		<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="价值建议" style="color: #90F790;"></span>
		-----------
		<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="确定决策者" style="color: #90F790;"></span>
		-----------
		<span class="glyphicon glyphicon-map-marker mystage" data-toggle="popover" data-placement="bottom" data-content="提案/报价" style="color: #90F790;"></span>
		-----------
		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="谈判/复审"></span>
		-----------
		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="成交"></span>
		-----------
		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="丢失的线索"></span>
		-----------
		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="因竞争丢失关闭"></span>
		-------------%>
		<span class="closingDate">${tran.expectedDate}</span>
	</div>
	
	<!-- 详细信息 -->
	<div style="position: relative; top: 0px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">金额</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${tran.money}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.customerId}-${tran.name}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">预计成交日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${tran.expectedDate}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div> 
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">客户名称</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.customerId}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">阶段</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b id="stage">${tran.stage}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">类型</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.type} &nbsp;</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">可能性</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b id="possibility">${s}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">来源</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.source}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">市场活动源</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${tran.activityId}&nbsp;</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">联系人名称</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${tran.contactsId}</b></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 60px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${tran.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${tran.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 70px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b id="editBy">${tran.editBy}&nbsp;&nbsp;</b><small id="editTime" style=" font-size: 10px; color: gray;">${tran.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 80px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${tran.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 90px;">
			<div style="width: 300px; color: gray;">联系纪要</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					&nbsp;${tran.contactSummary}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 100px;">
			<div style="width: 300px; color: gray;">下次联系时间</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${tran.nextContactTime}&nbsp;</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div style="position: relative; top: 100px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>
		
		<!-- 备注1 -->
		<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>哎呦！</h5>
				<font color="gray">交易</font> <font color="gray">-</font> <b>动力节点-交易01</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
		
		<!-- 备注2 -->
		<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>呵呵！</h5>
				<font color="gray">交易</font> <font color="gray">-</font> <b>动力节点-交易01</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
		
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary">保存</button>
				</p>
			</form>
		</div>
	</div>
	
	<!-- 阶段历史 -->
	<div>
		<div style="position: relative; top: 100px; left: 40px;">
			<div class="page-header">
				<h4>阶段历史</h4>
			</div>
			<div style="position: relative;top: 0px;">
				<table id="activityTable" class="table table-hover" style="width: 900px;">
					<thead>
						<tr style="color: #B3B3B3;">
							<td>阶段</td>
							<td>金额</td>
							<td>可能性</td>
							<td>预计成交日期</td>
							<td>创建时间</td>
							<td>创建人</td>
						</tr>
					</thead>
					<tbody id="tranHistoryTbody">

						<%--<tr>
							<td>需求分析</td>
							<td>5,000</td>
							<td>20</td>
							<td>2017-02-07</td>
							<td>2016-10-20 10:10:10</td>
							<td>zhangsan</td>
						</tr>
						<tr>
							<td>谈判/复审</td>
							<td>5,000</td>
							<td>90</td>
							<td>2017-02-07</td>
							<td>2017-02-09 10:10:10</td>
							<td>zhangsan</td>
						</tr>--%>
					</tbody>
				</table>
			</div>
			
		</div>
	</div>
	
	<div style="height: 200px;"></div>
	
</body>
</html>