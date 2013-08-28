//$(document).ready(function () {
//    initMovieList("movie");
//});

function initMovieList(listKey) {
    //初始化电影列表
    switch (listKey) {
        case "video":
            $("#vip_page_header").html("视频短片");
            initTitle("get_video_list", appendVideoList);
            getScroll("get_video_list", appendVideoList);
            break;
        case "movie":
            $("#vip_page_header").html("电影电视");
            initTitle("get_movie_list", appendMovieList);
            getScroll("get_movie_list", appendMovieList);
            break;
        default:
            alert("请从vip页面进入");
    }
}

var mainUrl = "http://shoptrekkers.mobi:8447/mi1/st6";
var mainImgUrl = "http://shoptrekkers.mobi:8447";
var movieAll;
var numMovie = 0;

var isScrollMovie = true;

var movieImgUrlCol = [];
var movieTitleCol = [];
var movieUrlCol = [];
var movieInfoCol = [];
var movieStarCol = [];
var movieTimeCol = [];
var movieHotCol = [];

/**************************************/
//initialaze the vip movie
/**************************************/

function initTitle(praUrl, appendList) {
    if (window.navigator.onLine) {
        $("#movie_load").html("<img src='img/ajax-loader.gif' />");
        $.get(mainUrl, { scr: praUrl, _offset: numMovie }, function (data, status) {
            if (status == "success") {
                if (data.list[0].item.length == 0) {
                    $("#movie_load").empty();
                    return;
                }
                movieAll = data.list[0];
                appendList();
                isScrollMovie = true;
                numMovie += 20;
            } else {
                alert("数据读取错误");
            }
            $('#movie_load').empty();
        }, "json");
    } else {
        $("#movie_list_box").append("<div class='content_padding'><h3>暂无火车票相关数据</h3><p>您可以通过以下几种方式解决该问题：</p><p>1.请检查您的设备是否接入网络。</p><p>2.请查看是否更新到最新版本。</p></div>");
    }

}

function appendMovieList() {
    for (var i = 0; i < movieAll.item.length; i++) {
        movieImgUrlCol.push(movieAll.item[i].img[0].url);
        movieTitleCol.push(movieAll.item[i].lbl[0].val);
        movieUrlCol.push(movieAll.item[i].lbl[1].val);
        movieInfoCol.push(movieAll.item[i].lbl[2].val);
        movieStarCol.push(movieAll.item[i].lbl[3].val);
        movieHotCol.push(movieAll.item[i].lbl[4].val);
        movieTimeCol.push(movieAll.item[i].lbl[5].val);
    }
    for (var i = numMovie; i < movieTitleCol.length; i++) {
        var movieImgUrl = movieImgUrlCol[i];
        var movieTitle = movieTitleCol[i];
        var movieUrl = movieUrlCol[i];
        var movieInfo = movieInfoCol[i].substring(0, 65) + "...";
        var movieStar = movieStarCol[i];
        var movieHot = movieHotCol[i]
        var movieTime = movieTimeCol[i];
        movieTime = parseInt(movieTime / 60) + "分钟";
        var movieLi = '<li class="hall_li movie_li">' +
                        '<a class="hall_li_click" href="movieDetails.html" onclick="getMovieInfo(\'' + movieHot + '\')">' +
                            '<div class="left_bar bg_blue"></div>' +
                            '<div class="m_li">' +
                                '<img src="' + mainImgUrl + movieImgUrl + '"/>' +
                                '<div class="v_info">' +
                                    '<h3>' + movieTitle + '</h3>' +
                                    '<p>' + movieInfo + '</p>' +
                                    '<ul class="star_box" id="m_star_box">' + getStar(movieStar) +
                                    '</ul>' +
                                    '<div class="time_box">时长：<span>' + movieTime + '</span></div>' +
                                '</div>' +
                                '<div class="clear"></div>' +
                            '</div>' +
                            '<div class="clear"></div>' +
                        '</a>' +
                    '</li>';
        $("#movie_list_box").append(movieLi);
    }
}

function appendVideoList() {
    for (var i = 0; i < movieAll.item.length; i++) {
        movieImgUrlCol.push(movieAll.item[i].img[0].url);
        movieTitleCol.push(movieAll.item[i].lbl[0].val);
        movieUrlCol.push(movieAll.item[i].lbl[1].val);
        movieTimeCol.push(movieAll.item[i].lbl[2].val);
        movieHotCol.push(movieAll.item[i].lbl[4].val);
    }
    //list box
    for (var i = numMovie; i < movieTitleCol.length ; i++) {
        var movieImgUrl = movieImgUrlCol[i];
        var movieTitle = movieTitleCol[i];
        var movieUrl = movieUrlCol[i];
        var movieTime = movieTimeCol[i];
        var movieHot = movieHotCol[i];
        movieTime = parseInt(movieTime / 60) + "分钟";
        var movieLi = '<li class="hall_li movie_li">' +
                        '<a class="hall_li_click" href="movieDetails.html" onclick="getMovieInfo(\'' + movieHot + '\')">' +
                            '<div class="left_bar bg_blue"></div>' +
                            '<div class="v_li">' +
                                '<img src="' + mainImgUrl + movieImgUrl + '"/>' +
                                '<div class="v_info">' +
                                    '<h3>' + movieTitle + '</h3>' +
                                    '<p class="font_black">时长：<span class="font_red font_bold">' + movieTime + '</span></p>' +
                                '</div>' +
                                '<div class="clear"></div>' +
                            '</div>' +
                            '<div class="clear"></div>' +
                        '</a>' +
                        '</li>';
        $("#movie_list_box").append(movieLi);
    }
}

//获取电影内容
function getMovieInfo(passMovieHot) {
    if (movieAll == null) {
        return;
    }
    for (var i = 0; i < movieTitleCol.length; i++) {
        if (movieHotCol[i] != passMovieHot) {
            continue;
        }
        var passMovieTitle = movieTitleCol[i];
        var passMovieUrl = movieUrlCol[i];
        var passMovieInfo = movieInfoCol[i];
        var passMovieData = {
            "movieTitle": passMovieTitle,
            "movieUrl": passMovieUrl,
            "movieInfo" : passMovieInfo
        }
        localStorage.setItem("passMovieData", JSON.stringify(passMovieData));
        window.actAndroid.actNavigation("返回", passMovieTitle, null);
        break;
    }
}

//电影星级评定

function getStar(starNo) {
    var starFull = parseInt(starNo / 2);
    var starHalf = starNo % 2;
    var starFullHTML = '<li class="star_1"></li>';
    var starHalfHTML = '<li class="star_5"></li>';
    var starEmptyHTML = '<li class="star_0"></li>';
    var starHTML = '';

    if (starFull > 0 && starFull < 5) {
        for (var i = 0; i < starFull; i++) {
            starHTML += starFullHTML;
        }
        if (starHalf == 1) {
            starHTML += starHalfHTML;
        }
        for (var i = 0; i < 5 - starFull - starHalf; i++) {
            starHTML += starEmptyHTML;
        }
    } else {
        for (var i = 0; i < 5; i++) {
            starHTML += starFullHTML;
        }
    }
    return starHTML;
}

//向下滚动时加载。
function getScroll(praUrl, appendList) {
    $(window).scroll(function () {
        var windowScrollTop = $(window).scrollTop();
        if (windowScrollTop == $(document).height() - $(window).height() && windowScrollTop != 0 && isScrollMovie) {
            initTitle(praUrl, appendList);
            isScrollMovie = false;
        }
    });
}