/** 全局变量的定义 **/


/** 事件的绑定 **/
//在表格区域的空白部分右键点击
$('#templateTable').parent().mousedown(function(e){
    var templateMenu = $('#templateMenu');
    var selectedRow = $('#templateTable').datagrid('getSelected');
    //右键单击
    if(e&&e.which==3){
        if(selectedRow == null){
            templateMenu.menu('disableItem', $('#changeTemplate'));
            templateMenu.menu('disableItem', $('#removeTemplate'));
            templateMenu.menu('disableItem', $('#designTemplate'));
        }else {
            templateMenu.menu('enableItem', $('#changeTemplate'));
            templateMenu.menu('enableItem', $('#removeTemplate'));
            templateMenu.menu('enableItem', $('#designTemplate'));
        }

        templateMenu.menu('show',{
            left: e.pageX,
            top: e.pageY
        });
        e.stopPropagation();  //  阻止事件冒泡
    }

    //菜单点击事件
    templateMenu.menu({
        onClick:function (item) {
            switch(item.id){
                case "addTemplate":
                    addTemplate(selectedRow);
                    break;
                case "changeTemplate":
                    changeTemplate(selectedRow);
                    break;
                case "removeTemplate":
                    removeTemplate(selectedRow);
                    break;
                case "designTemplate":
                    designTemplate(selectedRow);
                    break;
                default:
                    console.log("error");
            }
        }
    });
});


//取消增加模板
$('#addTemplateCancel').bind('click', cancelAddTemplate);

/** easyui控件的定义 **/
$('#templateTable').datagrid({
    width:'100%',
    height:'100%',
    singleSelect:true,
    columns:[[
        {title:'模板名称', field:'templateName', width:'30%'},
        {title:'模板编码', field:'templateCode', width:'30%'},
        {title:'备注', field:'comments', width:'38%'}
    ]]
});
//模板详情
$('#templateDetailTable').datagrid({
    width:'100%',
    height:'100%',
    singleSelect:true,
    pagination:true,
    columns:[[
        {title:'元素编码', field:'elementCode', width:120},
        {title:'控件标签', field:'pageTitle', width:120},
        {title:'控件编码', field:'pageCode', width:120},
        {title:'位于第几行', field:'locateRow', width:120},
        {title:'位于第几列', field:'locateColumn', width:120},
        {title:'对齐方式', field:'locateColumn', width:100},
        {title:'是否回填项', field:'isRet', width:100},
        {title:'是否必填', field:'isMust', width:100},
        {title:'是否使能', field:'isEnable', width:100},
        {title:'是否显示', field:'isDisplay', width:100},
        {title:'是否赋初值', field:'isInit', width:100},
        {title:'备注', field:'comments', width:120}
    ]]
});


/** 函数的定义 **/

//增加模板函数
function addTemplate(selectedRow) {
    $('#addTemplateDialog').dialog({
        title:'增加模板',
        width: 400,
        closed: true,
        cache: false,
        modal: true,
        onBeforeOpen:function () {
            if(selectedRow != null){
                $('#templateName').textbox('setText', selectedRow.templateName);
                $('#templateCode').textbox('setText', selectedRow.templateCode);
                $('#comments').textbox('setText', selectedRow.comments);
            }
        }
    });

    //确定增加模板按钮事件
    $('#addTemplateOK').unbind('click').bind('click', confirmAddTemplate);
    $('#addTemplateDialog').dialog('open');
}
//修改模板函数
function changeTemplate(selectedRow) {
    $('#addTemplateDialog').dialog({
        title:'修改模板',
        width:400,
        closed:false,
        cache:false,
        modal:true,
        onBeforeOpen:function () {
            $('#templateName').textbox('setText', selectedRow.templateName);
            $('#templateCode').textbox('setText', selectedRow.templateCode);
            $('#comments').textbox('setText', selectedRow.comments);
        }
    });
    //确定修改模板按钮事件
    $('#addTemplateOK').unbind('click').bind('click', function () {
        confirmChangeTemplate(selectedRow);
    });
}
//删除模板函数
function removeTemplate(selectedRow) {
    //从Table中删除
    var index = $('#templateTable').datagrid('getRowIndex', selectedRow);
    $('#templateTable').datagrid('deleteRow', index);
    //TODO 从数据表中删除

}
//设计模板函数
function designTemplate(item) {

}

//确定增加模板列表函数
function confirmAddTemplate(oldRow) {
    //获取弹窗的参数
    var param = {
        templateName:$('#templateName').textbox('getText'),
        templateCode:$('#templateCode').textbox('getText'),
        comments:$('#comments').textbox('getText'),
        createTime:new Date().toLocaleString(),
        routeId:1,
        tenantId:""
    };

    //校验表单
    if($('#editTemplateInfo').form('validate') == true){
        //TODO 调用callSyn向数据库中增加数据

        //增加到表格中
        $('#templateTable').datagrid('appendRow', param);
        $('#addTemplateDialog').dialog('close');
    }
}
//确定修改模板列表
function confirmChangeTemplate(oldRow) {
    //获取弹窗的参数
    var param = {
        templateName:$('#templateName').textbox('getText'),
        templateCode:$('#templateCode').textbox('getText'),
        comments:$('#comments').textbox('getText'),
        createTime:new Date().toLocaleString(),
        routeId:1,
        tenantId:""
    };

    //校验表单
    if($('#editTemplateInfo').form('validate') == true){
        //TODO 调用callSyn修改数据库的数据

        //修改表格的数据
        var index = $('#templateTable').datagrid('getRowIndex', oldRow);
        $('#templateTable').datagrid('updateRow', {
            index:index,
            row:param
        });

        $('#addTemplateDialog').dialog('close');
    }
}
//取消增加模板函数
function cancelAddTemplate() {
    $('#addTemplateDialog').dialog('close');
}