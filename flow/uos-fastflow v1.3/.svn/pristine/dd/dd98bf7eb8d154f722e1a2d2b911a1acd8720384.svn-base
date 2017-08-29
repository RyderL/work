define([
	'hbs!template/dialog/SetFlowParamDlg.html',
    '../../../../../ext/jquery.call.js',
], function (template) {
	var tag = 'SetFlowParamDlg';
	var SetFlowParamDlg = fish.View.extend({
		el:false,
		template: template,
		events: {
			"click #submit-button" : "submit",
			"click #cancle-button" : "cancle",
			"click #commonFlowParamGridAdd" : "addCommonParam",
			"click #commonFlowParamGridModify" : "modCommonParam",
			"click #commonFlowParamGridDelete" : "delCommonParam"
		},
		initialize: function(options) {
			this.processDefineId = fish.clone(options.processDefineId);
			this.systemCode = fish.clone(options.systemCode);
			this.paramType = fish.clone(options.paramType);
			this.tacheCode = fish.clone(options.tacheCode);
		},
        beforeRender: function () {
            console.log(tag, '--------beforeRender--------');
        },
		afterRender: function() {
            console.log(tag, '--------afterRender--------');
			this._initCommonGrid();
			//查询应用于某个流程的流程参数 type:FLOW/TACHE
			this._initFlowParamGrid();
		},
		_initCommonGrid: function () {
			var me = this;
			var mydata = $.callSyn("FlowServ","qryFlowParamDefs",{systemCode:this.systemCode});
			this.$("#commonFlowParamGrid").grid({
				data: mydata ,
				datatype: "json",
				width:'100%',
				height: 150,
				colModel: [{
					name: 'code',
					label: '参数编码',
					width: 160
				}, {
					name: 'name',
					width: 100,
					label: '参数名称'

				},{
					name: 'value',
					width: 100,
					label: '参数值'

				},{
					name: 'isVariable',
					width: 100,
					label: '是否可变',
					formatter: function(cellval, opts, rwdat, _act) {
						if(cellval=='1'){
							return '是';
						}else{
							return '否';
						}
					}
				},{
					name: 'comments',
					width: 100,
					label: '备注'

				},{
					name: 'systemCode',
					label: '系统编码',
					hidden:true
				}],
				pager: false,
				showMask:true,
				shrinkToFit:true,
				onDblClickRow: function (e, rowid, iRow, iCol) {//双击行事件
					var rows = $('#flowParamGrid').grid('getRowData');
		    		var data = $('#commonFlowParamGrid').grid('getRowData',rowid);
			    	var insertFlag = true;
			    	$.each(rows,function(i,n){
			    		if(data.code==n.code){
			    			insertFlag = false;
			    			$('#flowParamGrid').grid("setSelection", n);
			    		}
			    	});
			    	if(insertFlag){
			    		var param = {
				    		packageDefineId:me.processDefineId,
							code:data.code,
							value:data.value,
							type:me.paramType,
							isVariable:data.isVariable
						};
				    	$('#flowParamGrid').grid('addRowData',param);
				    	if(rows.length>1){
				    		$('#flowParamGrid').grid("setSelection", param);
				    	}
			    	}
			    }
			});
        },	
        _initFlowParamGrid:function(){
        	var params = {
        			packageDefineId:this.processDefineId,
        			type:this.paramType,
        			tacheCode:this.tacheCode
        	};
			var flowParamDefs = $.callSyn("FlowServ","qryFlowParamDefRels",params);
			this.$("#flowParamGrid").grid({
				datatype: "json",
				width:'100%',
				height: 150,
				colModel: [{
					name: 'code',
					label: '参数编码',
					width: 160
				},{
					name: 'value',
					width: 100,
					label: '参数值'

				}, {
					name: 'type',
					width: 100,
					label: '类型',
					formatter: function(cellval, opts, rwdat, _act) {
						if(cellval=='FLOW'){
							return '流程';
						}else{
							return '环节';
						}
					}

				},{
					name: 'isVariable',
					width: 100,
					label: '是否可变',
					formatter: function(cellval, opts, rwdat, _act) {
						if(cellval=='1'){
							return '是';
						}else{
							return '否';
						}
					}
				},{
					name:'del', 
					label: '操作',
					width:100,
					formatter: function(cellval, opts, rwdat, _act) {
						return '<button type="button" class="btn btn-link js-delete"><img src="/uos-manager/common/easyui/themes/icons/cancel.png"></button>';
//						return '<img id="deleteBtn" src="/uos-manager/common/easyui/themes/icons/cancel.png">';
			        }
				},{
					name: 'systemCode',
					label: '系统编码',
					hidden:true
				}],
				pager: false,
				showMask:true,
				shrinkToFit:true,
				data: flowParamDefs 
			});

	        $("#flowParamGrid").on('click', '.js-delete', function() {
	        	var row = $('#flowParamGrid').grid('getSelection');
	        	$("#flowParamGrid").grid('delRowData',row);
	        });
        },
		submit: function() {
    		var rows = $('#flowParamGrid').grid('getRowData');
			var params = {
				packageDefineId:this.processDefineId,
				type:this.paramType,
				tacheCode:this.tacheCode,
				rows:rows
			};
			var ret = $.callSyn("FlowServ","delAddBatchFlowParamDefRel",params);
			
			this.popup.close(ret);
		},
		cancle:function(){
			this.popup.close();
		},
		delFlowParam: function(rowData){
			$("#flowParamGrid").grid('delRowData',rowData);
		},
		addCommonParam:function(){
			 var me = this;
			 var pop = fish.popupView({url: 'views/dialog/FlowParamInfoDlg',
		            width: "70%",	viewOption:{systemCode:this.systemCode},
		            callback:function(popup,view){
		            	popup.result.then(function (data) {
		            		var isHas = false;
		    				$.each($("#commonFlowParamGrid").grid("getRowData"),function(i,n){
		    					if(n.code==data.code){
		    						isHas = true;
		    					}
		    				});
		    				if(isHas){
		    					$.messager.alert("提示","参数编码重复");
		    					return;
		    				}else{
		    					var ret = $.callSyn("FlowServ","addFlowParamDef",data);
		    					if(ret){
		    						$('#commonFlowParamGrid').grid('addRowData',ret);
		    					}
		    				}
				         	fish.info('新增成功');
		            	},function (e) {
		            		console.log('关闭了',e);
		            	});
		            }
	            });
		},
		modCommonParam:function(){
			 var me = this;
			 var rowData = $("#commonFlowParamGrid").grid("getSelection");
			 var pop = fish.popupView({url: 'views/dialog/FlowParamInfoDlg',
		            width: "70%",	viewOption:{paramData:rowData,systemCode:this.systemCode},
		            callback:function(popup,view){
		            	popup.result.then(function (data) {
		            		if(data){
		            			var ret = $.callSyn("FlowServ","modFlowParamDef",data);
			    				if(ret){
			    					var mydata = $.callSyn("FlowServ","qryFlowParamDefs",{systemCode:me.systemCode});
//			    					var rowid = $('#commonFlowParamGrid').grid("getGridParam", "selrow");
			    					$('#commonFlowParamGrid').grid('reloadData',mydata);
			    					$('#commonFlowParamGrid').grid('setSelection',ret);
			    				};
			    				fish.info('修改成功');
		            		}
		            	},function (e) {
		            		console.log('关闭了',e);
		            	});
		            }
	            });
		},
		delCommonParam:function(){
			var row = $("#commonFlowParamGrid").grid("getSelection");
			if(row){
				var ret = $.callSyn("FlowServ","isExistRela",{code:row.code});
				if(ret&&ret.isExist){
					fish.info('流程参数已被使用，不能删除');
				}else{
					fish.confirm('是否确认删除该流程参数？').result.then(function(){
						$("#commonFlowParamGrid").grid("delRowData",row);
						var result = $.callSyn("FlowServ","delFlowParamDef",{code:row.code});
						fish.info('删除成功');
					});
				}
			}else{
				fish.info('请选择你要删除的行');
			}
		}
	});
	return SetFlowParamDlg;
});

