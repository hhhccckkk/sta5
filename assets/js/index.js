
//初始化hall列表
//$(document).ready(function () {
//    if ($("#hall_list_box").length != 0) {
//        initTicketInfo(appendToListBox);
//    } else {
//        initTicketInfo(appendToTicketBox);
//    }
//});

var allInfo; //存储返回来的xml

//initialize the hall page

function initTicketInfo(appendList) {

    if (window.navigator.onLine) {
        $.ajax({
            type: "get",
            data: { arg0: "成都东" },
            //默认为true，如果需要发送同步请求，请将此选项设置为flase，设置为false后error里面的方法将不会执行
            async: true,
            url: "http://42.121.1.23:8083/GSMService/train/loadByStation",
            dataType: "jsonp",
            //传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
            jsonp: "callback",
            //自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以写"?"，jQuery会自动为你处理数据
            jsonpCallback: "trainStation",
            success: function (data) {
                allInfo = data;
                appendList("成都东");
                $("#center_loader").fadeOut(200);
            },
            timeout: 10000,
            error: function () {
                setTimeout(function () {
                    if ($("#hall_list_box").html() == 0) {
                        $("#hall_list_box").append("<div class='content_padding'><h3>暂无火车票相关数据</h3><p>您可以通过以下几种方式解决该问题：</p><p>1.请检查您的设备是否接入网络。</p><p>2.请查看是否更新到最新版本。</p></div>");
                        $("#center_loader").fadeOut(200);
                        window.actAndroid.offLine();
                    }
                }, 100);
            }
        });
    } else {
        $("#hall_list_box").append("<div class='content_padding'><h3>暂无火车票相关数据</h3><p>您可以通过以下几种方式解决该问题：</p><p>1.请检查您的设备是否接入网络。</p><p>2.请查看是否更新到最新版本。</p></div>");
        $("#center_loader").fadeOut(200);
        window.actAndroid.offLine();
    }
}

//append to list box
function appendToListBox(selectValue) {
    $("#hall_list_box").empty();
    var selectBoxValue = selectValue;
    for (var i = 0; i < allInfo.list.length; i++) {
        var endStation = allInfo.list[i].trainlist.endStation;
        var trainId = allInfo.list[i].trainlist.id;
        var startStation = allInfo.list[i].trainlist.startStation;
        var RDate = allInfo.list[i].trainlist.RDate;
        var ATime = allInfo.list[i].ATime;
        var DTime = allInfo.list[i].DTime;

        var trainSplit = trainId.split("/");
        //判断是否为成都
        var realStartTime = isChengdu(ATime, DTime, startStation);
        //获得到达时间
        var arriveDate = timeAdd(realStartTime, RDate);
        //执行list
        for (var j = 0; j < trainSplit.length; j++) {
            if (startStation != selectBoxValue) {
                continue; //停止当前这一次循环
            }
            var stratChengduHTML = '<li class="hall_li">'+
                                    '<div class="left_bar"></div>' +
                                    '<div class="hall_li_content">' +
                                        '<div class="hall_li_up">' +
                                            '<div class="floatleft">' +
                                                '<div class="hall_li_no">' + trainSplit[j] + '</div>' +
                                            '</div>' +
                                            '<div class="hall_li_center_box">' +
                                                '<div class="hall_li_cb_list">' +
                                                    '<div class="circle bg_black">圆</div>' +
                                                    '<div class="font_red floatleft">' + realStartTime + '</div>' +
                                                    '<div class="font_black floatleft hall_li_place">' + startStation + '</div>' +
                                                    '<div class="clear"></div>' +
                                                '</div>' +
                                                '<div class="hall_li_cb_list">' +
                                                    '<div class="circle bg_gray">圆</div>' +
                                                    '<div class="font_gray floatleft">' + arriveDate + '</div>' +
                                                    '<div class="font_gray floatleft hall_li_place">' + endStation + '</div>' +
                                                    '<div class="clear"></div>' +
                                                '</div>' +
                                            '</div>' +
                                            //'<div class="hall_li_state_box">' +
                                            //    '<div class="font_red hall_li_cb_list">检票中</div>' +
                                            //    '<div class="font_blue hall_li_cb_list">准点运行</div>' +
                                            //'</div>' +
                                            '<a class="btn_blue_small" href="indexcheck.html" rel="external" onclick="getDetailsInfo(\'' + trainId + '\')">查看</a>' +
                                            '<div class="clear"></div>' +
                                        '</div>' +
                                        //'<div class="hall_li_down">' +
                                        //    '<div class="hall_li_down_info">' +
                                        //        //'<div class="font_gray hall_li_cb_list">第二候车厅候车</div>' +
                                        //        //'<div class="font_gray hall_li_cb_list">5号站台上车</div>' +
                                        //    '</div>' +
                                        //    '<a class="btn_blue_small" href="#ticket_info" >查看</a>' +
                                        //    '<div class="clear"></div>' +
                                        //'</div>' +
                                    '</div>' +
                                '</li>';
            $("#hall_list_box").append(stratChengduHTML);
        }
    }
}

