window.console = window.console || (function () {
        var c = {};
        c.log = c.warn = c.debug = c.info = c.error = c.time = c.dir = c.profile
            = c.clear = c.exception = c.trace = c.assert = function () {
        };
        return c;
    })();

$(function () {

    var webRoot;

    function getWebRoot() {
        if (webRoot == null) {
            var href = window.location.href, index = href.lastIndexOf('/');
            webRoot = href.substr(0, index);
        }
        return webRoot;
    }

    //加载每一个控件的功能片段
    var $items = $(".list-group > a");
    $items.click(function (e) {

        $(document).trigger('pageleave');

        var rscript = /<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi;//匹配<script>
        // var ascript = /<script.*id="(.*)".*>[\s]*((.|\s)*)[\s]*<\/script>/;//匹配取值<script id=(1)>(2)...
        // 有bug,见以下两种情况
        // <script> id="i18n-script"> type="text/javascript" test</script>
        // <script id="i18n-script" type="text/javascript">test</script>
        var ascript = /<script[^>]*id="([^"]*)"[^>]*>\s*((.|\s)*)[\s]*<\/script>/;
        fish.ajax({
            url: this.id,
            type: 'get',
            dataType: 'html'
        }).done(function (responseText) {
            var jsArr = {};
            //取出并保存有ID的script
            responseText.replace(rscript, function (a) {
                var result = ascript.exec(a);
                if (result && result.length > 2) {
                    jsArr[result[1]] = result[2];
                    responseText = responseText.replace(a, "");
                }
            });
            $("#exampleContainer").html(responseText);
            // append过来的script没法调试，有ID的script会被清除，否则js会执行渲染block.innerHTML

            $('pre code').each(function (i, block) {
                var $block = $(block);
                if ($block.hasClass('html')) {
                    formatHighlightHTML($block);
                } else if ($block.hasClass('javascript')) { //jquery load方法会丢弃javascript语句，不能采取html的target方式
                    formatHighlightJS($block, jsArr);
                } else if ($block.hasClass('css')) {
                    formatHighlightCSS($block);
                }
                //如果已经HTML转义过就不处理
                (!!!$block.data('highlight')) && $block.html(hljs.highlight(block.className, block.innerHTML).value);// block.innerHTML.replace(/</g, "&lt;").replace(/>/g, "&gt;");
                $block.addClass('hljs');
                // hljs.highlightBlock(block);
            });
            //
        });

        $items.removeClass('active');
        $(this).addClass('active');
    });
    //默认选中第一个
    $(".list-group > a.active").trigger('click');

    // var themesHTML= '<div id="themes" style="width:82px;height:68px;right:18px" class="affix">'+
    //         '<button type="button" class="btn btn-default">'+
    //         '  <span class="glyphicon glyphicon-th-large" aria-hidden="true"></span>松散型'+
    //         '</button>'+
    //         '<button type="button" class="btn btn-default">'+
    //         '  <span class="glyphicon glyphicon-th" aria-hidden="true"></span>紧凑型'+
    //         '</button>'+
    //     '</div>';
    // $("body").append(themesHTML);
    $("#defaultCss").click(function (event) {
        $("#themesCss").attr("href", "../../dist/fish-desktop/css/fish-desktop-default.css");
        $("#defaultCss").addClass('btn-primary');
        $("#portalCss").removeClass('btn-primary');
        $("#compactCss").removeClass('btn-primary');
        fish.store.set('css-mode', 'default');
    });
    $("#compactCss").click(function (event) {
        $("#themesCss").attr("href", "../../dist/fish-desktop/css/fish-desktop-compact.css");
        $("#compactCss").addClass('btn-primary');
        $("#defaultCss").removeClass('btn-primary');
        $("#portalCss").removeClass('btn-primary');
        fish.store.set('css-mode', 'compact');
    });
    $("#portalCss").click(function (event) {
        $("#themesCss").attr("href", "../../dist/fish-desktop/css/fish-desktop-portal.css");
        $("#portalCss").addClass('btn-primary');
        $("#defaultCss").removeClass('btn-primary');
        $("#compactCss").removeClass('btn-primary');
        fish.store.set('css-mode', 'portal');
    });

    //获取用户选的样式
    initUserCssMode();

    function initUserCssMode() {
        var userCss = fish.store.get('css-mode');
        switch (userCss) {
            case 'compact':
            case 'portal':
                $('#' + userCss + 'Css').trigger('click');
                break;
            default:
                break;
        }
    }


    // setTimeout(function(){//IE8的js和css代码执行顺序与高版本不一样
    //     //固定左侧菜单
    //     //$(".list-group").width($(".list-group").width()); //affix样式会导致width变小
    //     // $(".list-group").affix();
    // },200);

    //format html
    function formatHighlightHTML($block) {
        var targetId = $block.attr("target");
        if (targetId) {
            $block[0].innerHTML = "    " + $("#" + targetId)[0].outerHTML;
        } else {
            $block[0].innerHTML = "    " + $.trim($block[0].innerHTML);
        }
    }

    //format css
    function formatHighlightCSS($block) {
        var targetId = $block.attr("target");
        if (targetId) {
            $block[0].innerHTML = "    " + $("#" + targetId).html();
        } else {
            $block.parent().before('<style>' + $block[0].innerHTML + '</style>');
        }
    }

    //format css
    function formatHighlightJS($block, jsArr) {
        var src = $block.attr("src"),
            targetId = $block.attr("target"),
            block = $block[0];
        if (src) {//如果配置了路径,引入路径
            $.ajax({url: src, dataType: 'html', async: false}).done(function (responseText) {
                if (fish.isIE) {//IE 对sourceURL只支持了一半
                    createScriptLink(src);
                } else {
                    eval(responseText + '//# sourceURL=' + getWebRoot() + '/' + src);
                }

                block.innerHTML = hljs.highlight('javascript', responseText).value;
                $block.data('highlight', true);//表示已转成HTML字符串
                //同步或者在回调里面处理
            })
        } else {
            if (targetId) {//如果配置了ID
                block.innerHTML = "    " + jsArr[targetId];
                new Function(jsArr[targetId].replace(/&lt;/g, "<").replace(/&gt;/g, ">"))();
                //$block.append(jsSrc[targetId]); //与上面清除script的逻辑同时使用
            } else { //非src场景下需要自执行JS代码
                block.innerHTML = "    " + $.trim(block.innerHTML);
                new Function(block.innerHTML.replace(/&lt;/g, "<").replace(/&gt;/g, ">").replace(/&amp;/g, '&'))();
                // eval(block.innerHTML);
            }
            // block.innerHTML = hljs.highlight('javascript', block.innerHTML).value;
        }
    }

    //手动创建script节点并设置src属性
    function createScriptLink(src) {
        var id = src.substring(0, src.length - 3) + "-js";
        id = String(id).replace(/[!"#$%&'()*+,.\/:; <=>?@\[\\\]\^`{|}~]/g, "\\$&");
        if ($("#" + id)[0]) {//如果存在
            $("#" + id).remove();
        }
        var head = document.getElementsByTagName('head')[0];
        var node = document.createElement('script');
        node.type = 'text/javascript';
        node.charset = 'utf-8';
        node.async = true;
        node.src = src;
        node.id = id;
        head.appendChild(node);
    }

})
