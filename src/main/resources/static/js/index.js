var authority='ROLE_ANONYMOUS';$('.modal')['on']('hidden.bs.modal',function(){$(this)['find']('.form-control')['val']('');});$('#btn-login-submit')['click'](function(){var _0x680d1a=new FormData($('#login-form')['find']('form')[0x0]);$['ajax']({'url':'/authentication/login','type':'POST','data':_0x680d1a,'contentType':![],'processData':![],'dataType':'json','clearForm':!![],'success':function(_0x4b7e32){layer['msg']('登录成功',{'icon':0x1});authority=_0x4b7e32['authorities'][0x0]['authority'];$('#login')['addClass']('d-none');$('#logout')['removeClass']('d-none');$('#login-form')['modal']('hide');},'error':function(_0x479400,_0x455fdf,_0x254dc3){var _0x44180e='请求失败';if(_0x479400['responseJSON']['message']!=null&&_0x479400['responseJSON']['message']!=''){_0x44180e=_0x479400['responseJSON']['message'];}layer['msg'](_0x44180e,{'icon':0x2});}});});$('#logout')['click'](function(){layer['confirm']('确认登出?',function(_0x3fc1bc){window['location']['replace']('/logout');layer['close'](_0x3fc1bc);});});$('#userMan')['click'](function(){if(authority=='ROLE_ANONYMOUS'){layer['msg']('请先完成登陆',{'icon':0x2});return;}$('#userMan-form')['modal']('show');});$('#btn-pass-submit')['click'](function(){var _0x4c58f5=new FormData($('#userMan-form')['find']('form')[0x0]);$['ajax']({'url':'/userMan/changePassword','type':'POST','data':_0x4c58f5,'contentType':![],'processData':![],'dataType':'json','clearForm':!![],'success':function(_0x148de3){if(_0x148de3['status']==0x1){layer['msg'](_0x148de3['message'],{'icon':0x2});return;}layer['msg']('修改成功',{'icon':0x1});$('#userMan-form')['modal']('hide');window['location']['replace']('/logout');},'error':function(_0x53fb64,_0x5d1d24,_0x3726c8){var _0x45e570='请求失败';layer['msg'](_0x45e570,{'icon':0x2});}});});