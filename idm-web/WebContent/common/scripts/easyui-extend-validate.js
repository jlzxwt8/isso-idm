/* MTP:台胞证   CHS:中文  diplomatic:护照  officerCard:军官证  HKCard:港澳通行证*/
$.extend($.fn.validatebox.defaults.rules, {
    CHS: {
        validator: function (value, param) {
        	console.log('CHS');
            return /^[\u0391-\uFFE5]+$/.test(value);
        },
        message: '请输入汉字'
    },
    ZIP: {
        validator: function (value, param) {
            return /^[1-9]\d{5}$/.test(value);
        },
        message: '邮政编码不存在'
    },
    mobile: {
        validator: function (value, param) {
            return /^((\(\d{2,3}\))|(\d{3}\-))?1\d{10}$/.test(value);
        },
        message: '手机号码不正确'
    },
    myMobile: {
    	validator: function (value, param) {
    		return /^((\(\d{2,3}\))|(\d{3}\-))?1\d{10}$/.test(value);
    	},
    	message: '手机号码不正确，用于找回密码，非常重要，请填写真实信息'
    },
	phoneOrMobile : {
		validator : function(value) {
			return /^(13|15|18)\d{9}$/i.test(value)
					|| /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
		},
		message : '请填入手机或电话号码,如13688888888或020-8888888'
	},    
    Name: {
        validator: function (value, param) {
            return /^[\u0391-\uFFE5a-zA-Z ]+$/.test(value);
        },
        message: '只允许汉字、英文字母。'
    },
    loginName: {
        validator: function (value, param) {
            return /^[\u0391-\uFFE5\w]+$/.test(value);
        },
        message: '登录名称只允许汉字、英文字母、数字及下划线。'
    },
    safepass: {
        validator: function (value, param) {
            return safePassword(value);
        },
        message: '密码由字母和数字组成，至少6位'
    },
    safepassTwo: {
        validator: function (value, param) {
        	/*var flag = !(/^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,7}|.{,})$|\s/.test(value));*/
        	var flag = /^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{8,}$/.test(value);
        	var rules = $.fn.validatebox.defaults.rules;
        	if (flag == true){
        		if(rules.passwordNotDiff.validator(value,param[0])){
        			rules.safepassTwo.message = rules.passwordNotDiff.message;
        			return false;
        		} else {
        			return true;
        		}
        	} else {
        		rules.safepassTwo.message ='密码设置不符合规则，请重新设置';
        	}
        },
        message: '密码设置不符合规则，请重新设置'
    },
    renewpasswordNotDiff: {
        validator: function (value, param){
            return value == param[0];
        },
        message: '新密码与确认密码不同，请重新填写'
    },
    passwordNotDiff: {
        validator: function (value, param) {
        	return value==param;
        },
        message: '新密码不能和旧密码一样，请重新设置'
    },
    /*passwordErr: {
        validator: function (value, param){
            return value == param[0];
        },
        message: '原密码输入错误，请再次输入'
    },*/
    equalTo: {
        validator: function (value, param) {
            return value == $(param[0]).val();
        },
        message: '两次输入的字符不一至'
    },
    number: {
        validator: function (value, param) {
            return /^\d+$/.test(value);
        },
        message: '请输入数字'
    },
    idcard: {
        validator: function (value, param) {
            return idCard(value);
        },
        message:'请输入正确的证件号码'
    },
    myEmail: {
        validator: function (value, param) {
        	return /^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/.test(value);
        },
        message:'请输入正确的邮箱，用于找回密码，非常重要，请填写真实信息'
    },
    diplomatic: {
        validator: function (value, param) {
        	return /^(P\d{7}|G\d{8}|E\d{8})$/.test(value);
        },
        message:'请输入正确的护照'
    },
    officerCard: {
        validator: function (value, param) {
        	return /^\d{8}$/.test(value);
        },
        message:'请输入正确的护照'
    },
    MTP: {
        validator: function (value, param) {
        	return /^\d{10}\((\d{2}|a-zA-Z){1}\)$/.test(value);
        },
        message:'请输入正确的台胞证'
    },
    HKCard: {
        validator: function (value, param) {
        	return /^[A-Z]{1}\d{8}$/.test(value);
        },
        message:'请输入正确的港澳通行证'
    }
});

