<%@ page contentType="text/html;charset=utf-8" language="java" %>
<% String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet"/>

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js" charset="UTF-8"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js" charset="UTF-8"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js" charset="UTF-8"></script>
<script type="text/javascript">

	$(function(){

		//给按钮绑定点击事件
		$("#addBut").click(function () {

			//时间控件
			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-cn',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});


			//打开模态窗口
			//先获取后台数据


			$.ajax({
				url:"activity/find.do",
				type:"get",
				dataType:"json",
				success:function (data) {
					var html ="";
					$.each(data,function (i,n) {
						//$("#create-option").append("<option>"+n.name+"</option>")
						html+="<option value='"+n.id+"'>"+n.name+"</option>"
					})

					$("#create-owner").html(html)
					$("#createActivityModal").modal("show");

					$("#create-owner").val("${user.id}")
				}
			})

		})
		//给保存加点击事件
		$("#saveBut").click(function () {
			$.ajax({
				url:"activity/save.do",
				data:{
					"owner":$.trim($("#create-owner").val()),
					"name":$.trim($("#create-name").val()),
					"startDate":$.trim($("#create-startDate").val()),
					"endDate":$.trim($("#create-endDate").val()),
					"cost":$.trim($("#create-cost").val()),
					"description":$.trim($("#create-description").val())
				},
				type:"post",
				dataType: "json",
				success:function (data) {
				if (data){
					//添加成功
					//清空原有数据 转换为dom对象，使用reset(),
					$("#saveForm")[0].reset()
					$("#createActivityModal").modal("hide")

					//维持在第一页，维持每页展现的数据
					pageList(1,$("#activityPage").bs_pagination("getOption","rowsPerPage"))
				}else {
					//添加失败
					alert("添加失败")
				}
				}
			})


			})

		   pageList(1,2)


		//点击查询按钮时把数据保存起来
		 $("#selectBut").click(function () {

		 	$("#hidden-name").val($.trim($("#select-name").val()))
		 	$("#hidden-owner").val($.trim($("#select-owner").val()))
		 	$("#hidden-startDate").val($.trim($("#select-startData").val()))
		 	$("#hidden-endDate").val($.trim($("#select-endData").val()))

			pageList(1,2)

		})

		//为全选的复选框绑定事件
		$("#qx").click(function () {

		$("input[name=xz]").prop("checked",this.checked)

		})
		//为选择框绑定事件
		//因为选择框是动态生成的，需要使用on方法来触发事件
		//语法  $(需要绑定元素的有效外层元素).on(绑定事件的方式,需要绑定元素的jQuery对象,回调函数)
		$("#activity-Tbody").on("click",$("input[name=xz]"),function () {
			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);
		})

        //删除按钮绑定事件
        //获取被选中的id，
        $("#deleteBut").click(function () {
            var $xz = $("input[name=xz]:checked");
            if ($xz.length==0){
                alert("请选择要删除的记录")
            }else {
               if (confirm("确定删除选中的数据吗")){
				   //获取参数
				   var param="";

				   for (var i=0;i<$xz.length;i++){
					   param += "id="+$($xz[i]).val();
					   if (i<$xz.length-1){
						   param += "&";
					   }
				   }
				   //alert(param)
				   $.ajax({
					   url:"activity/delete.do",
					   data:param,
					   type:"post",
					   dataType:"json",
					   success:function (data) {
						   if (data){

							   //维持在第一页，维持每页展现的数据
							   pageList(1,$("#activityPage").bs_pagination("getOption","rowsPerPage"))
						   }else {
							   alert("删除失败！")
						   }

					   }
				   })
			   }
            }
        })

		//给修改按钮绑定事件
		$("#editBut").click(function () {

			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-cn',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});



			var $xz = $("input[name=xz]:checked")
			if ($xz.length==0){
				alert("请选择需要修改的记录！")
			}else if ($xz.length>1){
				alert("一次只能修改一条记录！")
			}else {
				//处理请求
				var id = $xz.val();
				//alert(id)
			$.ajax({
				url:"activity/edit.do",
				data:{
					"id":id
				},
				type:"get",
				dataType:"json",
				success:function (data) {
					var html = "";
					$.each(data.ulist,function (i,n) {
						html+="<option value='"+n.id+"'>"+n.name+"</option>"
					})
					$("#edit-owner").html(html)


					$("#edit-id").val(data.a.id)
					$("#edit-name").val(data.a.name)
					$("#edit-owner").val(data.a.owner)
					$("#edit-startDate").val(data.a.startDate)
					$("#edit-endDate").val(data.a.endDate)
					$("#edit-cost").val(data.a.cost)
					$("#edit-description").val(data.a.description)

					//打开模态窗口
					$("#editActivityModal").modal("show")
				}
			})
			}
		})
		//给更新按钮绑定事件
		$("#updateBut").click(function () {
			$.ajax({
				url:"activity/update.do",
				data:{
					"id":$.trim($("#edit-id").val()),
					"owner":$.trim($("#edit-owner").val()),
					"name":$.trim($("#edit-name").val()),
					"startDate":$.trim($("#edit-startDate").val()),
					"endDate":$.trim($("#edit-endDate").val()),
					"cost":$.trim($("#edit-cost").val()),
					"description":$.trim($("#edit-description").val())
				},
				type:"post",
				dataType: "json",
				success:function (data) {
					if (data){
						//修改成功
						//维持在当前页，维持每页展现的数据
						pageList($("#activityPage").bs_pagination("getOption","currentPage"),
						$("#activityPage").bs_pagination("getOption","rowsPerPage"))
						//关闭模态窗口
						$("#editActivityModal").modal("hide")
					}else {
						//添加失败
						alert("修改失败")
					}
				}
			})
		})
	});

	//查询方法
	function pageList(pageNo,pageSize) {

		$("#qx").prop("checked",false)
		//alert("pageList启动")
		//获取隐藏域中的对象
		$("#select-name").val($.trim($("#hidden-name").val()))
		$("#select-owner").val($.trim($("#hidden-owner").val()))
		$("#select-startDate").val($.trim($("#hidden-startData").val()))
		$("#select-endDate").val($.trim($("#hidden-endData").val()))

		$.ajax({
			url:"activity/pageList.do",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$("#select-name").val(),
				"owner":$("#select-owner").val(),
				"startData":$("#select-startData").val(),
				"endDate":$("#select-endData").val()
			},
			type:"get",
			dataType:"json",
			success:function (data) {
				var html = ""
				$.each(data.dataList,function (i,n) {

				html+="<tr class='active'></tr>"
				html+="<td><input name='xz' type='checkbox' value="+n.id+"></td>"
				html+='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>'
				html+="<td>"+n.owner+"</td>"
				html+="<td>"+n.startDate+"</td>"
				html+="<td>"+n.endDate+"</td>"
				})
				$("#activity-Tbody").html(html)

				//数据处理完毕后，结合分页插件，在前端使用
				var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}
		})
	}
	/*<tr class="active">
			<td><input type="checkbox" /></td>
			<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
	<td>zhangsan</td>
	<td>2020-10-10</td>
	<td>2020-10-20</td>
	</tr>*/
</script>
</head>
<body>

	<input type="hidden" id="hidden-name">
	<input type="hidden" id="hidden-owner">
	<input type="hidden" id="hidden-startDate">
	<input type="hidden" id="hidden-endDate">


	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="saveForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-option" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="saveBut" type="button" class="btn btn-primary" data-dismiss="modal">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						<input id="edit-id" type="hidden">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description">123</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBut">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="select-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="select-owner" />
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="select-startData" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="select-endData">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="selectBut">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBut"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBut"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBut"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activity-Tbody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage">

				</div>
		</div>
		
	</div>
    </div>
</body>
</html>