$(document).ready(function () {
    var loaderCenterHTML = '<div class="center_loader" id="center_loader">'+
                                '<img src="img/ajax-loader-circle.gif"/>'+
                                '<div class="loader_font">正在加载中...</div>'+
                            '</div>'
    $("#hall,#ticket").append(loaderCenterHTML);
});