/* 密码由字母和数字组成，至少6位 */
/*var safePassword = function (value) {
    return !(/^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,5})$|\s/.test(value));
}*/

var safePassword = function (value) {
    return /^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{8,}$/.test(value);
}

var idCard = function (value) {
    if (value.length == 18 && 18 != value.length) return false;
    var number = value.toLowerCase();
    var d, sum = 0, v = '10x98765432', w = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2], a = '11,12,13,14,15,21,22,23,31,32,33,34,35,36,37,41,42,43,44,45,46,50,51,52,53,54,61,62,63,64,65,71,81,82,91';
    var re = number.match(/^(\d{2})\d{4}(((\d{2})(\d{2})(\d{2})(\d{3}))|((\d{4})(\d{2})(\d{2})(\d{3}[x\d])))$/);
    if (re == null || a.indexOf(re[1]) < 0) return false;
    if (re[2].length == 9) {
        number = number.substr(0, 6) + '19' + number.substr(6);
        d = ['19' + re[4], re[5], re[6]].join('-');
    } else d = [re[9], re[10], re[11]].join('-');
    if (!isDateTime.call(d, 'yyyy-MM-dd')) return false;
    for (var i = 0; i < 17; i++) sum += number.charAt(i) * w[i];
    return (re[2].length == 9 || number.charAt(17) == v.charAt(sum % 11));
}

var isDateTime = function (format, reObj) {
    format = format || 'yyyy-MM-dd';
    var input = this, o = {}, d = new Date();
    var f1 = format.split(/[^a-z]+/gi), f2 = input.split(/\D+/g), f3 = format.split(/[a-z]+/gi), f4 = input.split(/\d+/g);
    var len = f1.length, len1 = f3.length;
    if (len != f2.length || len1 != f4.length) return false;
    for (var i = 0; i < len1; i++) if (f3[i] != f4[i]) return false;
    for (var i = 0; i < len; i++) o[f1[i]] = f2[i];
    o.yyyy = s(o.yyyy, o.yy, d.getFullYear(), 9999, 4);
    o.MM = s(o.MM, o.M, d.getMonth() + 1, 12);
    o.dd = s(o.dd, o.d, d.getDate(), 31);
    o.hh = s(o.hh, o.h, d.getHours(), 24);
    o.mm = s(o.mm, o.m, d.getMinutes());
    o.ss = s(o.ss, o.s, d.getSeconds());
    o.ms = s(o.ms, o.ms, d.getMilliseconds(), 999, 3);
    if (o.yyyy + o.MM + o.dd + o.hh + o.mm + o.ss + o.ms < 0) return false;
    if (o.yyyy < 100) o.yyyy += (o.yyyy > 30 ? 1900 : 2000);
    d = new Date(o.yyyy, o.MM - 1, o.dd, o.hh, o.mm, o.ss, o.ms);
    var reVal = d.getFullYear() == o.yyyy && d.getMonth() + 1 == o.MM && d.getDate() == o.dd && d.getHours() == o.hh && d.getMinutes() == o.mm && d.getSeconds() == o.ss && d.getMilliseconds() == o.ms;
    return reVal && reObj ? d : reVal;
    function s(s1, s2, s3, s4, s5) {
        s4 = s4 || 60, s5 = s5 || 2;
        var reVal = s3;
        if (s1 != undefined && s1 != '' || !isNaN(s1)) reVal = s1 * 1;
        if (s2 != undefined && s2 != '' && !isNaN(s2)) reVal = s2 * 1;
        return (reVal == s1 && s1.length != s5 || reVal > s4) ? -10000 : reVal;
    }
};