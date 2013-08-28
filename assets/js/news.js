//使用live，翻页后同样绑定
var newsContentUrl;
//初始化新闻列表
var mainUrl = "http://shoptrekkers.mobi:8447/mi1/st6";
//存储取回的json
var newsAll;
//加载的新闻量
var newsNum = 0;
//是否滚动
var isScrollNews = true;
//存储list值
var gListKey;

//$(document).ready(function () {
//    initNewsList("notice");
//});

function initNewsList(listKey) {
    gListKey = listKey;
    switch (listKey) {
        case "notice":
            newsContentUrl = "get_notice_content";
            initTitle("get_notice_list", appendNewsList);
            getScroll("get_notice_list", appendNewsList);
            break;
        case "news":
            newsContentUrl = "get_news_content";
            initTitle("get_news_list", appendNewsList);
            getScroll("get_news_list", appendNewsList);
            break;
        case "joke":
            newsContentUrl = "get_joke_content";
            initTitle("get_joke_list", appendNewsList);
            getScroll("get_joke_list", appendNewsList);
            break;
        default:
            alert("请从vip页面进入");
    }
}

//初始化新闻
function initTitle(praUrl, appendList) {
    if (window.navigator.onLine) {
        $("#news_load").html("<img src='img/ajax-loader.gif' />");
        $.get(mainUrl, { scr: praUrl, _offset: newsNum }, function (data, status) {
            if (status == "success") {
                if (data.list[0].item.length == 0) {
                    $('#news_load').empty();
                    return;
                }
                newsAll = data.list[0];
                appendList();
                isScrollNews = true;
                newsNum += 20;
            } else {
                alert("数据读取错误");
            }
            $('#news_load').empty();
        }, "json");
    } else {
        $("#news_list").append("<div class='content_padding'><h3>暂无火车票相关数据</h3><p>您可以通过以下几种方式解决该问题：</p><p>1.请检查您的设备是否接入网络。</p><p>2.请查看是否更新到最新版本。</p></div>");
    }
}

//添加到列表
function appendNewsList() {
    for (var i = 0; i < newsAll.item.length; i++) {
        var newstitle = newsAll.item[i].lbl[0].val;
        var newstime = newsAll.item[i].lbl[1].val.substring(0, 10);
        var newsId = newsAll.item[i].lbl[2].val;
        var newsLi = '<li class= "hall_li news_li">' +
                        '<a class="hall_li_click" href="newsDetails.html" onclick="getNewsInfo(\'' + newsId + '\')" >' +
                            '<div class="left_bar"></div>' +
                            '<div class="notice_li">' +
                                '<h3 id="newsTitle' + newsId + '">' + newstitle + '</h3>' +
                                '<p class="font_gray">' + newstime + '</p>' +
                            '</div>' +
                            '<div class="clear"></div>' +
                        '</a>' +
                        '<div class="clear"></div>' +
                     '</li>';
        $("#news_list").append(newsLi);
    }
}

//获取新闻内容
function getNewsInfo(passNewsId) {
    if (newsAll == null) {
        return;
    }
    var passNewsData = {
        "listId": gListKey,
        "newsContentUrl":newsContentUrl,
        "newsId": passNewsId,
        "newstitle": $("#newsTitle" + passNewsId).html(),
    }
    localStorage.setItem("passNewsData", JSON.stringify(passNewsData));
}

//Scroll Action
//When scroll down, the scroller is at the bottom with the function below and fire the lastPostFunc function
function getScroll(praUrl, appendList) {
    $(window).scroll(function () {
        var windowScrollTop = $(window).scrollTop();
        if (windowScrollTop == $(document).height() - $(window).height() && windowScrollTop != 0 && isScrollNews) {
            initTitle(praUrl, appendList);
            isScrollNews = false;
        }
    });
}