function appendToTicketBox() {
    for (var i = 0; i < allInfo.list.length; i++) {
        var endStation = allInfo.list[i].trainlist.endStation;
        var trainId = allInfo.list[i].trainlist.id;
        var startStation = allInfo.list[i].trainlist.startStation;
        var RDate = allInfo.list[i].trainlist.RDate;
        var ATime = allInfo.list[i].ATime;
        var DTime = allInfo.list[i].DTime;

        var trainSplit = trainId.split("/");
        //判断是否为成都
        var realStartTime = isChengdu(ATime, DTime, startStation);
        //获得到达时间
        var arriveDate = timeAdd(realStartTime, RDate);
        //执行list
        for (var j = 0; j < trainSplit.length; j++) {
            if (startStation != "成都东") {
                continue; //停止当前这一次循环
            }
            var stratChengduHTML = '<li class="hall_li">' +
                                    '<div class="left_bar"></div>' +
                                    '<div class="hall_li_content">' +
                                        '<div class="hall_li_up">' +
                                            '<div class="floatleft">' +
                                                '<div class="hall_li_no">' + trainSplit[j] + '</div>' +
                                            '</div>' +
                                            '<div class="hall_li_center_box">' +
                                                '<div class="hall_li_cb_list">' +
                                                    '<div class="circle bg_black">圆</div>' +
                                                    '<div class="font_red floatleft">' + DTime + '</div>' +
                                                    '<div class="font_black floatleft hall_li_place">' + startStation + '</div>' +
                                                    '<div class="clear"></div>' +
                                                '</div>' +
                                                '<div class="hall_li_cb_list">' +
                                                    '<div class="circle bg_gray">圆</div>' +
                                                    '<div class="font_gray floatleft">' + arriveDate + '</div>' +
                                                    '<div class="font_gray floatleft hall_li_place">' + endStation + '</div>' +
                                                    '<div class="clear"></div>' +
                                                '</div>' +
                                            '</div>' +
                                            '<div class="floatright mr10">' +
                                                '<div class="info_main_left_yu">余</div>' +
                                                '<div class="info_main_left_no">-</div>' +
                                            '</div>' +
                                            '<div class="clear"></div>' +
                                        '</div>' +
                                        //'<div class="hall_li_down">' +
                                        //    '<div class="hall_li_down_info">' +
                                        //        '<div class="font_gray hall_li_cb_list">第二候车厅候车</div>' +
                                        //        '<div class="font_gray hall_li_cb_list">5号站台上车</div>' +
                                        //    '</div>' +
                                        //    '<div class="hall_li_state_box">'+
                                        //        '<div class="font_red hall_li_cb_list">检票中</div>' +
                                        //        '<div class="font_blue hall_li_cb_list">准点运行</div>' +
                                        //    '</div>'+
                                        //    '<div class="clear"></div>' +
                                        //'</div>' +
                                    '</div>' +
                                '</li>';
            $("#ticket_list_box").append(stratChengduHTML);
        }
    }
}

//if it is Chengdu
function isChengdu(startATime,startBTime,station) {
    var realStartTime = startATime;
    //从成都出发为DTime，从其他地方出发为ATime
    if (station == "成都东") {
        realStartTime = startBTime;
    }
    return realStartTime;
}

//获取该列车的详细信息
function getDetailsInfo(tid) {
    //json数据为空 返回
    if (allInfo == null) {
        return;
    }
    for (var i = 0; i < allInfo.list.length; i++) {
        var trainId = allInfo.list[i].trainlist.id;
        if (trainId != tid) {
            continue;
        }
        var RDate = allInfo.list[i].trainlist.RDate;
        var ATime = allInfo.list[i].ATime;
        var DTime = allInfo.list[i].DTime;
        var startStation = allInfo.list[i].trainlist.startStation;
        var endStation = allInfo.list[i].trainlist.endStation;
        var realStartTime = isChengdu(ATime, DTime, startStation);

        var passCheckData = {
            "endStation": endStation,
            "startStation": startStation,
            "trainId": trainId,
            "realStartTime": realStartTime,
            "arriveDate":timeAdd(realStartTime, RDate)
        }
        localStorage.setItem("passCheckData", JSON.stringify(passCheckData));
        break;
    }
}


function timeAdd(time1, time2) {
    var regex = /(\d+)[\u4E00-\u9FA5|:|\s]+(\d+)/;//匹配时间，可以解析 "2小时45分" 或"13:16"; 这两种类型 的时间。前面是小时，中间是冒号或者汉字，后面是分钟
    var match1 = time1.match(regex);
    var time1hour;
    var time1minutes;
    var time2hour;
    var time2minutes;
    if (RegExp.$1 != null && RegExp.$2 != null) {
        //得到time1 的小时 和分钟
        time1hour = parseInt(RegExp.$1, 10);
        time1minutes = parseInt(RegExp.$2, 10);
    } else {
        return 0;
    }
    var match2 = time2.match(regex);
    if (RegExp.$1 != null && RegExp.$2 != null) {
        //得到time2的小时 和分钟
        time2hour = parseInt(RegExp.$1, 10);
        time2minutes = parseInt(RegExp.$2, 10);
    } else {
        return 0;
    }
    //如果解析失败，不符合格式 返回0
    if (isNaN(time1hour) || isNaN(time1minutes) || isNaN(time2hour) || isNaN(time2minutes)) {
        return 0;
    }
    //最终的小时数
    var FinalTime = time1hour + time2hour;
    //最终的分钟数
    var FinalMinute = time1minutes + time2minutes;
    //不可能FinalMinute=120分钟
    if (FinalMinute >= 60) {
        //分钟大于60,小时数加1,分钟数减去60
        FinalTime = FinalTime + 1;
        FinalMinute = FinalMinute - 60;
    }
    if (FinalMinute < 10) {
        FinalMinute = "0" + FinalMinute;
    }
    if (FinalTime >= 24) {
        var days = parseInt(FinalTime / 24, 10);
        //小时数减去24 天数加1
        FinalTime -= days * 24; 
        if (FinalTime < 10) {
            //小于10 前面就加0
            FinalTime = "0" + FinalTime;
        }
        return FinalTime + ":" + FinalMinute + "+" + days + "天";
    }
    if (FinalTime < 10) {
        //小于10前面就加0
        FinalTime = "0" + FinalTime;
    }
    //返回最终的时间 是这种格式"13:16";
    return FinalTime + ":" + FinalMinute;
}