$(function($) {
	/** 全局变量*/
	var page=1;
	var pageSize=10;
	var session = $.session();
	var orginTimeStr = "";
	var holidayModelTimeUnit = "0";
	//方法：初始化区域树
	var initHolidayAreaTree = function(){
		//areaId为-1加载全部
		var areaTreeData = $.callSyn("AreaServ","getAreaJsonTree",{areaId: session["areaId"]});
		$('#holidayAreaTree').tree({
			data : areaTreeData,
			onClick:function(node)
			{
				funBtn($('#btnAddHolidayModel'),funAddHolidayModel,true);
				funBtn($('#btnAddHolidaySystem'),funAddHolidaySystem,true);
				funQryHolidayModel();//点击区域时加载节假日模板
				funQryHolidaySystem();//点击区域时加载节假日列表
			},
			onContextMenu: function (e, node) {
                e.preventDefault();
            }
		});
	};

	//easyui-linkbutton 点击问题(disable无法去除点击事件)
	var funBtn = function(btn, fun, enable){
		if(enable){
			btn.unbind('click').bind('click',fun);//每次绑定前先取消上次的绑定
			btn.linkbutton('enable');//激活
		}else{
			btn.unbind('click',fun);
			btn.linkbutton('disable');//失效
		}
	};
	
	//界面初始化------start--------------
	initHolidayAreaTree();
	$('#holidayAreaTree').tree("select",$("#holidayAreaTree").tree("getRoot").target);
	//打开页面立即加载节假日模板列表
	//界面初始化------end--------------
	
	//初始化节假日模板列表
	$('#holidayModelTable').datagrid({
		//height: 250,
		rownumbers:true,//显示带有行号的列
		singleSelect:true,//则只允许选中一行
		autoRowHeight:false,//是否设置基于该行内容的行高度
		pagination:true,//
		pageSize:pageSize,//初始化分页大小
	    idField:'holidayName',
	    fitColumns:true,
	    toolbar: '#holidayModelTb',
	    columns:[[
			{title:'主键',field:'id',hidden:true},
			{title:'状态',field:'state',hidden:true},
			{title:'备注',field:'comments',hidden:true},
			{title:'名称',field:'holidayName',width:180},
			{title:'节假日规则',field:'holidayRule',width:200,
				formatter:function(value,row,index){
					var timeUnit = row.timeUnit;
					var timeObject = row.holidayRule.split(" ");
					if(timeUnit==0){//将日期换成日月
						//06.06-11.18 01:01-02:02 02:03-03:02
						var monthObject= timeObject[0].split("-");//06.06-11.18
						var startMonth = ""+new Number( monthObject[0].split(".")[0] );
						var startDay = ""+new Number( monthObject[0].split(".")[1] );
						var endMonth = ""+new Number( monthObject[1].split(".")[0] );
						var endDay = ""+new Number( monthObject[1].split(".")[1] );
						var retHolidayRule = "";
						//alert(typeof startMonth);
						//alert(typeof startDay);
						switch(startMonth){
							case "1":retHolidayRule+="1月";
							break;
							case "2":retHolidayRule+="2月";
							break;
							case "3":retHolidayRule+="3月";
							break;
							case "4":retHolidayRule+="4月";
							break;
							case "5":retHolidayRule+="5月";
							break;
							case "6":retHolidayRule+="6月";
							break;
							case "7":retHolidayRule+="7月";
							break;
							case "8":retHolidayRule+="8月";
							break;
							case "9":retHolidayRule+="9月";
							break;
							case "10":retHolidayRule+="10月";
							break;
							case "11":retHolidayRule+="11月";
							break;
							case "12":retHolidayRule+="12月";
							break;
						}
						switch(startDay){
							case "1":retHolidayRule+="1日";
							break;
							case "2":retHolidayRule+="2日";
							break;
							case "3":retHolidayRule+="3日";
							break;
							case "4":retHolidayRule+="4日";
							break;
							case "5":retHolidayRule+="5日";
							break;
							case "6":retHolidayRule+="6日";
							break;
							case "7":retHolidayRule+="7日";
							break;
							case "8":retHolidayRule+="8日";
							break;
							case "9":retHolidayRule+="9日";
							break;
							case "10":retHolidayRule+="10日";
							break;
							case "11":retHolidayRule+="11日";
							break;
							case "12":retHolidayRule+="12日";
							break;
							case "12":retHolidayRule+="12日";
							break;
							case "13":retHolidayRule+="13月";
							break;
							case "14":retHolidayRule+="14日";
							break;
							case "15":retHolidayRule+="15日";
							break;
							case "16":retHolidayRule+="16日";
							break;
							case "17":retHolidayRule+="17日";
							break;
							case "18":retHolidayRule+="18日";
							break;
							case "19":retHolidayRule+="19日";
							break;
							case "20":retHolidayRule+="20日";
							break;
							case "21":retHolidayRule+="21日";
							break;
							case "22":retHolidayRule+="22日";
							break;
							case "23":retHolidayRule+="23日";
							break;
							case "24":retHolidayRule+="24日";
							break;
							case "25":retHolidayRule+="25日";
							break;
							case "26":retHolidayRule+="26日";
							break;
							case "27":retHolidayRule+="27日";
							break;
							case "28":retHolidayRule+="28日";
							break;
							case "29":retHolidayRule+="29日";
							break;
							case "30":retHolidayRule+="30日";
							break;
							case "31":retHolidayRule+="31日";
							break;
						}
						retHolidayRule += "-";
						switch(endMonth){
							case "1":retHolidayRule+="1月";
							break;
							case "2":retHolidayRule+="2月";
							break;
							case "3":retHolidayRule+="3月";
							break;
							case "4":retHolidayRule+="4月";
							break;
							case "5":retHolidayRule+="5月";
							break;
							case "6":retHolidayRule+="6月";
							break;
							case "7":retHolidayRule+="7月";
							break;
							case "8":retHolidayRule+="8月";
							break;
							case "9":retHolidayRule+="9月";
							break;
							case "10":retHolidayRule+="10月";
							break;
							case "11":retHolidayRule+="11月";
							break;
							case "12":retHolidayRule+="12月";
							break;
						}
						switch(endDay){
							case "1":retHolidayRule+="1日";
							break;
							case "2":retHolidayRule+="2日";
							break;
							case "3":retHolidayRule+="3日";
							break;
							case "4":retHolidayRule+="4日";
							break;
							case "5":retHolidayRule+="5日";
							break;
							case "6":retHolidayRule+="6日";
							break;
							case "7":retHolidayRule+="7日";
							break;
							case "8":retHolidayRule+="8日";
							break;
							case "9":retHolidayRule+="9日";
							break;
							case "10":retHolidayRule+="10日";
							break;
							case "11":retHolidayRule+="11日";
							break;
							case "12":retHolidayRule+="12日";
							break;
							case "12":retHolidayRule+="12日";
							break;
							case "13":retHolidayRule+="13月";
							break;
							case "14":retHolidayRule+="14日";
							break;
							case "15":retHolidayRule+="15日";
							break;
							case "16":retHolidayRule+="16日";
							break;
							case "17":retHolidayRule+="17日";
							break;
							case "18":retHolidayRule+="18日";
							break;
							case "19":retHolidayRule+="19日";
							break;
							case "20":retHolidayRule+="20日";
							break;
							case "21":retHolidayRule+="21日";
							break;
							case "22":retHolidayRule+="22日";
							break;
							case "23":retHolidayRule+="23日";
							break;
							case "24":retHolidayRule+="24日";
							break;
							case "25":retHolidayRule+="25日";
							break;
							case "26":retHolidayRule+="26日";
							break;
							case "27":retHolidayRule+="27日";
							break;
							case "28":retHolidayRule+="28日";
							break;
							case "29":retHolidayRule+="29日";
							break;
							case "30":retHolidayRule+="30日";
							break;
							case "31":retHolidayRule+="31日";
							break;
						}
						return retHolidayRule;
					}else{//日期换成周
						//6-7
						var weekObject= timeObject[0].split("-");
						var beginWeek = ""+new Number( weekObject[0] );
						var endWeek = ""+new Number( weekObject[1] );
						var retHolidayRule = "";
						switch(beginWeek){
							case "1":retHolidayRule+="周一";
							break;
							case "2":retHolidayRule+="周二";
							break;
							case "3":retHolidayRule+="周三";
							break;
							case "4":retHolidayRule+="周四";
							break;
							case "5":retHolidayRule+="周五";
							break;
							case "6":retHolidayRule+="周六";
							break;
							case "7":retHolidayRule+="周日";
							break;
						}
						retHolidayRule += "-";
						switch(endWeek){
							case "1":retHolidayRule+="周一";
							break;
							case "2":retHolidayRule+="周二";
							break;
							case "3":retHolidayRule+="周三";
							break;
							case "4":retHolidayRule+="周四";
							break;
							case "5":retHolidayRule+="周五";
							break;
							case "6":retHolidayRule+="周六";
							break;
							case "7":retHolidayRule+="周日";
							break;
						}
						return retHolidayRule;
					}	
			}},
			{title:'类型',field:'timeUnit',width:200,
				formatter:function(value,row,index){
					var hrule =row.holidayRule;
					if(hrule.split(" ").length>1){
						return '部分休息';
					}else{
						return '休息';
					}
				}	
			},
			{title:'生效时间',field:'effDate',width:200},
			{title:'失效时间',field:'expDate',width:200},
/*	    rowStyler: function(index,row){
			if (row.state=='10P'){//失效的记录
				return 'color:#99CC99;'; 
			}
		},*/
			]],
		onClickRow: function(index,row){//点击模板列表中的记录时，激活上方菜单按钮
			funBtn($('#btnAddHolidayModel'),funAddHolidayModel,true);//新增按钮
			funBtn($('#btnModHolidayModel'),funModHolidayModel,true);//修改按钮
			funBtn($('#btnDelHolidayModel'),funDelHolidayModel,true);//删除按钮
			modType = false;
			//holidayRule = $('#holidayModelTable').datagrid('getSelected').holidayRule;
			//是否加载下方表格数据？loadDataForm();
		}
	});
	
	
	//初始化节假日列表
	$('#holidaySystemTable').datagrid({
		//height: 250,
		rownumbers:true,//显示带有行号的列
		singleSelect:true,//则只允许选中一行
		autoRowHeight:false,//是否设置基于该行内容的行高度
		pagination:true,//
		pageSize:pageSize,//初始化分页大小
	    idField:'holidaySystemName',
	    fitColumns:true,
	    toolbar: '#holidaySystemTb',
	    columns:[[
			{title:'主键',field:'id',hidden:true},
			{title:'状态',field:'state',hidden:true},
			{title:'备注',field:'comments',hidden:true},
			{title:'名称',field:'holidaySystemName',width:180},
			{title:'类别',field:'operType',width:200,
				formatter:function(value,row,index){
					if(value=='1'){
						return '工作日';
					}else{
						return '休息日';
					}
				}	
			},
			{title:'开始日期',field:'beginDate',width:200},
			{title:'结束日期',field:'endDate',width:200},
/*	    rowStyler: function(index,row){
			if (row.state=='10P'){//失效的记录
				return 'color:#99CC99;'; 
			}
		},*/
			]],
		onClickRow: function(index,row){//点击模板列表中的记录时，激活上方菜单按钮
			funBtn($('#btnAddHolidaySystem'),funAddHolidaySystem,true);//新增按钮
			funBtn($('#btnModHolidaySystem'),funModHolidaySystem,true);//修改按钮
			funBtn($('#btnDelHolidaySystem'),funDelHolidaySystem,true);//删除按钮
			modType = false;
		}
	});
	//初始化工作时间列表
	$('#timeDetailGrid').datagrid({
		//singleSelect:true,
		autoRowHeight:false,
	   	fitColumns:true,
	   	toolbar:'#timeDetailTb',
	    columns:[[
	    	{title:'上班时间',field:'startTime',width:150},
			{title:'下班时间',field:'endTime',width:150},
	    ]]
	});
	
	//工作时间增改窗口
	$('#dlg').dialog({
		title: '工作时间段',
	    width: 600,
	    height:400,
	    closed: true,
	    cache: false,
	    modal: true
	    
	});
//----------------------------------开始加载数据----------------------------------------
	//查询节假日模板
	function funQryHolidayModel(){
		var areaId = $('#holidayAreaTree').tree('getSelected').id;
		var holidayModelData = $.callSyn("holidayServ","qryHolidayModelByAreaId",{areaId: areaId});
		if(holidayModelData.total>0){
			$('#holidayModelTable').datagrid('loadData',holidayModelData.rows);
		}else{
			$('#holidayModelTable').datagrid('loadData',{total: 0, rows:[]});//清空
		}
	
	}
	
	//节假日模板列表的分页处理
	var pager=$('#holidayModelTable').datagrid('getPager');
	pager.pagination({
		showPageList: false,
		total:0,//初始化展示分页数据
		onSelectPage:function(pageNumber, pageSize){
			funQry(pageNumber);
			pager.pagination('refresh',{
				pageNumber:pageNumber,
				pageSize:pageSize
			});
		}
	});
	
	//查询节假日holidaySystem
	function funQryHolidaySystem(){
		var areaId = $('#holidayAreaTree').tree('getSelected').id;
		var holidaySystemData = $.callSyn("holidayServ","qryHolidaySystemsByArea",{areaId: areaId});
		if(holidaySystemData.total>0){
			$('#holidaySystemTable').datagrid('loadData',holidaySystemData.rows);
		}else{
			$('#holidaySystemTable').datagrid('loadData',{total: 0, rows:[]});//清空
		}
	
	}
	
	//节假日列表的分页处理
	var pager=$('#holidaySystemTable').datagrid('getPager');
	pager.pagination({
		showPageList: false,
		total:0,//初始化展示分页数据
		onSelectPage:function(pageNumber, pageSize){
			funQry(pageNumber);
			pager.pagination('refresh',{
				pageNumber:pageNumber,
				pageSize:pageSize
			});
		}
	});
	
//----------------------------节假日模板holidayModel begin--------------------------------
	//节假日模板列表add&mod窗口
	$('#holidayModelDlg').dialog({
		/*position: 'absolute',
		top: 100,
		left: 300,*/
		title: '节假日模板',
	    width: 600,
//	    height: 250,
	    closed: true,
	    cache: false,
	    modal: true,
	    shadow:false
	});
	$('#holidaySystemDlg').dialog({
		/*position: 'absolute',
		top: 100,
		left: 300,*/
		title: '节假日模板',
	    width: 600,
//	    height: 250,
	    closed: true,
	    cache: false,
	    modal: true,
	    shadow:false
	});
	//时间单位默认与切换
/*	$('#timeUnitWeek').hide();
	$('#timeUnitMonth').show();
	$('#holidayMdWorkTimeRule').textbox('enable');*/
	var timeUnitFun1 = function(){
    	$('#timeUnitWeek').hide();
		$('#timeUnitMonth').show();
		$('#holidayMdWorkTimeRule').textbox('enable');
		$('#holidayMdWorkTimeRule').textbox({
			readonly:true,
			required:true,
			disable:false,
			icons: [{
				iconCls:'icon-search',
				handler: function(e){
					$('#timeDetailWin').dialog('open');
				}
			}]
		});
		$('a.textbox-icon.icon-search.textbox-icon-disabled').removeClass("textbox-icon-disabled");
    
	};
	var timeUnitFun2 = function(){
    	$('#timeUnitMonth').hide();
    	$('#timeUnitWeek').show();
    	//$('#holidayMdWorkTimeRule').css("display", "none");
    	$('#holidayMdWorkTimeRule').textbox('disable');
		//$("#holidayMdWorkTimeRule").toggle();
    	$('#holidayMdWorkTimeRule').textbox({
    		readonly:true,
    		required:false
    	});
    	//----------
	};
	timeUnitFun1();
	$("input:radio[name='holidayModelTimeUnit']").on('click',function(){
		holidayModelTimeUnit = $("input:radio[name='holidayModelTimeUnit']:checked").val();
	    if(holidayModelTimeUnit == 0){
	    	timeUnitFun1();
	    }else{
	    	timeUnitFun2();
	    }
	});

	
	//加载时间段
	$('#holidayMdWorkTimeRule').textbox({
		readonly:true,
		required:true,
		icons: [{
			iconCls:'icon-search',
			handler: function(e){
				$('#timeDetailWin').dialog('open');
			}
		}]
	});
	$('a.textbox-icon.icon-search.textbox-icon-disabled').removeClass("textbox-icon-disabled");//使添加Tip有效
	$('#timeDetailWin').dialog({
		onBeforeOpen:function(){//初始化之前的选择
			var timeStr = $('#holidayMdWorkTimeRule').val(); //"08:30-11:30 13:30-17:30 18:30-21:30";
			if(timeStr == ""){
				return;
			}
			var timeStrArr = timeStr.split(" ");
			var dataArr = new Array();
			for(var i=0;i<timeStrArr.length;i++){
				var timeArr =  timeStrArr[i].split("-");	
				var data = new Object();
				data.startTime = timeArr[0];
				data.endTime = timeArr[1];
				dataArr.push(data);
			}  	    
			$('#timeDetailGrid').datagrid('loadData',dataArr);
			$('#timeDetailGrid').datagrid('selectRow',0);
		},
		onOpen:function(){
			$('#timeDetailWin').dialog('resize',{height:350});
			$("#timeDetailWin").panel("move",{top:$(document).scrollTop() + ($(window).height()-350) * 0.5,left:$(document).scrollLeft() + ($(window).width()-350) * 0.5});
		}
	});
	
	//添加工作时间段
	$('#btnAddTd').click(function(e){
		$('#dlg').dialog({
			title: '增加工作时间',
			buttons:[{
				text:'确定',
				handler:function(){
					if($('#timeDetailInfoForm').form('validate')){
						var recObj = new Object();	
						
						recObj.startHour = $('#startHour').val();
						recObj.startMinute = $('#startMinute').val();
						recObj.endHour = $('#endHour').val();
						recObj.endMinute = $('#endMinute').val();
						
						recObj.startHour=	new Number(recObj.startHour) > 9?  new Number(recObj.startHour)+"":
						                  "0" + new Number(recObj.startHour);
						recObj.startMinute=	new Number(recObj.startMinute) > 9?  new Number(recObj.startMinute)+"":
						                  "0" + new Number(recObj.startMinute);
						recObj.endHour=	new Number(recObj.endHour) > 9?  new Number(recObj.endHour)+"":
						                  "0" + new Number(recObj.endHour);
						recObj.endMinute=	new Number(recObj.endMinute) > 9?  new Number(recObj.endMinute)+"":
						                  "0" + new Number(recObj.endMinute);
						if(!validatorTime(recObj)){
							$.messager.alert("提示","时间格式不对");
							return;
						}
						if(!checkTime(recObj)){
							$.messager.alert("提示","下班时间必须大于上班时间");
							return;
						}
						if(!isTimeNotConflict(recObj)){
							$.messager.alert("提示","输入时间段和已有时间段冲突");
							return;
						}
						var timeObject = new Object();
						timeObject.startTime = recObj.startHour+":"+ recObj.startMinute;
						timeObject.endTime = recObj.endHour+":"+ recObj.endMinute;
						//读取原有的所有时间段
						var preTimeStr = getAllTimeStr();
						//拼接新增的时间段
						var addNewTimeStr = timeObject.startTime +"-"+  timeObject.endTime;
						//合成新的时间段
						var totalTimeStr ;
						if(preTimeStr.length==0){
							totalTimeStr = addNewTimeStr;
						}else{
							totalTimeStr = preTimeStr +" "+ addNewTimeStr;
						}
						//展示
						var timeStrArr = totalTimeStr.split(" ");
						var dataArr = new Array();
						for(var i=0;i<timeStrArr.length;i++){
							var timeArr =  timeStrArr[i].split("-");	
							var data = new Object();
							data.startTime = timeArr[0];
							data.endTime = timeArr[1];
							dataArr.push(data);
						}  	    
						$('#timeDetailGrid').datagrid('loadData',dataArr);
						var lastIndex = $('#timeDetailGrid').datagrid('getRows').length - 1;
						$('#timeDetailGrid').datagrid('selectRow',lastIndex);
						$('#dlg').dialog('close');
					}
				}
			},{
				text:'取消',
				handler:function(){
						$('#dlg').dialog('close');
					}
				}],
			onBeforeOpen:function(){
				orginTimeStr = getAllTimeStr();
			},
			onOpen:function(){
				$('#dlg').dialog('resize',{height:200});
				$("#dlg").panel("move",{top:75,left:400});
			
			}
		});
		$('#dlg').dialog('open');
	});
	
	//修改
	$('#btnModTd').click(function(e){
		var selectedRows = $("#timeDetailGrid").datagrid("getSelections");
		if(selectedRows.length>1){
			$.messager.alert("提示","不能同时编辑多行！");
			return;
		}
		$('#dlg').dialog({
			title: '修改工作时间',
			buttons:[{
				text:'确定',
				handler:function(){
					if($('#timeDetailInfoForm').form('validate')){
						var recObj = new Object();	
						
						recObj.startHour = $('#startHour').val();
						recObj.startMinute = $('#startMinute').val();
						recObj.endHour = $('#endHour').val();
						recObj.endMinute = $('#endMinute').val();
						
						recObj.startHour=	new Number(recObj.startHour) > 9?  new Number(recObj.startHour)+"":
						                  "0" + new Number(recObj.startHour);
						recObj.startMinute=	new Number(recObj.startMinute) > 9?  new Number(recObj.startMinute)+"":
						                  "0" + new Number(recObj.startMinute);
						recObj.endHour=	new Number(recObj.endHour) > 9?  new Number(recObj.endHour)+"":
						                  "0" + new Number(recObj.endHour);
						recObj.endMinute=	new Number(recObj.endMinute) > 9?  new Number(recObj.endMinute)+"":
						                  "0" + new Number(recObj.endMinute);
						if(!validatorTime(recObj)){
							$.messager.alert("提示","时间格式不对");
							return;
						}
						if(!checkTime(recObj)){
							$.messager.alert("提示","下班时间必须大于上班时间");
							return;
						}
						if(!isTimeNotConflict(recObj)){
							$.messager.alert("提示","输入时间段和已有时间段冲突");
							return;
						}
						var timeObject = new Object();
						timeObject.startTime = recObj.startHour+":"+ recObj.startMinute;
						timeObject.endTime = recObj.endHour+":"+ recObj.endMinute;
						//得到编辑前的数组
						var timeStrArr = orginTimeStr.split(" ");//编辑前的时间字符串
						//得到当前编辑的行号
						var _rowIndex = $('#timeDetailGrid').datagrid('getRowIndex',$('#timeDetailGrid').datagrid('getSelected'));
						//替换数组中对应的行数组
						timeStrArr[_rowIndex] = timeObject.startTime+"-"+timeObject.endTime;
						//重新显示
						var dataArr = new Array();
						for(var i=0;i<timeStrArr.length;i++){
							var timeArr =  timeStrArr[i].split("-");	
							var data = new Object();
							data.startTime = timeArr[0];
							data.endTime = timeArr[1];
							dataArr.push(data);
						}  
						$('#timeDetailGrid').datagrid('loadData',dataArr);
						$('#timeDetailGrid').datagrid('selectRow',_rowIndex);
						$('#dlg').dialog('close');
						
					}
				}
			},{
				text:'取消',
				handler:function(){
						$('#dlg').dialog('close');
					}
				}],
				onBeforeOpen:function(){
					orginTimeStr = getTimeExcludeSelf();
					if($('#timeDetailGrid').datagrid('getSelected')){
						var sTime = $('#timeDetailGrid').datagrid('getSelected');
						var startTime =  sTime.startTime.split(":");
					    var endTime = sTime.endTime.split(":");
					    $('#startHour').textbox('setValue',startTime[0]);
					    $('#startMinute').textbox('setValue',startTime[1]);
					    $('#endHour').textbox('setValue',endTime[0]);
					    $('#endMinute').textbox('setValue',endTime[1]);
					}
					
					
					
					
					
				},
			onOpen:function(){
				$('#dlg').dialog('resize',{height:200});
				$("#dlg").panel("move",{top:75,left:400});
			}
		});
		$('#dlg').dialog('open');
	});
	//删除
	$('#btnDelTd').click(function(e){
		var selectedRows = $("#timeDetailGrid").datagrid("getSelections");
		for(var i=0; i<selectedRows.length; i++){
			var _rowIndex = $('#timeDetailGrid').datagrid('getRowIndex',selectedRows[i]);
			$('#timeDetailGrid').datagrid('deleteRow',_rowIndex);
		}
	});
	
	//确定选择工作时间
	$('#timeDetailWin-confirmBtn').click(function(e){
		var timeStr = getAllTimeStr();
		$('#holidayMdWorkTimeRule').textbox('setValue',timeStr);
		$('#timeDetailWin').dialog('close');
	});
	

	//新增节假日模板
	var funAddHolidayModel = function(){
		timeUnitFun1();
		$('#holidayModelDlg').dialog({
			title: '新增节假日模板',
			buttons:[{
				text:'确定',
				handler:function(){
					if($('#holidayModelForm').form('validate')){
						var areaId = $('#holidayAreaTree').tree('getSelected').id;
						if(areaId == null || areaId ==""){
							$.messager.alert("提示","请选择区域！");
							return;
						}
						var effDate = $('#holidayModelEffDate').datetimebox('getValue');
						var expDate = $('#holidayModelExpDate').datetimebox('getValue');
						var currentDate = new Date();
						var cYear = currentDate.getFullYear();
						var cMonth = currentDate.getMonth()+ 1 < 10 ? "0" + (currentDate.getMonth() + 1) : currentDate.getMonth();
						var cDate = currentDate.getDate()< 10 ? "0" + currentDate.getDate() : currentDate.getDate();
						var cunnentDateNum = ""+cYear+cMonth+cDate;
						var _tempeffDate;
						var _tempeffDateNum;
						var _tempexpDate;
						var _tempexpDateNum;
						
						if(effDate!=''){
							_tempeffDate = StringToDate(effDate);
							var efYear = _tempeffDate.getFullYear();
							var efMonth = _tempeffDate.getMonth()+ 1 < 10 ? "0" + (_tempeffDate.getMonth() + 1) : _tempeffDate.getMonth();
							var efDate = _tempeffDate.getDate()< 10 ? "0" + _tempeffDate.getDate() : _tempeffDate.getDate();
							_tempeffDateNum = ""+efYear+efMonth+efDate;
						}
						
						if(expDate!=''){
							_tempexpDate = StringToDate(expDate);
							var exYear = _tempexpDate.getFullYear();
							var exMonth = _tempexpDate.getMonth()+ 1 < 10 ? "0" + (_tempexpDate.getMonth() + 1) : _tempexpDate.getMonth();
							var exDate = _tempexpDate.getDate()< 10 ? "0" + _tempexpDate.getDate() : _tempexpDate.getDate();
							_tempexpDateNum = ""+exYear+exMonth+exDate;
						}
						
						if(effDate!=''){
							if( _tempeffDateNum < cunnentDateNum ){
								$.messager.alert("提示","生效效时间不能晚于当前时间");
								return;
							}
						}
						if(expDate!=''){
							if( _tempexpDateNum < cunnentDateNum ){
								$.messager.alert("提示","失效时间必须晚于当前时间");
								return;
							}
						}
						
						if((effDate!='') && (expDate !='')){
							if(  _tempeffDateNum > _tempexpDateNum ){
								$.messager.alert("提示","生效时间必须早于失效时间");
								return;
							}
						}
						
						//根据时间单位来拼接节假日规则：holidayModelRule
						var tmpHolidayModelRule ="";
						if(holidayModelTimeUnit == 0){
							//按日月 05.01-05.01 09:30-13:30 16:00-20:00
							//收集时间元素
							var startMonth = $('#holidayModelStartMonth').val();
							var startDay = $('#holidayModelStartDay').val();
							var endMonth = $('#holidayModelEndMonth').val();
							var endDay = $('#holidayModelEndDay').val();
							var workTimeRule = $('#holidayMdWorkTimeRule').val().length==0?"":" "+$('#holidayMdWorkTimeRule').val();
							//补全两位长度
							startMonth = new Number( startMonth ) >9? new Number(startMonth)+"":
				                  "0" + new Number(startMonth);
							startDay = new Number( startDay ) >9? new Number(startDay)+"":
				                  "0" + new Number(startDay);
							endMonth = new Number( endMonth ) >9? new Number(endMonth)+"":
				                  "0" + new Number(endMonth);
							endDay = new Number( endDay ) >9? new Number(endDay)+"":
				                  "0" + new Number(endDay);
							tmpHolidayModelRule = startMonth +"."+startDay
							+"-"
							+endMonth+"."+endDay
							+workTimeRule;
						}else{
							//按周
							var startWeek = $('#holidayModelStartWeek').val();
							var endWeek = $('#holidayModelEndWeek').val();
							tmpHolidayModelRule = startWeek +"-"+ endWeek;
						}
						
						var params = {
							holidayModelName:$('#holidayModelName').val(),
							holidayModelRule:tmpHolidayModelRule,
							holidayModelTimeUnit:holidayModelTimeUnit,
							holidayModelState:$("input:radio[name='holidayModelState']:checked").val(),
							areaId:areaId,
							holidayModelEffDate:effDate,
							holidayModelExpDate:expDate,
							holidayModelComments:$('#holidayModelComments').val()
						};
						var ret = $.callSyn('holidayServ','addHolidayModel',params);
						if(ret.isSuccess){
							if(ret.isSuccess ==true){
								$.messager.alert("提示","新增节假日模板成功！");
								funQryHolidayModel();
								$('#holidayModelDlg').dialog('close');
							}else{
								$.messager.alert("提示","新增节假日模板失败！");
							}
						}else{
							$.messager.alert("提示","新增节假日模板异常，操作失败！");
						}
					}
				}
			},{
				text:'取消',
				handler:function(){
						$('#holidayModelDlg').dialog('close');
					}
				}],
			onBeforeOpen:function(){
				//清空所有值
				$('#holidayModelStartMonth').empty();
				$('#holidayModelStartMonth').append('<option value="0" selected>-月-</option>')
		        $('#holidayModelStartDay').empty();
				$('#holidayModelStartDay').append('<option value="0" selected>-日-</option>')
		        $('#holidayModelEndMonth').empty();
				$('#holidayModelEndMonth').append('<option value="0" selected>-月-</option>')
		        $('#holidayModelEndDay').empty();
				$('#holidayModelEndDay').append('<option value="0" selected>-日-</option>')
		        initMonth();
	        	
				$('#holidayModelName').textbox('setValue',"");
				$("input[name='holidayModelTimeUnit']").get(0).checked=true;
				$('holidayModelStartWeek').val("0");
				$('holidayModelEndWeek').val("0");
				$('holidayModelStartMonth').val("0");
				$('holidayModelStartDay').val("0");
				$('holidayModelEndMonth').val("0");
				$('holidayModelEndDay').val("0");
				$('#holidayModelEffDate').combo('setText','');
				$('#holidayModelExpDate').combo('setText','');
				$("input[name='holidayModelState']").get(0).checked=true;
				$('#holidayMdWorkTimeRule').textbox('setValue',"");
				$('#holidayModelComments').textbox('setValue',"");
			},
			onOpen:function(){
				$('#holidayModelDlg').dialog('resize',{height:350});
				$("#holidayModelDlg").panel("move",{top:$(document).scrollTop() + ($(window).height()-350) * 0.5,left:$(document).scrollLeft() + ($(window).width()-350) * 0.5});
			}
		});
		$('#holidayModelDlg').dialog('open');
	};
	
	//修改节假日模板
	function funModHolidayModel(){
		$('#holidayModelDlg').dialog({
			title: '修改节假日模板',
			buttons:[{
				text:'确定',
				handler:function(){
					if($('#holidayModelForm').form('validate')){
						var areaId = $('#holidayAreaTree').tree('getSelected').id;
						if(areaId == null || areaId ==""){
							$.messager.alert("提示","请选择区域！");
							return;
						}
						
						var effDate = $('#holidayModelEffDate').datetimebox('getValue');
						var expDate = $('#holidayModelExpDate').datetimebox('getValue');
						var currentDate = new Date();
						var cYear = currentDate.getFullYear();
						var cMonth = currentDate.getMonth()+ 1 < 10 ? "0" + (currentDate.getMonth() + 1) : currentDate.getMonth();
						var cDate = currentDate.getDate()< 10 ? "0" + currentDate.getDate() : currentDate.getDate();
						var cunnentDateNum = ""+cYear+cMonth+cDate;
						var _tempeffDate;
						var _tempeffDateNum;
						var _tempexpDate;
						var _tempexpDateNum;
						
						if(effDate!=''){
							_tempeffDate = StringToDate(effDate);
							var efYear = _tempeffDate.getFullYear();
							var efMonth = _tempeffDate.getMonth()+ 1 < 10 ? "0" + (_tempeffDate.getMonth() + 1) : _tempeffDate.getMonth();
							var efDate = _tempeffDate.getDate()< 10 ? "0" + _tempeffDate.getDate() : _tempeffDate.getDate();
							_tempeffDateNum = ""+efYear+efMonth+efDate;
						}
						
						if(expDate!=''){
							_tempexpDate = StringToDate(expDate);
							var exYear = _tempexpDate.getFullYear();
							var exMonth = _tempexpDate.getMonth()+ 1 < 10 ? "0" + (_tempexpDate.getMonth() + 1) : _tempexpDate.getMonth();
							var exDate = _tempexpDate.getDate()< 10 ? "0" + _tempexpDate.getDate() : _tempexpDate.getDate();
							_tempexpDateNum = ""+exYear+exMonth+exDate;
						}
						
						if(effDate!=''){
							if( _tempeffDateNum < cunnentDateNum ){
								$.messager.alert("提示","生效效时间不能晚于当前时间");
								return;
							}
						}
						if(expDate!=''){
							if( _tempexpDateNum < cunnentDateNum ){
								$.messager.alert("提示","失效时间必须晚于当前时间");
								return;
							}
						}
						
						if((effDate!='') && (expDate !='')){
							if(  _tempeffDateNum > _tempexpDateNum ){
								$.messager.alert("提示","生效时间必须早于失效时间");
								return;
							}
						}
						
						//根据时间单位来拼接节假日规则：holidayModelRule
						var tmpHolidayModelRule ="";
						if(holidayModelTimeUnit == 0){
							//按日月 05.01-05.01 09:30-13:30 16:00-20:00
							//收集时间元素
							var startMonth = $('#holidayModelStartMonth').val();
							var startDay = $('#holidayModelStartDay').val();
							var endMonth = $('#holidayModelEndMonth').val();
							var endDay = $('#holidayModelEndDay').val();
							var workTimeRule = $('#holidayMdWorkTimeRule').val().length==0?"":" "+$('#holidayMdWorkTimeRule').val();
							//补全两位长度
							startMonth = new Number( startMonth ) >9? new Number(startMonth)+"":
				                  "0" + new Number(startMonth);
							startDay = new Number( startDay ) >9? new Number(startDay)+"":
				                  "0" + new Number(startDay);
							endMonth = new Number( endMonth ) >9? new Number(endMonth)+"":
				                  "0" + new Number(endMonth);
							endDay = new Number( endDay ) >9? new Number(endDay)+"":
				                  "0" + new Number(endDay);
							tmpHolidayModelRule = startMonth +"."+startDay
							+"-"
							+endMonth+"."+endDay
							+workTimeRule;
						}else{
							//按周
							var startWeek = $('#holidayModelStartWeek').val();
							var endWeek = $('#holidayModelEndWeek').val();
							tmpHolidayModelRule = startWeek +"-"+ endWeek;
						}
						
						var params = {
							id:$('#holidayModelTable').datagrid('getSelected').id,
							holidayModelName:$('#holidayModelName').val(),
							holidayModelRule:tmpHolidayModelRule,
							holidayModelTimeUnit:holidayModelTimeUnit,
							holidayModelState:$("input:radio[name='holidayModelState']:checked").val(),
							areaId:areaId,
							holidayModelEffDate:effDate,
							holidayModelExpDate:expDate,
							holidayModelComments:$('#holidayModelComments').val()
						};
						var ret = $.callSyn('holidayServ','modHolidayModel',params);
						if(ret.isSuccess){
							if(ret.isSuccess == true){
								$.messager.alert("提示","修改节假日模板成功！");
								funQryHolidayModel();
								$('#holidayModelDlg').dialog('close');
							}else{
								$.messager.alert("提示","修改节假日模板成功失败！");
							}
						}else{
							$.messager.alert("提示","修改节假日模板失败，发生异常！");
						}
					}
				}
			},{
				text:'取消',
				handler:function(){
						$('#holidayModelDlg').dialog('close');
					}
				}],
			onBeforeOpen:function(){
				var serow = $('#holidayModelTable').datagrid('getSelected');//.holidayRule
				//获取待修改的值
				$('#holidayModelName').textbox('setValue',serow.holidayName);
				var timeUnit = serow.timeUnit;
				var holidayRule = serow.holidayRule;//$.trim() 肯定不为空
				if(timeUnit==0){
					timeUnitFun1();
					$("input[name='holidayModelTimeUnit']").get(0).checked=true;
					var ruleArr = holidayRule.split(" ");//03.18-12.03 09:30-12:00 13:00-17:00
					var monthObj = ruleArr[0];//03.18-12.03
					var monthArr = monthObj.split("-");//[03.18][12.03]
					var startMonth = monthArr[0];//[03.18]
					var endMonth = monthArr[1];//[12.03]
					$('#holidayModelStartMonth').val( ""+new Number( startMonth.split(".")[0] ));
					$('#holidayModelStartDay').val( ""+new Number( startMonth.split(".")[1] ));
					$('#holidayModelEndMonth').val( ""+new Number( endMonth.split(".")[0] ));
					$('#holidayModelEndDay').val( ""+new Number( endMonth.split(".")[1] ));
					var len = ruleArr.length;
					var timeStr = "";
					if( len > 1){
						timeStr = ruleArr[1];
						for(var i=2; i<len; i++){
							timeStr += " "+ruleArr[i];
						}
					}
					$('#holidayMdWorkTimeRule').textbox('setValue',timeStr);
					
				}else{
					timeUnitFun2();
					$("input[name='holidayModelTimeUnit']").get(1).checked=true;
					var weekArr = holidayRule.split("-");
					$('#holidayModelStartWeek').val( ""+new Number(weekArr[0]) );
					$('#holidayModelEndWeek').val( ""+new Number(weekArr[1]) );
					$('#holidayMdWorkTimeRule').textbox('setValue',"");
				}
				
				$('#holidayModelEffDate').datebox("setValue", serow.effDate.substring(0,10));
				$('#holidayModelExpDate').datebox("setValue", serow.expDate.substring(0,10));
				if(serow.state == "10A" ){
					$("input[name='holidayModelState']").get(0).checked=true;
				}else{
					$("input[name='holidayModelState']").get(1).checked=true;
				}
				$('#holidayModelComments').textbox('setValue',serow.comments);
			},
			onOpen:function(){
				$('#holidayModelDlg').dialog('resize',{height:350});
				$("#holidayModelDlg").panel("move",{top:$(document).scrollTop() + ($(window).height()-350) * 0.5,left:$(document).scrollLeft() + ($(window).width()-350) * 0.5});
			}
		});
		$('#holidayModelDlg').dialog('open');
	}
	
	//删除节假日模板
	function funDelHolidayModel(){
		$.messager.confirm('提示', '确定删除该条模板吗？', function(r){
			if (r){
				var ret = $.callSyn('holidayServ','delHolidayModel',{id:$('#holidayModelTable').datagrid('getSelected').id});
				if(ret.isSuccess){
					$.messager.alert("提示","删除节假日模板成功！");
					funQryHolidayModel();
					$('#holidayModelTable').datagrid('selectRow',0);
				}else{
					$.messager.alert("提示","删除节假日模板失败！");
				}
			}
		});
	}
//----------------------------节假日模板holidayModel end--------------------------------
	
	
//----------------------------节假日holidaySystem begin---------------------------------
	//新增节假日
	var funAddHolidaySystem = function(){
		$('#holidaySystemDlg').dialog({
			title: '新增节假日',
			buttons:[{
				text:'确定',
				handler:function(){
					if($('#holidaySystemForm').form('validate')){
						var areaId = $('#holidayAreaTree').tree('getSelected').id;
						if(areaId == null || areaId ==""){
							$.messager.alert("提示","请选择区域！");
							return;
						}
						var effDate = $('#holidaySystemStartDate').datetimebox('getValue');
						var expDate = $('#holidaySystemEndDate').datetimebox('getValue');
						var currentDate = new Date();
						var cYear = currentDate.getFullYear();
						var cMonth = currentDate.getMonth()+ 1 < 10 ? "0" + (currentDate.getMonth() + 1) : currentDate.getMonth();
						var cDate = currentDate.getDate()< 10 ? "0" + currentDate.getDate() : currentDate.getDate();
						var cunnentDateNum = ""+cYear+cMonth+cDate;
						var _tempeffDate;
						var _tempeffDateNum;
						var _tempexpDate;
						var _tempexpDateNum;
						
						if(effDate!=''){
							_tempeffDate = StringToDate(effDate);
							var efYear = _tempeffDate.getFullYear();
							var efMonth = _tempeffDate.getMonth()+ 1 < 10 ? "0" + (_tempeffDate.getMonth() + 1) : _tempeffDate.getMonth();
							var efDate = _tempeffDate.getDate()< 10 ? "0" + _tempeffDate.getDate() : _tempeffDate.getDate();
							_tempeffDateNum = ""+efYear+efMonth+efDate;
						}
						
						if(expDate!=''){
							_tempexpDate = StringToDate(expDate);
							var exYear = _tempexpDate.getFullYear();
							var exMonth = _tempexpDate.getMonth()+ 1 < 10 ? "0" + (_tempexpDate.getMonth() + 1) : _tempexpDate.getMonth();
							var exDate = _tempexpDate.getDate()< 10 ? "0" + _tempexpDate.getDate() : _tempexpDate.getDate();
							_tempexpDateNum = ""+exYear+exMonth+exDate;
						}
						
						if(effDate!=''){
							if( _tempeffDateNum < cunnentDateNum ){
								$.messager.alert("提示","生效效时间不能晚于当前时间");
								return;
							}
						}
						if(expDate!=''){
							if( _tempexpDateNum < cunnentDateNum ){
								$.messager.alert("提示","失效时间必须晚于当前时间");
								return;
							}
						}
						
						if((effDate!='') && (expDate !='')){
							if(  _tempeffDateNum > _tempexpDateNum ){
								$.messager.alert("提示","生效时间必须早于失效时间");
								return;
							}
						}
						
						var params = {
							holidaySystemName:$('#holidaySystemName').val(),
							holidaySystemOperType:$("input:radio[name='holidaySystemOperType']:checked").val(),
							holidaySystemStartDate:$('#holidaySystemStartDate').datetimebox('getValue'),
							holidaySystemEndDate:$('#holidaySystemEndDate').datetimebox('getValue'),
							areaId:areaId,
							holidaySystemState:"10A",
							/*holidaySystemEffDate:$('#holidaySystemEffDate').datetimebox('getValue'),
							holidaySystemExpDate:$('#holidaySystemEndDate').datetimebox('getValue'),*/
							holidaySystemComments:$('#holidaySystemComments').val()
						};
						var ret = $.callSyn('holidayServ','addHolidaySystems',params);
						if(ret.isSuccess){
							if(ret.isSuccess == true){
								$.messager.alert("提示","新增节假日成功");
								$('#holidaySystemDlg').dialog('close');
								funQryHolidaySystem();
							}else{
								$.messager.alert("提示","新增节假日失败！");
							}
						}else{
							$.messager.alert("提示","新增节假日发生异常，操作失败！");
						}
					}
				}
			},{
				text:'取消',
				handler:function(){
						$('#holidaySystemDlg').dialog('close');
					}
				}],
			onBeforeOpen:function(){
				//清空所有值
				$('#holidaySystemName').textbox('setValue',"");
				$("input[name='holidaySystemOperType']").get(0).checked=true;
				$('#holidaySystemStartDate').combo('setText','');
				$('#holidaySystemEndDate').combo('setText','');
				$('#holidaySystemEffDate').combo('setText','');
				$('#holidaySystemExpDate').combo('setText','');
				$('#holidaySystemComments').textbox('setValue',"");
			},
			onOpen:function(){
				$('#holidaySystemDlg').dialog('resize',{height:350});
				$("#holidaySystemDlg").panel("move",{top:$(document).scrollTop() + ($(window).height()-350) * 0.5,left:$(document).scrollLeft() + ($(window).width()-350) * 0.5});
			}
		});
		$('#holidaySystemDlg').dialog('open');
	};
	
	//修改节假日
	function funModHolidaySystem(){
		$('#holidaySystemDlg').dialog({
			title: '修改节假日',
			buttons:[{
				text:'确定',
				handler:function(){
					if($('#holidaySystemForm').form('validate')){
						var areaId = $('#holidayAreaTree').tree('getSelected').id;
						/*if(areaId == null || areaId ==""){
							$.messager.alert("提示","请选择区域！");
							return;
						
						var effDate = $('#holidaySystemStartDate').datetimebox('getValue');
						var expDate = $('#holidaySystemEndDate').datetimebox('getValue');
						var currentDate = new Date();
						var cYear = currentDate.getFullYear();
						var cMonth = currentDate.getMonth()+ 1 < 10 ? "0" + (currentDate.getMonth() + 1) : currentDate.getMonth();
						var cDate = currentDate.getDate()< 10 ? "0" + currentDate.getDate() : currentDate.getDate();
						var cunnentDateNum = ""+cYear+cMonth+cDate;
						var _tempeffDate;
						var _tempeffDateNum;
						var _tempexpDate;
						var _tempexpDateNum;
						
						if(effDate!=''){
							_tempeffDate = StringToDate(effDate);
							var efYear = _tempeffDate.getFullYear();
							var efMonth = _tempeffDate.getMonth()+ 1 < 10 ? "0" + (_tempeffDate.getMonth() + 1) : _tempeffDate.getMonth();
							var efDate = _tempeffDate.getDate()< 10 ? "0" + _tempeffDate.getDate() : _tempeffDate.getDate();
							_tempeffDateNum = ""+efYear+efMonth+efDate;
						}
						
						if(expDate!=''){
							_tempexpDate = StringToDate(expDate);
							var exYear = _tempexpDate.getFullYear();
							var exMonth = _tempexpDate.getMonth()+ 1 < 10 ? "0" + (_tempexpDate.getMonth() + 1) : _tempexpDate.getMonth();
							var exDate = _tempexpDate.getDate()< 10 ? "0" + _tempexpDate.getDate() : _tempexpDate.getDate();
							_tempexpDateNum = ""+exYear+exMonth+exDate;
						}
						
						if(effDate!=''){
							if( _tempeffDateNum < cunnentDateNum ){
								$.messager.alert("提示","生效效时间不能晚于当前时间");
								return;
							}
						}
						if(expDate!=''){
							if( _tempexpDateNum < cunnentDateNum ){
								$.messager.alert("提示","失效时间必须晚于当前时间");
								return;
							}
						}
						
						if((effDate!='') && (expDate !='')){
							if(  _tempeffDateNum > _tempexpDateNum ){
								$.messager.alert("提示","生效时间必须早于失效时间");
								return;
							}
						}
						*/
						var params = {
							id:$('#holidaySystemTable').datagrid('getSelected').id,
							holidaySystemName:$('#holidaySystemName').val(),
							holidaySystemOperType:$("input:radio[name='holidaySystemOperType']:checked").val(),
							holidaySystemStartDate:$('#holidaySystemStartDate').datetimebox('getValue'),
							holidaySystemEndDate:$('#holidaySystemEndDate').datetimebox('getValue'),
							areaId:areaId,
							holidaySystemState:"10A",
							/*holidaySystemEffDate:$('#holidaySystemEffDate').datetimebox('getValue'),
							holidaySystemExpDate:$('#holidaySystemEndDate').datetimebox('getValue'),*/
							holidaySystemComments:$('#holidaySystemComments').val()
						};
						var ret = $.callSyn('holidayServ','modHolidaySystems',params);
						if(ret.isSuccess){
							if(ret.isSuccess == true){
								$.messager.alert("提示","修改节假日成功！");
								$('#holidaySystemDlg').dialog('close');
								funQryHolidaySystem();
							}else{
								$.messager.alert("提示","修改节假日失败！");
							}
						}else{
							$.messager.alert("提示","修改节假日失败，发生异常！");
						}
					}
				}
			},{
				text:'取消',
				handler:function(){
						$('#holidaySystemDlg').dialog('close');
					}
				}],
			onBeforeOpen:function(){
				//获取待修改的值
				var serow = $('#holidaySystemTable').datagrid('getSelected');//.holidayRule
				$('#holidaySystemName').textbox('setValue',serow.holidaySystemName);
				if(serow.operType == "0" ){
					$("input[name='holidaySystemOperType']").get(0).checked=true;
				}else{
					$("input[name='holidaySystemOperType']").get(1).checked=true;
				}
				$('#holidaySystemStartDate').datebox("setValue", serow.beginDate.substring(0,10));
				$('#holidaySystemEndDate').datebox("setValue", serow.beginDate.substring(0,10));
				//$('#holidaySystemEffDate').datebox("setValue", serow.expDate.substring(0,10));
				//$('#holidaySystemExpDate').datebox("setValue", serow.expDate.substring(0,10));
				$('#holidaySystemComments').textbox('setValue',serow.comments);
			},
			onOpen:function(){
				$('#holidaySystemDlg').dialog('resize',{height:350});
				$("#holidaySystemDlg").panel("move",{top:$(document).scrollTop() + ($(window).height()-350) * 0.5,left:$(document).scrollLeft() + ($(window).width()-350) * 0.5});
			}
		});
		$('#holidaySystemDlg').dialog('open');
	}
	
	//删除节假日
	function funDelHolidaySystem(){
		$.messager.confirm('提示', '确定删除该条节假日记录吗？', function(r){
			if (r){
				var ret = $.callSyn('holidayServ','delHolidaySystems',{id:$('#holidaySystemTable').datagrid('getSelected').id});
				if(ret.isSuccess){
					$.messager.alert("提示","删除节假日成功！");
					funQryHolidaySystem();
					$('#holidaySystemTable').datagrid('selectRow',0);
				}else{
					$.messager.alert("提示","删除节假日失败！");
				}
			}
		});
	}

	
	
	
	
	
//----------------------------节假日holidaySystem end---------------------------------	
	
	
	
	
	
	//-----------------------------公共方法----------------------------
	var StringToDate = function(s){
		if(s&&typeof(s)=="string"){
			var s = s.substring(0,19);
			var aD=s.split(/[\/\-: ]/);
			if(aD.length<3) return null;
			if(aD.length<4) aD[3]=aD[4]=aD[5]="00";
			var d=new Date(aD[0],parseInt(aD[1]-1,10),aD[2],aD[3],aD[4],aD[5]);
			if(isNaN(d)) return null;
			return d;
			}
		else return null;
	}
	
	var getAllTimeStr = function(){
	  	var timeStr ="";
	  	var allItem = $('#timeDetailGrid').datagrid('getRows');
		for(var i=0;i< allItem.length;i++){
			var tempTimeStr = allItem[i].startTime +"-"+  allItem[i].endTime;	
			if(i==0){			
				timeStr =tempTimeStr;
			}else{
				timeStr =timeStr + " " + tempTimeStr;
			}
		}
		return timeStr;
	}
	var getTimeExcludeSelf= function(){
		var selItem = $('#timeDetailGrid').datagrid('getSelected');
		var timeStr ="";
		var allItem = $('#timeDetailGrid').datagrid('getRows');
		for(var i=0;i< allItem.length;i++){
			if(allItem[i].startTime != selItem.startTime && allItem[i].endTime != selItem.endTime){
				var tempTimeStr = allItem[i].startTime +"-"+  allItem[i].endTime;	
				if(i==0){			
					timeStr =tempTimeStr;
				}else{
					timeStr =timeStr + " " + tempTimeStr;
				}
			}
		}
		return timeStr;
	}

	//校验时间格式
	var validatorTime = function(time){
		if(!time) return false;
		if(!time.startHour || !time.startMinute) return false;
		if(!time.endHour || !time.endMinute) return false;
		var startHour = new Number(time.startHour);
		var startMinute = new Number(time.startMinute);
		var endHour = new Number(time.endHour);
		var endMinute = new Number(time.endMinute);
		
		if(startHour>23 || startHour<0)return false;			
		if(startMinute >59 || startMinute<0)return false;	
		if(endHour>23 || endHour<0)return false;			
		if(endMinute>59 || endMinute<0)return false;
		return true;
	};
	
	//校验时间大小
	var checkTime =function(time){
		var startHour = new Number(time.startHour)+ new Number(time.startMinute)/60;
		var endHour = new Number(time.endHour)+  new Number(time.endMinute)/60;
		if(startHour >= endHour) return false;	
		return true;
	};
	//判断时间是否冲突
	var isTimeNotConflict = function(time){	
		var startTime = new Number(time.startHour)+ new Number(time.startMinute)/60;		
		var endTime = new Number(time.endHour)+ new Number(time.startMinute)/60;
		if(orginTimeStr=="") return true; 
		var orginTimeArr = $.trim(orginTimeStr).split(" ");
		var orginTimeNumberArr = new Object();
		for(var i = 0 ;i<orginTimeArr.length;i++){
			var tempObj = orginTimeArr[i].split("-");
			var startTimeObj = tempObj[0].split(":");
			var endTimeObj = tempObj[1].split(":");
			var startTimeNum = new Number(startTimeObj[0])+  new Number(startTimeObj[1])/60;
			var endTimeNum = new Number(endTimeObj[0])+  new Number(endTimeObj[1])/60;		 	
			
			if(startTimeNum<=startTime && endTimeNum>=startTime){
				return false;
			}
			if(startTimeNum<=endTime && endTimeNum>=endTime){
				return false;
			}
			if(startTimeNum >=startTime && endTimeNum<=endTime){
				return false;
			} 			
		}
		return true;
	};

	//初始化日期选择框
	var initMonth = function(){
          var startMonth = $('#holidayModelStartMonth');
          var startDay = $('#holidayModelStartDay');
          var endMonth = $('#holidayModelEndMonth');
          var endDay = $('#holidayModelEndDay');
          
          var yearValue = new Date().getFullYear();
          
          for(var i=1;i<=12;i++){
        	  startMonth.append('<option value="'+i+'">'+i+'</option>');
        	  endMonth.append('<option value="'+i+'">'+i+'</option>');
          }
          for(var i=1;i<=31;i++){
        	  //为了让点击修改按钮时可以赋值
        	  startDay.append('<option value="'+i+'">'+i+'</option>');
        	  endDay.append('<option value="'+i+'">'+i+'</option>');
          }
          
          startMonth.change(function(){
        	  startDay.empty();
        	  startDay.append('<option value="0" selected>-日-</option>')
              var monthValue = parseInt(startMonth.val());
              var dayvalue;
              if(monthValue==1||monthValue==3
            		  ||monthValue==5||monthValue==7
            		  ||monthValue==8||monthValue==10||monthValue==12){
                  dayvalue = 31
              }else if(monthValue==4||monthValue==6
            		  ||monthValue==11||monthValue==9){
                  dayvalue = 30
              }else if(monthValue==2){
                  //判断闰年
                  if(yearValue%4==0 && (yearValue%4 !=0 || yearValue%400==0)){ //闰年
                      dayvalue = 29
                  }else{
                      dayvalue = 28    
                  }
                 //判断闰年结束 
              }
              
              for(var i=1;i<=dayvalue;i++){
            	  startDay.append('<option value="'+i+'">'+i+'</option>')
              }
          });
          
          endMonth.change(function(){
        	  endDay.empty();
        	  endDay.append('<option value="0" selected>-日-</option>')
              var monthValue = parseInt(endMonth.val());
              var dayvalue;
              if(monthValue==1||monthValue==3
            		  ||monthValue==5||monthValue==7
            		  ||monthValue==8||monthValue==10||monthValue==12){
                  dayvalue = 31
              }else if(monthValue==4||monthValue==6
            		  ||monthValue==11||monthValue==9){
                  dayvalue = 30
              }else if(monthValue==2){
                  //判断闰年
                  if(yearValue%4==0 && (yearValue%4 !=0 || yearValue%400==0)){ //闰年
                      dayvalue = 29
                  }else{
                      dayvalue = 28    
                  }
                 //判断闰年结束 
              }
              
              for(var i=1;i<=dayvalue;i++){
            	  endDay.append('<option value="'+i+'">'+i+'</option>')
              }
          });
		
		
	};
	initMonth();
	
	
});
