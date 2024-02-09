<%--
  Created by IntelliJ IDEA.
  User: mayn
  Date: 2022/1/24
  Time: 13:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>


    <%--------------------前端-----------------%>
    $.ajax({
        url:"",
        data:{

        },
        type:"",
        dataType:"json",
        success:function (data) {

        }
    })

    $("#")

    pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
    ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

    //创建页面的日历插件
    $(".time").datetimepicker({
    minView: "month",
    language:  'zh-CN',
    format: 'yyyy-mm-dd',
    autoclose: true,
    todayBtn: true,
    pickerPosition: "bottom-left"
    });

    <%--------------------controller-----------------%>
    String id= UUIDUtil.getUUID();
    String createTime= DateTimeUtil.getSysTime();
    String createBy= ((User) request.getSession().getAttribute("user")).getName();

    String pageNoStr=request.getParameter("pageNo");
    int pageNo=Integer.valueOf(pageNoStr);

    String pageSizeStr=request.getParameter("pageSize");
    int pageSize=Integer.valueOf(pageSizeStr);

    Map<String,String> map = new HashMap<String,String>();


    <%--------------------Service-----------------%>
    boolean flag=true;

    int count=activityRemarkDao.deleteRemark(id);

    if(count!=1){
    flag=false;
    }
    return flag;






</body>
</html>
