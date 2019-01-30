/**
 * 角色管理
 */
layui.use(['form' ,'tree','layer', 'table'], function(){
    var table = layui.table;
    var form = layui.form;
    tableRole = table.render({
        elem: '#roleList'
        ,url:'/auth/getRoleList'
        ,cellMinWidth: 80
        ,page: true,
        request: {
            pageName: 'page' //页码的参数名称，默认：page
            ,limitName: 'limit' //每页数据量的参数名，默认：limit
        },response:{
            statusName: 'code' //数据状态的字段名称，默认：code
            ,statusCode: 200 //成功的状态码，默认：0
            ,countName: 'totals' //数据总数的字段名称，默认：count
            ,dataName: 'list' //数据列表的字段名称，默认：data
        }
        ,cols: [[
            {type:'numbers'}
            ,{field:'id', title:'ID', width:80, unresize: true, sort: true}
            ,{field:'code', title:'编码'}
            ,{field:'role_name', title:'名称'}
            ,{field:'descpt', title: '说明',}
            ,{field:'create_time', title: '添加时间',align:'center'}
            ,{fixed:'right', title:'操作', width:260,align:'center', toolbar:'#optBar'}
        ]]
        ,  done: function(res, curr, count){
            pageCurr=curr;
        }
    });
    
    //监听提交
    form.on('submit(roleSubmit)', function(data){
        // TODO 校验
    	submitAjax(data);
        return false;
    });
    
    //提交权限
    //<![CDATA[ 
    form.on('submit(permSubmit)', function(data){
    
        //获取选中的权限
        var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
        var nodes = treeObj.getCheckedNodes(true);
        //选中的复选框
        var nodeIds = new Array();
        for (var i = 0; i < nodes.length; i++) {
            nodeIds.push(nodes[i].id);
        }
        $.post("/auth/addRolePerms", {perms: nodeIds, rid: $("#rid").val()},function(data){
            if(data.code == "200"){
	            layer.alert(data.msg, function(){
	                layer.closeAll();
	            });
            }
    	});
        return false;
    });
   //]]>
    
    


    //监听工具条
    table.on('tool(roleTable)', function(obj){
        var data = obj.data;
        if(obj.event === 'del'){
            delRole(data.id);
        } else if(obj.event === 'edit'){
            //编辑
        	openRole(data, "编辑角色");
        }else if(obj.event == 'perm'){
        	//设置权限
        	openPerm(data.id);
        }
    });
    
	  // zTree 的参数配置
	  var setting = {
	      check: {
	          enable: true,
	          chkboxType:{ "Y":"p", "N":"s"}
	      },
	      data: {
	          simpleData: {
	        	  idKey: "id",
	        	  pIdKey: "pid",
	              enable: true
	          }
	      }
	  };
    function openPerm(rid){
    	$("#rid").val(rid);
	    $.get("/auth/getpermList", {"rid": rid},function(data){
	    	console.log(data)
	        if(data.code == "200"){
	        	 $.fn.zTree.init($("#treeDemo"), setting, data.data);
	        }
		});
    	
        layer.open({
            type:1,
            title: "权限",
            fixed:false,
            resize :false,
            shadeClose: true,
            area: ['550px'],
            content:$('#setPerm'),
    	    success:function(){
    	    	
    	    }
        });
    }
    
    //删除
    function delRole(id) {
        layer.confirm('您确定要删除该角色吗？', {
            btn: ['确认','返回'] //按钮
        }
        , function(){
    	    $.post("/auth/delRole", {"id": id},function(data){
    	        if(data.code == "200"){
    	            //回调弹框
    	            layer.alert("删除成功！",function(){
    	                layer.closeAll();
    	                tableRole.reload({where:{}});
    	            });
    	        }else{
    	        	layer.alert(data,function(){
    	            	layer.closeAll();
    	            });
    	        }
    		});
        }
        );
    }
    
    //提交表单
    function submitAjax(obj){
        $.ajax({
            type: "POST",
            data: $("#roleForm").serialize(),
            url: "/auth/addRole",
            success: function (data) {
                if (data.code == "200") {
                    layer.alert("操作成功",function(){
                    	tableRole.reload({where:{}});
                    	layer.closeAll();
                    });
                } else {
                	layer.alert(data,function(){
                    	layer.closeAll();
                    });
                }
            },
            error: function () {
                layer.alert("操作请求错误，请您稍后再试",function(){
                    layer.closeAll();
                });
            }
        });
    }
    
    //创建用户
    $("#addRole").bind("click", function(){
    	 openRole(null,"添加角色");
    });
    
    function openRole(data, title){
        layer.open({
            type:1,
            title: title,
            fixed:false,
            resize :false,
            shadeClose: true,
            area: ['550px'],
            content:$('#roleDiv'),
    	    success:function(){
    		  if(data){
    			  //编辑
    			 // form.val("user", data);
    			 for(var p in data){
    				 $("#"+p).val(data[p]);
    			 }
    		  }else{
    			  $("#id").val("");
    			  $("#roleForm")[0].reset();
    		  }
    		  form.render();
    	   }
        });
    }
});
