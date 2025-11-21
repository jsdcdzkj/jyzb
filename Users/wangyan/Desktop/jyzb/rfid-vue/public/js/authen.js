/* eslint-disable */
var JIT_AUTHEN = function () {

    // initParam：PNX初始化参数，数据可从网关系统：认证管理->Key类型管理中导出
    var initParam = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
        "<authinfo><liblist>" +
        "<lib type=\"CSP\" version=\"1.0\" dllname=\"\" ><algid val=\"SHA1\" sm2_hashalg=\"sm3\"/></lib>" + //软证书
        "<lib type=\"SKF\" version=\"1.1\" dllname=\"U0tGQVBJMjAwNzkuZGxs\" ><algid val=\"SHA1\" sm2_hashalg=\"sm3\"/></lib>" + //SKFAPI20079.dll			海泰20079
        "</liblist><checkkeytimes><item times=\"1\" ></item></checkkeytimes></authinfo>";

    //请求随机数地址
    var randomUrl = location.origin+":8082/jyzb_api/jitGWRandom";
    //请求p7认证地址
    var P7certAuthUrl = location.origin+":8082/jyzb_api/jitGWAuthP7";
    //请求p1认证地址
    var P1certAuthUrl = location.origin+":8082/jyzb_api/jitGWAuthP1";

    return{
        InitParam : initParam,
        //请求生成随机数
        random : function () {
            var randomResult = "";
            $.ajax({
                url : randomUrl,
                type : "post",
                async: false,
                data : "",
                success : function (data) {
                    randomResult= data;
                },
                error : function () {
                },
                datatype : "json"
            })
            return randomResult;
        },

        //请求p7认证方法
        P7certAuth : function (random,signed_data) {
            var authResult = "";
            $.ajax({
                url : P7certAuthUrl,
                type : "post",
                async: false,
                data : {"random":random,"signed_data":signed_data},
                success : function (data) {
                    authResult = data ;
                },
                error : function () {
                },
                datatype : "json",
            })
            return authResult;
        },

        //请求p1认证
        P1certAuth : function (random,signed_data,certBase64,hashAlg) {
            var authResult = "";
            $.ajax({
                url : P1certAuthUrl,
                type : "post",
                async: false,
                data : {"random":random,"signed_data":signed_data,"certBase64":certBase64,"hashAlg":hashAlg},
                success : function (data) {
                    authResult = data ;
                },
                error : function () {
                },
                datatype : "json",
            })
            return authResult;
        },

        //p7方式签名数据处理
        AuthP7Sign: function (random) {
            // 调用网关工具脚本中的detachSignStr（P7）进行签名，并返回签名结果
            var sign_Result = "";
            try {
                //用于判断插件是否可用（可选）
                JIT_GW_ExtInterface.GetVersion();
            } catch (e) {
                alert("未安装控件，请进行安装控件");
                return false;
                // window.location.href = "/PNXClient.exe";
            }
            try {
                //清理过滤条件（可选）
                JIT_GW_ExtInterface.ClearFilter();
                // 初始化KEY信息
                JIT_GW_ExtInterface.Initialize("", initParam);
                // 控制证书为一个时，不弹出证书选择框(可选)
                JIT_GW_ExtInterface.SetChooseSingleCert(1);
                // 添加过滤条件，颁发者过滤、同DN时SM2优先级设置等(可选)
                //同DN情况下优先使用sm2证书
                // JIT_GW_ExtInterface.AddFilter(11,"2");
                //强制弹PIN码(可选)
                //JIT_GW_ExtInterface.SetForcePinDialog(0);
            } catch (e) {
                alert("调用方法失败：" + JIT_GW_ExtInterface.GetLastErrorMessage());
                return false;
            }
            try {
                // 原文做P7签名
                sign_Result = JIT_GW_ExtInterface.P7SignString(random, true, true);
            } catch (e) {
                alert("生成签名信息失败：" + JIT_GW_ExtInterface.GetLastErrorMessage());
                return false;
            }
            if (JIT_GW_ExtInterface.GetLastError() != 0) {
                if (JIT_GW_ExtInterface.GetLastError() == 3758096386 || JIT_GW_ExtInterface.GetLastError() == 2148532334 || JIT_GW_ExtInterface.GetLastError() == 3758096385) {
                    alert("用户取消操作");
                } else if (JIT_GW_ExtInterface.GetLastError() == -536870815 || JIT_GW_ExtInterface.GetLastError() == 3758096481) {
                    alert("没有找到有效的证书，如果使用的是KEY，请确认已经插入key");
                } else {
                    alert(JIT_GW_ExtInterface.GetLastErrorMessage());
                }
            }
            // 释放签名对象
            JIT_GW_ExtInterface.Finalize();
            // 返回签名结果
            return sign_Result;
        },

        //p7带证书下拉框方式的签名数据处理
        AuthP7Sign_certSelect : function(authContent, certsn) {
            var sign_Result = "";
            try {
                // 用于判断插件是否可用(可选)
                JIT_GW_ExtInterface.GetVersion();
            } catch (e) {
                alert("未安装控件，请进行安装控件");
                window.location.href = "/PNXClient.exe";
            }
            try {
                //清理过滤条件（可选）
                JIT_GW_ExtInterface.ClearFilter();
                // 初始化KEY信息
                JIT_GW_ExtInterface.Initialize("", initParam);
                //下拉框参考Cert_Select方法
                // 根据SN过滤唯一证书
                JIT_GW_ExtInterface.AddFilter(1,certsn);
                // 设置单证书不弹出对话框
                JIT_GW_ExtInterface.SetChooseSingleCert(1);
                //强制弹PIN码(可选)
                JIT_GW_ExtInterface.SetForcePinDialog(0);
            } catch (e) {
                alert("调用方法失败：" + JIT_GW_ExtInterface.GetLastErrorMessage());
            }
            try {
                // 原文做P7签名
                sign_Result = JIT_GW_ExtInterface.P7SignString (authContent, true, true);
            } catch (e) {
                alert("生成签名信息失败：" + JIT_GW_ExtInterface.GetLastErrorMessage());
            }
            if (JIT_GW_ExtInterface.GetLastError() != 0) {
                if (JIT_GW_ExtInterface.GetLastError() == 3758096386
                    || JIT_GW_ExtInterface.GetLastError() == 2148532334 || JIT_GW_ExtInterface.GetLastError() == 3758096385) {
                    alert("用户取消操作");
                } else if (JIT_GW_ExtInterface.GetLastError() == -536870815
                    || JIT_GW_ExtInterface.GetLastError() == 3758096481) {
                    alert("没有找到有效的证书，如果使用的是KEY，请确认已经插入key");
                } else {
                    alert(JIT_GW_ExtInterface.GetLastErrorMessage());
                }
            }
            // 释放签名对象
            JIT_GW_ExtInterface.Finalize();
            // 返回签名结果对象
            return sign_Result;
        },

        //p1带证书下拉框方式的签名数据处理
        AuthP1Sign_certSelect : function(random, certsn, pin) {
            var sign_ResultObj = "";
            try {
            // 用于判断插件是否可用(可选)
                JIT_GW_ExtInterface.GetVersion();
            } catch (e) {
                alert("未安装控件，请进行安装控件");
                window.location.href = "/PNXClient.exe";
            }
            try {
                //清理过滤条件（可选）
                JIT_GW_ExtInterface.ClearFilter();
                // 初始化KEY信息
                JIT_GW_ExtInterface.Initialize("", initParam);
            } catch (e) {
                alert("调用方法失败：" + JIT_GW_ExtInterface.GetLastErrorMessage());
            }
            //下拉框展示部分参考p1Select方法
            try {
                // 原文做P1签名
                var signobj = JIT_GW_ExtInterface.P1SignWithPin(certsn, pin, random);
                sign_ResultObj = JSON.parse(signobj);
                if (sign_ResultObj.result != 0) {
                    if (sign_ResultObj.remainretrycount < 0) {
                        var errorMessage = JIT_GW_ExtInterface.GetLastErrorMessage();
                        alert(errorMessage);
                    } else {
                        alert("输入的pin码不正确，还剩余" + sign_ResultObj.remainretrycount + "次重试次数");
                    }
                }
            } catch (e) {
                alert("生成签名信息失败：" + JIT_GW_ExtInterface.GetLastErrorMessage());
            }
            if (JIT_GW_ExtInterface.GetLastError() != 0) {
                if (JIT_GW_ExtInterface.GetLastError() == 3758096386
                    || JIT_GW_ExtInterface.GetLastError() == 2148532334 || JIT_GW_ExtInterface.GetLastError() == 3758096385) {
                    alert("用户取消操作");
                } else if (JIT_GW_ExtInterface.GetLastError() == -536870815
                    || JIT_GW_ExtInterface.GetLastError() == 3758096481) {
                    alert("没有找到有效的证书，如果使用的是KEY，请确认已经插入key");
                } else {
                    alert(JIT_GW_ExtInterface.GetLastErrorMessage());
                }
            }
            // 释放签名对象
            JIT_GW_ExtInterface.Finalize();
            // 返回签名结果对象
            return sign_ResultObj;
        },

        //获取证书列表
        getCertList : function () {
            // 初始化KEY信息
            JIT_GW_ExtInterface.Initialize("",initParam);
            // 添加过滤条件，颁发者过滤、同DN时SM2优先级设置等(可选)
            //同DN情况下优先使用sm2证书
            // JIT_GW_ExtInterface.AddFilter(11,"2");
            // 获取证书列表
            var cert = JIT_GW_ExtInterface.GetCertList();
            var arr = cert.split("\r\n");
            var options=[];
            for (var i = 0; i < arr.length; i++) {
                var option={};
                var ele = arr[i];
                var n = ele.lastIndexOf(" ");
                var dn = ele.substr(0, n);
                var sn = ele.substr(n + 1);
                option.sn=sn;
                option.dn=dn;
                options.push(option);
            }
            // 释放签名对象
            JIT_GW_ExtInterface.Finalize();
            return options;
        },
    }
}();