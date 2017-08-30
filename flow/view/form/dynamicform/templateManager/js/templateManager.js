$(function(){
    /** 全局变量的定义 **/
    //模板设计-元素所在列数
    var locateColumnNumber=0;
    var locateRowNumber=0;
    var lineNumberArray=[{"code":'0', "lineNumber":'第0行'}];

    /** 事件的绑定 **/
    //在表格区域的空白部分右键点击
    $('#templateTable').parent().mousedown(function(e){
        var templateMenu = $('#templateMenu');
        var selectedRow = $('#templateTable').datagrid('getSelected');
        //右键单击
        if(e&&e.which==3){
            //没有选中模板
            if(selectedRow == null){
                templateMenu.menu('disableItem', $('#modTemplate'));
                templateMenu.menu('disableItem', $('#delTemplate'));
                templateMenu.menu('disableItem', $('#designTemplate'));
            }else {   //选中了模板
                templateMenu.menu('enableItem', $('#modTemplate'));
                templateMenu.menu('enableItem', $('#delTemplate'));
                templateMenu.menu('enableItem', $('#designTemplate'));
            }

            templateMenu.menu('show',{
                left: e.pageX,
                top: e.pageY
            });
            e.stopPropagation();  //  阻止事件冒泡
        }

        //表单模板-菜单点击事件
        templateMenu.menu({
            onClick:function (item) {
                switch(item.id){
                    case "addTemplate":
                        addTemplate(selectedRow);
                        break;
                    case "modTemplate":
                        modTemplate(selectedRow);
                        break;
                    case "delTemplate":
                        delTemplate(selectedRow);
                        break;
                    case "designTemplate":
                        designTemplate(selectedRow);
                        break;
                    default:
                        $.messager.alert("error");
                }
            }
        });
    });
    //模板约束条件-添加约束条件表达式
    $('#addExpression').bind('click', function () {
        //构造条件表达式
        var value = '['+$('#relyWidget').combobox('getValue') + ']' 
        + $('#operator').combobox('getValue');

        //获取值下拉框/文本框的值
        if($('#optionDiv').is(':hidden')){
            value = value + $('#value').textbox('getText')+' ';
        }else{
            value = value + $('#optionValue').combobox('getValue')+' ';
        }

        var oldValue = $('#consCondition').textbox('getText');            
        $('#consCondition').textbox('setText', oldValue+value);
    });
    //模板约束条件-向约束表达式中添加符号(&、|、(、))
    $('button.conditionButton').bind('click', function () {
        var oldValue = $('#consCondition').textbox('getText');
        var value = this.value;
        $('#consCondition').textbox('setText', oldValue+value);
    })

    //模板设计-按钮事件
    $('#addNewLine').bind('click', addNewLine);
    $('#addNewLineAfterOne').bind('click', addNewLineAfterOne);
    $('#delOneLine').bind('click', delOneLine);
    $('#closeWindow').bind('click', closeWindow);
    $('#addProperties').bind('click', addProperties);

    /** easyui控件的定义 **/
    //表单模板-模板列表
    $('#templateTable').datagrid({
        height:'100%',
        singleSelect:true,
        fitColumns:true,
        striped:true,
        columns:[[
            {title:"模板标识", field:"id", hidden:true},
            {title:'模板名称', field:'templateName', width:200},
            {title:'模板编码', field:'templateCode', width:180},
            {title:'备注', field:'comments', width:300},
            {title:"创建时间", field:"createDate", hidden:true},
            {title:"路由", field:"routeId", hidden:true},
            {title:"租户", field:"tenantId", hidden:true}
        ]],
        data:qryTemplate(),
        //点击选中模板，加载模板详情
        onClickRow:function (index, row) { 
            //连接数据库加载模板详情
            var param={templateCode:row.templateCode};
            var templateDetail= $.callSyn('FormManagerServ', 'qryTemplateDetail', param);
            $('#templateDetailTable').datagrid('loadData', templateDetail);
            var templateRule = $.callSyn('FormManagerServ', 'qryPageTemplateRule', param);
            $('#templateRuleTable').datagrid('loadData', templateRule);
            var pageConstraint = $.callSyn('FormManagerServ', 'qryPageConstraint', param);
            $('#templateConstraintTable').datagrid('loadData', pageConstraint);

            //激活增加模板使用条件和约束按钮
            funBtn($('#addRule'), addRule, true);  
            funBtn($('#addConstraint'), addConstraint, true); 
            //失效修改、删除模板使用条件按钮
            funBtn($('#modRule'), modRule, false);
            funBtn($('#delRule'), delRule, false);
            //失效修改、删除模板约束条件按钮
            funBtn($('#modConstraint'), modConstraint, false);
            funBtn($('#delConstraint'), delConstraint, false);
        }
    });

    //模板明细-模板详情表
    $('#templateDetailTable').datagrid({
        height:'100%',
        singleSelect:true,
        pagination:true,
        rownumbers:true,
        striped:true,
        // fitColumns:true,
        columns:[[
            {title:'模板明细Id', field:'<templateDetail></templateDetail>Id', hidden:true},
            {title:'页面模板编码', field:'templateCode', hidden:true},
            {title:'元素编码', field:'elementCode', width:120},
            {title:'控件标签', field:'pageTitle', width:120},
            {title:'控件编码', field:'pageCode', width:120},
            {title:'位于第几行', field:'locateRow', width:120},
            {title:'位于第几列', field:'locateColumn', width:120},
            {title:'对齐方式', field:'align', width:100},
            {title:'是否回填项', field:'isRet', width:100},
            {title:'是否必填', field:'isMust', width:100},
            {title:'是否使能', field:'isEnabled', width:100},
            {title:'是否显示', field:'isDisplay', width:100},
            {title:'是否赋初值', field:'isInit', width:100},
            {title:'备注', field:'comments', width:120},
            {title:'路由id', field:'routeId', hidden:true},
            {title:'租户', field:'tenantId', hidden:true}
        ]]
    });

    //模板适用规则-模板适用规则表
    $('#templateRuleTable').datagrid({
        height:'100%',
        singleSelect:true,
        autoRowHeight:false,
        toolbar:'#ruleToolBar',
        fitColumns:true,
        striped:true,
        // pagination:true,
        columns:[[
            {field: 'ck', checkbox: true },
            {title:'规则标识', field:'ruleId', hidden:true},
            {title:'流程模板', field:'packageCode', width:180},
            {title:'流程模板Id', field:'packageId', hidden:true},
            {title:'环节', field:'tacheCode', width:180},
            {title:'环节Id', field:'tacheId', hidden:true},
            {title:'功能', field:'funcCode', width:200},
            {title:'功能Id', field:'funcId', hidden:true},
            {title:'框架页面URL', field:'framePageUrl', width:200},
            {title:'页面模板编码',field:'templateCode', hidden:true},
            {title:'创建时间', field:'createDate', width:180},
            {title:'修改时间', field:'modifyDate', hidden:true},
            {title:'路由', field:'routeId', hidden:true},
            {title:'租户', field:'tenantId', hidden:true}
        ]],
        onClickRow:function (index, row) {
            //激活修改按钮和删除按钮        
            funBtn($('#modRule'), modRule, true);
            funBtn($('#delRule'), delRule, true);
        }
    });
    //模板约束条件-模板约束条件表
    $('#templateConstraintTable').datagrid({
        height:'100%',
        singleSelect:true,
        toolbar:'#constraintToolBar',
        fitColumns:true,
        striped:true,
        columns:[[
            {field: 'ck', checkbox: true },
            {title:'约束标识', field:'consId', hidden:true},
            {title:'页面模板', field:'templateCode', hidden:true},
            {title:'约束类型', field:'consType', width:120},
            {title:'被约束控件', field:'pageCode', width:150},
            {title:'约束条件', field:'consCondition', width:180},
            {title:'约束结果值', field:'resultValue', width:180},
            {title:'约束结果类', field:'resultClass', width:150},
            {title:'约束结果方法', field:'resultMethod', width:150},
            {title:'路由', field:'routeId', hidden:true},
            {title:'租户', field:'tenantId', hidden:true}
        ]],
        onClickRow:function (index, row) {
            funBtn($('#modConstraint'), modConstraint, true);
            funBtn($('#delConstraint'), delConstraint, true);
        }
    });

    //模板约束条件-约束类型下拉框
    $('#consType').combobox({
        valueField:"code",
        textField:"name",
        panelHeight:'auto',
        required:true,
        //选中某个选项
        onChange:function (newValue, oldValue) {
            //初始化配置约束弹窗界面
            $('#optionResultDiv').show();
            $('#optionResultValue').combobox({multiple:false, required:true});
            $('#textResultDiv').hide();
            $('#textResultValue').textbox({required:false});
            $('#resultCMDiv').hide();

            switch(newValue){
                //约束类型为必填、使能、显示时
                case "MUST":
                case "ENABLED":
                case "DISPLAY":
                    $('#optionResultValue').combobox('clear');
                    $('#optionResultValue').combobox('loadData',
                        [{
                            code:'yes',
                            name:'是',
                        },{
                            code:'no',
                            name:'否'
                        }]
                    );
                break;
                //约束类型为联动赋值约束
                case "DEFAULT":
                    //显示结果类和结果方法
                    $('#resultCMDiv').show();
                    $('#optionResultValue').combobox('clear');

                    //被约束控件为空时跳过
                    if(!$('#pageCode').combobox('getValue')) {
                        break;
                    }
                    //查询被约束控件的类型
                    var defaultParam={
                        pageCode:$('#pageCode').combobox('getValue')
                    }
                    var defaultResult= $.callSyn('FormManagerServ', 'qryWidgetType', defaultParam);
                    if(defaultResult[0].elementType=="OPTION"){
                        var session=$.session();
                        var defaultValueParam = {
                            elementCode:'\''+defaultResult[0].elementCode+'\'',
                            workitemId:"",
                            staffId:session.staffId
                        };
                        //获取约束结果值下拉框的数据
                        var valueResult=$.callSyn(defaultResult[0].optionDataClass, defaultResult[0].optionDataMethod, defaultValueParam);
                        $('#optionResultValue').combobox('loadData', valueResult);
                    }else{
                        $('#optionResultDiv').hide();
                        $('#textResultDiv').show();
                        $('#optionResultValue').combobox({required:false});
                        $('#textResultValue').textbox({required:true});
                    }
                break;
                //过滤约束
                case "FILTER":
                    //被约束控件为空时退出
                    if(!$('#pageCode').combobox('getValue')){
                        $.messager.alert("warning","被约束控件为option类型时，才能选择过滤约束")
                        $('#consType').combobox('clear');
                        break;
                    }
                    //查询被约束控件的类型
                    var filterParam={
                        pageCode:$('#pageCode').combobox('getValue')
                    }
                    var filterResult= $.callSyn('FormManagerServ', 'qryWidgetType', filterParam);
                    if(filterResult[0].elementType=="OPTION"){
                        $('#resultCMDiv').show();
                        $('#optionResultValue').combobox('clear');

                        //获取回话id
                        var session=$.session();
                        var filterValueParam = {
                            elementCode:'\''+filterResult[0].elementCode+'\'',
                            workitemId:"",
                            staffId:session.staffId
                        };

                        //获取约束值下拉框的数据
                        var filterValueResult=$.callSyn(filterResult[0].optionDataClass, filterResult[0].optionDataMethod, filterValueParam);
                        //加载数据到下拉框中,这是多选下拉框
                        $('#optionResultValue').combobox({multiple:true, required:true});
                        $('#textResultValue').textbox({required:false});
                        $('#optionResultValue').combobox('loadData', filterValueResult);

                    }else{              //被约束控件不为option时退出
                        $.messager.alert("warning","被约束控件为option类型时，才能选择过滤约束")
                        $('#consType').combobox('clear');
                    }          
                break;
                default:

            }
        }
    });
    //模板约束条件-被约束控件下拉框
    $('#pageCode').combobox({
        valueField:"code",
        textField:"name",
        panelHeight:'auto',
        required:true,
        onChange:function (newValue, oldValue) {
            var consType=$('#consType').combobox('getValue');
            //约束类型为空时，直接退出函数
            if(!consType)
                return;

            //查询选择的控件的类型    
            var param={
                    pageCode:newValue
            };
            var result = $.callSyn('FormManagerServ', 'qryWidgetType', param);
            if(consType=='FILTER'||consType=='DEFAULT'){
                if(result[0].elementType == "OPTION"){
                    $('#optionResultDiv').show();
                    $('#optionResultValue').combobox({multiple:false, required:true});
                    $('#textResultValue').textbox({required:false});
                    $('#textResultDiv').hide();
                    $('#optionResultValue').combobox('clear');
                    if(consType=='FILTER'){
                        $('#optionResultValue').combobox({multiple:true});
                    }

                    //获取回话id
                    var session=$.session();
                    var pageValueParam = {
                        elementCode:'\''+result[0].elementCode+'\'',
                        workitemId:"",
                        staffId:session.staffId
                    };

                    //获取约束结果下拉框的数据
                    var pageValueResult=$.callSyn(result[0].optionDataClass, result[0].optionDataMethod, pageValueParam);
                    $('#optionResultValue').combobox('loadData', pageValueResult);

                } else{
                    $('#optionResultDiv').hide();
                    $('#textResultDiv').show();
                    $('#optionResultValue').combobox({required:false});
                    $('#textResultValue').textbox({required:true});

                    //如果约束类型为过滤约束，不能选择非OPTION类型的控件
                    if(consType=='FILTER'){
                        $.messager.alert("warning", "约束类型为过滤约束，控件只能选择OPTION类型");
                        $('#pageCode').combobox('setValue', oldValue);
                    }
                }
            }
        }
    });

    //模板约束条件-依赖控件下拉框
    $('#relyWidget').combobox({
        valueField:'code',
        textField:'name',
        panelHeight:'auto',
        onSelect:function (item) {
            var param={
                pageCode:item.code
            };
            var result = $.callSyn('FormManagerServ', 'qryWidgetType', param);
            if(result[0].elementType == "OPTION"){
                $('#valueDiv').hide();
                $('#optionDiv').show();
                $('#optionValue').combobox('clear');

                //获取回话id
                var session=$.session();
                var valueParam = {
                    elementCode:'\''+result[0].elementCode+'\'',
                    workitemId:"",
                    staffId:session.staffId
                };

                //获取值下拉框的数据
                var valueResult=$.callSyn(result[0].optionDataClass, result[0].optionDataMethod, valueParam);
                $('#optionValue').combobox('loadData', valueResult);
            }else{
                $('#optionDiv').hide();
                $('#valueDiv').show();
                $('#value').textbox('clear');
            }
        }
    });

    //模板约束条件-操作符下拉框
    $('#operator').combobox({
        panelHeight:'auto',
        valueField:'id',
        textField:'operator',
        data:[{
            id:'>',
            operator:'大于',
            selected:'ture'
        },{
            id:'<',
            operator:'小于'
        },{
            id:'=',
            operator:'等于'
        },{
            id:'>=',
            operator:'大于等于'
        },{
            id:'<=',
            operator:'小于等于'
        },{
            id:'!=',
            operator:'不等于'
        }]
    });
    //模板约束条件-结果值下拉框
    $('#optionResultValue').combobox({
        valueField:'code',
        textField:'name',
        panelHeight:'auto',
        required:true
    });
    //模板约束条件-条件表达式'值'下拉框
    $('#optionValue').combobox({
        width:157,
        valueField:'code',
        textField:'name',
        panelHeight:'auto'
    });

    //模板适用规则-环节名称文本选择框
    $('#tacheCode').textbox({
        // readonly:true,
        required:true,
        prompt:'-全部-',
        icons: [{
            iconCls:'icon-clear',
            handler:function (e) {
                $(e.data.target).textbox('clear');
            }
        },{
            iconCls:'icon-search',
            handler: function(e){
                selectTache();
            }
        }]
    });
    //模板适用规则-流程名称选择框
    $('#packageCode').textbox({
        required:true,
        prompt:'-全部-',
        icons:[{
            iconCls:'icon-clear',
            handler:function (e) {
                $(e.data.target).textbox('clear');
            }
        },{
            iconCls:'icon-search',
            handler:function (e) {
                selectPackage();
            }
        }]
    });
    //模板适用规则-功能下拉框
    $('#funcCode').combobox({
        // readonly:true,
        valueField:'code',
        textField:'name',
        panelHeight:'auto'
    });

    //模板设计-属性列表
    $('#propertiesTable').datagrid({
        height:'100%',
        singleSelect:true,
        toolbar:'#propertiesTableBar',
        striped:true,
        columns:[[
            {title:'模板明细Id', field:'templateDetailId', hidden:true},
            {title:'页面模板编码', field:'templateCode', hidden:true},
            {title:'元素编码', field:'elementName', width:120},
            {title:'控件标签', field:'pageTitle', width:120},
            {title:'控件编码', field:'pageCode', width:120},
            {title:'位于第几行', field:'locateRow', width:120},
            {title:'位于第几列', field:'locateColumn', width:120},
            {title:'对齐方式', field:'align', width:100},
            {title:'是否回填项', field:'isRet', width:100},
            {title:'是否必填', field:'isMust', width:100},
            {title:'是否使能', field:'isEnabled', width:100},
            {title:'是否显示', field:'isDisplay', width:100},
            {title:'是否赋初值', field:'isInit', width:100},
            {title:'备注', field:'comments', width:120},
            {title:'路由id', field:'routeId', hidden:true},
            {title:'租户', field:'tenantId', hidden:true}
        ]]              
    });

    //模板设计-选择元素下拉框
    $('#elementName').combobox({
        valueField:'code',
        textField:'name',
        panelHeight:'auto',
        required:true
    });

    //模板设计-指定增加的行下拉框
    $('#lineNumber').combobox({
        valueField:'code',
        textField:'lineNumber',
        width:80,
        panelHeight:'auto',
        data:lineNumberArray
    });

    /** 函数的定义 **/

    //表单模板-增加模板
    function addTemplate(selectedRow) {
        $('#addTemplateDialog').dialog({
            title:'增加模板',
            width: 400,
            closed: false,
            cache: false,
            modal: true,
            buttons:[{
                text:'确定',
                handler:function () {
                    //获取弹窗的参数
                    var param = {
                        templateName:$('#templateName').textbox('getText'),
                        templateCode:$('#templateCode').textbox('getText'),
                        comments:$('#comments').textbox('getText'),
                        routeId:1,
                        tenantId:""
                    };

                    //校验表单
                    if($('#editTemplateInfo').form('validate') == true){
                        //调用callSyn向数据库中增加数据
                        var result = $.callSyn('FormManagerServ', 'addTemplate', param);

                        if(result.templateId != -1){
                            $('#addTemplateDialog').dialog('close');
                            $.messager.alert("提示", "增加模板成功");

                            //增加到表格中
                            $('#templateTable').datagrid('appendRow', param);
                        }else{
                            $.messager.alert("错误", "增加模板失败, 模板编码不唯一")
                        }
                        
                    } 
                }
            },{
                text:'取消',
                handler:function () {
                    $('#addTemplateDialog').dialog('close');
                } 
            }],
            onBeforeOpen:function () {
                if(selectedRow != null){
                    $('#templateName').textbox('setText', selectedRow.templateName);
                    $('#templateCode').textbox('setText', selectedRow.templateCode);
                    $('#comments').textbox('setText', selectedRow.comments);
                }else{
                    $('#templateName').textbox('setText', "");
                    $('#templateCode').textbox('setText', "");
                    $('#comments').textbox('setText', "");
                }
            }
        });
    }

    //表单模板-修改模板
    function modTemplate(selectedRow) {
        $('#addTemplateDialog').dialog({
            title:'修改模板',
            width:400,
            closed:false,
            cache:false,
            modal:true,
            buttons:[{
                text:'确定',
                handler:function () {
                    //获取弹窗的参数
                    var param = {
                        id:selectedRow.id,
                        templateName:$('#templateName').textbox('getText'),
                        templateCode:$('#templateCode').textbox('getText'),
                        comments:$('#comments').textbox('getText'),
                        createDate:new Date().toLocaleString(),
                        routeId:selectedRow.routeId,
                        tenantId:selectedRow.tenantId
                    };
                
                    //校验表单
                    if($('#editTemplateInfo').form('validate') == true){
                        //调用callSyn修改数据库的数据
                        var result = $.callSyn('FormManagerServ', 'modTemplate', param);

                        if(result.isSuccess){
                            $.messager.alert("提示", "修改模板成功");

                            //修改表格的数据
                            var index = $('#templateTable').datagrid('getRowIndex', selectedRow);
                            $('#templateTable').datagrid('updateRow', {
                                index:index,
                                row:param
                            });
                            
                        }else{
                            $.messager.alert("错误", "修改模板失败");
                        }

                        $('#addTemplateDialog').dialog('close');   
                    }
                }
            },{
                text:'取消',
                handler:function () {
                   $('#addTemplateDialog').dialog('close');
                }
            }],
            onBeforeOpen:function () {
                $('#templateName').textbox('setText', selectedRow.templateName);
                $('#templateCode').textbox('setText', selectedRow.templateCode);
                $('#comments').textbox('setText', selectedRow.comments);
            }
        });
    }

    //表单模板-删除模板
    function delTemplate(selectedRow) {
        //清空模板详情中的数据
        $('#templateDetailTable').datagrid('loadData', {total:0, rows:[]});
        $('#templateRuleTable').datagrid('loadData', {total:0, rows:[]});
        $('#templateConstraintTable').datagrid('loadData', {total:0, rows:[]});
        //失效增删改按钮
        funBtn($('#addRule'), addRule, false);
        funBtn($('#addConstraint'), addConstraint, false);
        funBtn($('#modRule'), modRule, false);
        funBtn($('#modConstraint'), modConstraint, false);
        funBtn($('#delRule'), delRule, false);
        funBtn($('#delConstraint'), delConstraint, false);

        //从数据表中删除
        var param = {
            id:selectedRow.id
        };
        var result = $.callSyn('FormManagerServ', 'delTemplate', param);
        if(result.isSuccess){
            $.messager.alert("提示", "删除模板成功");

            //从Table中删除
            var index = $('#templateTable').datagrid('getRowIndex', selectedRow);
            $('#templateTable').datagrid('deleteRow', index);
        }else{
            $.messager.alert("错误", "删除模板失败");
        }
    }

    //表单模板-设计模板函数
    function designTemplate(item) {
        locateRowNumber=0;
        lineNumberArray=[{"code":locateRowNumber, "lineNumber":'第'+locateRowNumber+'行'}];
        $('#lineNumber').combobox('clear');
        $('#lineNumber').combobox('loadData', lineNumberArray);
        //弹出页面
        $('#templateDesignWindow').window('open');
    }

    //模板适用规则-增加模板适用规则
    function addRule() {
        var selectedRow = $('#templateTable').datagrid('getSelected');
        $('#addRuleDialog').dialog({
            title:'增加模板使用条件',
            width: 400,
            closed: false,
            cache: false,
            modal: true,
            buttons:[{
                text:'确定',
                handler:function () {
                   
                    //获取表单参数
                    var param={
                        packageId:$('#packageId').val(),
                        packageCode:$('#packageCode').textbox('getText'),
                        tacheCode:$('#tacheCode').textbox('getText'),
                        tacheId:$('#tacheId').val(),
                        funcCode:$('#funcCode').combobox('getText'),
                        funcId:$('#funcCode').combobox('getValue'),
                        framePageUrl:$('#framePageUrl').textbox('getText'),
                        templateCode:selectedRow.templateCode,
                        createDate:new Date().toLocaleString(),
                        modifyDate:"",
                        routeId:1,
                        tenantId:""
                    };

                    //校验表单
                    if($('#addRuleForm').form('validate') == true){
                        //TODO 调用函数添加数据到数据库表中
                        var result = $.callSyn('FormManagerServ', 'addTemplateRule', param);

                        //提示结果
                        if(result.ruleId !== null){
                            $.messager.alert("提示", "增加模板适用条件成功");

                            //添加到当前页面的表中
                            $('#templateRuleTable').datagrid('appendRow', param);            
                        }else{
                            $.messager.alert("错误", "增加模板适用条件失败");
                        }
                        
                        $('#addRuleDialog').dialog('close');    
                    }
                }
            },{
                text:'取消',
                handler:function () {
                    $('#addRuleDialog').dialog('close');
                }
            }],
            onBeforeOpen:function () {
                //清空表单
                $('#packageCode').textbox('clear');
                $('#tacheCode').textbox('clear');
                $('#framePageUrl').textbox('clear');
                $('#createDate').datetimebox('clear');
                $('#funcCode').combobox('clear');
                $('#funcCode').combobox('loadData', qryFunc());
            }
        });      
    }

    //模板适用规则-修改模板适用规则
    function modRule() {
        var selectedRow = $('#templateRuleTable').datagrid('getSelected');

        $('#addRuleDialog').dialog({
            title:'修改模板使用条件',
            width: 400,
            closed: false,
            cache: false,
            modal: true,
            buttons:[{
                text:'确定',
                handler:function () {
                    //获取表单参数
                    var param={
                        ruleId:selectedRow.ruleId,
                        packageId:$('#packageId').val(),
                        packageCode:$('#packageCode').textbox('getText'),
                        tacheCode:$('#tacheCode').textbox('getText'),
                        tacheId:$('#tacheId').val(),
                        funcCode:$('#funcCode').combobox('getText'),
                        funcId:$('#funcCode').combobox('getValue'),
                        framePageUrl:$('#framePageUrl').textbox('getText'),
                        templateCode:selectedRow.templateCode,
                        createDate:selectedRow.createDate,
                        modifyDate:new Date().toLocaleString(),
                        routeId:1,
                        tenantId:""
                    };

                    //校验表单
                    if($('#addRuleForm').form('validate') == true){
                        //调用函数修改数据库表中的数据
                        var result = $.callSyn('FormManagerServ', 'modTemplateRule', param);

                        if(result.isSuccess){
                            $.messager.alert("提示", "修改模板适用条件成功");

                            //修改当前页面的数据
                            var index = $('#templateRuleTable').datagrid('getRowIndex', selectedRow);
                            $('#templateRuleTable').datagrid('updateRow', {
                                index:index,
                                row:param
                            });
                        }else{
                            $.messager.alert("错误", "修改模板适用条件失败");
                        }

                        $('#addRuleDialog').dialog('close');
                    }
                }
            },{
                text:'取消',
                handler:function () {
                    $('#addRuleDialog').dialog('close');
                }
            }],
            onBeforeOpen:function () {
                var param={
                    ruleId:selectedRow.ruleId
                };
                var result=$.callSyn('FormManagerServ', 'qryTemplateRule', param);
                //自动填充表单
                $('#packageId').val(result[0].packageCode);
                $('#packageCode').textbox('setText', selectedRow.packageCode);
                $('#tacheCode').textbox('setText', selectedRow.tacheCode);
                $('#tacheId').val(result[0].tacheCode);
                $('#funcCode').combobox('loadData', qryFunc());
                $('#funcCode').combobox('setValue', result[0].funcCode);
                $('#framePageUrl').textbox('setText', selectedRow.framePageUrl);
                $('#createDate').datetimebox('setText', new Date().toLocaleString());

                //限制某些项不能修改
                $('#createDate').datetimebox('disable');
            }
        });       
    }

    //删除模板适用条件
    function delRule() {
        var selectedRow = $('#templateRuleTable').datagrid('getSelected');

        //删除数据库表中的数据
        var param = {
            ruleId:selectedRow.ruleId
        };
        var result = $.callSyn('FormManagerServ', 'delTemplateRule', param);
        if(result.isSuccess){
            $.messager.alert("提示", "删除模板适用条件成功");

            //从当前页面删除
            var index = $('#templateRuleTable').datagrid('getRowIndex', selectedRow);
            $('#templateRuleTable').datagrid('deleteRow', index);
        }else{
            $.messager.alert("错误", "删除模板适用条件失败");
        }

    }

    //模板适用规则-选择环节
    function selectTache() {
        $('#selectTacheDialog').dialog({
            title:'选择环节',
            width:350,
            height:350,
            closed:false,
            maximizable:true,
            iconCls:'icon-save',
            cache:false,
            modal:true,
            buttons:[{
                text:"确定",
                handler:function () {
                    var row = $('#tacheWinTree').tree('getSelected');
                    $('#tacheCode').textbox('setValue',row.text);
                    $('#tacheId').val(row.id);
                    $('#selectTacheDialog').dialog('close');
                }
            },{
                text:"取消",
                handler:function () {
                    $('#selectTacheDialog').dialog('close');
                }
            }],
            onBeforeOpen:function () {
                initTacheWinTree();
                $('#tacheWinTree').find('.tree-node.tree-node-selected').removeClass('tree-node-selected');
                $('#selectTacheDialog').panel("move",{top:$(document).scrollTop() + ($(window).height()-350) * 0.5,left:$(document).scrollLeft() + ($(window).width()-350) * 0.5});
            }
        });
    }

    //模板适用规则-初始化环节树
    var initTacheWinTree = function(){
        var session = $.session();
        var tacheTree = $.callSyn('TacheServ','qryTacheCatalogTree',{systemCode:session["systemCode"]});
        $('#tacheWinTree').tree({
            data : tacheTree,
            onClick:function(node)
            {
                if(node.type==1&&!node.loaded)
                {
                    //加载环节列表
                    var ret = $.callSyn('TacheServ','qryTaches',{tacheCatalogId:node.id,state:'10A',currentDate:1});
                    var taches = [];
                    for(var i = 0;i<ret.rows.length;i++)
                    {
                        var row = ret.rows[i];
                        var subNode = $('#tacheWinTree').tree('find', row.id); 
                        if(subNode){
                            continue;
                        }
                        taches.push({id:row.id,text:row.tacheName,code:row.tacheCode});
                    }
                    node.loaded = true;
                    $('#tacheWinTree').tree('append', { parent : node.target,  data : taches });  
                }
            },
            onContextMenu: function(e, node){
                e.preventDefault();
            }
        });
    };

    //模板适用规则-选择环节
    function selectPackage() {
        $('#selectPackageDialog').dialog({
            title:'选择流程',
            width:350,
            height:350,
            closed:false,
            maximizable:true,
            iconCls:'icon-save',
            cache:false,
            modal:true,
            buttons:[{
                text:"确定",
                handler:function () {
                    var row = $('#flowWinTree').tree('getSelected');
                    $('#packageCode').textbox('setValue',row.text);
                    $('#packageId').val(row.id);
                    $('#selectPackageDialog').dialog('close');
                }
            },{
                text:"取消",
                handler:function () {
                    $('#selectPackageDialog').dialog('close');
                }
            }],
            onBeforeOpen:function () {
                var session = $.session();
                var flowTreeData = $.callSyn("FlowServ","queryPackageCatalogByAreaIdAndSystemCode",{areaId: session["areaId"],systemCode:session["systemCode"]});
                initFlowWinTree(flowTreeData);
                $('#flowWinTree').find('.tree-node.tree-node-selected').removeClass('tree-node-selected');
                $('#flowWin').panel("move",{top:$(document).scrollTop() + ($(window).height()-350) * 0.5,left:$(document).scrollLeft() + ($(window).width()-350) * 0.5});
            }
        });
    }            
    //模板适用规则-初始化流程选择树
    var initFlowWinTree = function(flowTreeData){
        var nodeType = {
            CATALOG:"1",        //目录
            ELEMENT:"2",        //元素--环节或者流程
            VERSION:"3"         //版本--流程
        };

        var subLoad = function(node){
                if(node.type==1&&!node.loaded){
                    //加载环节列表
                    var ret = $.callSyn('FlowServ','qryProcessDefineByCatalogId',{catalogId:node.id});
                    node.loaded = true;
                    $('#flowWinTree').tree('append', { parent : node.target,  data : ret });  
                }
        };
        $('#flowWinTree').tree({
            data : flowTreeData,
            onExpand:function(node){
                subLoad(node);
            },
            onClick:function(node){
                if(node.type==nodeType.CATALOG){
                    subLoad(node);
                }
            }
        });
    };



    //模板约束条件-配置模板约束条件
    function addConstraint() {
        var selectedRow=$('#templateTable').datagrid('getSelected');

        $('#addConstraintDialog').dialog({
            title:'配置模板约束',
            width: 630,
            closed: false,
            cache: false,
            modal: true,
            buttons:[{
                text:'确定',
                handler:function () {
                    //获取表单参数
                    var resultClass=$('#resultClass').textbox('getText');
                    var resultMethod=$('#resultMethod').textbox('getText');
                    var resultValue=$('#optionResultValue').combobox('getText');
                    if($('#resultCMDiv').is(':hidden')){
                        resultClass=""
                        resultMethod="";
                    }
                    if($('#optionResultDiv').is(':hidden')){
                        resultValue=$('#textResultValue').textbox('getText');
                    }
                    var param={
                        consType:$('#consType').combobox('getText'),
                        templateCode:selectedRow.templateCode,
                        pageCode:$('#pageCode').combobox('getText'),
                        consCondition:$('#consCondition').textbox('getText'),
                        resultValue:resultValue,
                        resultClass: resultClass,
                        resultMethod: resultMethod,
                        routeId:1,
                        tenantId:""
                    };

                    //表单校验
                    if($('#pageAndTypeForm').form('validate')&&$('resultForm').form('validate')){
                        //调用函数添加数据到数据库表中
                        var result = $.callSyn('FormManagerServ', 'addPageConstraint', param);

                        if(result.consId !== null){
                            $.messager.alert("提示", "增加模板约束条件成功");
                            //添加到当前页面的表中
                            $('#templateConstraintTable').datagrid('appendRow', param);                                    
                        } else {
                            $.messager.alert("error", "增加模板约束条件失败");
                        }

                        $('#addConstraintDialog').dialog('close');
                    }
                }
            },{
                text:'取消',
                handler:function () {
                    $('#addConstraintDialog').dialog('close');
                }
            }],
            onBeforeOpen:function () {
                // 初始化配置约束弹窗界面
                $('#optionResultDiv').show();
                $('#optionResultValue').combobox({required:true});
                $('#textResultDiv').hide();
                $('#textResultValue').textbox({required:false});
                $('#resultCMDiv').hide();

                //初始化数据
                $('#consType').combobox('loadData', qryConsType());
                $('#pageCode').combobox('loadData', qryPageCode());
                $('#relyWidget').combobox('loadData', qryPageCode());
                $('#relyWidget').combobox('clear');
                $('#consType').combobox('clear');
                $('#pageCode').combobox('clear');
                $('#consCondition').textbox('clear');
                $('#optionValue').combobox('clear');
                $('#optionValue').combobox('loadData',[]);
                $('#value').textbox('clear');
                $('#optionResultValue').combobox('clear');
                $('#optionResultValue').combobox('loadData',[]);
                $('#textResultValue').textbox('clear');
                $('#resultClass').textbox('clear');
                $('#resultMethod').textbox('clear');
            }
        });      
    }

    //修改模板约束条件配置
    function modConstraint() {
        var selectedRow = $('#templateConstraintTable').datagrid('getSelected');

        $('#addConstraintDialog').dialog({
            title:'修改模板约束',
            width: 630,
            closed: false,
            cache: false,
            modal: true,
            buttons:[{
                text:'确定',
                handler:function () {
                    //获取表单参数
                    var resultClass=$('#resultClass').textbox('getText');
                    var resultMethod=$('#resultMethod').textbox('getText');
                    var resultValue=$('#optionResultValue').combobox('getText');
                    if($('#resultCMDiv').is(':hidden')){
                        resultClass=""
                        resultMethod="";
                    }
                    if($('#optionResultDiv').is(':hidden')){
                        resultValue=$('#textResultValue').textbox('getText');
                    }
                    var param={
                        consId:selectedRow.consId,
                        consType:$('#consType').textbox('getText'),
                        templateCode:selectedRow.templateCode,
                        pageCode:$('#pageCode').combobox('getText'),
                        consCondition:$('#consCondition').textbox('getText'),
                        resultValue:resultValue,
                        resultClass:resultClass,
                        resultMethod:resultMethod,
                        routeId:selectedRow.routeId,
                        tenantId:selectedRow.tenantId
                    };

                    if($('#pageAndTypeForm').form('validate')&&$('resultForm').form('validate')){
                         //调用函数修改数据库表中的数据
                        var result = $.callSyn('FormManagerServ', 'modPageConstraint', param);

                        if(result.isSuccess){
                            $.messager.alert("提示", "修改模板约束条件成功");

                            //修改当前页面的表格的数据
                            var index = $('#templateConstraintTable').datagrid('getRowIndex', selectedRow);
                            $('#templateConstraintTable').datagrid('updateRow', {
                                index:index,
                                row:param
                            }); 
                        } else {
                            $.messager.alert("error", "修改模板约束条件失败");
                        }

                        $('#addConstraintDialog').dialog('close');
                    }
                }
            },{
                text:'取消',
                handler:function () {
                    $('#addConstraintDialog').dialog('close');
                }
            }],
            onBeforeOpen:function () {

                //初始化数据
                var consType=qryConsType();
                var pageCode=qryPageCode();
                $('#consType').combobox('loadData', consType);
                $('#pageCode').combobox('loadData', pageCode);
                $('#relyWidget').combobox('loadData', pageCode);
                $('#pageCode').combobox('setValue', getValue(pageCode, selectedRow.pageCode));
                $('#consType').combobox('setValue', getValue(consType, selectedRow.consType));
                $('#relyWidget').combobox('clear');

                $('#consCondition').textbox('setText', selectedRow.consCondition);
                if($('#optionResultDiv').is(':hidden')){
                    $('#textResultValue').textbox('setText',selectedRow.resultValue);
                }else{
                    $('#optionResultValue').combobox('setText', selectedRow.resultValue);
                }
                $('#resultClass').textbox('setText', selectedRow.resultClass);
                $('#resultMethod').textbox('setText', selectedRow.resultMethod);
            }
        });    
    }

    //删除模板约束条件配置
    function delConstraint() {
        var selectedRow = $('#templateConstraintTable').datagrid('getSelected');

        //从数据库中删除
        var param = {
            consId:selectedRow.consId
        };
        var result = $.callSyn('FormManagerServ', 'delPageConstraint', param);
        if(result.isSuccess){
            $.messager.alert("提示", "删除模板约束条件成功");

            //从当前页面表格中删除
            var index = $('#templateConstraintTable').datagrid('getRowIndex', selectedRow);
            $('#templateConstraintTable').datagrid('deleteRow', index);    
        }else{
            $.messager.alert("错误", "删除模板约束条件失败");
        }
    }

    //模板设计-新增一行
    function addNewLine() {
        $('#addNewLineDialog').dialog({
            title:'新增一行',
            width: 600,
            height:350,
            closed: false,
            cache: false,
            modal: true,
            buttons:[{
                text:'确定',
                handler:function () {
                    //TODO 在模板设计窗口中显示控件预览
                    
                    //指定函数下拉框的数值
                    locateRowNumber++;
                    lineNumberArray.push({"code":locateRowNumber, "lineNumber":'第'+locateRowNumber+'行'});
                    $('#lineNumber').combobox('loadData', lineNumberArray);

                    $('#addNewLineDialog').dialog('close');
                }
            },{
                text:'取消',
                handler:function () {
                    $('#addNewLineDialog').dialog('close');
                }
            }],
            onBeforeOpen:function () {
                locateColumnNumber=0;
                //清除上次的数
                $('#propertiesTable').datagrid('loadData', {total:0, rows:[]})
            }
        });
    }
    //模板设计-在指定行之后新增一行
    function addNewLineAfterOne() {
       $('#addNewLineDialog').dialog({
            title:'新增一行(在指定行之后)',
            width: 600,
            height:350,
            closed: false,
            cache: false,
            modal: true,
            buttons:[{
                text:'确定',
                handler:function () {
                    //TODO 在模板设计窗口中显示控件预览
                    
                    //更新指定行的数值
                    locateRowNumber++;
                    lineNumberArray.push({"code":locateRowNumber, "lineNumber":'第'+locateRowNumber+'行'});
                    $('#lineNumber').combobox('loadData', lineNumberArray);
                    $('#addNewLineDialog').dialog('close');
                }
            },{
                text:'取消',
                handler:function () {
                    $('#addNewLineDialog').dialog('close');
                }
            }],
            onBeforeOpen:function () {
                locateColumnNumber=0;
                //清除上次的数
                $('#propertiesTable').datagrid('loadData', {total:0, rows:[]})
                locateRowNumber=parseInt($('#lineNumber').combobox('getValue'));
            }
        }); 
    }
    //模板设计-删除一行
    function delOneLine() {
        
    }
    //模板设计-关闭窗口
    function closeWindow() {
        
    }
    //模板设计-新增属性
    function addProperties() {
        
        $('#addPropertiesDialog').dialog({
            title:'新增属性',
            width: 400,
            height:430,
            closed: false,
            cache: false,
            modal: true,
            buttons:[{
                text:'确定',
                handler:function () {
                    //获取表单参数
                    var param={
                        templateCode:$('#templateTable').datagrid('getSelected').templateCode,
                        elementName:$('#elementName').combobox('getValue'),
                        pageTitle:$('#pageTitle').val(),
                        pageCode:$('#widgetCode').val(),
                        locateRow:locateRowNumber+1,
                        locateColumn:locateColumnNumber+1,
                        align:$('#align').find('option:selected').text(),
                        isRet:$('#isRet').find('option:selected').text(),
                        isMust:$('#isMust').find('option:selected').text(),
                        isEnable:$('#isEnabled').find('option:selected').text(),
                        isDisplay:$('#isDisplay').find('option:selected').text(),
                        isInit:$('#isInit').find('option:selected').text(),
                        comments:$('#propertiesComments').val(),
                        routeId:1
                    }

                    //表单校验
                    if($('#addPropertiesForm').form('validate')==true){
                        //添加到数据库中
                        var addPropertiesResult = $.callSyn('FormManagerServ', 'addTemplateDetail', param);
                        if(addPropertiesResult.templateDetailId!==null){
                            $.messager.alert("提示", "添加属性成功");
                            //添加到表格中        
                            $('#propertiesTable').datagrid('appendRow', param);
                            locateColumnNumber++;
                        }else{
                            $.messager.alert("错误", "添加属性失败"); 
                        }
                        $('#addPropertiesDialog').dialog('close');
                    }
                }
            },{
                text:'取消',
                handler:function () {
                    $('#addPropertiesDialog').dialog('close');
                }
            }],
            onBeforeOpen:function () {
                $('#elementName').combobox('clear');
                $('input.propertiesInputStyle').val("");
                $('select.propertiesInputStyle').val("yes");
                $('#align').val("left");
                $('#elementName').combobox('loadData', qryElementName());
            }
        });
    }


    /** 数据库数据查询函数 **/

    //查询模板列表
    function qryTemplate() {
        var result = $.callSyn('FormManagerServ', 'qryTemplate', {});
        return result;
    }
    //查询模板约束类型
    function qryConsType() {
        var param = {
            codeColumn:"CONS_TYPE",
            nameColumn:"CONS_NAME",
            tableName:"UOS_PAGE_CONSTRAINT_TYPE"
        };
        var result = $.callSyn('FormManagerServ', 'qryCombox', param);
        return result;
    }
    //查询被约束控件
    function qryPageCode() {
        var selectedRow = $('#templateTable').datagrid('getSelected');
        var param={
            codeColumn:"PAGE_CODE",
            nameColumn:"PAGE_TITLE",
            tableName:"UOS_PAGE_TEMPLATE_DETAIL",
            whereColumnName:"TEMPLATE_CODE",
            whereColumnValue:selectedRow.templateCode
        };
        var result = $.callSyn('FormManagerServ', 'qryCombox', param);
        return result;
    }
    function qryFunc() {
        var param = {
            codeColumn:"FUNC_CODE",
            nameColumn:"FUNC_NAME",
            tableName:"UOS_PAGE_FUNC"
        };
        var result = $.callSyn('FormManagerServ', 'qryCombox', param);
        return result;
    }
    //查询所有元素的名称
    function qryElementName() {
        var param = {
            codeColumn:"ELEMENT_CODE",
            nameColumn:"ELEMENT_NAME",
            tableName:"UOS_PAGE_ELEMENT"
        };
        var result = $.callSyn('FormManagerServ', 'qryCombox', param);
        return result;
    }



    //根据combobox的text获取value
    function getValue(array, text) {
        for(var object in array){
            if(array[object].name==text){
                return array[object].code;
            }
        }
        return null;
    }

    //easyui-linkbutton 点击问题(disable无法去除点击事件)
    var funBtn= function(btn, fun, enable){
        if(enable){
            btn.unbind('click').bind('click',fun);//每次绑定前先取消上次的绑定
            btn.linkbutton('enable');//激活
        }else{
            btn.unbind('click',fun);
            btn.linkbutton('disable');//失效
        }
    }
});