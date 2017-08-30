$(function($){
	var session = $.session();
	var modType = false;
	var orginTimeStr = "";
	
	//方法：初始化区域树
	var initAreaTree = function(){
		//areaId为-1加载全部
		var areaTreeData = $.callSyn("AreaServ","getAreaJsonTree",{areaId: session["areaId"]});
		$('#areaTree').tree({
			data : areaTreeData,
			onClick:function(node)
			{
				funBtn($('#btnAddWt'),funAddWt,true);//激活修改按钮
				funQryWt();
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
	//界面初始化------start--------------
	initAreaTree();
	$('#areaTree').tree("select",$("#areaTree").tree("getRoot").target);
	//界面初始化------end--------------
	//初始化工作时间列表
	$('#workTimeTable').datagrid({
		rownumbers:false,//显示带有行号的列
		singleSelect:true,//则只允许选中一行
		autoRowHeight:false,//是否设置基于该行内容的行高度
	    striped:true,//奇偶行使用不同背景色
	    pagination:false,//
		//pageSize:pageSize,//初始化页面尺寸	
	    toolbar: '#workTimeTb',
	    columns:[[
			{title:'名称',field:'workTimeName',width:120,sortable:true},
			{title:'工作时间段',field:'workTimeRule',width:200},
			{title:'生效时间',field:'effDate',width:200},
			{title:'失效时间',field:'expDate',width:200},
			{title:'id',field:'id',hidden:true},
			{title:'备注',field:'comments',hidden:true}
	    ]],
		onClickRow: function(index,row){//激活按钮+加载工作时间
			funBtn($('#btnAddWt'),funAddWt,true);//激活修改按钮
			funBtn($('#btnModWt'),funModWt,true);//激活修改按钮
			funBtn($('#btnDelWt'),funDelWt,true);//激活删除按钮
			modType = false;
			loadWTForm();
		}
	});
	//加载时间段
	$('#workTimeRule').textbox({
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
			var timeStr = $('#workTimeRule').val();
			if(timeStr == ""){
				return;
			}
			var timeStrArr = timeStr.split(" ");
			for(var i=0;i<timeStrArr.length;i++){
				var timeArr =  timeStrArr[i].split("-");	
				var data = new Object();
				data.startTime = timeArr[0];
				data.endTime = timeArr[1];
				var dataArr = new Array();
				dataArr.push(data);
				$('#timeDetailGrid').datagrid('loadData',dataArr);
				$('#timeDetailGrid').datagrid('selectRow',0);
			}  	    
		}
	});
	//查询工作时间列表
	var funQryWt = function(){
		var areaId = $('#areaTree').tree('getSelected').id;
		var workTimeData = $.callSyn("WorkTimeServ","qryWorkTimeByAreaId",{areaId: areaId});
		if(workTimeData.total>0){
			$('#workTimeTable').datagrid('loadData',workTimeData.rows);
		}else{
			$('#workTimeTable').datagrid('loadData',{total: 0, rows:[]});//清空
		}
	};
	// 加载工作时间详情form
	var loadWTForm = function(){
		var workTime = $('#workTimeTable').datagrid('getSelected');
		$('#workTimeId').val(workTime.id);
		if(modType == true){
			$('#workTimeName').textbox('setValue',workTime.workTimeName).textbox('enable');
			$('#workTimeRule').textbox('setValue',workTime.workTimeRule);
			$('a.textbox-icon.icon-search.textbox-icon-disabled').removeClass("textbox-icon-disabled");
			$('#comments').textbox('setValue',workTime.comments).textbox('enable');
			$('#effDate').datetimebox('setValue',workTime.effDate).datetimebox('enable');
			$('#expDate').datetimebox('setValue',workTime.expDate).datetimebox('enable');
		}else{
			$('#workTimeName').textbox('setValue',workTime.workTimeName).textbox('disable');
			$('#workTimeRule').textbox('setValue',workTime.workTimeRule).textbox('disable');
			$('#comments').textbox('setValue',workTime.comments).textbox('disable');
			$('#effDate').datetimebox('setValue',workTime.effDate).datetimebox('disable');
			$('#expDate').datetimebox('setValue',workTime.expDate).datetimebox('disable');
		}
	};
	//初始化工作时间
	var resetWt = function(){
		$('#workTimeName').textbox('clear').textbox('enable');
		$('#workTimeRule').textbox('clear');
		$('a.textbox-icon.icon-search.textbox-icon-disabled').removeClass("textbox-icon-disabled");
		$('#effDate').datetimebox('clear');
		$('#expDate').datetimebox('clear');
		$('#comments').textbox('clear').textbox('enable');
	};
	//增加工作时间
	var funAddWt = function(){
		modType = false;
		funBtn($('#btnOkWt'),funOkWt,true);//激活保存按钮
		resetWt();
	};
	//修改工作时间
	var funModWt = function(){
		modType = true;
		funBtn($('#btnOkWt'),funOkWt,true);//激活保存按钮
		loadWTForm();
	};
	//删除工作时间
	var funDelWt = function(){
		$.messager.confirm('提示', '你确定要删除吗？', function(r){
			if (r){
				var ret = $.callSyn('WorkTimeServ','delWorkTime',{id:$('#workTimeTable').datagrid('getSelected').id});
				if(ret.isSuccess){
					$.messager.alert("提示","删除成功");
					funQryWt();
					resetWt();
				}else{
					$.messager.alert("提示","删除失败");
				}
			}
		});
	};
	var funOkWt = function(){
		if($('#workTimeForm').form('validate')){
			var effDate = $('#effDate').datetimebox('getValue');
			var expDate = $('#expDate').datetimebox('getValue');
			var currentDate = new Date();
			
			if(effDate!=''){
				if(StringToDate(effDate).getTime() > currentDate.getTime()){
					$.messager.alert("提示","生效效时间不能晚于当前时间");
					return;
				}
			}
			if(expDate!=''){
				if(StringToDate(expDate).getTime() < currentDate.getTime()){
					$.messager.alert("提示","失效时间必须晚于当前时间");
					return;
				}
			}
			
			if((effDate!='') && (expDate !='')){
				if(StringToDate(effDate).getTime() >= StringToDate(expDate).getTime()){
					$.messager.alert("提示","生效时间必须早于失效时间");
					return;
				}
			}
			var param = {
				workTimeName:$('#workTimeName').val(),
				workTimeRule:$('#workTimeRule').val(),
				effDate:effDate,
				expDate:expDate,
				comments:$('#comments').val(),
				id:$('#workTimeId').val(),
				areaId:$('#areaTree').tree('getSelected').id
			};
			if(modType){
				var ret = $.callSyn('WorkTimeServ','modWorkTime',param);
				if(ret.isSuccess){
					$.messager.alert("提示","修改成功");
					funQryWt();
					resetWt();
				}else{
					$.messager.alert("提示","修改失败");
				}
			}else{
				var ret = $.callSyn('WorkTimeServ','addWorkTime',param);
				if(ret.isSuccess){
					$.messager.alert("提示","添加成功");
					funQryWt();
					resetWt();
				}else{
					$.messager.alert("提示","添加失败");
				}
			}
		}
	};
	$('#btnCancelWt').click(function(e){
		funBtn($('#btnOkWt'),funOkWt,false);
		resetWt();
	});
	$('#timeDetailGrid').datagrid({
		autoRowHeight:false,
	   	fitColumns:true,
	   	singleSelect:true,
	   	toolbar:'#timeDetailTb',
	    columns:[[
	    	{title:'上班时间',field:'startTime',width:150},
			{title:'下班时间',field:'endTime',width:150},
	    ]]
	});
	$('#timeDetailWin-confirmBtn').click(function(e){
		var timeStr = getAllTimeStr();
		$('#workTimeRule').textbox('setValue',timeStr);
		$('#timeDetailWin').dialog('close');
	});

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
						$('#timeDetailGrid').datagrid('appendRow',timeObject);
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
			}
		});
		$('#dlg').dialog('open');
	});
	$('#btnModTd').click(function(e){
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
						var rowIndex=$('#timeDetailGrid').datagrid('getRowIndex',$('#timeDetailGrid').datagrid('getSelected'));
						$('#timeDetailGrid').datagrid('updateRow',{index:rowIndex,row:timeObject});
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
			}
		});
		$('#dlg').dialog('open');
	});
	$('#btnDelTd').click(function(e){
		$.messager.confirm('提示', '确定删除该工作时间吗？', function(r){
			if (r){
				var rowIndex=$('#timeDetailGrid').datagrid('getRowIndex',$('#timeDetailGrid').datagrid('getSelected'));
				$('#timeDetailGrid').datagrid('deleteRow',rowIndex);
				$.messager.alert("提示","删除工作时间成功");
				$('#dlg').dialog('close');
			}
		});
	});
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
	};
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
	};
	//工作时间增改窗口
	$('#dlg').dialog({
		title: '工作时间段',
	    width: 500,
	    closed: true,
	    cache: false,
	    modal: true
	});
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
		var orginTimeArr = Trim(orginTimeStr).split(" ");
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
	var Trim = function(s) {
		return ((typeof (s) != "string") ? NullRepl(s, "") : s.replace(
				/(^\s*)|(\s*$)/g, ""));
	};
	var NullRepl = function(s, repStr){
		return (s===null||s===undefined)?repStr:s;
	};
});
