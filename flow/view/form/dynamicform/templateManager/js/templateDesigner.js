//增加一行或在指定行之后增加一行都弹出新增一行对话框
$('#addNewLine').bind('click', addNewLineDialog);
$('#addNewLineAfterOne').bind('click', addNewLineDialog);
//绑定新增一行对话框的确定和取消按钮
$('#addNewLineOK').bind('click', addNewLineOK);
$('#addNewLineCancel').bind('click', addNewLineCancel);
//绑定新增属性对话框
$('#addProperties').bind('click', addPropertiesDialog);
//绑定新增一行对话框的确定和取消按钮
$('#addPropertiesOK').bind('click', addPropertiesOK);
$('#addPropertiesCancel').bind('click', addPropertiesCancel);

var widgetNumber = 0;


//新增属性对话框设置
$('#addPropertiesDialog').dialog({
	title: '新增属性',
	width: 400,
	height: 'auto',
	closed: true,
	cache: false,
	modal: true
});

//新增一行的对话框面板
function addNewLineDialog() {
	$('#propertiesTable').datagrid('loadData',{total:0,rows:[]});
	widgetNumber = 0;
	$('#addNewLineDialog').dialog({

	});

	$('#addNewLineDialog').dialog('open');
}

//确定增加一行控件，添加到主页中去,然后关闭对话框
function addNewLineOK() {
	var lineDiv = $("<div class = 'rowStyle'></div>");
	$('#menu').after(lineDiv);

	var allWidget = $('#propertiesTable').datagrid('getData');
	for(var i = 0; i < widgetNumber; i++){
		var labelName = allWidget.rows[i].label;

		//生成控件
		var label = $("<label>" + labelName + "</label>").css({
			"position":'relative',
			"left":'1%',
			"width":100,
			"display":"inline-block"
		});
		var input = $("<input type = 'text'/>").css({
			"position":'relative',
			"width":'25%',
		});
		var button = $("<input type = 'button' value = '...'/>").css({
			"position":'relative',
			"margin-right":50,
		});

		lineDiv.append(label, input, button);
	}

	$('#addNewLineDialog').dialog('close');
}

//取消增加控件，关闭对话框
function addNewLineCancel() {
	$('#addNewLineDialog').dialog('close');
}

//删除一行
function removeOneLine() {
	
}

//关闭窗口
function closeWindow() {
	
}

//新增属性对话框
function addPropertiesDialog() {
	widgetNumber++;
	//清空之前的值
	$('#showLabel').val("");
	$('#widgetCode').val("");

	$('#addPropertiesDialog').dialog({

	});

	$('#addPropertiesDialog').dialog('open');
}

//确定增加一行控件，添加到主页中去,然后关闭对话框
function addPropertiesOK() {
	//获取参数
	var params = {
		// selectedElement:$('#elementType').val(),
		label:$("#showLabel").val(),
		code:$("#widgetCode").val(),
		align:$("#align").find("option:selected").text(),
		backfill:$("#backfill").find("option:selected").text(),
		required:$("#required").find("option:selected").text(),
		enable:$("#enable").find("option:selected").text(),
		display:$("#display").find("option:selected").text(),
		assignment:$("#assignment").find("option:selected").text()
	};
	//给表格增加一行数据
	$('#propertiesTable').datagrid('insertRow', {
		index:widgetNumber - 1,
		row:params
	});

	$('#addPropertiesDialog').dialog('close');
}

//取消增加控件，关闭对话框
function addPropertiesCancel() {
	$('#addPropertiesDialog').dialog('close');
}

