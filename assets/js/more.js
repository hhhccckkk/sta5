//more page
$(document).ready(function () {
    getList();
});

//存储全部common信息
var commonJSON = "data/train_commons.json";
var passengerJSON = "data/passenger_commons.json";

function getList() {
    $("[id*=listenter]").unbind("click");
    $("[id*=listenter]").bind("click",function () {
        var dataID = $(this).attr("data-id");
        switch (dataID) {
            case "0":
                $("#more_list_box").empty();
                localStorage.setItem("changeToMorePage", "page1");
                localStorage.setItem("passMoreList", passengerJSON);
                window.actAndroid.actNavigation("返回", "乘客须知", null);
                break;
            case "1":
                $("#more_list_box").empty();
                localStorage.setItem("changeToMorePage", "page1");
                localStorage.setItem("passMoreList", commonJSON);
                window.actAndroid.actNavigation("返回", "铁路常识", null);
                break;
            case "2":
                localStorage.setItem("changeToMorePage", "page2a");
                getSinglePage();
                window.actAndroid.actNavigation("返回", "关于我们", null);
                break;
            case "3":
                localStorage.setItem("changeToMorePage", "page2b");
                getSinglePage();
                window.actAndroid.actNavigation("返回", "使用条款", null);
                break;
            case "4":
                localStorage.setItem("changeToMorePage", "page2c");
                getSinglePage();
                window.actAndroid.actNavigation("返回", "免责声明", null);
                break;
            default:
                return;
        }
    });
}

function getSinglePage() {
    var singlePageData = [{
        "spTitle":"关于我们",
        "spContent": "“火车门户”是成铁传媒和富士康成都通讯科技联手打造，专为旅客提供各种资讯内容和增值服务的智能手机APP，欢迎您下载使用。为了更好的为您提供服务，在使用过程中遇到问题或则您有好的建议请及时与我们联系，万分感谢。"
    }, {
        "spTitle": "使用条款",
        "spContent": "火车门户是由成铁传媒和富士康成都科技联合创作开发而成。火车门户的设计图纸、源代码、系统前台、后台功能介绍文档等均已提交中国国家版权局登记备案，这些权利受国家法律的严格保护，任何的侵权、盗版行为，火车门户均将追究其法律责任，希望广大用户遵守法律、尊重著作权人的劳动成果，同时也为了我们国家软件产业更好的发展，支持正版，使用正版。"
    }, {
        "spTitle":"免责声明",
        "spContent": "任何经由本服务以上载、张贴、发送即时信息、电子邮件或任何其他方式传送的资讯、资料、文字、软件、照片、图形、视讯、信息、用户的登记资料或其他资料（以下简称“内容”），无论系公开还是私下传送，均由内容提供者承担责任。火车门户无法控制经由本服务传送之内容，也无法对用户的使用行为进行全面控制，因此不保证内容的合法性、正确性、完整性、真实性或品质；您已预知使用本服务时，可能会接触到令人不快、不适当或令人厌恶之内容，并同意将自行加以判断并承担所有风险，而不依赖于火车门户。 火车门户各项服务存在因不可抗力、计算机病毒或黑客攻击、系统不稳定、用户所在位置、互联网络、通信线路原因等造成的服务中断或不能满足用户要求的风险。开通服务的用户须自行承担以上风险，火车门户及合作公司对服务之及时性、安全性、准确性不作担保，对因此导致用户不能发送和接受阅读消息、或传递错误，个人设定之时效、未予储存或其他问题不承担任何责任。如火车门户提供的服务项目发生故障影响到本服务的正常运行，火车门户承诺在第一时间内与相关单位配合，及时处理进行修复。但用户因此而产生的经济损失，火车门户及合作公司不承担责任。 火车门户各项服务或第三人可提供与其他国际互联网上之网站或资源之链接。由于火车门户无法控制这些网站及资源，您了解并同意：无论此类网站或资源是否可供利用，火车门户不予负责；火车门户亦对存在或源于此类网站或资源之任何内容、广告、产品或其他资料不予保证或负责。因您使用或依赖任何此类网站或资源发布的或经由此类网站或资源获得的任何内容、商品或服务所产生的任何损害或损失，火车门户不负任何直接或间接之责任。若您认为该链接所载的内容或搜索引擎所提供之链接的内容侵犯您的权利，火车门户声明与上述内容无关，不承担任何责任。火车门户建议您与该网站或法律部门联系，寻求法律保护。"
    }, ]
    
    localStorage.setItem('singlePageData', JSON.stringify(singlePageData));
}

