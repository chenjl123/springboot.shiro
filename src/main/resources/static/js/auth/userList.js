/**
 * 用户列表
 */
var pageCurr;
layui.use(['jquery', 'table', 'form', 'element'], function(){
    var table = layui.table
    var form = layui.form;

    tableIns=table.render({
        elem: '#uesrList'
        ,url:'/user/getUsers'
    	,method: 'post' //默认：get请求
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
            ,{field:'user_code', title:'用户名'}
            ,{field:'mobile', title:'手机号'}
            ,{field:'email', title: '邮箱',}
            ,{field:'role_names', title: '角色名称', }
            ,{field:'create_time', title: '添加时间'}
            ,{fixed:'', title:'操作', width:260, toolbar:'#optBar'}
        ]]
        ,  done: function(res, curr, count){
            pageCurr=curr;
        }
    });

    var allRoles = [];
    //获取所有角色
    $.get("/auth/allRoles", {},function(data){
        if(data.code == "200"){
        	allRoles = data.data;
        }
	});
    
    //监听工具条
    table.on('tool(userTable)', function(obj){
        var data = obj.data;
        if(obj.event === 'del'){
            delUser(data.id, data.user_code);
        } else if(obj.event === 'edit'){
            //编辑
        	openUser(data, "编辑用户");
        } else if(obj.event == 'role'){
        	openRole(data.id);
        }
    });
    //监听提交
    form.on('submit(userSubmit)', function(data){
        // TODO 校验
    	submitAjax(data);
        return false;
    });
    
    //设置角色
    //<![CDATA[ 
    form.on('submit(roleSubmit)', function(data){
        //选中的角色
        var array = new Array();
        var roleCheckd=$(".layui-form-checked");
        //获取选中的权限id
        for(var i=0;i<roleCheckd.length;i++){
            array.push($(roleCheckd.get(i)).prev().val());
        }
        console.log(array);
        $.post("/auth/addUserRoles", {roles: array, uid: $("#ruid").val()},function(data){
            if(data.code == "200"){
	            layer.alert(data.msg, function(){
	                layer.closeAll();
	            });
            }
    	});
        return false;
    });
   //]]>
    
    //搜索框
    form.on('submit(searchSubmit)', function(data){
        //重新加载table
        load(data);
        return false;
    });
    
    //创建用户
    $("#addUser").bind("click", function(){
    	 openUser(null,"添加用户");
    });
    
    function openUser(data, title){
        layer.open({
            type:1,
            title: title,
            fixed:false,
            resize :false,
            shadeClose: true,
            area: ['550px'],
            content:$('#setUser'),
    	    success:function(){
    		  if(data){
    			  //编辑
    			 // form.val("user", data);
    			 for(var p in data){
    				 $("#"+p).val(data[p]);
    			 }
    		  }else{
    			  $("#id").val("");
    			  $("#userForm")[0].reset();
    		  }
    		  form.render();
    	   }
        });
    }
    
    function openRole(uid){
    	$("#roleDiv").empty();
    	$("#ruid").val(uid);
        $.get("/auth/getRoleByUser", {uid: uid},function(data){
            if(data.code == "200"){
            	var uroles = data.data;
            	console.log(uroles);
            	$.each(allRoles, function (index, item) {
            		var checked = "";
            		$.each(uroles, function (i, urole) {
            			if(urole.id == item.id){
            				checked = "checked";
            			}
                    });
                    var roleInput=$("<input type='checkbox' "+checked+" name='roleId' value="+item.id+" title="+item.role_name+" lay-skin='primary'/>");
                    var div=$("<div class='layui-unselect layui-form-checkbox' lay-skin='primary'>" +
                        "<span>"+item.role_name+"</span><i class='layui-icon'>&#xe626;</i>" +
                        "</div>");
                    $("#roleDiv").append(roleInput).append(div);
                    layui.form.render('checkbox');
            	});
            }
    	});
        layui.form.render('checkbox');
        layer.open({
            type:1,
            title: "角色",
            fixed:false,
            resize :false,
            shadeClose: true,
            area: ['550px'],
            content:$('#setRole'),
    	    success:function(){
    	   }
        });
    }

    //删除
    function delUser(id, name) {
        layer.confirm('您确定要删除'+name+'用户吗？', {
            btn: ['确认','返回'] //按钮
        }
        , function(){
    	    $.post("/user/delUser", {"id": id},function(data){
    	        if(data.code == "200"){
    	            //回调弹框
    	            layer.alert("删除成功！",function(){
    	                layer.closeAll();
    	                tableIns.reload({where:{}});
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

    function load(obj){
        //重新加载table
        tableIns.reload({
            where: obj.field
            , page: {
                curr: pageCurr //从当前页码开始
            }
        });
    }

    //提交表单
    function submitAjax(obj){
        $.ajax({
            type: "POST",
            data: $("#userForm").serialize(),
            url: "/user/setUser",
            success: function (data) {
                if (data.code == "200") {
                    layer.alert("操作成功",function(){
                    	tableIns.reload({where:{}});
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

});



