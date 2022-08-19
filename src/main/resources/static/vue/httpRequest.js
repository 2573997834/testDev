//axios封装post请求
document.write('<script src="/static/vue/vue.min.js" type="text/javascript" charset="utf-8"></script>');
document.write('<script src="/static/elementUI/index.js" type="text/javascript" charset="utf-8"></script>');
function axiosPost(url,data,flag) {
    let headers = {
        'Content-Type':'application/json;charset=utf-8',
        'Authorization': localStorage.getItem("access_token")
    }
    if (url.lastIndexOf("/doLogin") != -1) {
        headers = {
            'Content-Type':'application/json;charset=utf-8'
        }
    }
    let result = axios({
        method: 'post',
        url: url,
        data: flag != true ? data : Qs.stringify(data),
        headers: headers
    }).then(resp=> {
        if (resp.headers.refresh_token != undefined && resp.headers.refresh_token != null && resp.headers.refresh_token != '') {
            localStorage.setItem("access_token",resp.headers.refresh_token);
            cookieHandler.set("access_token", resp.headers.refresh_token);
        }
        return resp.data;
    }).catch(error=>{
        if (error.response.status === 403) {
            localStorage.removeItem("access_token")
            alert("无权访问，请先登录");
            window.location.href="/page/login.html"
        }
        if (error.response.status === 401) {
            localStorage.removeItem("access_token")
            alert(error.response.data.message);
            window.location.href="/page/login.html"
        }
        return error.message;
    });
    return result;
}

//get请求
function axiosGet(url) {
    let headers = {
        'Authorization': localStorage.getItem("access_token")
    }
    if (url.lastIndexOf("/doLogin") != -1) {
        headers = {}
    }
    var result = axios({
        method: 'get',
        url: url,
        headers:headers
    }).then(function (resp) {
        if (resp.headers.refresh_token != undefined && resp.headers.refresh_token != null && resp.headers.refresh_token != '') {
            localStorage.setItem("access_token",resp.headers.refresh_token);
            cookieHandler.set("access_token", resp.headers.refresh_token);
        }
        return resp.data;
    }).catch(function (error) {
        if (error.response.status === 403) {
            localStorage.removeItem("access_token")
            alert("无权访问，请先登录");
            window.location.href="/page/login.html"
        }
        if (error.response.status === 401) {
            localStorage.removeItem("access_token")
            alert(error.response.data.message);
            window.location.href="/page/login.html"
        }
        return error.response.data.message;
    });
    return result;
}

var cookieHandler = {
    path: "/",
    get: function (cookie_name, default_value) {
        var reg = '(/(^|;| )' + cookie_name + '=([^;]*)(;|$)/g)';
        var temp = eval(reg).exec(document.cookie);
        if (temp != null) {
            var value = temp[2];
            return escape(value);
        }
        return default_value;
    },
    set: function (name, value, time) {
        time = time == undefined ? 2 : time;
        var str = name + '=' + value + '; ';
        if (time) {
            var date = new Date();
            date.setTime(date.getTime() + time * 60 * 1000);
            str += 'expires=' + date.toGMTString() + '; ';
        }
        str += "path=" + this.path;
        document.cookie = str;
    },
    del: function (name) {
        this.set(name, null, -1);
    }
};